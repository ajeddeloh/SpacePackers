package lwjglbase;

import java.util.ArrayList;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;

/**
 * Base class for all menus. Menus only need to implement a addButtons and 
 * handleButtonClick function
 */

class MenuRoom extends Sprite implements RoomBase {
    
    String texFname;
    ArrayList<Button> buttons = new ArrayList<Button>();
    RoomBase nextRoom;
    boolean prevMouseLeft, prevMouseRight, currentMouseLeft, currentMouseRight;
    int mouseX = 0;
    int mouseY = 0;

    public void initSelf() {
        depth = -0.9f;
        initTex(texFname);
        x = 0;
        y = 0;
        width = 1200;
        height = 800;

        glDisable(GL_LIGHTING);
        glDisableClientState(GL_NORMAL_ARRAY);
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0f, 1200f, 800f, 0f, -1f, 1f);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        
        addButtons();
    }
    
    public void handleButtonClick(String name) {
        //this should be overridden
    }
    
    public void addButtons() {
        //this should be overridden
    }

    @Override
    public RoomBase update(long delta) {
        nextRoom = this;

        mouseX = Mouse.getX();
        mouseY = Display.getHeight() - Mouse.getY(); //0,0 at top instaed of bottom
        currentMouseLeft = Mouse.isButtonDown(0);
        currentMouseRight = Mouse.isButtonDown(1);
        
        if (prevMouseLeft == true && currentMouseLeft == false) { // Mouse(left) released
            for (int i = 0; i < buttons.size(); i++) {
                Button button = buttons.get(i);
                if(button.checkClicked(mouseX, mouseY)) {
                    handleButtonClick(button.name);
                }
            }
        }
        
        prevMouseLeft = currentMouseLeft;
        prevMouseRight = currentMouseRight;

        return nextRoom;
    }

    @Override
    public void draw(long delta) {
        render();
        for (Button button : buttons) {
            button.render();
        }
    }
}
