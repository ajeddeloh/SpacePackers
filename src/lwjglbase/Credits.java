package lwjglbase;

/**
 * Room that has credits
 */
public class Credits extends MenuRoom {
    public Credits() {
        texFname = "creditsScreen.png";
        initSelf();
    }
    
    @Override
    public void addButtons() {
        buttons.add(new Button("menu.png", "menu", 1000, 720, false));
    }
    
    @Override
    public void handleButtonClick(String clicked) {
        if(clicked.equals("menu")) {
            nextRoom = new Menu();
        }
    }
}
