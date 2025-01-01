package Tanks;

/** Import necessary library */
import processing.core.PApplet;
import processing.data.JSONObject;
import java.io.*;
import java.util.*;

/**
 * Represent Setting object
 * Each round will have one Setting object
 */
public class Setting extends PApplet{

    /** Relative path of the backgrounds */
    private final String background;

    /** RGB values of the colour of the terrain */
    private final int[] foregroundColour = new int[3];

    /** Relative path of the tree image */
    private String tree;

    /** Initialise the Layout object */
    private final Layout layoutObj = new Layout();

    /** Initialise the Wind object */
    private final Wind windObj;

    /**Initialise the random object */
    private Random random = new Random();

    /** The App object running the program */
    private final App app;

    /**
     * Constructor for the class
     * Extract all the information from JSON objects and assign value for attributes
     * @param jsonObject stores all the information for the setting of each level
     * @param playersColour store all the players' character and their colours
     */
    public Setting(App app, JSONObject jsonObject, JSONObject playersColour) {
        String layout = jsonObject.getString("layout");
        this.background = "/src/main/resources/Tanks/" + jsonObject.getString("background");
        String foregroundColour = jsonObject.getString("foreground-colour");
        String[] foreground = foregroundColour.split(",");
        for (int i = 0; i < 3; i++)
        {
            this.foregroundColour[i] = Integer.parseInt(foreground[i]);
        }

        if (jsonObject.hasKey("trees"))
        {
            this.tree = "src/main/resources/Tanks/" + jsonObject.getString("trees");
        }

        this.windObj = new Wind(this.random);
        this.app = app;

        try
        {   
            FileReader fileReader = new FileReader(layout);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            ArrayList<String> lines = new ArrayList<String>();
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                lines.add(line);
            }
            fileReader.close();
            bufferedReader.close();


            ArrayList<Character> tankArrayKey = new ArrayList<Character>();
            for (char ch = 'A'; ch <= 'I'; ch++)
            {
                tankArrayKey.add(ch);
            }

            for (char ch = '0'; ch <= '9'; ch++)
            {
                tankArrayKey.add(ch);
            }

            while (lines.size() > 20)
            {
                lines.remove(lines.size() - 1);
            }
            int x = lines.size() - 1;

            char[][] layoutArray = new char[20][28];

            while (x >= 0)
            {   
                for (int i = 0; i < lines.get(x).length(); i++)
                {   
                    char symbol = lines.get(x).charAt(i);
                    layoutArray[x][i] = symbol;
                }
                x--;
            }



            for (int i = 0; i < layoutArray[1].length; i++)
            {
                for (int j = 0; j < layoutArray.length; j++)
                {   
                    char symbol = layoutArray[j][i];
                    if (symbol == 'X')
                    {
                        this.layoutObj.createTerrain(i, j);
                    }
                    
                }
            }

            for (int i = 0; i < layoutArray[1].length; i++)
            {
                for (char[] chars : layoutArray)
                {
                    char symbol = chars[i];
                    if (symbol == 'T')
                    {
                        this.layoutObj.createTree(i);
                    }
                    else if (tankArrayKey.contains(symbol))
                    {
                        int[] colour = new int[3];
                        String value = playersColour.getString(Character.toString(symbol));
                        if (value.equals("random"))
                        {
                            Random colourRandom = new Random();
                            for (int z = 0; z < colour.length; z++)
                            {
                                colour[z] = colourRandom.nextInt(256);
                            }
                        }
                        else
                        {
                            String[] valueArray = value.split(",");
                            for (int k = 0; k < 3; k++)
                            {
                                colour[k] = Integer.parseInt(valueArray[k]);
                            }
                        }
                        this.layoutObj.createTank(i, symbol, colour, windObj);
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Return the relative path of the background image
     * @return the string of the relative path
     */
    public String getBackground()
    {
        return this.background;
    }

    /**
     * Return the RGB values of the colour for terrain
     * @return the array storing RGB values
     */
    public int[] getForegroundColour()
    {
        return this.foregroundColour;
    }

    /**
     * Return the Layout object
     * @return the Layout object
     */
    public Layout getLayout()
    {
        return this.layoutObj;
    }

    /**
     * Return the relative path of tree image
     * @return the string storing the relative path
     */
    public String getTree()
    {
        return this.tree;
    }

    /**
     * Return the Wind object
     * @return the Wind object
     */
    public Wind getWind()
    {
        return this.windObj;
    }

    /**
     * Set the random object to the new random object
     * @param random, a new random object
     */
    public void setRandom(Random random) {
        this.random = random;
    }

    /**
     * Return the random object
     * @return the random object
     */
    public Random getRandom() {
        return this.random;
    }
}