package lwjglbase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.opengl.Texture;
import static org.lwjgl.opengl.GL11.*;

/*
 * Helper class. Full of random helpful functions we got tired of typing that 
 * dont seem to fit in any one place
 */

public class Utils {
    
    //creates a FloatBuffer from the array, makes code elsewhere tidy
    static FloatBuffer createFB(float[] input) {
        FloatBuffer buf = BufferUtils.createFloatBuffer(input.length);
        buf.put(input);
        buf.rewind();
        return buf;
    }
    
    //sin, without having Math. infront of it, in degrees
    static float sin(float degrees) {
        return (float)Math.sin(degrees*Math.PI/180);
    }

    //cos, without having Math. infront of it, in degrees
    static float cos(float degrees) {
        return (float)Math.cos(degrees*Math.PI/180);
    }
    
    /*
     * Splits the input string over the delimiter then parses each part to a 
     * float. similar to ruby: in.split(delim).map {|i| i.to_f}
     */
    static float[] stringToFloatArray(String in, String delim) {
        String[] strings = in.split(delim);
        float[] ret = new float[strings.length];
        for (int i = 0; i < strings.length; i++) {
            ret[i] = Float.parseFloat(strings[i]);            
        }
        return ret;
    }

    //prints the floatbuffer, not pretty, useful for debugging matrix issues
    static void printFloatBuffer(FloatBuffer in) {
        System.out.print("[");
        for (int i = 0; i < in.limit(); i++) {
            System.out.print(String.format("%01f",in.get(i))+" ");
            if((i+1)%4 == 0) {
                System.out.print("]\n[");
            }
        }
        System.out.println("]");
    }

    //loads a texture and returns it.
    static Texture loadTex(String fname) {
        try {
            return InternalTextureLoader.get().getTexture(LWJGLbase.SPRITES_PATH + fname, false, GL_LINEAR);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static int[] negVector(int[] forward) {
        int[] ret = new int[3];
        for (int i = 0; i < 3; i++) {
            ret[i] = -forward[i];
        }
        return ret;
    }

    static void printVector(int[] v) {
        System.out.println("[" + v[0] + " " + v[1] + " " + v[2] + "]");
    }
    
    /*
     * Loads on obj model. This REQUIRES a properly formed OBJ file and will
     * probably crash in really ugly ways if it doesn't get a good file. Files 
     * *MUST* have normals AND texture coordinates. Also, files *MUST* be
     * triangulated, this wont work with quads. Can use lots of mem when 
     * loading big models. (multiple MB), so make sure to run with -Xmx and
     * -Xms adjusted accordingly
     */
    static FloatBuffer[] loadModel(String modelPath) {
        //declare these in here so they get garbage collected
        long time = System.currentTimeMillis();
        ArrayList<float[]> vertices = new ArrayList<float[]>();
        ArrayList<float[]> normals = new ArrayList<float[]>();
        ArrayList<float[]> texCoords = new ArrayList<float[]>();
        ArrayList<String> faces = new ArrayList<String>();
        
        FloatBuffer vertBuffer, texCoordBuffer, normalBuffer;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(LWJGLbase.MODELS_PATH + modelPath)));
            while ((modelPath = br.readLine()) != null) { //modelpath is reused... this is hard to read, damnit josh
                if (modelPath.startsWith("v ")) {
                    vertices.add(Utils.stringToFloatArray(modelPath.substring(2), " "));
                } else if (modelPath.startsWith("vn ")) {
                    normals.add(Utils.stringToFloatArray(modelPath.substring(3), " "));
                } else if (modelPath.startsWith("vt ")) {
                    texCoords.add(Utils.stringToFloatArray(modelPath.substring(3), " "));
                } else if (modelPath.startsWith("f ")) {
                    faces.add(modelPath.substring(2));
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading model file.");
        }

        //create the actual buffers
        vertBuffer = BufferUtils.createFloatBuffer(faces.size() * 9);
        normalBuffer = BufferUtils.createFloatBuffer(faces.size() * 9);
        texCoordBuffer = BufferUtils.createFloatBuffer(faces.size() * 6);

        //parse the face strings
        for (String face : faces) {
            String[] verts = face.split(" ");
            for (String vert : verts) {
                String[] str = vert.split("/");
                vertBuffer.put(vertices.get(Integer.parseInt(str[0]) - 1));
                normalBuffer.put(normals.get(Integer.parseInt(str[2]) - 1));
                texCoordBuffer.put(texCoords.get(Integer.parseInt(str[1]) - 1));
            }
        }
        vertBuffer.rewind();
        texCoordBuffer.rewind();
        normalBuffer.rewind();
        FloatBuffer[] ret = {vertBuffer,texCoordBuffer,normalBuffer};
        return ret;
    }
    
    static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            System.out.println("Something failed at sleeping");
        }
    }
}
