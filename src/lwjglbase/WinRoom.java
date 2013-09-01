package lwjglbase;

import org.lwjgl.input.Mouse;

/**
 * Room to go to when you win
 */
public class WinRoom extends MenuRoom {
    public WinRoom() {
        texFname = "winScreen.png";
        initSelf();
        Mouse.setGrabbed(false);
    }
    
    @Override
    public void addButtons() {
        buttons.add(new Button("menu.png", "menu", 540, 560, false));
    }
    
    @Override
    public void handleButtonClick(String clicked) {
        if(clicked.equals("menu")) {
            nextRoom = new Menu();
        }
    }
}
