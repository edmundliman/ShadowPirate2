import bagel.*;

/**
 * An abstract class for levels in the game
 * @author Edmund Liman
 */
public abstract class Level {
    // starting messages specifications
    private static final String START_MESSAGE = "PRESS SPACE to START";
    private static final String ATTACK_MESSAGE = "PRESS S TO ATTACK";
    private static final String LOSING_MESSAGE = "GAME OVER";
    private static final int MESSAGE_HEIGHT = 402;
    private static final int MESSAGE_HEIGHT_GAP = 70;

    // font specifications
    private static final int FONT_SIZE = 55;
    private final Font font = new Font("res/wheaton.otf", FONT_SIZE);

    /**
     * Method that draws a message on screen
     * @param message message to be drawn
     * @param position the message position below the first message
     */
    public void drawMessage(String message, int position){
        double messageX = Window.getWidth() / 2.0 - font.getWidth(message) / 2.0;
        font.drawString(message, messageX, MESSAGE_HEIGHT + MESSAGE_HEIGHT_GAP * position);
    }

    /**
     * Method that draws the starting message for the level
     * @param objectiveMessage the objective message to complete the level
     */
    public void drawStartingMessage(String objectiveMessage){
        drawMessage(START_MESSAGE, 0);
        drawMessage(ATTACK_MESSAGE, 1);
        drawMessage(objectiveMessage, 2);
    }

    /**
     * Method that draws the losing message across the levels
     */
    public void drawLosingMessage(){
        drawMessage(LOSING_MESSAGE, 0);
    }
    
    /**
     * Method that performs a state update
     * @param input input from the computer input device
     */
    public abstract void update(Input input);
}
