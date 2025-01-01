package Tanks;
import java.util.*;

/** Represents Tank data */
public class Tank {

    /** Declare the X-coordinate of the Tank */
    private int x;

    /** Declare the Y-coordinate of the Tank */
    private float y;

    /** Declare X-coordinate of the Tank turret */
    private float turretX;

    /** Declare the Y-coordinate of the Tank turret */
    private float turretY;

    /** Declare the Y-coordinate that the Tank falls to (if falling) */
    private float yFalling;

    /** Declare the character of the Tank object */
    private final char character;

    /** Declare the RGB values of the colour of the Tank */
    private int[] colour;

    /** Declare the health of the tank */
    private int health;

    /** Declare the power of the tank */
    private int power;

    /** Declare the score of tank */
    private int score;

    /** Declare the number of remaining parachutes */
    private int parachute;

    /** Declare the fuel of the Tank */
    private int fuel;

    /** Declare the angle between the turret with the X-axis */
    private double angle;

    /** Declare the Wind object */
    private final Wind windObj;

    /** Declare the boolean checking whether the Tank is falling */
    private boolean falling;

    /** Declare the boolean checking whether the Tank has exploded */
    private boolean exploded;

    /** Declare the Projectile object that hit the Tank, or hit the terrain causing the Tank to fall down */
    private Projectile hitBy;

    /** Declare the Explosion object for the Tank */
    private final Explosion explosion;

    /** Declare the health that the Tank lost when falling down */
    private int damageWhenFalling;

    /** Declare a boolean to check whether the statistics are calculated */
    private boolean completed;

    /** Declare the ArrayList storing all the projectiles belong to the Tank */
    private final ArrayList<Projectile> activeProjectiles = new ArrayList<>();

    /** Declare the boolean checking whether the Tank has shield */
    private boolean shield;

    /** Declare the App object running this program */
    private final App app;

    /** Declare the teleport object for the tank */
    private final Teleport teleport;

    /**
     * Constructor of the Tank class
     * Assign values to attributes
     * @param x the value of X-coordinate, ranging from 0 to 864
     * @param y the value of Y-coordinate, ranging from 0 to 640
     * @param character the character of the Tank
     * @param colour RGB values of the colour of the tank
     * @param windObj the Wind object affecting the tank
     * @param app the App object running the program
     */
    public Tank(int x, float y, char character, int[] colour, Wind windObj, App app) {
        this.app = app;
        this.x = x;
        this.y = y;
        this.character = character;
        this.colour = colour;
        this.health = 100;
        this.fuel = 250;
        this.power = 50;
        this.parachute = 1;
        this.score = 0;
        this.windObj = windObj;
        this.falling = false;
        this.explosion = new Explosion(200);
        this.exploded = false;
        this.damageWhenFalling = 0;
        this.angle = Math.PI / 2;
        this.shield = false;
        this.teleport = new Teleport(this.colour);
        calculateTurretCoordinates();
    }

    /**
     * Calculate the statistics for the tank turret
     */
    private void calculateTurretCoordinates() {
        int turretLength = 15;
        this.turretX = (float) (Math.cos(this.angle)* turretLength + this.x);
        this.turretY = (float) (-Math.sin(this.angle)* turretLength + this.y - 4);
    }

    /**
     * Return the X-coordinate of the Tank
     * @return the X-coordinate of the Tank
     */
    public int getX()
    {
        return this.x;
    }

    /**
     * Set the new X-coordinate to the current tank
     * @param x, new X-coordinate
     */
    public void setX(int x)
    {
        this.x = x;
    }

    /**
     * Return the Y-coordinate of the Tank
     * @return the Y-coordinate of the Tank
     */
    public float getY()
    {
        return this.y;
    }

    /**
     * Assign the Y-coordinate to the new value Y
     * @param y the new Y-coordinates
     */
    public void setY(float y)
    {
        this.y = y;
    }

    /**
     * Return the character of the tank
     * @return the character of the tank
     */
    public char getCharacter()
    {
        return this.character;
    }

    /**
     * Return the RGB values of the colour of the tank
     * @return the RGB values of the colour of the tank
     */
    public int[] getColour()
    {
        return this.colour;
    }

    /**
     * Assign a new colour for the tank
     */
    public void setColour(int[] colour) {
        this.colour = colour;
    }

    /**
     * Return the fuel of the tank
     * @return the fuel of the tank
     */
    public int getFuel()
    {
        return this.fuel;
    }

    /**
     * Return the power of the tank
     * @return the power of the tank
     */
    public int getPower()
    {
        return this.power;
    }

    /**
     * Return the number of remaining parachutes of the tank
     * @return the number of remaining parachutes of the tank
     */
    public int getParachute()
    {
        return this.parachute;
    }

