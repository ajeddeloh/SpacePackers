package lwjglbase;

import java.util.ArrayList;

/*
 *  The gui for the main level. Has the action bank and functions. Handles its
 *  own clicks as well
 */
public class LevelGui extends Sprite {

    final String bgFName = "guiBackground.png";
    ArrayList<Button> buttons = new ArrayList<Button>();
    Button selected = null;
    Thread playerThread = null;
    Function mainF = new Function(40, 270);
    Function F1 = new Function(40, 400);
    Function F2 = new Function(40, 520);

    //creates the gui
    public LevelGui() {
        depth = -.9f; //put it at the back
        initTex(bgFName);
        x = 0;
        y = 0;
        width = tex.getImageWidth();
        height = tex.getImageHeight();
        setupButtons();
        selected = buttons.get(0);
    }

    //deals with any clicks that happen within the gui
    public void handleClicks(int mx, int my, Player player) {
        for (Button button : buttons) {
            if (button.checkClicked(mx, my)) {
                if (button.isSelectable()) {
                    selected = button;
                }
                if (button.name.equals("run") && !player.isRunning()) {
                    player.loadActions(mainF.getActions(), F1.getActions(), F2.getActions());
                    player.setRunning(true);
                    playerThread = new Thread(player);     
                    playerThread.start();
                }
                if (button.name.equals("stop")) {
                    System.out.println("stopping");
                    player.setRunning(false); //this will kill the thread
                }

                if (button.name.equals("reset")) {
                    player.running = false;
                    player.pointer = 0;
                    player.reset();
                }
                //refactor this if anyone has time. horrible to read
                if (button.name.equals("step") && player.running == false) {
                    if (player.pointer < player.actions.size() || player.actions.isEmpty()) {//not in the middle of a program 
                        if(player.pointer == 0 || player.actions.isEmpty()) { //need to load the actions
                            System.out.println("laoding");
                            player.loadActions(mainF.getActions(), F1.getActions(), F2.getActions());
                            player.executeAction(); //do reset
                        }
                        player.running = false;
                        playerThread = new Thread(player);
                        playerThread.start();
                    }
                }
                
                if(button.name.equals("menu")) {
                    player.level.nextRoom = new Menu();
                }
            }
        }

        if (mainF.checkClicked(mx, my)) {
            mainF.addAction(selected, mx, my);
        }
        if (F1.checkClicked(mx, my)) {
            F1.addAction(selected, mx, my);
        }
        if (F2.checkClicked(mx, my)) {
            F2.addAction(selected, mx, my);
        }
    }

    public void handleRClicks(int mx, int my, Player player) {
        if (mainF.checkClicked(mx, my)) {
            mainF.deleteAction(mx, my);
        }
        if (F1.checkClicked(mx, my)) {
            F1.deleteAction(mx, my);
        }
        if (F2.checkClicked(mx, my)) {
            F2.deleteAction(mx, my);
        }
    }

    //draws all actions and buttons (through the functions) and the background
    @Override
    public void render() {
        super.render();
        for (Button button : buttons) {
            button.render();
        }
        if (selected != null) {
            selected.renderSelected();
        }

        mainF.draw();
        F1.draw();
        F2.draw();
    }

    //adds the buttons
    private void setupButtons() {
        //action buttons
        buttons.add(new Button("forward.png", "forward", 40, 120, true));
        buttons.add(new Button("left.png", "left", 80, 120, true));
        buttons.add(new Button("right.png", "right", 120, 120, true));
        buttons.add(new Button("push.png", "push", 160, 120, true));
        buttons.add(new Button("antig.png", "antig", 200, 120, true));
        buttons.add(new Button("f1.png", "f1", 240, 120, true));
        buttons.add(new Button("f2.png", "f2", 280, 120, true));

        //other buttons
        buttons.add(new Button("run.png", "run", 30, 640, false));
        buttons.add(new Button("stop.png", "stop", 210, 640, false));
        buttons.add(new Button("step.png", "step", 30, 720, false));
        buttons.add(new Button("reset.png", "reset", 210, 720, false));
        buttons.add(new Button("menu.png", "menu", 220, 20, false));
    }
}
