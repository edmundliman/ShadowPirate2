import bagel.*;
import bagel.util.*;

/**
 * A Block class
 * @author Edmund Liman
 */
public class Block extends StationaryEntities {
    // initialise static variables that are common to all instances of block
    private final Image BLOCK_IMAGE = new Image("res/block.png");

    // initialise instance variables for each instance of block
    private Point topLeftPoint;
    private Rectangle hitBox;

    /**
     * Constructor for Block class
     * @param topLeftPoint this is the topLeftCoordinate for the block
     */
    public Block(Point topLeftPoint){
        this.topLeftPoint = topLeftPoint;
        this.hitBox = new Rectangle(topLeftPoint, BLOCK_IMAGE.getWidth(), BLOCK_IMAGE.getHeight());
    }

    /**
     * Getter for hitBox of block
     * @return Rectangle the hitbox of the block
     */
    public Rectangle getHitBox(){
        return this.hitBox;
    }

    /**
     * Method that performs state update for block
     */
    public void update(){
        BLOCK_IMAGE.drawFromTopLeft(topLeftPoint.x, topLeftPoint.y);
    }
}
