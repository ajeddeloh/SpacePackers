package lwjglbase;

import java.awt.Color;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.*;

/**
 * class that handles an opengl light (GL_LIGHT[0-7]) has all the attributes
 */
public class Light {
    final static int DIRECTION = 0;
    final static int POSITIONAL = 1;
    int id;
    FloatBuffer color;
    FloatBuffer pos;
    int type = -1;
    
    //creates a light. id must be GL_LIGHT[0-7]. 
    public Light(int id, int type, float x, float y, float z, Color col, float brightness) {
        this.id = id; //GL_LIGHT0-7
        float[] posAr = {x,y,z,(float)type};
        float[] colorAr = {(float)col.getRed()/255,(float)col.getGreen()/255,(float)col.getBlue()/255,(float)col.getAlpha()/255};
        this.color = Utils.createFB(colorAr);
        this.pos = Utils.createFB(posAr);
        
        glLight(id, GL_POSITION, pos);
        glLight(id, GL_SPECULAR, color);
        glLight(id, GL_DIFFUSE, color);
        
        if(type == POSITIONAL) {
            glLightf(id, GL_CONSTANT_ATTENUATION, 0f);
            glLightf(id, GL_LINEAR_ATTENUATION, 1/brightness);
            glLightf(id, GL_QUADRATIC_ATTENUATION, 1/brightness);
        }
        glEnable(id);
    }
    
    //enables this light
    void enable() {
        glEnable(id);
    }
    
    //disables this light
    void disable() {
        glDisable(id);
    }
}
