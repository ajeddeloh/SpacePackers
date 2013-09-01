package lwjglbase;

/*
 * all rooms must have an update and draw function. allows us to have a variable
 * to keep track of the current room without having a ton of variables
 */

public interface RoomBase {
    public RoomBase update(long delta);
    public void draw(long delta);
}
