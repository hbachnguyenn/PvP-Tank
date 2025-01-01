package Tanks;

import java.util.*;

/** Represent wind data */
public class Wind {

    /** Declare the previous speed */
    private int previousSpeed;

    /** Declare the speed after randomising */
    private int speed;

    /**
     * Constructor for the Wind class
     * Assign values to attributes
     */
    public Wind(Random random) {
        this.previousSpeed = random.nextInt(71) - 35;
        this.speed = this.previousSpeed;

    }

    /**
     * Return the speed after randomising
     * @return the integer storing wind speed
     */
    public int getSpeed()
    {
        return this.speed;
    }

    /**
     * Calculate the wind speed for next turn
     */
    public void setRandomSpeed(Random random) {
        int difference = random.nextInt(11) - 5;
        this.speed = this.previousSpeed + difference;
        this.previousSpeed = this.speed;
    }
}
