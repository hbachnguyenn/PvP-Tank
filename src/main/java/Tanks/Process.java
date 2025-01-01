package Tanks;
import java.util.*;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;

/** Represent Process object data */
public class Process extends App
{
    /** Declare the ArrayList storing Setting object for each round */
    private final ArrayList<Setting> settingList = new ArrayList<>();

    /** Declare the ArrayList storing all players' character in one round in order */
    private final ArrayList<Character> playersInOrder = new ArrayList<>();

    /** Declare the ArrayList storing all players' character in JSON file in order */
    private final ArrayList<Character> tankKeyArray = new ArrayList<>();

    /** Declare the ArrayList storing the active Projectile object of all players */
    private final ArrayList<Projectile> activeProjectile = new ArrayList<>();

    /** Declare an integer representing the round (starting from 1) */
    private int round;

    /** Declare an integer representing the player turns */
    private int player;

    /** Declare an integer representing the number of */
    private int totalPlayers;

    /** Declare an integer representing the alive player in the current round */
    private int alivePlayer;

    /** Declare a boolean checking whether the game has ended */
    private boolean endGame = false;

    /** Declare a pointer pointing to the tank */
    private final Pointer pointer = new Pointer();

    private long endTime = 0;

    /** Declare the App object */
    private final App app;

    /**
     * The constructor of Process object
     * @param app The App object used for the game
     * @param config The JSON object got from the config path
     */
    public Process(App app, JSONObject config) {
        JSONArray levels = config.getJSONArray("levels");
        JSONObject playerColours = config.getJSONObject("player_colours");
        createSettingList(levels, playerColours);

        this.round = 1;
        this.player = 1;

        for (char ch = 'A'; ch <= 'I'; ch++)
        {
            tankKeyArray.add(ch);
        }

        for (char ch = '0'; ch <= '9'; ch++)
        {
            tankKeyArray.add(ch);
        }
        this.app = app;
        this.setup();
    }

    /**
     * Create the Setting object for each round
     * @param levels the JSON array storing data for each round
     * @param playerColours JSON object storing the players' character and colour
     */
    private void createSettingList(JSONArray levels, JSONObject playerColours) {
        for (int i = 0; i < levels.size(); i++) 
        {
            JSONObject levelJsonObject = levels.getJSONObject(i);
            Setting setting = new Setting(this.app, levelJsonObject, playerColours);
            settingList.add(setting);
        }
    }

    /**
     * Setup for the next round after the current round has ended
     */
    public void setup() {
        playersInOrder.clear();
        activeProjectile.clear();
        Setting setting = this.settingList.get(round - 1);
        Layout layout = setting.getLayout();
        HashMap<Character, Tank> tankList = layout.getTankList();
        for (char key : tankKeyArray) {
            if (tankList.containsKey(key)) {
                playersInOrder.add(key);
            }
        }
        this.totalPlayers = playersInOrder.size();
        calculateAlivePlayer();
        this.pointer.updateStartTime(System.currentTimeMillis());
    }

    /**
     * return the Setting object for the current round
     * @return a Setting object for the current round
     */
    public Setting getCurrentSetting() {
        return this.settingList.get(round - 1);
    }

    /**
     * Return the ArrayList storing all Setting objects of all rounds
     * @return the ArrayList storing all Setting objects of all rounds
     */
    public ArrayList<Setting> getSettingList()
    {
        return this.settingList;
    }

    /**
     * Return the round number
     * @return the round number
     */
    public int getCurrentRound()
    {
        return this.round;
    }

    /**
     * Return the number of alive players
     * @return the number of alive players
     */
    public int getAlivePlayer()
    {
        return this.alivePlayer;
    }

    /**
     * Calculate the alive player in the current round
     */
    public void calculateAlivePlayer() {
        this.alivePlayer = 0;
        HashMap <Character, Tank> tankList = getCurrentSetting().getLayout().getTankList();
        for (int i = 0; i < tankList.size(); i++)
        {
            Tank currentTank = tankList.get(playersInOrder.get(i));
            if (!currentTank.isCompleted() || !currentTank.getExplosion().isExploding())
            {
                this.alivePlayer++;
                System.out.println(this.alivePlayer);
            }
        }
    }

