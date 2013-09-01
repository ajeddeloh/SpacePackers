package lwjglbase;

import java.util.ArrayList;

/*
 * This is the function in the game sense, not programming. It holds the actions
 * to be excuted by the robot
 */
public class Function {

    public static final int X_SIZE = 8;
    public static final int Y_SIZE = 2;
    int x, y;
    public static final int WIDTH = 320;
    public static final int HEIGHT = 80;
    Action[][] actions = new Action[X_SIZE][Y_SIZE];

    //returns if the fucntion is clicked by a mouse at (xi,yi)
    public boolean checkClicked(int xi, int yi) {
        return (xi > x && xi < (x + WIDTH) && yi > y && yi < (y + HEIGHT));
    }

    //creates a function at x,y
    Function(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    void setMaxActions(int max) {
        int blanks = X_SIZE * Y_SIZE - max;
        int i = X_SIZE - 1;
        int j = Y_SIZE - 1;
        for (int k = 0; k < blanks; k++) {
            actions[i--][j] = new Action(x + (i + 1) * 40, y + j * 40, null);
            if (i < 0) {
                j--;
                i = X_SIZE - 1;
                if (j < 0) {
                    break;
                }
            }
        }
    }

    //adds an action to the function in the slot that xx,yy falls in. It assumes
    //that xx,yy is within the area of the function
    public void addAction(Button base, int xx, int yy) {
        //40 is button width
        xx = (xx - x) / 40;
        yy = (yy - y) / 40;
        if (actions[xx][yy] != null && "blank".equals(actions[xx][yy].getName())) {
            return;
        }
        Action newAction = new Action(x + xx * 40, y + yy * 40, base);
        actions[xx][yy] = newAction;
    }

    //draws all the actions. Does not draw the function body. that is part of
    //the background image
    void draw() {
        for (Action[] actL : actions) {
            for (Action act : actL) {
                if (act != null) {
                    act.render();
                }
            }
        }
    }

    //returns a ArrayList<String> of the action names
    public ArrayList<String> getActions() {
        ArrayList<String> actionQ = new ArrayList<String>(); //actionQ=actionQueue
        //dont use foreach loops, it will give the transpose of the array
        for (int i = 0; i < Y_SIZE; i++) {
            for (int j = 0; j < X_SIZE; j++) {
                Action act = actions[j][i];
                if (act != null) {
                    actionQ.add(act.getName());
                }
            }
        }
        return actionQ;
    }

    public void deleteAction(int mx, int my) {
        mx = (mx - x) / 40;
        my = (my - y) / 40;
        if (actions[mx][my] != null && !"blank".equals(actions[mx][my].getName())) {
            actions[mx][my] = null;
        }
    }
}
