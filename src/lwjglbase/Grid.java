package lwjglbase;

import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.*;

/**
 * 3d grid with grid size 1 - Useful for finding where in 3d space object are
 */

public class Grid {
    FloatBuffer verticesBuffer;
    int size;
    
    //creates the grid, including initializing vertices (that part should be 
    //moved to a static function
    public Grid(int size) {
        this.size = size;
        float[] verts = new float[(size+1)*6*2];
        for (int i = 0; i < (size+1); i++) {
            verts[i*12+0] = (float)i; //x
            verts[i*12+1] = 0;//y
            verts[i*12+2] = 0;//z
            verts[i*12+3] = (float)i; //x
            verts[i*12+4] = 0;//z
            verts[i*12+5] = (float)size;//z
            verts[i*12+6] = 0; //x
            verts[i*12+7] = 0;//y
            verts[i*12+8] = (float)i;//z
            verts[i*12+9] = (float)size; //x
            verts[i*12+10] = 0;//y
            verts[i*12+11] = (float)i;//z
        }
        verticesBuffer = Utils.createFB(verts);
    }

    //draws the grid.
    void draw() {
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_NORMAL_ARRAY);
        glVertexPointer(3, 0, verticesBuffer);
        
        glPushMatrix();
        glTranslatef(-0.5f, -0.5f, -0.5f);
        glDrawArrays(GL_LINES, 0, verticesBuffer.capacity()/3);
        glPopMatrix();
        
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
    }
}