    /**
     * Set the turn to the next player
     */
    public void setNextPlayer() {
        HashMap <Character, Tank> tankList = getCurrentSetting().getLayout().getTankList();
        do {
            player++;
            if (player > totalPlayers) {
                player = 1;
            }

            if (totalPlayers <= 1) {
                break;
            }
        } while (!tankList.get(playersInOrder.get(player - 1)).isAlive());
    }

    /**
     * Switch the round to the following one and adjust every parameter to the subsequent round's specifications.
     */
    public void setNextRound() {
        if (this.round == settingList.size())
        {
            this.endGame = true;
        }
        else
        {
            this.activeProjectile.clear();
            HashMap <Character,Tank> tankListCurrentMap = getCurrentSetting().getLayout().getTankList();
            round++;
            HashMap <Character,Tank> tankListNextMap = getCurrentSetting().getLayout().getTankList();
            player = 1;
            for (char key : tankKeyArray) {
                if (tankListNextMap.containsKey(key)) {
                    Tank nextMapTank = tankListNextMap.get(key);
                    if (tankListCurrentMap.containsKey(key)) {
                        Tank currentMapTank = tankListCurrentMap.get(key);
                        int currentScore = currentMapTank.getScore();
                        nextMapTank.setScore(currentScore);
                        int[] colour = currentMapTank.getColour();
                        nextMapTank.setColour(colour);
                    }
                } else {
                    continue;
                }
            }

        }
    }

    /**
     * Get the current Tank object
     * @return the Tank object
     */
    public Tank getPlayer() {
        Setting currentSetting = getCurrentSetting();
        return currentSetting.getLayout().getTankList().get(playersInOrder.get(player - 1));
    }

    /**
     * Return the ArrayList storing active projectiles
     * @return the ArrayList storing active projectiles
     */
    public ArrayList<Projectile> getActiveProjectiles()
    {
        return activeProjectile;
    }

    /**
     * Move the tank to the left side when the left arrow key is pressed
     */
    public void leftArrow() {
        if (!isEndGame()) {
            Teleport currentTeleport = getPlayer().getTeleport();
            if (currentTeleport.isChoosingLocation())
            {
                if (getPlayer().getX() - 1 >= 0)
                {
                    HashMap<Integer, Terrain> terrainList = getCurrentSetting().getLayout().getTerrainList();
                    currentTeleport.setCoordinates("left", terrainList);
                }
            }
            else
            {
                int x = getPlayer().getX() - 2;
                if (x >= 0)
                {
                    HashMap<Integer, Terrain> terrainList = getCurrentSetting().getLayout().getTerrainList();
                    Terrain currentTerrain = terrainList.get(x);
                    float y = currentTerrain.getY();
                    getPlayer().move("left", y);
                    getPlayer().checkTankBelowScreen();
                }
            }
        }
    }

    /**
     * Move the tank to the right side when the right arrow key is pressed
     */
    public void rightArrow() {
        if (!isEndGame()) {
            Teleport currentTeleport = getPlayer().getTeleport();
            if (currentTeleport.isChoosingLocation())
            {
                if (getPlayer().getX() + 1 <= 864)
                {
                    HashMap<Integer, Terrain> terrainList = getCurrentSetting().getLayout().getTerrainList();
                    currentTeleport.setCoordinates("right", terrainList);
                }
            }
            else
            {
                int x = getPlayer().getX() + 2;
                if (x <= 864)
                {
                    HashMap<Integer, Terrain> terrainList = getCurrentSetting().getLayout().getTerrainList();
                    Terrain currentTerrain = terrainList.get(x);
                    float y = currentTerrain.getY();
                    getPlayer().move("right" , y);
                    getPlayer().checkTankBelowScreen();
                }
            }
        }
    }

