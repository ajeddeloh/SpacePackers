package lwjglbase;

import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.Texture;

/**
 * Cube class. can have textures. used to build levels
 */

public class Cube {
    public static final int CONC = 0; //concrete
    public static final int METAL = 1;
    float x, y, z;
    float size = 0.5f;
    //-x, +x,-y,+y,-z,+z
    int[] sides = {METAL, CONC, CONC, CONC, CONC, CONC};
    static Texture tex;
    static float[] vertices = {
        -0.5f, -0.5f, -0.5f, //-X
        -0.5f, 00.5f, -0.5f,
        -0.5f, 00.5f, 00.5f,
        -0.5f, -0.5f, 00.5f,
        00.5f, -0.5f, -0.5f, //+X
        00.5f, 00.5f, -0.5f,
        00.5f, 00.5f, 00.5f,
        00.5f, -0.5f, 00.5f,
        -0.5f, -0.5f, -0.5f, //-Y
        00.5f, -0.5f, -0.5f,
        00.5f, -0.5f, 00.5f,
        -0.5f, -0.5f, 00.5f,
        -0.5f, 00.5f, -0.5f, //+Y
        00.5f, 00.5f, -0.5f,
        00.5f, 00.5f, 00.5f,
        -0.5f, 00.5f, 00.5f,
        -0.5f, -0.5f, -0.5f, //-Z
        -0.5f, 00.5f, -0.5f,
        00.5f, 00.5f, -0.5f,
        00.5f, -0.5f, -0.5f,
        -0.5f, -0.5f, 00.5f, //+Z
        -0.5f, 00.5f, 00.5f,
        00.5f, 00.5f, 00.5f,
        00.5f, -0.5f, 00.5f};
    static float[] normals = {
        -1, 0, 0,
        -1, 0, 0,
        -1, 0, 0,
        -1, 0, 0,
        1, 0, 0,
        1, 0, 0,
        1, 0, 0,
        1, 0, 0,
        0, -1, 0,
        0, -1, 0,
        0, -1, 0,
        0, -1, 0,
        0, 1, 0,
        0, 1, 0,
        0, 1, 0,
        0, 1, 0,
        0, 0, -1,
        0, 0, -1,
        0, 0, -1,
        0, 0, -1,
        0, 0, 1,
        0, 0, 1,
        0, 0, 1,
        0, 0, 1};
    float[] texCoord = new float[2 * 4 * 6];
    static FloatBuffer verticesBuffer;
    static FloatBuffer normalBuffer;
    FloatBuffer texCoordBuffer; //not static

    //creates a new cube at x,y,z with sides textured according to sides[]
    public Cube(float x, float y, float z, int[] sides) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.sides = sides;
        size = 1;
        setupTexCoords(sides);
    }

    //creates a new cube with all sides as the first texture
    public Cube(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        setupTexCoords(sides);
    }

    //creates a new cube with all sides as the first texture and variable size.
    //useful for debugging/marking certain 3d coordinates
    public Cube(float x, float y, float z, float size) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
        setupTexCoords(sides);
    }

    //initializes/loads the textures, vertices, and normals
    public static void init() {
        tex = Utils.loadTex("wallTex.png");
        verticesBuffer = Utils.createFB(vertices);
        normalBuffer = Utils.createFB(normals);
    }

    public int getSide(int[] input) {
        if(input[0] == -1) {
            return sides[0];
        } else if (input[0] == 1) {
            return sides[1];
        } else if (input[1] == -1) {
            return sides[2];
        } else if (input[1] == 1) {
            return sides[3];
        } else if (input[2] == -1) {
            return sides[4];
        } else {
            return sides[5];
        }
    }

    //draws the cube.
    void draw() {
        glPushMatrix();
        glTranslatef(x, y, z);
        glScalef(size, size, size);
        tex.bind();
        glVertexPointer(3, 0, verticesBuffer);
        glNormalPointer(0, normalBuffer);
        glTexCoordPointer(2, 0, texCoordBuffer);
        glDrawArrays(GL_QUADS, 0, 24);
        glPopMatrix();
    }

    //sets up the texture coordinates so it draws with the correct sides. The
    //textures are actually one big texture and this changes where it reads from
    //it.
    private void setupTexCoords(int[] sides) {
        float numTex = tex.getImageWidth() / tex.getImageHeight();
        float stepSize = tex.getWidth() / numTex;
        System.out.println();
        for (int i = 0; i < sides.length; i++) {
            texCoord[i * 8 + 0] = sides[i] * stepSize;//x top left
            texCoord[i * 8 + 1] = 0f;//y top left

            texCoord[i * 8 + 2] = (sides[i] + 1) * stepSize; //x top right
            texCoord[i * 8 + 3] = 0f; //y top right

            texCoord[i * 8 + 4] = (sides[i] + 1) * stepSize; //x bottom right
            texCoord[i * 8 + 5] = tex.getHeight(); //y bottom right

            texCoord[i * 8 + 6] = (sides[i]) * stepSize; //x bottom left
            texCoord[i * 8 + 7] = tex.getHeight(); //y bottom left
        }
        texCoordBuffer = Utils.createFB(texCoord);
    }
}
