package lwjglbase;

import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.Texture;

/*
 * this is the class for the box you push
 */
public class Box {

    int x, y, z;
    int ox, oy, oz;
    int[] down = {0, -1, 0}; //nowhere is down.
    int[] oDown = {0, -1, 0}; //nowhere is down.
    int[] moveVec = {0,0,0};
    Level level;
    long startTime;
    long commandTime=300;
    static FloatBuffer vertBuffer, normalBuffer, texCoordBuffer;
    static Texture tex;
    static final String TEX_NAME = "monkeyTex.png";
    static final String MODEL_NAME = "box.obj";

    static void init() {
        tex = Utils.loadTex(TEX_NAME);
        FloatBuffer[] array = Utils.loadModel(MODEL_NAME);
        vertBuffer = array[0];
        texCoordBuffer = array[1];
        normalBuffer = array[2];
    }

    public void reset() {
        level.boxGrid[x][y][z] = null;
        x = ox;
        y = oy;
        z = oz;
        level.boxGrid[x][y][z] = this;
        down = oDown.clone();
    }

    public Box(int x, int y, int z, Level level) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.ox = x;
        this.oy = y;
        this.oz = z;
        this.level = level;
    }

    //returns if it's still falling
    public boolean fallStep() {
        //out of bounds
        if (x + down[0] == -1 || x + down[0] == 10 || y + down[1] == -1 || y + down[1] == 10 || z + down[2] == -1 || z + down[2] == 10) {
            level.player.pointer = 10000;
            moveVec = new int[] {0,0,0};
            return false;
        }
        if (level.isFree(x + down[0], y + down[1], z + down[2])) {
            level.boxGrid[x][y][z] = null;
            x += down[0];
            y += down[1];
            z += down[2];
            level.boxGrid[x][y][z] = this;
            
            moveVec = down;
            commandTime = Player.FALL_TIME;
            startTime = System.currentTimeMillis();
            return true;
        }
        moveVec = new int[] {0,0,0};
        return false;
    }

    public void move(int[] vec, int[] down) {
        startTime = System.currentTimeMillis();
        commandTime = Player.MOVE_TIME;
        moveVec = vec.clone();
        level.boxGrid[x][y][z] = null;
        x += vec[0];
        y += vec[1];
        z += vec[2];
        this.down = down.clone();
        level.boxGrid[x][y][z] = this;
    }

    public void render() {
        tex.bind();
        glVertexPointer(3, 0, vertBuffer);
        glNormalPointer(0, normalBuffer);
        glTexCoordPointer(2, 0, texCoordBuffer);

        float t = 1 - (((float) (System.currentTimeMillis() - startTime)) / commandTime);
        glPushMatrix();
        glTranslatef(x - moveVec[0] * t, y - moveVec[1] * t, z - moveVec[2] * t);
        glDrawArrays(GL_TRIANGLES, 0, vertBuffer.capacity() / 3);
        glPopMatrix();
    }
}