    /**
     * Rotate the turret when the up arrow is pressed
     */
    public void upArrow() {
        if (!isEndGame()) {
            if (!getPlayer().getTeleport().isChoosingLocation())
            {
                getPlayer().rotateTurret("up");
            }
        }
    }

    /**
     * Rotate the turret when the down arrow is pressed
     */
    public void downArrow() {
        if (!isEndGame()) {
            if (!getPlayer().getTeleport().isChoosingLocation())
            {
                getPlayer().rotateTurret("down");
            }
        }
    }

    /**
     * Increase the power when the S key is pressed
     */
    public void sKey() {
        if (!isEndGame()) {
            getPlayer().setPower("S");
        }
    }

    /**
     * Decrease the power when the W key is pressed
     */
    public void wKey() {
        if (!isEndGame()) {
            getPlayer().setPower("W");
        }
    }

    /**
     * Using the repair kit for the current tank or reset the game when the R key is pressed
     */
    public void rKey() {
        if (!isEndGame()) {
            getPlayer().repairKit();
        } else {
            restartGame();
        }
    }

    /**
     * Adding fuel to the current tank when the F key is pressed
     */
    public void fKey() {
        if (!isEndGame()) {
            getPlayer().addFuel();
        }
    }

    /**
     * Adding a shield to the current tank when H key is pressed
     */
    public void hKey() {
        if (!isEndGame()) {
            getPlayer().enableShield();
        }
    }

    /**
     * Applying teleport when T key is pressed
     */
    public void tKey() {
        if (!isEndGame()) {
            Teleport currentTeleport = getPlayer().getTeleport();
            if (currentTeleport.isChoosingLocation())
            {
                getPlayer().teleport();
            }
            else
            {
                if (getPlayer().getScore() >= 15)
                {
                    HashMap<Integer, Terrain> terrainList = getCurrentSetting().getLayout().getTerrainList();
                    currentTeleport.setChoosingLocation(getPlayer().getX(), terrainList);
                }
            }
        }
    }

    /**
     * Fire a projectile and add it to the ArrayList
     */
    public void spaceBar() {
        if (!isEndGame()) {
            this.pointer.updateStartTime(System.currentTimeMillis());
            if (!getPlayer().getTeleport().isChoosingLocation())
            {
                getPlayer().fire();
                ArrayList<Projectile> currentProjectileList = getPlayer().getProjectile();
                for (Projectile currentProjectile : currentProjectileList) {
                    if (!this.activeProjectile.contains(currentProjectile)) {
                        this.activeProjectile.add(currentProjectile);
                    }
                }
                setNextPlayer();
                Setting currentSetting = getCurrentSetting();
                Wind currentWind = currentSetting.getWind();
                currentWind.setRandomSpeed(getCurrentSetting().getRandom());
            }
        }
    }

    /**
     * Delete the Projectile that has exploded
     */
    public void deleteProjectile() {
        int i = 0;
        while (i < this.activeProjectile.size()) {
            Projectile currentProjectile = this.activeProjectile.get(i);
            boolean completeAll = currentProjectile.isCompleted();
            if (completeAll) {
                this.activeProjectile.remove(currentProjectile);
            }
            else {
                i++;
            }
        }
    }

    /**
     * Check the location of the projectile when exploding and set the tank is that area to fall down
     * @param projectile, the projectile that has landed
     */
    public void setTankFalling(Projectile projectile) {   
        HashMap<Integer, Terrain> terrainList = getCurrentSetting().getLayout().getTerrainList();
        for (Character character : playersInOrder) {
            Tank currentTank = getCurrentSetting().getLayout().getTankList().get(character);
            int xTank = (int) currentTank.getX();
            for (int j = 0; j < terrainList.size(); j++) {
                Terrain currentTerrain = terrainList.get(j);
                int xTerrain = (int) currentTerrain.getX();
                if (xTerrain == xTank) {
                    if (currentTank.getY() < currentTerrain.getY()) {
                        if (Math.abs(projectile.getX() - xTank) <= 30) {
                            currentTank.setFalling(currentTerrain.getY(), projectile);
                        }
                    }
                }
            }
        }  
    }

