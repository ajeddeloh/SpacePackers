package lwjglbase;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.Texture;

/**
 * This is the object the player "programs" through the actions
 */
public class Player implements Runnable {

    public static final int MOVE_TIME = 300;//time to sleep between commands
    public static final int FALL_TIME = 150;//time to sleep between commands
    int x = 0;
    int y = 0;
    int z = 0;
    int[] moveVec = {0, 0, 0};//for moment
    long startTime = 0;
    long commandTime = 300;
    int originX = 0;
    int originY = 0;
    int originZ = 0;
    //these MUST be perpendicular to each other otherwise weird shit could 
    //happen while rendering. I swear matrices are voodoo magic.
    int[] up;
    int[] fwd;
    int[] right;
    int[] oFwd = {1, 0, 0};
    int[] oUp = {0, 1, 0};
    int[] oRight = {0, 0, 1};
    Level level;
    static FloatBuffer vertBuffer, normalBuffer, texCoordBuffer;
    static Texture tex;
    static String modelPath = "player.obj";
    int f1return = 0; //where f1 was called from
    int f2return = 0; //where f2 was called from
    int pointer = 0; //where we are in the program
    int f1pointer = 0; //where f1 is in the program
    int f2pointer = 0; //where f2 is in the program
    ArrayList<String> actions = new ArrayList<String>(); //commands to be excuted
    boolean running = false;
    boolean busy = false;
    FloatBuffer rotMatrix;

