package Tanks;

/**
 * Object represents the pointer
 */
public class Pointer {
    /** Declare an integer storing the X-coordinate of the current tank */
    private int x;

    /** Declare an integer storing the Y-coordinate of the current tank */
    private float y;

    /** Declare a variable storing the time that the turn of the current tank starts */
    private long startTime;

    /**
     * Update the X, Y coordinates of the current tank
     * @param x, X-coordinate of the tank
     * @param y, Y-coordinate of the tank
     */
    public void updateCoordinate(int x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Update the time that the player turn starts
     * @param time, the time that the player turn starts
     */
    public void updateStartTime(long time) {
        startTime = time;
    }

    /**
     * Draw the pointer
     * @param app, the App object which is currently using
     * @param time, the current time
     */
    public void draw(App app, long time) {
        if (time - startTime < 2000) {
            app.stroke(5);
            app.noFill();
            app.color(0);
            app.line(x, y - 40, x, y - 80);
            app.line(x, y - 40, x - 10, y - 60);
            app.line(x, y - 40, x + 10, y - 60);
        }
    }
}
