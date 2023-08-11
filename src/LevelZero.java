import java.io.*;
import java.util.*;
import bagel.*;
import bagel.util.*;

/**
 * A class for level 0
 * @author Edmund Liman
 */
public class LevelZero extends Level {
    // level specifications
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private static final String OBJECTIVE_MESSAGE = "USE ARROW KEYS TO FIND LADDER";
    private static final String WIN_MESSAGE = "LEVEL COMPLETE!";
    private static final int WIN_MESSAGE_DISPLAY_TIME = 3;
    private static final String WORLD_FILE = "res/level0.csv";
    private static final int LEVEL_WIN_X = 990;
    private static final int LEVEL_WIN_Y = 630;

    // game time management for showing winning message
    private static final int FRAMES_PER_SECOND = 60;
    private int timeCounter = 0;

    // instances of game entities
    private Sailor sailor;
    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<Pirate> pirates = new ArrayList<Pirate>();

    // game boundary variables
    private static Point topLeft;
    private static Point bottomRight;

    // setup flag variables
    private boolean levelZeroStart = false;
    private boolean levelZeroWin = false;
    private boolean levelZeroEnd = false;
    private boolean startNextLevel = false;

    /**
     * Constructor for LevelZero class
     */
    public LevelZero(){
        readCSV(WORLD_FILE);
    }

    /**
     * getter for top left boundary coordinate
     * @return Point the coordinates of top left boundary
     */
    public static Point getTopLeft(){
        return topLeft;
    }

    /**
     * getter for bottom right boundary coordinate
     * @return Point the coordinates of bottom right boundary
     */
    public static Point getBottomRight(){
        return bottomRight;
    }

    /**
     * Method that notifies main ShadowPirate class whether it can start level 1
     * @return boolean this method returns whether it is appropriate to start next level
     */
    public boolean getStartNextLevel(){
        return startNextLevel;
    }    

    /**
     * Method that performs state update for level 0
     * @param input input from the computer input device
     */
    @Override
    public void update(Input input){
        // detecting pressed space to start level
        if (input.wasPressed(Keys.SPACE)){
            levelZeroStart = true;
        }

        if (levelZeroStart && !levelZeroEnd){
            // ongoing level actions
            BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
            sailor.update(input, blocks);
            for (Block block : blocks){
                block.update();
            }
            for (Pirate pirate : pirates){
                pirate.update(sailor, blocks);
            }
            loseDetection();
            winDetection();
        } else if (levelZeroEnd) {
            if (levelZeroWin) {
                // level win actions
                timeCounter++;
                if (timeCounter < FRAMES_PER_SECOND * WIN_MESSAGE_DISPLAY_TIME){
                    super.drawMessage(WIN_MESSAGE, 0);
                } else {
                    startNextLevel = true;
                }
            } else {
                // level lose actions
                super.drawLosingMessage();
            }
        } else {
            // start of level messages
            super.drawStartingMessage(OBJECTIVE_MESSAGE);
        }
        
    }

    // read csv method that reads a csv file and creates objects
    private void readCSV(String filename){
        // reads the coordinates of topLeft and bottomRight first
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            String text;

            while ((text = br.readLine()) != null){
                String contents[] = text.split(",");

                if (contents[0].equals("TopLeft")){
                    Point position = new Point(Double.parseDouble(contents[1]), Double.parseDouble(contents[2]));
                    topLeft = position;
                }

                if (contents[0].equals("BottomRight")){
                    Point position = new Point(Double.parseDouble(contents[1]), Double.parseDouble(contents[2]));
                    bottomRight = position;
                }

                if (contents[0].equals("Sailor")){
                    Point position = new Point(Double.parseDouble(contents[1]), Double.parseDouble(contents[2]));
                    sailor = new Sailor(position);
                }

                if (contents[0].equals("Block")){
                    Point position = new Point(Double.parseDouble(contents[1]), Double.parseDouble(contents[2]));
                    blocks.add(new Block(position));
                }

                if (contents[0].equals("Pirate")){
                    Point position = new Point(Double.parseDouble(contents[1]), Double.parseDouble(contents[2]));
                    pirates.add(new Pirate(position));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // lose detection method that detects whether level is lost
    private void loseDetection(){
        if (sailor.getSailorHP() <= 0){
            levelZeroEnd = true;
        }
    }

    // win detection method that detects whether level is won
    private void winDetection(){
        if ((sailor.getSailorPosition().x >= LEVEL_WIN_X) && (sailor.getSailorPosition().y > LEVEL_WIN_Y)){
            levelZeroWin = true;
            levelZeroEnd = true;
        }
    }
}
