import bagel.*;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 1, 2022
 *
 * Please fill your name below
 * @author Edmund Liman
 */
public class ShadowPirate extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "ShadowPirate";

    // initialise the levels
    private LevelZero level0 = new LevelZero();
    private LevelOne level1 = new LevelOne();

    private boolean nextlvl = false;
    
    /**
     * Constructor for the ShadowPirate class
     */
    public ShadowPirate() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * The entry point for the program
     * @param args a string array which stores string input from command line
     */
    public static void main(String[] args) {
        ShadowPirate game = new ShadowPirate();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     * @param input input from the computer input device
     */
    @Override
    public void update(Input input) {
        if (level0.getStartNextLevel() || nextlvl){
            level1.update(input);
        } else {
            level0.update(input);
        }
        
        if (input.wasPressed(Keys.W)){
            nextlvl = true;
        }

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

    }

}
