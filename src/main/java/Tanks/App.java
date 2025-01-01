package Tanks;

/**
 * Import necessary library
 */
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import java.util.*;

public class App extends PApplet {

    /** The size of cell */
    public static final int CELLSIZE = 32;

    /** The number of cell per row */
    public static final int BOARD_WIDTH = 28;

    /** The number of cell per column */
    public static final int BOARD_HEIGHT = 20;

    /** The width of the window (pixel) */
    public static int WIDTH = (CELLSIZE * (BOARD_WIDTH - 1));

    /** The height of the window (pixel) */
    public static int HEIGHT = (CELLSIZE * BOARD_HEIGHT);

    /** Initialise the FPS */
    public static final int FPS = 30;

    /** Declare the string for config path */
    public String configPath;

    /** Initialise Random object */
    public static Random random = new Random();

    /** Declare Process object */
    public Process process;

    /** Declare the HashMap storing background images of all round */
    public HashMap<Integer, PImage> backgroundImage = new HashMap<>();

    /** Declare the HashMap storing tree images of all round */
    public HashMap<Integer, PImage> treeImage = new HashMap<>();

    /** Declare the HashMap storing other images */
    public HashMap<String, PImage> otherImage = new HashMap<>();


    /**
     * Constructs a new instance of the App class with a default configuration path.
     */
    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Set frame rate to 30
     * Create a new Process object and assign it to this.process
     * Assign the setting of the first round to this.currentSetting
     */
	@Override
    public void setup() {
        frameRate(FPS);
        setUpGame();
        ArrayList<Setting> settings = this.process.getSettingList();
        for (int i = 0; i < settings.size(); i++) {
            String backgroundPath = settings.get(i).getBackground();
            PImage backgroundImg = loadImage(backgroundPath);
            backgroundImage.put(i, backgroundImg);

            String treePath = settings.get(i).getTree();
            if (treePath != null) {
                PImage treeImg = loadImage(treePath);
                treeImage.put(i, treeImg);
            }
        }
        PImage wind1 = loadImage("src/main/resources/Tanks/wind.png");
        PImage wind2 = loadImage("src/main/resources/Tanks/wind-1.png");
        PImage fuel = loadImage("src/main/resources/Tanks/fuel.png");
        PImage parachute = loadImage("src/main/resources/Tanks/parachute.png");

        otherImage.put("positiveWind", wind1);
        otherImage.put("negativeWind", wind2);
        otherImage.put("fuel", fuel);
        otherImage.put("parachute", parachute);
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(KeyEvent event){
        if (keyCode == LEFT)
        {;
            process.leftArrow();
        }
        else if (keyCode == RIGHT)
        {
            process.rightArrow();
        }
        else if (keyCode == UP)
        {
            process.upArrow();
        }
        else if (keyCode == DOWN)
        {
            process.downArrow();
        }
        else if (keyCode == 'W' || keyCode == 'w')
        {
            process.wKey();
        }
        else if (keyCode == 'S' || keyCode == 's')
        {
            process.sKey();
        }
        else if (keyCode == ' ')
        {
            process.spaceBar();
        }
        else if (keyCode == 'R' || keyCode == 'r')
        {
            process.rKey();
        }
        else if (keyCode == 'F' || keyCode == 'f')
        {
            process.fKey();
        }
        else if (keyCode == 'H' || keyCode == 'h')
        {
            process.hKey();
        }
        else if (keyCode == 'T' || keyCode == 't')
        {
            process.tKey();
        }
    }

    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {
        process.drawSetting();
        process.drawTank();
        process.drawStatistic();
        process.drawProjectile();
        process.drawNextRoundOrEndGame();
    }

    /**
     * Run the program
     * Set the "Tanks.App" to the main file
     * @param args command line arguments
     */
    public static void main(String[] args)
    {
        PApplet.main("Tanks.App");
    }

    /**
     * Set up the game, is used to start or restart
     */
    public void setUpGame() {
        try {
            JSONObject config = loadJSONObject(this.configPath);
            this.process = new Process(this, config);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
