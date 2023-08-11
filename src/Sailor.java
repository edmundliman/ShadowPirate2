import java.util.ArrayList;

import bagel.*;
import bagel.util.*;

/**
 * A class for Sailor entity
 * @author Edmund Liman
 */
public class Sailor extends MovingEntities{
    // static variables that are shared with all instances of sailor
    private final Image SAILOR_RIGHT = new Image("res/sailor/sailorRight.png");
    private final Image SAILOR_LEFT = new Image("res/sailor/sailorLeft.png");
    private final Image SAILOR_RIGHT_ATTACK = new Image("res/sailor/sailorHitRight.png");
    private final Image SAILOR_LEFT_ATTACK = new Image("res/sailor/sailorHitLeft.png");
    private static final int SAILOR_STARTING_HP = 100;
    private static final int SAILOR_STARTING_AP = 15;
    private static final int SAILOR_SPEED = 1;
    private static final int SAILOR_ATTACK_TIME = 1;
    private static final int SAILOR_COOLDOWN_TIME = 2;
    private static final double HEALTH_X = 10;
    private static final double HEALTH_Y = 25;
    private static final int HEALTH_FONT_SIZE = 30;

    // time management for sailor's actions
    private int timeCounter = 0;

    // instance variables of the sailor instance
    private double sailorX, sailorY;
    private Point sailorLastPosition;
    private Image sailorImage;
    private Rectangle hitBox;
    private int currentHP;
    private int currentMaxHP;
    private int currentAP;
    private boolean attackState;
    private boolean inCooldown;

    /**
     * Constructor for Sailor class
     * @param position initial top left point of the sailor
     */
    public Sailor(Point position){
        this.sailorX = position.x;
        this.sailorY = position.y;
        this.sailorImage = SAILOR_RIGHT;
        this.hitBox = new Rectangle(position, sailorImage.getWidth(), sailorImage.getHeight());
        this.currentHP = SAILOR_STARTING_HP;
        this.currentMaxHP = SAILOR_STARTING_HP;
        this.currentAP = SAILOR_STARTING_AP;
        this.attackState = false;
        this.inCooldown = false;
    }

    /**
     * getter for the coordinates of the sailor
     * @return Point the coordinates of the sailor
     */
    public Point getSailorPosition(){
        return new Point(sailorX, sailorY);
    }

    /**
     * setter for sailor position
     * @param position a point to where the sailor should be set to
     */
    public void setSailorPosition(Point position){
        this.sailorX = position.x;
        this.sailorY = position.y;
    }

    /**
     * getter for the hitbox of the sailor
     * @return Rectangle the hitbox of the sailor
     */
    public Rectangle getSailorHitBox(){
        return this.hitBox;
    }

    /**
     * getter for the health points of sailor
     * @return int the current health points of the sailor
     */
    public int getSailorHP(){
        return this.currentHP;
    }

    /**
     * setter for sailor health points
     * @param newHP the value of the new sailor health points
     */
    public void setSailorHP(int newHP){
        this.currentHP = newHP;
    }

    /**
     * getter for the maximum health points of sailor
     * @return int the current maximum health points of the sailor
     */
    public int getMaxSailorHP(){
        return this.currentMaxHP;
    }

    /**
     * getter for the attack points of the sailor
     * @return int the current attack points of sailor
     */
    public int getSailorAP(){
        return this.currentAP;
    }

    /**
     * getter for the attack state of the sailor
     * @return boolean the attack state of the sailor
     */
    public boolean getSailorAttackState(){
        return this.attackState;
    }

    /**
     * Method that performs a state update
     * @param input input from the computer input device
     * @param ArrayList<Block> ArrayList of Blocks
     */
    public void update(Input input, ArrayList<Block> blocks){
        // sailor image and hit box rendering
        sailorImage.drawFromTopLeft(sailorX, sailorY);
        this.hitBox = new Rectangle(getSailorPosition(), sailorImage.getWidth(), sailorImage.getHeight());

        if (input.wasPressed(Keys.S)){
            if (!inCooldown && !attackState){
                attackState = true;
            }
        }

        if (attackState){
            // attack state movements and actions
            move(input, SAILOR_RIGHT_ATTACK, SAILOR_LEFT_ATTACK, blocks);
            super.renderHealthBar(HEALTH_X, HEALTH_Y, HEALTH_FONT_SIZE, currentHP, currentMaxHP);
            timeCounter++;

            // 1000 milliseconds in attack state
            if (timeCounter >= (GameEntity.FRAMES_PER_SECOND * SAILOR_ATTACK_TIME)){
                attackState = false;
                inCooldown = true;
                resetTimeCounter();
            }
        } else {
            // idle state movements and actions
            move(input, SAILOR_RIGHT, SAILOR_LEFT, blocks);
            super.renderHealthBar(HEALTH_X, HEALTH_Y, HEALTH_FONT_SIZE, currentHP, currentMaxHP);
        }

        // cooldown actions
        if (inCooldown){
            timeCounter++;
            
            // wait for 2000 milliseconds before cooldown ends
            if (timeCounter >= (GameEntity.FRAMES_PER_SECOND * SAILOR_COOLDOWN_TIME)){
                inCooldown = false;
                resetTimeCounter();
            }
        }

    }

    // movement dynamics for the sailor
    private void move(Input input, Image rightImage, Image leftImage, ArrayList<Block> blocks){
        // detect collisions when moving
        blockCollisionDetection(blocks);
        levelBoundaries(LevelZero.getTopLeft(), LevelZero.getBottomRight());
        
        // move left
        if (input.isDown(Keys.LEFT)){
            sailorLastPosition = new Point(sailorX, sailorY);
            sailorX -= SAILOR_SPEED;
            this.sailorImage = leftImage;
        } 

        // move right
        if (input.isDown(Keys.RIGHT)){
            sailorLastPosition = new Point(sailorX, sailorY);
            sailorX += SAILOR_SPEED;
            this.sailorImage = rightImage;
        } 

        // move up
        if (input.isDown(Keys.UP)){
            sailorLastPosition = new Point(sailorX, sailorY);
            sailorY -= SAILOR_SPEED;
        }

        // move down
        if (input.isDown(Keys.DOWN)){
            sailorLastPosition = new Point(sailorX, sailorY);
            sailorY += SAILOR_SPEED;
        }
    }

    // level boundary detection and correction
    private void levelBoundaries(Point topLeft, Point bottomRight){
        // left boundary detection and correction
        if (sailorX <= topLeft.x){
            this.setSailorPosition(new Point(topLeft.x, sailorY));
        }

        // right boundary detection and correction
        if (sailorX >= bottomRight.x){
            this.setSailorPosition(new Point(bottomRight.x, sailorY));
        }

        // top boundary detection and correction
        if (sailorY <= topLeft.y){
            this.setSailorPosition(new Point(sailorX, topLeft.y));
        }

        // bottom boundary detection and correction
        if (sailorY >= bottomRight.y){
            this.setSailorPosition(new Point(sailorX, bottomRight.y));
        }
    }

    // detect block collision
    private void blockCollisionDetection(ArrayList<Block> blocks){
        for (Block block : blocks){
            if (this.hitBox.intersects(block.getHitBox())){
                setSailorPosition(sailorLastPosition);
            }
        }
    }

    // reset the time counter for next use
    private void resetTimeCounter(){
        this.timeCounter = 0;
    }
}
