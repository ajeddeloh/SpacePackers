package lwjglbase;

/**
 * Displays the team screen
 */
public class TeamScreen extends MenuRoom{
    long startTime = 0;
    public TeamScreen() {
        texFname = "TeamScreen.png";
        initSelf();
        startTime = System.currentTimeMillis();
        
    }

    @Override
    public RoomBase update(long delta) {
        render();
        if(System.currentTimeMillis()-startTime > 1000){
            return new Menu();
        }
        return this;
    }
}
