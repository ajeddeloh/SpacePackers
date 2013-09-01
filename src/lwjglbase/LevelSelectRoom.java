package lwjglbase;

import java.io.File;
import java.util.ArrayList;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class LevelSelectRoom extends MenuRoom {

    UnicodeFont font = null;
    ArrayList<String> files = new ArrayList<String>();
    ArrayList<Button> unusedButtons = new ArrayList<Button>();
    int min = 0;
    int multiplier = 80;
    int baseY = 200;
    int baseX1 = 50;
    int baseX2 = 650;
    public LevelSelectRoom() {
        texFname = "lsbackground.png";

        try {
            font = new UnicodeFont("Fixedsys500c.ttf", 32, false, false);
            font.addAsciiGlyphs();
            font.addAsciiGlyphs();
            font.getEffects().add(new ColorEffect(java.awt.Color.GREEN));
            font.loadGlyphs();
        } catch (Exception e) {
            System.out.println("Error Loading Font");
        }
        initSelf();
    }

    @Override
    public void addButtons() {
        buttons.add(new Button("previous.png", "previous", 50, 75, false));
        buttons.add(new Button("next.png", "next", 650, 75, false));
        buttons.add(new Button("menu.png", "menu", 1000, 720, false));
        
        File dir = new File(LWJGLbase.LEVELS_PATH);
        File[] names = dir.listFiles();
        for (int i = 0; i < names.length; i++) {
            files.add(names[i].getName());
        }

        for (int i = 0; i < 12; i++) {
            if (i < 6) {
                buttons.add(new Button("play.png", "play" + i, baseX1, baseY + (multiplier * i), false));
            } else {
                buttons.add(new Button("play.png", "play" + i, baseX2, baseY + (multiplier * (i - 6)), false));
            }
        }
        if (files.size() < buttons.size()) {
            int btr = buttons.size() - (files.size() + 3); //+3 for prev, next, and menu
            for (int i = 0; i < btr; i++) {
                unusedButtons.add(buttons.remove(buttons.size() - 1));
            }
        }

    }

    @Override
    public void handleButtonClick(String name) {
        if ("next".equals(name)) {
            if (min + 6 < (files.size() - 6)) {
                min += 6;
                if (getNumFilesOnScreen() < 12) {
                    int btr = (files.size() % 12)/2;
                    for (int i = 0; i < btr; i++) {
                        unusedButtons.add(buttons.remove(buttons.size() - 1));
                    }
                }
            }
        } else if ("previous".equals(name)) {
            if (min - 6 >= 0) {
                min -= 6;
                
                if (getNumFilesOnScreen() == 12) {
                    int bta = unusedButtons.size();
                    for (int i = 0; i < bta; i++) {
                        buttons.add(unusedButtons.remove(0));
                    }
                }
            }
        } else if (name.startsWith("play")) {
            int level = Integer.parseInt(Character.toString(name.charAt(name.length() - 1)));
            nextRoom = new Level(files.get(level + min));
        } else if (name.equals("menu")) {
            nextRoom = new Menu();
        }

    }
    
    public int getNumFilesOnScreen(){
        return files.size() > min+12 ? 12  : files.size()%12;
    }

    @Override
    public void render() {
        super.render();
        font.drawString(40f, 20, "Level Select:");
        for (int i = min; i < files.size(); i++) {
            if (i < min + 6) {
                font.drawString(baseX1 + 200, (baseY+15)+ ((i - min) * multiplier), files.get(i));
            } else if (i >= min + 6 && i < min + 12) {
                font.drawString(850f, (baseY+15) + (((i - min) - 6) * multiplier), files.get(i));
            }
        }
    }
}
