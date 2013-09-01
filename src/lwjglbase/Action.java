package lwjglbase;
/*
 * Class that hold the info for each action and handles drawing each action once
 * they are placed in the functions.
 */

public class Action extends Sprite {

    String action; //String to determine what this action does

    //Takes in the x and y position to render as well as the button it is
    //representing. E.g. A "forward" Button creates a "forward" action
    Action(int x, int y, Button base) {
        this.x = 40 * (x / 40);
        this.y = y;
        width = 40;
        height = 40;
        if (base != null) {//valid action
            this.tex = base.getTex();
            this.action = base.getName();
        } else { //blank action
            this.tex = Utils.loadTex("nullAction.png");
            this.action = "blank";
        }
        initTextCoords();
    }

    //returns the name identifying what this action does
    String getName() {
        return action;
    }
}
