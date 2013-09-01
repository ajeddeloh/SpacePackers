 package lwjglbase;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

/*
 * Master class that everything runs from. handles very base level opengl 
 * initialization and screen init. 
 */

public class LWJGLbase {

    //FPS stuff
    long prevTime; // Time since last frame
    int fps; //Frames per second
    long prevSecond; // Time that FPS was last reset

    RoomBase currentLevel;
    
    static final String LEVELS_PATH = "levels/"; // Path to folder with level files
    static final String SPRITES_PATH = "resources/sprites/"; // Path to folder with level files
    static final String MODELS_PATH = "resources/models/"; // Path to folder with level files

    //returns system time
    private long getTime() {
        return System.nanoTime() / 1000000;
    }

    //get time since last update
    private long getDelta() {
        long time = getTime();
        long delta = time - prevTime;
        prevTime = time;
        return delta;
    }

    //updates the fps variable
    private void updateFPS() {
        if (getTime() - prevSecond > 1000) {
            Display.setTitle("FPS:" + fps);
            fps = 0;
            prevSecond += 1000;
        }
        fps++;
    }

    //starts the game but is not the only init work done. should be renamed
    private void init() {
        prevTime = getTime();
        prevSecond = getTime();
        currentLevel = new TeamScreen();
    }

    //creates the display object
    private void initDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(1200, 800));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
    
    private void initObjects() {
        Sprite.init();
        Cube.init();
        Player.init();
        Box.init();
        Goal.init();
        Player.init();
        Button.init();
    }

    //inits all the opengl stuff
    private void initGL() {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
        glMatrixMode(GL_PROJECTION); // init Projection matrix
        glLoadIdentity();
        glMatrixMode(GL_MODELVIEW);// Init modelview matrix
        glLoadIdentity();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glEnable(GL_NORMALIZE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glClearColor(.2f, .2f, .2f, 1);
    }

    //updates the game does this until close
    private void gameLoop() {
        while (!Display.isCloseRequested()) { // Main loop
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            updateFPS();
            currentLevel = currentLevel.update(getDelta());
            if(currentLevel == null) {
                break;
            }
            currentLevel.draw(getDelta());
            Display.sync(60);
            Display.update();

            int er = glGetError();
            while(er != GL_NO_ERROR) {
                System.out.println(gluErrorString(er));
                er = glGetError();
            }
        }
        cleanUp();
    }

    //not really needed since the program exits after this, but its "correct"
    private void cleanUp() {
        Display.destroy();
        if(currentLevel != null && currentLevel.getClass() == Level.class) {
            ((Level)currentLevel).player.stop();
        }
    }

    //starts the game. should this be merged with init()?
    public void start() {
        long t = System.currentTimeMillis();
        initDisplay();
        
        initObjects();

        //System.out.println(System.currentTimeMillis()-t);
        initGL();

        //System.out.println(System.currentTimeMillis()-t);
        init();

        //System.out.println(System.currentTimeMillis()-t);
        gameLoop();
        cleanUp();
    }

    //if you dont know what this is, you probably shouldn't be here.
    public static void main(String[] args) {
        LWJGLbase l = new LWJGLbase();
        l.start();
    }
}
