package Tanks;

/**
 * Represent the explosion of tanks and projectiles
 */
public class Explosion {
    /** Declare a float number storing the X-coordinate of the centre of the explosion */
    float x;

    /** Declare a float number storing the Y-coordinate of the centre of the explosion */
    float y;

    /** Declare a float number storing the maximum of the radius*/
    float maxRadius;

    /** Declare a float number storing the duration*/
    float duration;

    /** Declare a variable storing the start time in millisecond*/
    long startTime;

    /** Declare a boolean to check whether the explosion has completed */
    boolean completed;

    /**
     * Constructor of the explosion object
     * @param duration, the duration of the explosion
     */
    public Explosion(float duration) {
        this.maxRadius = 30;
        this.duration = duration;
        this.startTime = 0;
        this.completed = false;
    }

    /**
     * Update the radius of the explosion
     * @param x, the X-coordinate of the explosion
     * @param y, the Y-coordinate of the explosion
     * @return the current radius
     */
    public float[] updateExplosion(float x, float y) {
        this.x = x;
        this.y = y;
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }
        float elapsedTime = System.currentTimeMillis() - startTime;
        float[] radius = new float[3];
        float redRadius = ((float) maxRadius) * elapsedTime / duration;
        float orangeRadius = ((float) 0.5 * maxRadius) * elapsedTime / duration;
        float yellowRadius = ((float) 0.2 * maxRadius) * elapsedTime / duration;

        radius[0] = redRadius;
        radius[1] = orangeRadius;
        radius[2] = yellowRadius;

        if (elapsedTime >= duration) {
            completed = true;
        }
        return radius;
    }

    /**
     * Change the maximum value of the radius
     * @param newRadius, the new value of the radius
     */
    public void changeRadius(float newRadius) {
        this.maxRadius = newRadius;
    }

    /**
     * Get the maximum radius of the explosion
     * @return the maximum radius of the explosion
     */
    public int getMaxRadius() {
        return (int)this.maxRadius;
    }

    /** Return a boolean checking whether the tank is exploding, if start time is not 0, the object is exploding */
    public boolean isExploding() {
        return !(startTime == 0);
    }

    /**
     * Return true if the explosion is completed
     * @return a boolean checking whether the explosion is completed
     */
    public boolean isCompleted() {
        return this.completed;
    }
}