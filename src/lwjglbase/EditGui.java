package lwjglbase;

import java.util.ArrayList;

/*
 * Editor Gui - deals with all the buttons and such for the editor. Isn't used
 * as the editor is put on hold until the rest of the game is done
 */

public class EditGui extends Sprite{
    final static int ADD_MODE = 0;
    final static int DEL_MODE = 1;
    final static int MOD_MODE = 2;
    
    int mode = ADD_MODE;
    final String bgFName = LWJGLbase.SPRITES_PATH+"editGui.png";
    ArrayList<Button> buttons = new ArrayList<Button>();

    //creates a new editor gui
    public EditGui() {
        depth = -.9f; //put it at the back
        initTex(bgFName);
        x = 0;
        y = 0;
        width = tex.getImageWidth();
        height = tex.getImageHeight();
        setupButtons();
    }
    
    //handles clicks and make sure that button presses get dealt with
    public void handleClicks(float mx, float my) {
        for (Button button : buttons) {
            if(button.checkClicked(mx, my)) {
                String name = button.name;
                if("add".equals(name)) { //"add" first incase of null pointer
                    mode = ADD_MODE;
                } else if("delete".equals(name)) {
                    mode = DEL_MODE;
                } else if("modify".equals(name)) {
                    mode = MOD_MODE;
                }
            }
        }
    }

    //draws all the buttons and background
    @Override
    public void render() {
        super.render();
        for (Button button : buttons) {
            button.render();
        }
    }

    //adds the buttons
    private void setupButtons() {
        buttons.add(new Button("addButton.png","add",40,80,false));
        buttons.add(new Button("delButton.png","delete",200,80,false));
        buttons.add(new Button("modButton.png","modify",40,160,false));
    }
}
