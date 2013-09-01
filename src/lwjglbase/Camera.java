package lwjglbase;

import org.lwjgl.input.Keyboard;
import static org.lwjgl.util.glu.GLU.*;
/**
 * Camera class. Deals with movement of the camera and adjusting the view, as a
 * camera would do.
 */
public class Camera {

    float speed = 0.1f;
    float x, y, z, xAng, yAng, dx, dy, dz;
    float ox, oy, oz;

    //creates a new camera at x,y,z looking in direction xAng side to side and
    //direction yang up and down
    public Camera(float x, float y, float z, float xAng, float yAng) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.ox = x;
        this.oy = y;
        this.oz = z;
        this.xAng = xAng;
        this.yAng = yAng;
        calcPoints(xAng, yAng);
    }

    //calculates a unit vector representing the direction which the camera looks
    private void calcPoints(float xa, float ya) {
        dx = Utils.cos(xa) * Utils.sin(ya);
        dy = Utils.cos(ya);
        dz = Utils.sin(xa) * Utils.sin(ya);
    }

    //actually change the matrices to move the projection
    void look() {
        calcPoints(xAng, yAng);
        gluLookAt(x, y, z, x + dx, y + dy, z + dz, 0, 1, 0);
    }

    //moves the camera. LCtrl slows down. WASD movement, space/Lshift = up/down
    void move() {
        calcPoints(xAng, yAng);
        float spd = speed;
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            spd /= 4;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            x += dx * spd;
            z += dz * spd;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            x -= dx * spd;
            z -= dz * spd;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            calcPoints(xAng - 90, yAng);
            x += dx * spd;
            z += dz * spd;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            calcPoints(xAng + 90, yAng);
            x += dx * spd;
            z += dz * spd;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            y -= spd / 2;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            y += spd / 2;
        }
    }

    //rotates the camera in accordance to the change in mouse position, dxa/dya
    void rotate(float dxa, float dya) {
        xAng += dxa / 5;
        yAng -= dya / 5;
        if (yAng > 179) {
            yAng = 179;
        }
        if (yAng < 1) {
            yAng = 1;
        }
    }
}
