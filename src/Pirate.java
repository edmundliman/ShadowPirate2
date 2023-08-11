import bagel.*;
import bagel.util.*;
import java.util.*;

/**
 * A class for Pirate entity
 * @author Edmund Liman
 */
public class Pirate extends MovingEntities{
    // static variables that are shared with all instances of sailor
    private final Image PIRATE_RIGHT = new Image("res/pirate/pirateRight.png");
    private final Image PIRATE_LEFT = new Image("res/pirate/pirateLeft.png");
    private final Image PIRATE_RIGHT_INVINCIBLE = new Image("res/pirate/pirateHitRight.png");
    private final Image PIRATE_LEFT_INVINCIBLE = new Image("res/pirate/pirateHitLeft.png");
    private final Image PIRATE_PROJECTILE = new Image("res/pirate/pirateProjectile.png");
    private static final int PIRATE_STARTING_HP = 45;
    private static final int PIRATE_STARTING_AP = 10;
    private static final double PIRATE_ATTACK_SPEED = 0.4;
    private static final int PIRATE_ATTACK_RANGE = 100;
    private static final double PIRATE_MIN_SPEED = 0.2;
    private static final double PIRATE_MAX_SPEED = 0.7;
    private static final double INVINCIBLE_TIME = 1.5;
    private static final int COOLDOWN_TIME = 3;
    private static final int HP_HEIGHT_GAP = 6;
    private static final int HP_FONT_SIZE = 15;

    // time management for pirate's actions
    private int cooldownTimer = 0;
    private int invincibleTimer = 0;

    // randomiser for pirate's attributes
    Random rand = new Random();
    private int initialDirection;

    // instance variables for pirate instances
    private double pirateX, pirateY;
    private double pirateSpeed;
    private Image pirateImage;
    private Rectangle hitBox;
    private int currentHP;
    private double pirateDirectionX, pirateDirectionY;
    private boolean inCooldown;
    private boolean isInvincible;
    
    // To store projectiles
    ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

    /**
     * Constructor for Pirate class
     * @param position initial top left point of the pirate
     */
    public Pirate(Point position){
        this.pirateX = position.x;
        this.pirateY = position.y;
        this.pirateSpeed = rand.nextDouble(PIRATE_MIN_SPEED, PIRATE_MAX_SPEED);
        this.pirateImage = PIRATE_RIGHT;

        // randomising movement of pirate
        this.initialDirection = rand.nextInt(4);
        if (initialDirection == 0){
            pirateDirectionX = pirateSpeed * 1;
            pirateDirectionY = 0;
        } else if (initialDirection == 1){
            pirateDirectionX = pirateSpeed * -1;
            pirateDirectionY = 0;
            this.pirateImage = PIRATE_LEFT;
        } else if (initialDirection == 2){
            pirateDirectionX = 0;
            pirateDirectionY = pirateSpeed * -1;
        } else {
            pirateDirectionX = 0;
            pirateDirectionY = pirateSpeed * 1;
        }
        
        this.hitBox = new Rectangle(position, pirateImage.getWidth(), pirateImage.getHeight());
        this.currentHP = PIRATE_STARTING_HP;
        this.inCooldown = false;
        this.isInvincible = false;
    }
    
    /**
     * getter for the coordinates of the pirate
     * @return Point the coordinates of the pirate
     */
    public Point getPiratePosition(){
        return new Point(pirateX, pirateY);
    }

    /**
     * setter for pirate position
     * @param position a point to where the pirate should be set to
     */
    public void setPiratePosition(Point position){
        this.pirateX = position.x;
        this.pirateY = position.y;
    }