    /**
     * Calculate the score and the lost health for tanks
     * @param currentTank The tank is hit by
     * @param lostHealth The health the tank lost
     */
    public void calculateScoreWhenFalling(Tank currentTank, int lostHealth) {
        Tank tankGetScore = currentTank.getHitByProjectile().getTank();
        if (currentTank.getCharacter() != tankGetScore.getCharacter())
        {
            tankGetScore.setScore(lostHealth);
        }
    }

    /**
     * Calculate the score when a tank fall down without parachute
     * @param tank The tank that hit the terrain causing another tank to fall down
     * @param lostHealth the health that the tank lost
     */
    public void calculateScoreWhenHitting(Tank tank, int lostHealth) {
        tank.setScore(lostHealth);
    }

    /**
     * Calculate the lost health or disable the shield if a tank is hit
     * @param currentProjectile, the projectile that has landed
     */
    public void calculateHealth(Projectile currentProjectile) {
        Setting currentSetting = getCurrentSetting();
        HashMap<Character, Tank> tankList = currentSetting.getLayout().getTankList();
        for (Character character : playersInOrder) {
            int xProjectile = (int) currentProjectile.getX();
            float yProjectile = (float) currentProjectile.getY();
            char currentCharacter = character;
            Tank currentTank = tankList.get(currentCharacter);
            int xTank = currentTank.getX();
            float yTank = currentTank.getY();

            int xDiff = Math.abs(xTank - xProjectile);

            if (xDiff > 30) {
                continue;
            } else {
                float yChange = (float) Math.sqrt((Math.pow(30, 2) - Math.pow(xDiff, 2)));
                float yDiff = Math.abs(yTank - yProjectile);
                if (yChange > yDiff) {
                    if (!currentTank.getShield()) {
                        int lostHealth = (int) (2 * (30 - xDiff));
                        if (currentTank.getHealth() < lostHealth) {
                            lostHealth = currentTank.getHealth();
                        }
                        currentTank.setHealth(-lostHealth);
                        calculateScoreWhenHitting(currentProjectile.getTank(), lostHealth);
                    } else {
                        currentTank.disableShield();
                    }

                }
            }
        }
    }

    /**
     * Organise the tanks by their score of all tank that has ended
     * @return The ArrayList storing the tanks that are in order
     */
    public HashMap<Integer, Tank> endGame() {
        HashMap<Character, Tank> tankList = getCurrentSetting().getLayout().getTankList();
        ArrayList<Character> copiedTankCharacter = new ArrayList<>(playersInOrder);
        HashMap<Integer, Tank> rankedTankList = new HashMap<>();


        int i = 1;
        while (!copiedTankCharacter.isEmpty()) {
            int index = 0;
            Character initCharacter = copiedTankCharacter.get(index);
            Tank highestScoreTank = tankList.get(initCharacter);
            int highestScore = highestScoreTank.getScore();
            for (int j = 0; j < copiedTankCharacter.size(); j++) {
                Character currentCharacter = copiedTankCharacter.get(j);
                Tank currentTank = tankList.get(currentCharacter);
                int currentScore = currentTank.getScore();
                if (currentScore > highestScore) {
                    highestScoreTank = currentTank;
                    highestScore = currentScore;
                    index = j;
                }
            }
            rankedTankList.put(i, highestScoreTank);
            copiedTankCharacter.remove(index);
            i++;
        }
        return rankedTankList;
    }

    /**
     * Checking whether the game has ended
     * @return the boolean checking whether the game has ended
     */
    public boolean isEndGame() {
        return this.endGame;
    }

    /**
     * Restart the game
     */
    public void restartGame() {
        this.app.setUpGame();
    }