    //creates the player at x,y,z
    public Player(int x, int y, int z, lwjglbase.Level level) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.originX = x;
        this.originY = y;
        this.originZ = z;
        up = oUp.clone();
        fwd = oFwd.clone();
        right = oRight.clone();
        this.level = level;
        getRotMatrix();
    }

    //loads the 3d obj file model and the texture to use with it. called once.
    public static void init() {
        FloatBuffer[] arrays = Utils.loadModel(modelPath);
        vertBuffer = arrays[0];
        texCoordBuffer = arrays[1];
        normalBuffer = arrays[2];
        tex = Utils.loadTex("player.png");
    }

    //moves fwd, dealing with 
    void forward() {
        if (level.isFree(x + fwd[0], y + fwd[1], z + fwd[2])) {
            int[] down = Utils.negVector(up);
            if (level.isFree(x + fwd[0] + down[0], y + fwd[1] + down[1], z + fwd[2] + down[2])
                    && level.cubeGrid[x + down[0]][y + down[1]][z + down[2]] != null
                    && level.cubeGrid[x + down[0]][y + down[1]][z + down[2]].getSide(fwd) == Cube.METAL) {
                move();
                turnDown();
                move();
            } else {
                move();
            }
        } else {
            Cube inFront = level.cubeGrid[x + fwd[0]][y + fwd[1]][z + fwd[2]];
            if (inFront != null && inFront.getSide(Utils.negVector(fwd)) == Cube.METAL) {
                turnUp();
            }
        }
    }

    void move() {
        x += fwd[0];
        y += fwd[1];
        z += fwd[2];
        moveVec = fwd.clone();
        commandTime = MOVE_TIME;
        startTime = System.currentTimeMillis();
        Utils.sleep(MOVE_TIME);
        moveVec = new int[]{0, 0, 0};
    }

    void turnUp() {
        int[] oldFwd = fwd.clone();
        fwd = up.clone();
        up = Utils.negVector(oldFwd);
        getRotMatrix();
    }

    void turnDown() {
        int[] oldUp = up.clone();
        up = fwd.clone();
        fwd = Utils.negVector(oldUp);
        getRotMatrix();
    }

    //rotates left
    void turnLeft() {
        int[] newFwd = Utils.negVector(right);
        right = fwd.clone();
        fwd = newFwd;
        getRotMatrix();
    }

    //rotates right
    void turnRight() {
        int[] newRight = Utils.negVector(fwd);
        fwd = right.clone();
        right = newRight;
        getRotMatrix();
    }

    void antig() {
        up = Utils.negVector(up);
        right = Utils.negVector(right);
        getRotMatrix();
    }

    //returns if it's still falling
    private boolean fallStep() {
        int[] down = Utils.negVector(up);
        //out of bounds
        if (x + down[0] == -1 || x + down[0] == 10 || y + down[1] == -1 || y + down[1] == 10 || z + down[2] == -1 || z + down[2] == 10) {
            pointer = 10000; //exit
            return false;
        }
        if (level.isFree(x + down[0], y + down[1], z + down[2])) {
            x += down[0];
            y += down[1];
            z += down[2];
            moveVec = Utils.negVector(up);
            return true;
        }
        return false;
    }

    private void fall() {
        boolean falling = true;
        while (falling) {
            startTime = System.currentTimeMillis();
            commandTime = FALL_TIME;
            falling = fallStep();
            for (Box b : level.boxes) {
                falling |= b.fallStep();
            }
            if(falling) {
                Utils.sleep(FALL_TIME);
            }
            moveVec = new int[] {0,0,0};
        }
    }
    //resets the player

    void reset() {
        x = originX;
        y = originY;
        z = originZ;
        fwd = oFwd.clone();
        right = oRight.clone();
        up = oUp.clone();
        getRotMatrix();
        for (Box b : level.boxes) {
            b.reset();
        }
    }

    private void getRotMatrix() {
        float[] ret = {
            fwd[0], fwd[1], fwd[2], 0,
            up[0], up[1], up[2], 0,
            right[0], right[1], right[2], 0,
            0, 0, 0, 1};
        rotMatrix = Utils.createFB(ret);
    }

    //executes the action described by the input string
    public void executeAction() {
        if (pointer >= actions.size()) {
            running = false;
            pointer = 0;
            return;
        }
        String action = actions.get(pointer++);
        if (action.equals("forward")) {
            forward();
        } else if (action.equals("left")) {
            turnLeft();
        } else if (action.equals("right")) {
            turnRight();
        } else if (action.equals("exit")) {
            stop();
            return; //dont fall;
        } else if (action.equals("reset")) {
            reset();
        } else if (action.equals("f1")) {
            f1return = pointer;//know where to return to
            pointer = f1pointer;
            return; //dont fall or sleep
        } else if (action.equals("f2")) {
            f2return = pointer; //know where to return to
            pointer = f2pointer;
            return; //dont fall or sleep
        } else if (action.equals("return1")) {
            pointer = f1return;
        } else if (action.equals("return2")) {
            pointer = f2return;
        } else if (action.equals("push")) {
            push();
        } else if (action.equals("antig")) {
            antig();
        }
        fall();
    }

    //loads the given command arraylists
    public void loadActions(ArrayList<String> mainList, ArrayList<String> f1List, ArrayList<String> f2List) {
        actions = mainList;
        actions.add(0, "reset"); //tell it to reset at the start
        actions.add("exit");
        f1pointer = actions.size();
        actions.addAll(f1List);
        actions.add("return1");
        f2pointer = actions.size();
        actions.addAll(f2List);
        actions.add("return2");
    }

    //runs the thingy
    @Override
    public void run() {
        if (!running && !busy) { //single action
            System.out.println("single");
            busy = true;
            executeAction();
            busy = false;
        }
        while (running && !busy) {
            executeAction();
        }
        System.out.println("exiting thread");
    }

    private void push() {
        Box b = level.getBox(x + fwd[0], y + fwd[1], z + fwd[2]);
        if (b != null && level.isFree(x + 2 * fwd[0], y + 2 * fwd[1], z + 2 * fwd[2])) {
            b.move(fwd, Utils.negVector(up));
            move();
            b.moveVec = new int[] {0,0,0};
        }
    }

    public void setRunning(boolean b) {
        running = b;
    }

    public boolean isRunning() {
        return running;
    }

    //draws the player
    public void draw() {
        tex.bind();
        glVertexPointer(3, 0, vertBuffer);
        glNormalPointer(0, normalBuffer);
        glTexCoordPointer(2, 0, texCoordBuffer);
        float t = 1 - (((float) (System.currentTimeMillis() - startTime)) / commandTime);
        glPushMatrix();
        glTranslatef(x - moveVec[0] * t, y - moveVec[1] * t, z - moveVec[2] * t);
        glMultMatrix(rotMatrix);
        glTranslatef(.15f, -.27f, 0);//offset so it touches the ground
        glScalef(0.1f, 0.1f, 0.1f);
        glDrawArrays(GL_TRIANGLES, 0, vertBuffer.capacity() / 3);
        glPopMatrix();
    }

    void stop() {
        pointer = 10000;
    }
}
