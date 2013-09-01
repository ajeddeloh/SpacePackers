package lwjglbase;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.lwjgl.input.*;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

/*
 * Main gameplay level. Can be any level, which is loaded from a file when this
 * is constructed. holds camera, lights, cubes, the player, guis, etc
 */
public class Level implements RoomBase {

    public static final int GRID_SIZE = 10;
    Grid grid = new Grid(GRID_SIZE);
    ArrayList<Cube> cubes = new ArrayList<Cube>();
    ArrayList<Box> boxes = new ArrayList<Box>();
    Cube[][][] cubeGrid = new Cube[GRID_SIZE][GRID_SIZE][GRID_SIZE];
    Box[][][] boxGrid = new Box[GRID_SIZE][GRID_SIZE][GRID_SIZE];
    boolean prevMouseLeft, prevMouseRight, currentMouseLeft, currentMouseRight;
    int mouseX = 0;
    int mouseY = 0;
    Camera cam = new Camera(0f, 5f, 0f, 90f, 90f);
    LevelGui gui = new LevelGui();
    Player player;
    Goal goal;
    RoomBase nextRoom;

    //loads the level from the file specified
    public Level(String in) {
        initLights();

        loadLevel(LWJGLbase.LEVELS_PATH + in);
    }

    //initializes all the lights. They are all directional not point source
    private void initLights() {
        for (int light = GL_LIGHT0; light < GL_LIGHT7; light++) {
            glDisable(light); //disable all lights that are not out own
        }
        Light dir1 = new Light(GL_LIGHT0, Light.DIRECTION, 1f, 1f, 1f, Color.GRAY, 0f);
        Light dir2 = new Light(GL_LIGHT1, Light.DIRECTION, -1f, -1f, 0f, Color.GRAY, 0f);
        Light dir3 = new Light(GL_LIGHT2, Light.DIRECTION, -1f, -1f, -1f, Color.GRAY, 0f);

        float[] white = {.6f, .6f, .6f, 1f};
        glLightModel(GL_LIGHT_MODEL_AMBIENT, Utils.createFB(white));
    }

    //loads all the cubes from the level file
    private void loadLevel(String in) {
        try {
            // Read from in and parse cubes
            String parse;                                                               //Path to levels
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(in)));
            while ((parse = br.readLine()) != null) {
                char start = parse.charAt(0);
                parse = parse.substring(1);
                StringTokenizer st = new StringTokenizer(parse, " ");
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                int z = Integer.parseInt(st.nextToken());
                switch (start) {
                    case 'W': //wall
                        int[] sides = new int[6];
                        for (int i = 0; i < sides.length; i++) {
                            sides[i] = Integer.parseInt(st.nextToken());
                        }
                        Cube c = new Cube(x, y, z, sides);
                        cubes.add(c);
                        cubeGrid[x][y][z] = c;
                        break;
                    case 'P': //player
                        player = new Player(x, y, z, this);
                        break;
                    case 'G': //goal
                        goal = new Goal(x, y, z);
                        break;
                    case 'B': //box
                        Box b = new Box(x, y, z, this);
                        boxGrid[x][y][z] = b;
                        boxes.add(b);
                        break;
                    case 'L':
                        gui.mainF.setMaxActions(x);
                        gui.F1.setMaxActions(y);
                        gui.F2.setMaxActions(z);
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isFree(int x, int y, int z) {
        if(player.x == x && player.y == y && player.z == z) {
            return false;
        }
        return getBox(x, y, z) == null && getWall(x, y, z) == null;
    }

    public Box getBox(int x, int y, int z) {
        return boxGrid[x][y][z];
    }

    public Cube getWall(int x, int y, int z) {
        return cubeGrid[x][y][z];
    }

    //performs a game tick, updating, but not rendering everything.
    @Override
    public RoomBase update(long delta) {
        nextRoom = this;
        //Handle input
        mouseX = Mouse.getX();
        mouseY = Display.getHeight() - Mouse.getY(); //0,0 at top instaed of bottom
        currentMouseLeft = Mouse.isButtonDown(0);
        currentMouseRight = Mouse.isButtonDown(1);


        if (prevMouseLeft == false && currentMouseLeft == true) {// Mouse(left) pressed
        } else if (prevMouseLeft == true && currentMouseLeft == false) { // Mouse(left) released
            if (mouseX > Display.getHeight()) { //clicked in gui
                int mx = (int) mouseX - Display.getHeight();
                int my = (int) mouseY;
                gui.handleClicks(mx, my, player);
            }
        }
        if (prevMouseRight == false && currentMouseRight == true) { // Mouse(right) pressed
            if (mouseX < Display.getHeight()) {
                Mouse.setGrabbed(true);
            }
        } else if (prevMouseRight == true && currentMouseRight == false) { // Mouse(right) released
            Mouse.setGrabbed(false);
            if(mouseX > Display.getHeight()) {
                int mx = (int) mouseX - Display.getHeight();
                int my = (int) mouseY;
                gui.handleRClicks(mx, my, player);
            }
        }
        if (currentMouseRight) {
            if (mouseX < Display.getHeight()) {
                cam.rotate(Mouse.getDX(), Mouse.getDY());
            }
        }
        cam.move();
        prevMouseLeft = currentMouseLeft;
        prevMouseRight = currentMouseRight;
        checkWin();
        return nextRoom;
    }

    private void checkWin() {
        for (Box b : boxes) {
            if (b.x == goal.x && b.y == goal.y && b.z == goal.z &&
                    player.moveVec[0] == 0 && player.moveVec[1] == 0 && player.moveVec[2] == 0) {
                nextRoom = new WinRoom();
            }
        }
    }

    //draws everything
    @Override
    public void draw(long delta) {
        //draw main screen
        glEnable(GL_LIGHTING);
        glViewport(0, 0, Display.getHeight(), Display.getHeight());
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(90.0f, Display.getWidth() / Display.getHeight(), .1f, 1000f);
        cam.look();
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        for (Cube cube : cubes) {
            cube.draw();
        }
        for (Box box : boxes) {
            box.render();
        }
        player.draw();
        grid.draw();
        goal.render();

        //draw sidescreen
        glDisable(GL_LIGHTING);
        glViewport(Display.getHeight(), 0, Display.getWidth() - Display.getHeight(), Display.getHeight());
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0f, 400f, 800f, 0f, -1f, 1f);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        //draw gui
        gui.render();
    }
}