    /**
     * Change the number of remaining parachute
     * @param change, the change in parachute, could be positive or negative
     */
    public void setParachute (int change) {
        if (this.parachute + change >= 0) {
            this.parachute += change;
        }
    }

    /**
     * Return the statistics of the tank's turret
     * @return the statistics of the tank's turret
     */
    public float[] getTankTurret() {
        float[] result = new float[4];

        result[0] = this.x;
        result[1] = this.y - 4;
        result[2] = this.turretX;
        result[3] = this.turretY;
        return result;
    }

    /**
     * Return the score of the Tank
     * @return the score of the Tank
     */
    public int getScore()
    {
        return this.score;
    }

    /**
     * Return the health of the tank
     * @return the health of the tank
     */
    public int getHealth()
    {
        return this.health;
    }

    /**
     * Return the angle between the tank turret with the X-axis
     * @return the angle between the tank turret with the X-axis
     */
    public double getAngle()
    {
        return angle;
    }

    /**
     * Return the ArrayList storing all the Projectile objects belong to the tank
     * @return the ArrayList storing all the Projectile objects belong to the tank
     */
    public ArrayList<Projectile> getProjectile()
    {
        return this.activeProjectiles;
    }

    /**
     * Return the Projectile hitting the tank, or hitting the terrain causing the tank to fall down
     * @return the Projectile hitting the tank, or hitting the terrain causing the tank to fall down
     */
    public Projectile getHitByProjectile()
    {
        return this.hitBy;
    }

    /**
     * Calculate the health of the tank
     * @param health the change in health in either negative or positive
     */
    public void setHealth(int health) {
        if (this.health + health <= 0)
        {
            this.health = 0;
        }
        else if (this.health + health >= 100)
        {
            this.health = 100;
        }
        else
        {
            this.health += health;
        }
        
        if (this.power > this.health) 
        {
            this.power = this.health;
        }
    }

    /**
     * Calculate the score of the tank
     * @param score the change in the score in either positive or negative
     */
    public void setScore(int score)
    {
        this.score += score;
    }

    /**
     * Move the tank
     * @param direction the direction the tank will move
     * @param y the new Y-coordinate value for the tank (calculated base on the Y-coordinate of the terrain)
     */
    public void move(String direction, float y) {
        if (this.fuel > 0 && !this.falling)
        {   
            int FPS = App.FPS;
            float timeBetweenFrames = (float) (1.0)/FPS;
            float speed = (float) 60;

            if (direction.equals("left"))
            {
                if (this.x >= 2)
                {
                    this.x -= (int) (speed * timeBetweenFrames);
                    setY(y);
                    calculateTurretCoordinates();
                    setFuel(-1);
                }
            }
            else if (direction.equals("right"))
            {
                if (this.x <= 862)
                {   
                    this.x += (int) (speed * timeBetweenFrames);
                    setY(y);
                    calculateTurretCoordinates();
                    setFuel(-1);
                }
            }

        }
    }

    /**
     * Rotate the tank's turret
     * @param direction the direction that the turret will rotate
     */
    public void rotateTurret(String direction) {
        int FPS = App.FPS;
        float timeBetweenFrames = (float) (1.0)/FPS;
        float speed = (float) 3;
        float change = speed * timeBetweenFrames;
        if (direction.equals("up"))
        {
            if (this.angle + change <= Math.PI) {
                this.angle += change;
            } else {
                this.angle = Math.PI;
            }
            calculateTurretCoordinates();

        }
        else if (direction.equals("down"))
        {   
            if (this.angle - change >= 0) {
                this.angle -= change;
            } else {
                this.angle = 0;
            }
            calculateTurretCoordinates();
        }
    }

    /**
     * Increase or decrease the power
     * @param key the key that the player press
     */
    public void setPower(String key) {
        int increment = (int) (36/30);
        if (key.equals("S")) {
            if (this.power - increment >= 0) {
                this.power -= increment;
            }
            if (this.power < 0) {
                this.power = 0;
            }
        } else if (key.equals("W")) {
            if (this.power + increment <= this.health) {
                this.power += increment;
            } else {
                this.power = this.health;
            }
        }
    }

    /**
     * Calculate a new fuel
     * @param fuel the change in fuel in either negative or positive
     */
    public void setFuel(int fuel) {
        if (this.fuel + fuel <= 0) {
            this.fuel = 0;
        }
        else {
            this.fuel += fuel;
        }
    }

    /**
     * Create a projectile and fire it
     */
    public void fire() {
        Projectile currentProjectile = new Projectile(this, this.windObj, this.app);
        this.activeProjectiles.add(currentProjectile);
    }