    /**
     * Method that performs a state update
     */
    public void update(Sailor sailor, ArrayList<Block> blocks){
        if (currentHP > 0){
            pirateImage.drawFromTopLeft(pirateX, pirateY);
            this.hitBox = new Rectangle(getPiratePosition(), pirateImage.getWidth(), pirateImage.getHeight());
            super.renderHealthBar(pirateX, pirateY - HP_HEIGHT_GAP, HP_FONT_SIZE, this.currentHP, PIRATE_STARTING_HP);
            sailorCollision(sailor);

            for (Projectile projectile : projectiles){
                projectile.update(sailor);
            }

            if ((detectSailor(sailor, pirateImage, new Point(pirateX, pirateY), PIRATE_ATTACK_RANGE)) && !inCooldown){
                projectiles.add(new Projectile(getPiratePosition(), sailor.getSailorPosition(), PIRATE_PROJECTILE, PIRATE_ATTACK_SPEED, PIRATE_STARTING_AP));
                inCooldown = true;
            }

            if (inCooldown){
                cooldownTimer++;
                if (cooldownTimer >= GameEntity.FRAMES_PER_SECOND * COOLDOWN_TIME){
                    inCooldown = false;
                    cooldownTimer = 0;
                }
            }
            
            if (isInvincible){
                move(PIRATE_RIGHT_INVINCIBLE, PIRATE_LEFT_INVINCIBLE, blocks);
                invincibleTimer++;
                if (invincibleTimer >= GameEntity.FRAMES_PER_SECOND * INVINCIBLE_TIME){
                    isInvincible = false;
                    invincibleTimer = 0;
                }
            } else {
                move(PIRATE_RIGHT, PIRATE_LEFT, blocks);
            }
        }
    }

    // movement dynamics for pirate
    private void move(Image rightImage, Image leftImage, ArrayList<Block> blocks){
        if (pirateDirectionX < 0){
            pirateImage = leftImage;
        } else {
            pirateImage = rightImage;
        }

        pirateX += pirateDirectionX;
        pirateY += pirateDirectionY;

        blockCollisionDetection(blocks);
        levelBoundaries(LevelZero.getTopLeft(), LevelZero.getBottomRight());
    }

    // level boundary detection and correction
    private void levelBoundaries(Point topLeft, Point bottomRight){
        if ((pirateX <= topLeft.x) || (pirateX >= bottomRight.x)
            || (pirateY <= topLeft.y) || (pirateY >= bottomRight.y)){
                pirateDirectionX *= -1;
                pirateDirectionY *= -1;
        }
    }

    // helper variables to ensure that pirate will not be stuck after colliding with a block
    private boolean turned = false;
    private int timeCounter = 0;
    // detect block collision and reverse direction
    private void blockCollisionDetection(ArrayList<Block> blocks){
        for (Block block : blocks){
            if (this.hitBox.intersects(block.getHitBox()) && !turned){
                pirateDirectionX *= -1;
                pirateDirectionY *= -1;
                turned = true;
            }
        }
        timeCounter++;
        if (timeCounter >= GameEntity.FRAMES_PER_SECOND){
            turned = false;
            timeCounter = 0;
        }
    }

    // sailor collision
    private void sailorCollision(Sailor sailor){
        if (this.hitBox.intersects(sailor.getSailorHitBox())){
            if (!isInvincible && sailor.getSailorAttackState()){
                currentHP = currentHP - sailor.getSailorAP();
                isInvincible = true;
                System.out.println("Sailor inflicts " + sailor.getSailorAP() 
                + " damage points on Pirate. Pirate's current health: "
                + currentHP + "/" + PIRATE_STARTING_HP);
            }
        }
    }

    // sailor detection
    private boolean detectSailor(Sailor sailor, Image image, Point piratePosition, int detectionRange){
        Point center = new Point(piratePosition.x + (image.getWidth() / 2.0), (piratePosition.y + (image.getHeight() / 2.0)));
        Point detectionBoxTopLeft = new Point(center.x - (detectionRange / 2), center.y - (detectionRange / 2));
        Rectangle detectionBox = new Rectangle(detectionBoxTopLeft, detectionRange, detectionRange);

        return detectionBox.intersects(sailor.getSailorHitBox());
    }
}
