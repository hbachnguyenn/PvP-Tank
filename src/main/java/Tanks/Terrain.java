package Tanks;

/** Represent Terrain data */
public class Terrain
{
    /** Declare X value of a Terrain object */
    private final int x;

    /** Declare Y value of a Terrain object */
    private float y;

    /**
     * Constructor of the Terrain class
     * Assigning values to attributes
     * @param x the value of X-coordinate, ranging from 0 to 895
     * @param y the value of Y-coordinate, ranging from 0 to 200
     */
    public Terrain(int x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * return the X value of a Terrain object
     * @return the X value (integer)
     */
    public int getX() {
        return this.x;
    }

    /**
     * return the Y value of a Terrain object
     * @return the Y value (float)
     */
    public float getY() {
        return this.y;
    }

    /**
     * Set a new value for Y
     * @param y new value y for terrain
     */
    public void setY(float y) {
        this.y = y;
    }
}