    /**
     * Start the falling period
     * @param newY The new Y-coordinate of the terrain after changing
     * @param projectile The Projectile object hitting the terrain causing the tank falling down
     */
    public void setFalling(float newY, Projectile projectile) {
        this.yFalling = newY;
        this.hitBy = projectile;
        if (!this.isFalling())
        {
            this.damageWhenFalling = 0;
        }
        this.falling = true;
        
    }

    /**
     * Check whether the Tank is falling
     * @return the boolean checking whether the Tank is falling
     */
    public boolean isFalling()
    {
        return this.falling;
    }

    /**
     * Update the X and Y coordinates of the tank while falling
     * @return the lost health when the tank is landed
     */
    public int falling() {
        int fallingRate;
        if (this.parachute > 0) {
            fallingRate = 60/App.FPS;
            if (this.y + fallingRate < this.yFalling) {
                this.y += fallingRate;
            }
            else if (this.y < this.yFalling && this.y + fallingRate > this.yFalling) {
                this.y = this.yFalling;
            }
            else {
                setParachute(-1);
                this.falling = false;
            }
        }
        else {
            fallingRate = 120/App.FPS;
            if (this.y + fallingRate < this.yFalling) {
                this.y += fallingRate;
                damageWhenFalling += fallingRate;
            }
            else if (this.y < this.yFalling && this.y + fallingRate > this.yFalling) {
                damageWhenFalling += (int) Math.abs(this.yFalling - this.y);
                this.y = this.yFalling;
            }
            else {
                if (this.damageWhenFalling > this.health) {
                    this.damageWhenFalling = this.health;
                }
                setHealth(-damageWhenFalling);
                calculateTurretCoordinates();
                return damageWhenFalling;
            }
        }
        calculateTurretCoordinates();
        return 0;
    }

    /**
     * Check whether the tank is alive
     * @return the boolean checking whether the tank is alive
     */
    public boolean isAlive()
    {
        return this.health > 0;
    }

    /**
     * Update the animation of the explosion when the tank is dead
     * @return the radius of 3 circle in the animation of the explosion
     */
    public float[] updateExplosion() {
        float[] radius = new float[3];
        if (this.y >= 640) {
            this.explosion.changeRadius(15);
            this.explosion.updateExplosion(this.x, this.y);
        }
        radius = this.explosion.updateExplosion(this.x, this.y);
        return radius;
    }

    /**
     * Check whether the explosion animation has completed
     * @return the boolean checking whether the explosion animation has completed
     */
    public boolean completeExploding() {
        this.exploded = this.explosion.isCompleted();
        return this.exploded;
    }

    /** Check the position of the tank
     * Set the health to 0 if the tank is out of the window
     */
    public void checkTankBelowScreen() {
        if (this.y >= 640)
        {
            this.health = 0;
        }
    }

    /**
     * Return the boolean checking whether all the statistics has been calculated after the tank is dead
     * @return the boolean checking whether all the statistics has been calculated after the tank is dead
     */
    public boolean isCompleted()
    {
        return this.completed;
    }

    /**
     * Set the state of the Tank object to completed (true)
     */
    public void setCompleted()
    {
        this.completed = true;
    }

    /**
     * Return the Explosion object
     * @return the Explosion object
     */
    public Explosion getExplosion()
    {
        return this.explosion;
    }

    /**
     * Spend 20 points to increase 20 health
     */
    public void repairKit() {
        if (this.score >= 20)
        {
            if (this.health < 100 && this.health + 20 > 100) {
                setScore(-20);
                setHealth(100 - this.health);
            } else if (this.health < 100) {
                setScore(-20);
                setHealth(20);
            }
        }
    }

    /**
     * Spend 10 points to increase 200 health
     */
    public void addFuel() {
        if (this.score >= 10)
        {
            setScore(-10);
            setFuel(200);
        }
    }

    /**
     * Spend 20 points to get shield
     */
    public void enableShield() {
        if (this.score >= 20)
        {
            if (!this.shield)
            {
                this.score -= 20;
                this.shield = true;
            }
        }
    }

    /**
     * Check whether the current tank has shield
     * @return the boolean checking whether the current tank has shield
     */
    public boolean getShield() {
        return this.shield;
    }

    /**
     * Disable the shield when is hit
     */
    public void disableShield()  {
        this.shield = false;
    }

    /**
     * Check the location of teleport and complete teleporting
     */
    public void teleport() {
        if (this.teleport.getX() != this.x)
        {
            this.teleport.setTeleport();
        }

        if (!this.teleport.completedTeleport())
        {
            this.teleport.resetTeleport();
        }
        else
        {
            setScore(-15);
            setX(this.teleport.getX());
            setY(this.teleport.getY());
            calculateTurretCoordinates();
        }
        this.teleport.resetTeleport();
    }

    /**
     * Checking whether the current tank is using teleport
     * @return the boolean checking whether the current tank is using teleport
     */
    public Teleport getTeleport()
    {
        return this.teleport;
    }
}