    /**
     * Draw the setting of each round
     */
    public void drawSetting() {
        // draw background
        PImage background = this.app.backgroundImage.get(getCurrentRound() - 1);
        this.app.image(background, 0, 0, App.WIDTH, App.HEIGHT);
        
        //draw terrain
        HashMap<Integer, Terrain> terrainList = getCurrentSetting().getLayout().getTerrainList();
        int[] foregroundColour = getCurrentSetting().getForegroundColour();
        this.app.stroke(foregroundColour[0], foregroundColour[1], foregroundColour[2]);
        for (int i = 0; i < terrainList.size() - 32; i++)
        {
            Terrain currentTerrain = terrainList.get(i);
            this.app.line((float) i, (float) currentTerrain.getY(), (float) i, (float) 640);
        }

        //draw tree
        ArrayList<Tree> treeArrayList = getCurrentSetting().getLayout().getTreeList();
        if (treeArrayList.isEmpty()) {
            return;
        }
        for (Tree currentTree : treeArrayList) {
            Terrain currentTerrain = terrainList.get((int) (currentTree.getX()));
            PImage tree = this.app.treeImage.get(getCurrentRound() - 1);
            if (currentTerrain.getY() >= 640) {
                continue;
            }
            this.app.image(tree, (float) (currentTree.getX() - 16), (float) (currentTerrain.getY() - 28), CELLSIZE, CELLSIZE);
        }
    }

    /**
     * Draw the tanks
     */
    public void drawTank() {
        HashMap<Character, Tank> tanksList = getCurrentSetting().getLayout().getTankList();
        HashMap<Integer, Terrain> terrainList = getCurrentSetting().getLayout().getTerrainList();
        this.pointer.updateCoordinate(getPlayer().getX(), getPlayer().getY());
        for (Character key : playersInOrder) {
            Tank currentTank = tanksList.get(key);

            float[] tankTurretStat = currentTank.getTankTurret();
            Terrain currentTerrain = terrainList.get((int) (currentTank.getX()));
            float height = (currentTerrain.getY());

            if (currentTank.getShield()) {
                this.app.noStroke();
                this.app.fill(0, 255, 255, 50);
                this.app.ellipse(currentTank.getX(), currentTank.getY(), 50, 50);
            }

            if (currentTank.isFalling()) {
                int score = currentTank.falling();
                calculateScoreWhenFalling(currentTank, score);
                if (currentTank.getParachute() > 0) {
                    if (currentTank.getHealth() > 0) {
                        PImage parachuteImage = this.app.otherImage.get("parachute");
                        this.app.image(parachuteImage, currentTank.getX() - 30, currentTank.getY() - 57, 60, 60);
                    }
                }
            } else {
                currentTank.setY(height);
            }

            int xTank = currentTank.getX();
            float yTank = currentTank.getY();
            if (currentTank.getHealth() > 0) {
                this.app.strokeWeight(5);
                int[] colour = currentTank.getColour();
                this.app.stroke(0, 0, 0);
                this.app.line(tankTurretStat[0], tankTurretStat[1], tankTurretStat[2], tankTurretStat[3]);
                this.app.stroke(colour[0], colour[1], colour[2]);
                this.app.line(xTank - 9, yTank, xTank + 9, yTank);
                this.app.line(xTank - 6, yTank - 4, xTank + 6, yTank - 4);
                currentTank.checkTankBelowScreen();
                if (currentTank.getTeleport().isChoosingLocation()) {
                    currentTank.getTeleport().draw(this.app);
                }
                pointer.draw(this.app, System.currentTimeMillis());
            } else if (currentTank.getHealth() == 0) {
                if (!currentTank.completeExploding()) {
                    int[] red = {255, 0, 0};
                    int[] orange = {255, 165, 0};
                    int[] yellow = {255, 255, 0};

                    float[] radius = currentTank.updateExplosion();
                    this.app.noStroke();
                    this.app.fill(red[0], red[1], red[2]);
                    this.app.ellipse(xTank, yTank, radius[0], radius[0]);
                    this.app.fill(orange[0], orange[1], orange[2]);
                    this.app.ellipse(xTank, yTank, radius[1], radius[1]);
                    this.app.fill(yellow[0], yellow[1], yellow[2]);
                    this.app.ellipse(xTank, yTank, radius[2], radius[2]);
                }
                if (currentTank.completeExploding() && !currentTank.isCompleted()) {
                    getCurrentSetting().getLayout().changeTerrain(xTank, yTank, currentTank.getExplosion().getMaxRadius());
                    currentTank.setCompleted();
                }
            }
        }
    }

