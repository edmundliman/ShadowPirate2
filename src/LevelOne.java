import bagel.*;

/**
 * A class for level 1
 * @author Edmund Liman
 */
public class LevelOne extends Level {
    private final Image BACKGROUND_IMAGE = new Image("res/background1.png");
    private static final String OBJECTIVE_MESSAGE = "FIND THE TREASURE";
    private static final String WIN_MESSAGE = "CONGRATULATIONS!";

    // setup flag variables
    private boolean levelOneStart = false;
    private boolean levelOneWin = false;
    private boolean levelOneEnd = false;
    private boolean spacePressed = false;

    /**
     * Constructor for LevelOne class
     */
    public LevelOne(){
        
    }

    /**
     * Method that performs state update for level 1
     * @param input input from the computer input device
     */
    public void update(Input input){
        if (!spacePressed && (input.wasPressed(Keys.SPACE))){
            levelOneStart = true;
        }

        if (levelOneStart && !levelOneEnd){
            BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
            loseDetection(input);
            winDetection(input);
        } else if (levelOneEnd) {
            if (levelOneWin) {
                super.drawMessage(WIN_MESSAGE, 0);
            } else {
                super.drawLosingMessage();
            }
        } else {
            super.drawStartingMessage(OBJECTIVE_MESSAGE);
        }
        
    }

    private void loseDetection(Input input){
        if (input.wasPressed(Keys.L)){
            levelOneEnd = true;
        } 
    }

    private void winDetection(Input input){
        if (input.wasPressed(Keys.Q)){
            levelOneWin = true;
            levelOneEnd = true;
        }
    }
}
