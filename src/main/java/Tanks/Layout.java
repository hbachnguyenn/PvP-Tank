package Tanks;

/** Import necessary library */
import java.util.*;

/** Represent Layout data */
public class Layout {

    /** Declare a HashMap storing Terrain objects */
    private HashMap<Integer, Terrain> terrainList = new HashMap<>();

    /** Declare an ArrayList storing Tree objects*/
    private final ArrayList<Tree> treeList = new ArrayList<>();

    /** Declare a HashMap storing Tank objects */
    private final HashMap<Character, Tank> tankList = new HashMap<>();

    /** The App object running this program */
    private App app;

    /**
     * Create new Terrain objects
     * @param x the value of X-coordinate, ranging from 0 to 27
     * @param y the value of Y-coordinate, ranging from 0 to 19
     */
    public void createTerrain(int x, float y) {
        int lowerBound = 32 * x;
        int upperBound = 32 * x + 31;
        for (int i = lowerBound; i <= upperBound; i++)
        {   
            Terrain newTerrain = new Terrain(i, y*32);
            terrainList.put(i, newTerrain);
        }

        if (terrainList.size() == 896)
        {
            this.terrainList = movingAverage(movingAverage(this.terrainList));
        }
    }

    /**
     * Create a new Tank object
     * @param x the value of X-coordinate, ranging from 0 to 27
     * @param character the character of the tank
     * @param colour the RGB values of the colour of the tank
     * @param windObj the Wind object
     */
    public void createTank(int x, char character, int[] colour, Wind windObj) {
        float y = terrainList.get(x*32).getY();
        Tank newTank = new Tank(x * 32, y, character, colour, windObj, this.app);
        tankList.put(character, newTank);
    }

    /**
     * Create a new Tree object
     * @param x the value of X-coordinate, ranging from 0 to 27
     */
    public void createTree(int x) {
        Tree newTree = new Tree(x * 32);
        treeList.add(newTree);
    }

    /**
     * Return the HashMap storing Terrain objects
     * @return the HashMap storing Terrain objects
     */
    public HashMap<Integer, Terrain> getTerrainList() {
        return this.terrainList;
    }

    /**
     * Return the ArrayList storing Tree objects
     * @return the ArrayList storing Tree objects
     */
    public ArrayList<Tree> getTreeList() {
        return this.treeList;
    }

    /**
     * Return the HashMap storing Tank objects
     * @return the HashMap storing Tank objects
     */
    public HashMap<Character, Tank> getTankList() {
        return this.tankList;
    }

    /**
     * Make the terrain become smoother by moving average
     * @param terrainMap the hashMap of Terrain objects
     * @return the hashMap of Terrain objects
     */
    private HashMap<Integer, Terrain> movingAverage(HashMap<Integer, Terrain> terrainMap) {
        HashMap<Integer, Terrain> newTerrainList = new HashMap<Integer, Terrain>();
        for (int i = 0; i < terrainMap.size(); i++)
        {   
            Terrain currentTerrain = terrainMap.get(i);
            if (i < 864)
            {   
                float newY = 0.0f;
                for (int j = 0; j < 32; j++)
                {
                    newY += terrainMap.get(i + j).getY();
                }
                newY = newY/32.0f;
                Terrain newTerrain = new Terrain(i, newY);
                newTerrainList.put(i, newTerrain);
            }
            else
            {
                newTerrainList.put(i, currentTerrain);
            }
        }
        return newTerrainList; 
    }

    /**
     * Change the terrain when a projectile hits or tank explodes
     * @param x, X-coordinate of where the projectile hits
     * @param y, Y-coordinate of where the projectile hits
     * @param radius, the radius of explosion
     */
    public void changeTerrain(int x, float y, float radius) {
        for (int i = -30; i <= 30; i++)
        {
            float yChange = (float) Math.sqrt((Math.pow(radius, 2) - Math.pow(i, 2)));

            int xTerrain = (int) (x + i);
            if (xTerrain < 0 || xTerrain > 864)
            {
                continue;
            }

            Terrain currentTerrain = this.terrainList.get(xTerrain);
            float bottom = y + yChange;
            float top = y - yChange;

            if (currentTerrain.getY() < bottom)
            {
                if (currentTerrain.getY() > top)
                {
                    currentTerrain.setY(bottom);
                }
                else
                {
                    currentTerrain.setY(currentTerrain.getY() + 2 * yChange);
                }
            }
            if (currentTerrain.getY() > 640)
            {
                currentTerrain.setY(640);
            }
        }
    }
}