    /**
     * Draw all active projectiles
     */
    public void drawProjectile() {
        ArrayList<Projectile> activeProjectiles = getActiveProjectiles();
        HashMap<Integer, Terrain> terrainList = getCurrentSetting().getLayout().getTerrainList();
        if (!activeProjectiles.isEmpty()) {
            for (Projectile currentProjectile : activeProjectiles) {
                int[] colour = currentProjectile.getTank().getColour();
                float x = currentProjectile.getX();
                float y = currentProjectile.getY();
                currentProjectile.checkHit(terrainList);
                if (!currentProjectile.isLanded()) {
                    this.app.noStroke();
                    this.app.fill(colour[0], colour[1], colour[2]);
                    this.app.ellipse(x, y, 7, 7);
                    this.app.fill(0);
                    this.app.ellipse(x, y, 3, 3);
                    currentProjectile.updateProjectile();
                    currentProjectile.checkHit(terrainList);
                }
                if (currentProjectile.isLanded() && !currentProjectile.isExploded()) {
                    int[] red = {255, 0, 0};
                    int[] orange = {255, 165, 0};
                    int[] yellow = {255, 255, 0};
                    float[] radius = currentProjectile.updateExplosion();
                    this.app.noStroke();
                    this.app.fill(red[0], red[1], red[2]);
                    this.app.ellipse(x, y, radius[0], radius[0]);
                    this.app.fill(orange[0], orange[1], orange[2]);
                    this.app.ellipse(x, y, radius[1], radius[1]);
                    this.app.fill(yellow[0], yellow[1], yellow[2]);
                    this.app.ellipse(x, y, radius[2], radius[2]);
                    currentProjectile.checkExploded();
                }
                if (currentProjectile.isLanded() && currentProjectile.isExploded() && !currentProjectile.isCompleted()) {
                    getCurrentSetting().getLayout().changeTerrain((int) x, y, (float) 30);
                    setTankFalling(currentProjectile);
                    calculateHealth(currentProjectile);
                    currentProjectile.setCompleted();
                }
            }
            deleteProjectile();
        }
    }

