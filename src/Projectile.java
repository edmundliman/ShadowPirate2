import bagel.*;
import bagel.util.*;

/**
 * A class for Projectile entity
 * @author Edmund Liman
 */
public class Projectile extends MovingEntities{
    // static constants for projectile class
    private static final int PIRATE_AP = 10;

    // instance variables needed for each instance of projectile
    private Point attackerPosition, victimPosition;
    private Image projectileImage;
    private double projectileSpeed;
    private int projectileDamage;
    private double projectileX, projectileY;
    private double directionX, directionY;
    private boolean mustRender;

    /**
     * Constructor for Projectile class
     * @param attackerPosition the coordinates of pirate or blackbeard
     * @param victimPosition the coordinates of sailor when it is first in range of attacker
     * @param projectileImage the image of projectile
     * @param projectileSpeed the speed of projectile
     * @param projectileDamage the damage of projectile
     */
    public Projectile(Point attackerPosition, Point victimPosition, Image projectileImage, double projectileSpeed, int projectileDamage){
        this.attackerPosition = attackerPosition;
        this.victimPosition = victimPosition;
        this.projectileImage = projectileImage;
        this.projectileSpeed = projectileSpeed;
        this.projectileX = attackerPosition.x;
        this.projectileY = attackerPosition.y;
        this.directionX = victimPosition.x - attackerPosition.x;
        this.directionY = victimPosition.y - attackerPosition.y;
        this.projectileDamage = projectileDamage;
        this.mustRender = true;
    }

    /**
     * Method that performs a state update
     * @param sailor the sailor instance
     */
    public void update(Sailor sailor){
        if (mustRender){
            projectileImage.draw(projectileX, projectileY, projectileRotation(this.directionX, this.directionY));
            this.move();
            boundaryDetection(LevelZero.getTopLeft(), LevelZero.getBottomRight());
            sailorCollision(sailor);
        }
    }

    // move method for projectile
    private void move(){
        projectileX += (directionX * projectileSpeed) / attackerPosition.distanceTo(victimPosition);
        projectileY += (directionY * projectileSpeed) / attackerPosition.distanceTo(victimPosition);
    }

    // method to calculate angle rotation for projectile
    private DrawOptions projectileRotation(double distanceX, double distanceY){
        double angle = Math.atan(distanceY / distanceX);
        return new DrawOptions().setRotation(angle);
    }

    // detects the level bounds
    private void boundaryDetection(Point topLeft, Point bottomRight){
        if ((projectileX <= topLeft.x) || (projectileX >= bottomRight.x)
            || (projectileY <= topLeft.y) || (projectileY >= bottomRight.y)){
                mustRender = false;
        }
    }

    // detects and performs actions when sailor is hit by projectile
    private void sailorCollision(Sailor sailor){
        if (sailor.getSailorHitBox().intersects(new Point(projectileX, projectileY))){
            sailor.setSailorHP(sailor.getSailorHP() - projectileDamage);
            if (projectileDamage == PIRATE_AP){
                System.out.println("Pirate inflicts " + PIRATE_AP 
                + " damage points on Sailor. Sailor's current health: "
                + sailor.getSailorHP() + "/" + sailor.getMaxSailorHP());
            } else {
                System.out.println("Blackbeard inflicts " + projectileDamage 
                + " damage points on Sailor. Sailor's current health: "
                + sailor.getSailorHP() + "/" + sailor.getMaxSailorHP());
            }
            mustRender = false;
        }
    }
}
