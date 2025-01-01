package Tanks;
import java.util.*;

/**
 * Ojbect represents the teleport ability
 */
public class Teleport
{
    /** Declare an integer storing the X-coordinate of the teleport pointer */
    private int x;

    /** Declare a float number storing the Y-coordinate of the teleport pointer */
    private float y;

    /** Declare an array of integer storing the RGB values of the teleport pointer */
    private final int[] colour;

    /** Declare the boolean checking whether the teleport object is in the choosing location period */
    private boolean chooseLocation;

    /** Declare the boolean checking whether the teleport has completed */
    private boolean teleport;

    /**
     * Constructor of the teleport object
     * @param colour, the RGB values of the color of the current tanks
     */
    public Teleport(int[] colour) {
        this.colour = colour;
        this.chooseLocation = false;
        this.teleport = false;
        this.x = -1;
        this.y = -1;
    }

    /**
     * Return the X-coordinate
     * @return the X-coordinate
     */
    public int getX() {
        return this.x;
    }

    /**
     * Return the Y-coordinate
     * @return the Y-coordinate
     */
    public float getY() {
        return this.y;
    }

    /**
     * Set the new coordinates of the teleport pointer when choosing the location
     * @param direction, the direction that the teleport pointer moves
     * @param terrainList, the terrain ArrayList of the current round
     */
    public void setCoordinates(String direction, HashMap<Integer, Terrain> terrainList) {
        if (direction.equals("left"))
        {
            if (this.x >= 1)
            {
                this.x--;
                this.y = terrainList.get(this.x).getY();
            }
        }

        if (direction.equals("right"))
        {
            if (this.x <= 863)
            {
                this.x++;
                this.y = terrainList.get(this.x).getY();
            }
        }
    }

    /**
     * Return the boolean checking whether the teleport is in the choosing location period
     * @return the boolean checking whether the teleport is in then choosing location period
     */
    public boolean isChoosingLocation()
    {
        return this.chooseLocation;
    }

    /**
     * Return the boolean checking whether the teleport has completed
     * @return the boolean checking whether the teleport has completed
     */
    public boolean completedTeleport() {
        return this.teleport;
    }

    /**
     * Change to the choosing location period
     * @param x, the new X that the pointer move to
     * @param terrainList, the current terrain list
     */
    public void setChoosingLocation(int x, HashMap<Integer, Terrain> terrainList) {
        this.x = x;
        this.y = terrainList.get(x).getY();
        this.chooseLocation = true;

    }

    /**
     * Set the boolean teleport to true
     */
    public void setTeleport()
    {
        this.teleport = true;
    }

    /**
     * Reset the teleport object
     */
    public void resetTeleport() {
        this.chooseLocation = false;
        this.teleport = false;
    }

    /**
     * Draw the teleport pointer
     * @param app, the app which is currently used
     */
    public void draw(App app) {
        if (isChoosingLocation() && !completedTeleport())
        {
            app.stroke(colour[0], colour[1], colour[2]);
            app.noFill();
            app.strokeWeight(3);
            app.line(this.x, this.y + 2, this.x, this.y + 8);
            app.line(this.x, this.y - 2, this.x, this.y - 8);
            app.line(this.x + 2, this.y, this.x + 8, this.y);
            app.line(this.x - 2, this.y, this.x - 8, this.y);
        }
    }
}
