package lwjglbase;

import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.*;

/*
 * This is for a button. Each button only holds only its name and image
 *  The other classes read this name and deal with it accordingly.
 * NO FUNCTIONS SPECIFIC TO SPECIFIC BUTTONS SHOULD BE ADDED
 */
public class Button extends Sprite {

    String name;
    boolean selectable = false;
    static FloatBuffer colors;

    //initializes the color array used when drawing the border around the
    //selected button
    static void init() {
        float[] colorarray = {1f, 0f, 0f, 1f, 1f, 0f, 0f, 1f, 1f, 0f, 0f, 1f, 1f, 0f, 0f, 1f};
        colors = Utils.createFB(colorarray);
    }

    //Creates a new button, loading the image file image, with name name at x,y
    public Button(String image, String name, float x, float y, boolean selectable) {
        initTex(image);
        this.x = x;
        this.y = y;
        this.width = tex.getImageWidth();
        this.height = tex.getImageHeight();
        this.name = name;
        this.selectable = selectable;
    }
    //if you can't figure this out you deserve to die
    String getName() {
        return name;
    }

    //draws the selected square around button
    public void renderSelected() {
        //draw the border
        glBindTexture(GL_TEXTURE_2D, 0);
        glPushMatrix();
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glTranslatef(x, y, depth + 1);
        glScalef(width, height, 0);
        glVertexPointer(2, 0, verts);
        glColorPointer(4, 0, colors);

        glDrawArrays(GL_LINE_LOOP, 0, 4);

        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_COLOR_ARRAY);
        glColor4f(1f, 1f, 1f, 1f); //required on some *cough* intel *cough*
        //drivers that dont implement opengl correctly
        glPopMatrix();
    }
    
    public boolean isSelectable() {
        return selectable;
    }
}
