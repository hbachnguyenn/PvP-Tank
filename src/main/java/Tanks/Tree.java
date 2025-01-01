package Tanks;

import java.util.*;

/** Represent Tree data */
public class Tree {

    /** Declare X-coordinate value of a tree object */
    private final int x;

    /**
     * Constructor of the Tree class
     * Assign values to attributes
     * @param x the value of X-coordinate, ranging from 0 to 895
     */
    public Tree (int x) {
        Random random = new Random();
        int randomIndex = random.nextInt(31) - 15;
        int xInit = x + 16;
        this.x = xInit + randomIndex;
    }

    /**
     * Return the X value
     * @return the integer storing the X value
     */
    public float getX()
    {
        return this.x;
    }
}