    /**
     * Display the statistic
     */
    public void drawStatistic() {
        this.app.fill(0);
        this.app.textFont(this.app.createFont("Arial", 18));
        while (true)
        {
            if (getPlayer().isCompleted()) {
                calculateAlivePlayer();
                setNextPlayer();
                if (this.alivePlayer == 0) {
                    break;
                }
            } else {
                break;
            }
        }

        String stringName = Character.toString(getPlayer().getCharacter());
        this.app.text("Player " + stringName + "'s turn" , 20, 32);

        String stringPower = String.valueOf(getPlayer().getPower());
        this.app.text("Power:", 330, 62);
        this.app.text(stringPower, 400, 62);

        String stringHealth = String.valueOf(getPlayer().getHealth());
        this.app.text("Health:", 330, 32);
        this.app.text(stringHealth, 590, 32);

        PImage fuelImage = this.app.otherImage.get("fuel");
        this.app.image(fuelImage, 160, 9, 28, 28);
        String fuel = String.valueOf(getPlayer().getFuel());
        this.app.text(fuel, 200, 32);

        PImage parachuteImage = this.app.otherImage.get("parachute");
        this.app.image(parachuteImage, 160, 45, 28, 28);
        String parachute = String.valueOf(getPlayer().getParachute());
        this.app.text(parachute, 200, 64);


        int windSpeed = getCurrentSetting().getWind().getSpeed();
        if (windSpeed > 0)
        {
            PImage windImage = this.app.otherImage.get("positiveWind");
            this.app.image(windImage, 750, 0, 50, 50);
        }
        else
        {
            PImage windImage = this.app.otherImage.get("negativeWind");
            this.app.image(windImage, 750, 0, 50, 50);
        }
        this.app.text(String.valueOf(windSpeed), 810, 32);


        // draw the score board
        HashMap<Character, Tank> tankList = getCurrentSetting().getLayout().getTankList();
        if (!isEndGame())
        {
            int height = 100;
            for (Character currentKey : playersInOrder) {
                Tank currentTank = tankList.get(currentKey);
                int[] colour = currentTank.getColour();
                this.app.stroke(colour[0], colour[1], colour[2]);
                this.app.fill(colour[0], colour[1], colour[2]);
                String display = "Player " + Character.toString(currentTank.getCharacter());
                int score = currentTank.getScore();
                this.app.text(display, 735, height);
                this.app.fill(0);
                this.app.stroke(0);
                this.app.text(score, 820, height);
                height += 25;
            }

            this.app.strokeWeight(4);
            this.app.stroke(0);
            this.app.fill(255, 0, 0, 0);

            int numTanks = tankList.size();
            this.app.rect(720, 50, 135, 25);
            this.app.rect(720, 75, 135, 25*numTanks + 10);
            this.app.fill(0);
            this.app.text("Scores", 735, 70);
        }
        this.app.stroke(0);
        this.app.strokeWeight(6);
        this.app.fill(255, 255, 255);
        this.app.rect(400, 12, 180, 25);

        this.app.noStroke();
        int[] colour = getPlayer().getColour();
        this.app.fill(colour[0], colour[1], colour[2]);
        int health = getPlayer().getHealth();
        float healthPercent = (float) (health * 1.0 / 100);
        this.app.rect(401, (float) 13.5, (float) (178 * healthPercent), 23);

        this.app.strokeWeight(4);
        this.app.stroke(128, 128, 128);
        int power = getPlayer().getPower();
        this.app.fill(255, 0, 0, 0);
        this.app.rect(400, 12, ((float) (180 * power) / 100), 25);

        this.app.strokeWeight(1);
        this.app.stroke(255, 0, 0);
        this.app.line(400 + ((float) (180 * power) / 100), 5, 400 + ((float) (180 * power) / 100), 43);
    }

    /**
     * Switch to the next round or end the game if needed
     */
    public void drawNextRoundOrEndGame() {
        calculateAlivePlayer();
        if (getAlivePlayer() <= 1) {
            setNextRound();
        }

        if (isEndGame())
        {
            if (endTime == 0) {
                endTime = System.currentTimeMillis();
            }
            HashMap<Integer, Tank> rank = endGame();
            int height = 170;
            this.app.textSize(20);
            int numTanks = rank.size();
            this.app.fill(147, 147, 255);
            this.app.stroke(0);
            this.app.rect(232, 100, 400, 40);
            this.app.rect(232, 140, 400, 30*numTanks + 20);
            this.app.fill(0);
            this.app.text("Final Scores", 255, 130);

            for (int i = 1; i <= rank.size(); i++) {
                long time = System.currentTimeMillis();
                if (time - endTime > i* 700L) {
                    Tank currentTank = rank.get(i);
                    char currentKey = rank.get(i).getCharacter();
                    int[] colour = currentTank.getColour();
                    this.app.stroke(colour[0], colour[1], colour[2]);
                    this.app.fill(colour[0], colour[1], colour[2]);
                    String display = "Player " + Character.toString(currentKey);
                    int score = currentTank.getScore();
                    this.app.text(display, 255, height);
                    this.app.fill(0);
                    this.app.stroke(0);
                    this.app.text(score, 510, height);
                    height += 30;
                }
            }
        }
    }
}
