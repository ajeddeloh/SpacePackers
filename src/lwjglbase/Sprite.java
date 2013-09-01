package lwjglbase;

import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.Texture;

/*
 * class all 2d things that are drawn inherit from. has a texture to draw and 
 * coordinates to draw it at. handles loading the texture and all that 
 */

public class Sprite {
    static FloatBuffer verts;
    FloatBuffer texCoords;
    float x, y, width, height;
    public Texture tex;
    float depth = 0;

    //checks if mouse at position (mx,my) clicks on this action
    public boolean checkClicked(float mx, float my) {
        return ((mx < x + width) && (mx > x) && (my < y + height) && (my > y));
    }

    //loads the texture
    void initTex(String input) {
        tex = Utils.loadTex(input);
        initTextCoords();
    }

    //creates the texture coords to draw
    void initTextCoords() {
        float[] t = {0, 0, tex.getWidth(), 0, tex.getWidth(), tex.getHeight(), 0, tex.getHeight()};
        texCoords = Utils.createFB(t);
    }

    //initializes the vertices which are static and the same for all 2d images
    static void init() {
        float[] v = {0,0, 1,0, 1,1, 0,1};
        verts = Utils.createFB(v);
    }
    
    //draws the sprite
    public void render() {
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glPushMatrix();
        tex.bind();
        glTranslatef(x, y, depth);
        glScalef(width,height,0);
        glVertexPointer(2, 0, verts);
        glTexCoordPointer(2, 0, texCoords);
        glDrawArrays(GL_QUADS, 0, 4);
        glPopMatrix();
    }

    //returns the texture this uses
    public Texture getTex() {
        return tex;
    }
}
