//package lwjglbase;
//
//import java.awt.Color;
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
//import java.nio.FloatBuffer;
//import java.nio.IntBuffer;
//import java.util.ArrayList;
//import java.util.StringTokenizer;
//import org.lwjgl.BufferUtils;
//import org.lwjgl.input.*;
//import org.lwjgl.opengl.Display;
//import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.util.glu.GLU.*;
//import org.newdawn.slick.opengl.Texture;
//
///*
// * this is the editor mode. Current put on hold until the main game stuff is 
// * done. Very non-finished code
// */
//
//public class Editor implements RoomBase {
//
//    boolean print = false;
//    FloatBuffer modelview;
//    FloatBuffer projection;
//    IntBuffer viewport;
//    Texture firstSprite;
//    ArrayList<Cube> cubes = new ArrayList<Cube>();
//    Button button;
//    int mouseX = 0;
//    int mouseY = 0;
//    Camera cam = new Camera(3f, 2f, 2f, 90f, 90f);
//    EditGui gui = new EditGui();
//    boolean prevMouseLeft, prevMouseRight, currentMouseLeft, currentMouseRight;
//    final int GRID_SIZE = 10;
//    Grid grid = new Grid(GRID_SIZE);
//    Cube[][][] cubeGrid = new Cube[GRID_SIZE][GRID_SIZE][GRID_SIZE];
//
//    //creates the editor
//    public Editor() {
//        initLights();
//        Cube.init();
//        Sprite.init();
//    }
//
//    //sets up the lights for the 3d part of rendering. They are directional not
//    //point source lights
//    private void initLights() {
//        for (int light = GL_LIGHT0; light < GL_LIGHT7; light++) {
//            glDisable(light); //disable all lights that are not out own
//        }
//        Light dir1 = new Light(GL_LIGHT0, Light.DIRECTION, 1f, 1f, 1f, Color.WHITE, 0f);
//        Light dir2 = new Light(GL_LIGHT1, Light.DIRECTION, -1f, -1f, 0f, Color.GRAY, 0f);
//
//        float[] white = {.4f, .4f, .4f, 1f};
//        glLightModel(GL_LIGHT_MODEL_AMBIENT, Utils.createFB(white));
//    }
//
//    //loads the level in from a file to allow loading of saved work. (saving not
//    //implemented
//    private void loadLevel(String in) {
//        try {
//            // Read from in and parse cubes
//            String parse = "";                                                               //Path to levels
//            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(LWJGLbase.LEVELS_PATH + in)));
//            while ((parse = br.readLine()) != null) {
//                int[] sides = new int[6];
//                StringTokenizer st = new StringTokenizer(parse, " ");
//                int x = Integer.parseInt(st.nextToken());
//                int y = Integer.parseInt(st.nextToken());
//                int z = Integer.parseInt(st.nextToken());
//                for (int i = 0; i < sides.length; i++) {
//                    sides[i] = Integer.parseInt(st.nextToken());
//                }
//                cubes.add(new Cube(x, y, z, sides));
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    //handles all user input and logic, no rendering
//    @Override
//    public void update(long delta) {
//        //Handle input
//        mouseX = Mouse.getX();
//        mouseY = Display.getHeight() - Mouse.getY(); //0,0 at top instaed of bottom
//        currentMouseLeft = Mouse.isButtonDown(0);
//        currentMouseRight = Mouse.isButtonDown(1);
//
//        if (prevMouseLeft == false && currentMouseLeft == true) {// Mouse(left) pressed
//            if (mouseX > Display.getHeight()) { //clicked in gui
//                float mx = mouseX - Display.getHeight();
//                float my = mouseY;
//                gui.handleClicks(mx, my);
//            } else { //clicked in 3d area, DOES NOT WORK, HERE BE DRAGONS
//                if (gui.mode == EditGui.ADD_MODE) {
//                    print = true;
//                    glViewport(0, 0, Display.getHeight(), Display.getHeight());
//                    glMatrixMode(GL_PROJECTION);
//                    glLoadIdentity();
//                    gluPerspective(90.0f, 1, .1f, 1000f);
//                    glMatrixMode(GL_MODELVIEW);
//                    glLoadIdentity();
//                    cam.look();
//
//                    modelview = BufferUtils.createFloatBuffer(16);
//                    projection = BufferUtils.createFloatBuffer(16);
//                    viewport = BufferUtils.createIntBuffer(16);
//                    FloatBuffer result = BufferUtils.createFloatBuffer(3);
//
//                    glGetFloat(GL_MODELVIEW_MATRIX, modelview);
//                    glGetFloat(GL_PROJECTION_MATRIX, projection);
//                    glGetInteger(GL_VIEWPORT, viewport);
//                    
//                    gluProject(1f, 0f, 0f, modelview, projection, viewport, result);
//                    Utils.printFloatBuffer(projection);
//                    Utils.printFloatBuffer(modelview);
//                    System.out.println(result.get(0) + " " + result.get(1) + " " + result.get(2));
//                }
//            }
//        } else if (prevMouseLeft == true && currentMouseLeft == false) { // Mouse(left) released
//        } else if (prevMouseRight == false && currentMouseRight == true) { // Mouse(right) pressed
//            if (mouseX < Display.getHeight()) {
//                Mouse.setGrabbed(true);
//            }
//        } else if (prevMouseRight == true && currentMouseRight == false) { // Mouse(right) released
//            Mouse.setGrabbed(false);
//        }
//        if (currentMouseRight) {
//            if (mouseX < Display.getHeight()) {
//                cam.rotate(Mouse.getDX(), Mouse.getDY());
//            }
//        }
//        cam.move();
//        prevMouseLeft = currentMouseLeft;
//        prevMouseRight = currentMouseRight;
//    }
//
//    //renders all the 3d area, buttons, gui, etc
//    @Override
//    public void draw(long delta) {
//        //draw main screen
//        glEnable(GL_LIGHTING);
//        glEnable(GL_NORMAL_ARRAY);
//        glViewport(0, 0, Display.getHeight(), Display.getHeight());
//        glMatrixMode(GL_PROJECTION);
//        glLoadIdentity();
//        gluPerspective(90.0f, 1, .1f, 1000f);
//        glMatrixMode(GL_MODELVIEW);
//        glLoadIdentity();
//        cam.look();
//        for (Cube cube : cubes) {
//            cube.draw();
//        }
//        grid.draw();
//
//
//        //for debugging
//        if (print) {
//            print = false;
//            FloatBuffer modelview2 = BufferUtils.createFloatBuffer(16);
//            FloatBuffer projection2 = BufferUtils.createFloatBuffer(16);
//            IntBuffer viewport2 = BufferUtils.createIntBuffer(16);
//            glGetFloat(GL_MODELVIEW_MATRIX, modelview2);
//            glGetFloat(GL_PROJECTION_MATRIX, projection2);
//            glGetInteger(GL_VIEWPORT, viewport2);
//            Utils.printFloatBuffer(modelview2);
//            Utils.printFloatBuffer(projection2);
//            System.out.println(modelview2.equals(modelview));
//            System.out.println(projection2.equals(projection));
//            System.out.println(viewport2.equals(viewport));
//        }
//
//        //draw sidescreen
//        glDisable(GL_LIGHTING);
//        glDisableClientState(GL_NORMAL_ARRAY);
//        glViewport(Display.getHeight(), 0, Display.getWidth() - Display.getHeight(), Display.getHeight());
//        glMatrixMode(GL_PROJECTION);
//        glLoadIdentity();
//        glOrtho(0f, 400f, 800f, 0f, -1f, 1f);
//        glMatrixMode(GL_MODELVIEW);
//        glLoadIdentity();
//        //draw gui
//        gui.render();
//    }
//
//    @Override
//    public RoomBase getNextRoom() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//}
