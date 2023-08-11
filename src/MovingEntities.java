import bagel.*;

/**
 * An abstract class for MovingEntities that extends GameEntity
 * @author Edmund Liman
 */
public abstract class MovingEntities extends GameEntity {
    // health bar rendering variables
    private static final int TO_PERCENTAGE = 100;
    private static final int MEDIUM_HEALTH_PERCENTAGE = 65;
    private static final int LOW_HEALTH_PERCENTAGE = 35;
    private static final DrawOptions COLOUR_HIGH_HEALTH = new DrawOptions().setBlendColour(0, 0.8, 0.2);
    private static final DrawOptions COLOUR_MEDIUM_HEALTH = new DrawOptions().setBlendColour(0.9, 0.6, 0);
    private static final DrawOptions COLOUR_LOW_HEALTH = new DrawOptions().setBlendColour(1, 0, 0);

    /**
     * Method to draw the health percentage of moving entities
     * @param x health percentage bottom coordinate
     * @param y health percentage left coordinate
     * @param fontSize the size of font
     * @param currentHealth numerator of percentage
     * @param maxHealth denominator of percentage
     */
    public void renderHealthBar(double x, double y, int fontSize, int currentHealth, int maxHealth){
        Font font = new Font("res/wheaton.otf", fontSize);

        int healthPercentage = (int)Math.round(((double)currentHealth / (double)maxHealth) * TO_PERCENTAGE);

        if (healthPercentage >= MEDIUM_HEALTH_PERCENTAGE){
            font.drawString(healthPercentage + "%", x, y, COLOUR_HIGH_HEALTH);
        } else if (healthPercentage < LOW_HEALTH_PERCENTAGE){
            font.drawString(healthPercentage + "%", x, y, COLOUR_LOW_HEALTH);
        } else {
            font.drawString(healthPercentage + "%", x, y, COLOUR_MEDIUM_HEALTH);
        }
    }
}
