package Tanks;

import java.util.*;

/** Represent the Projectile class */
public class Projectile {

    /** Declare the Tank object that the projectile belongs to */
    private final Tank tank;

    /** Declare the X-coordinate */
    private float x;

    /** Declare the Y-coordinate */
    private float y;

    /** Declare the velocity of the projectile */
    private final int velocity;

    /** Declare a counter (count from when the projectile is fired) */
    private int count;

    /** Declare the Wind object */
    private final Wind windObj;

    /** Declare the acceleration of the gravity */
    private final float gravityAccel;

    /** Declare the angle of the projectile */
    private final double angle;

    /** Declare a boolean to check whether the projectile has landed */
    private boolean landed;

    /** Declare a boolean to check whether the projectile has exploded */
    private boolean exploded;

    /** Declare a boolean to check whether the statistics are calculated */
    private boolean isCompleted;

    /** Declare the Explosion object for the projectile */
    private final Explosion explosion;

    /** Declare the App object runing the file */
    private final App app;

    /**
     * Constructor of the Projectile class
     * Assign values to attributes
     * @param tank The Tank object that the projectile belongs to
     * @param windObj The Wind object affecting the way the projectile flies
     * @param app The App object running the program
     */
    public Projectile(Tank tank, Wind windObj, App app) {
        this.tank = tank;
        this.app = app;
        this.x = this.tank.getTankTurret()[2];
        this.y = this.tank.getTankTurret()[3];
        this.angle = this.tank.getAngle();
        this.windObj = windObj;
        this.count = 0;
        this.gravityAccel = (float) 3.6;
        this.velocity = (int) Math.round(2 + 0.16 * this.tank.getPower());
        this.explosion = new Explosion(200);
        this.exploded = false;
        this.landed = false;
        this.isCompleted = false;

    }

    /**
     * Update the coordinates of the projectile once called
     */
    public void updateProjectile() {
        if (!this.landed)
        {   
            float windAccel = (float) (windObj.getSpeed() * 0.03);
            float velocityX = (float) (velocity * Math.cos(this.angle));
            float velocityY = (float) (velocity * -Math.sin(this.angle));
            this.x += velocityX + this.count * (windAccel * 1/30);
            this.y += velocityY + this.count * (gravityAccel * 1/30);
            count++;
        }
    }

    /**
     * Check whether the projectile has landed
     * @param terrainList the HashMap storing Terrain Objects that is currently used
     */
    public void checkHit(HashMap<Integer, Terrain> terrainList) {
        if ((x >= 0 && x <= 864) && (y <= 640))
        {
            Terrain xTerrain = terrainList.get((int) x);
            if (xTerrain.getY() <= y) 
            {
                this.landed = true;
            }
        }
        else
        {
            this.landed = true;
            this.exploded = true;
        }
    }

    /**
     * Update the explosion of the projectile when landed
     */
    public float[] updateExplosion() {
        return this.explosion.updateExplosion(this.x, this.y);
    }

    /**
     * Check whether the projectile has exploded
     */
    public void checkExploded() {
        if (this.explosion.isCompleted())
        {
            this.exploded = true;
        }
    }

    /**
     * Return the X-coordinates of the projectile
     * @return the float storing the X-coordinate
     */
    public float getX()
    {
        return this.x;
    }

    /**
     * Return the Y-coordinates of the projectile
     * @return the float storing the Y-coordinate
     */
    public float getY()
    {
        return this.y;
    }

    /**
     * Check whether the projected has landed
     * @return the boolean (false means has not landed yet, otherwise true)
     */
    public boolean isLanded()
    {
        return this.landed;
    }

    /**
     * Check whether the projected has exploded
     * @return the boolean (false means has not exploded yet, otherwise true)
     */
    public boolean isExploded()
    {
        return this.exploded;
    }

    /**
     * Check whether the projected has exploded and all statistics have been calculated
     * @return the boolean (false means all statistics have been calculated, otherwise true)
     */
    public boolean isCompleted()
    {
        return this.isCompleted;
    }

    /**
     * Set the isCompleted boolean to true
     */
    public void setCompleted()
    {
        this.isCompleted = true;
    }

    /**
     * Return the Tank object
     * @return the Tank object
     */
    public Tank getTank()
    {
        return this.tank;
    }
}
