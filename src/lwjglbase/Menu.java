package lwjglbase;

import org.lwjgl.input.Mouse;

/**
 * Displays menu
 */
public class Menu extends MenuRoom {
    public Menu () {
        texFname = "menuBackground.png";
        initSelf();
        Mouse.setGrabbed(false); 
    }
    
    @Override
    public void addButtons() {
        buttons.add(new Button("play.png","play",900,300,false));
        buttons.add(new Button("credits.png","credits",900,380,false));
        buttons.add(new Button("exit.png","exit",900,460,false));
    }
    
    @Override
    public void handleButtonClick(String clicked) {
        if(clicked.equals("exit")) {
            nextRoom = null;
        } else if(clicked.equals("play")) {
            nextRoom = new LevelSelectRoom();
        } else if(clicked.equals("credits")) {
            nextRoom = new Credits();
        }
    }
}
