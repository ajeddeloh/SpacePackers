package lwjglbase;

import java.nio.FloatBuffer;
import org.newdawn.slick.opengl.Texture;
import static org.lwjgl.opengl.GL11.*;

/*
 * Goal class that respresents were the go
 */
public class Goal {
    int x,y,z;
    static FloatBuffer vertBuffer, normalBuffer, texCoordBuffer;
    static Texture tex;
    static final String TEX_NAME = "goal.png";
    static final String MODEL_NAME = "box.obj";

    public Goal(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    static void init() {
        tex = Utils.loadTex(TEX_NAME);
        FloatBuffer[] array = Utils.loadModel(MODEL_NAME);
        vertBuffer = array[0];
        texCoordBuffer = array[1];
        normalBuffer = array[2];
    }

    public void render() {
        tex.bind();
        glVertexPointer(3, 0, vertBuffer);
        glNormalPointer(0, normalBuffer);
        glTexCoordPointer(2, 0, texCoordBuffer);

        glPushMatrix();
        glTranslatef(x, y, z);
        glScalef(.2f,.2f,.2f);
        glRotatef((System.currentTimeMillis()%10000)/3,0,1,0);
        glDrawArrays(GL_TRIANGLES, 0, vertBuffer.capacity() / 3);
        glPopMatrix();
    }
}
