import bagel.*;
import bagel.util.*;
import java.util.ArrayList;

/** represents a character, who can move, attack and take damage
 */
public abstract class Character extends MovingEntity {
    private int maxHealthPoints;
    private int healthPoints;
    private int damagePoints;
    private Image currentImage;
    private String name;
    private double oldX;
    private double oldY;

    protected final static Colour GREEN = new Colour(0, 0.8, 0.2);
    protected final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    protected final static Colour RED = new Colour(1, 0, 0);
    protected final static int ORANGE_BOUNDARY = 65;
    protected final static int RED_BOUNDARY = 35;

    /** creates a new character with its coordinates, speed, damage points and maximum health points they have.
     */
    public Character(double x, double y, double movementSpeed, int damagePoints, int maxHealthPoints) {
        super(x, y, movementSpeed);
        setDamagePoints(damagePoints);
        setHealthPoints(maxHealthPoints);
        setMaxHealthPoints(maxHealthPoints);
        setOldX(x);
        setOldY(y);
    }

    /** checks if the character has collided with any of the game objects in the input arrayList
     * where stationaryEntities an arrayList of game objects, <T> is any class that is a subclass of GameObject
     * and returns a boolean.
     */
    protected <T extends GameObject> boolean checkCollision(ArrayList<T> stationaryEntities) {
        for (int i=0; i<stationaryEntities.size(); i++) {
            T stationaryEntity = stationaryEntities.get(i);
            if (stationaryEntity!=null && getObjectBox().intersects(stationaryEntity.getObjectBox())) {
                return true;
            }
        }
        return false;
    }

    public int getHealthPoints() {
        return healthPoints;
    }
    public int getDamagePoints() {
        return damagePoints;
    }
    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }
    public Image getCurrentImage() {
        return currentImage;
    }
    public String getName() {
        return name;
    }
    public double getOldX() {
        return oldX;
    }
    public double getOldY() {
        return oldY;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }
    public void setDamagePoints(int damagePoints) {
        this.damagePoints = damagePoints;
    }
    public void setMaxHealthPoints(int maxHealthPoints) {
        this.maxHealthPoints = maxHealthPoints;
    }
    public void setCurrentImage(Image currentImage) {
        this.currentImage = currentImage;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setOldX(double oldX) {
        this.oldX = oldX;
    }
    public void setOldY(double oldY) {
        this.oldY = oldY;
    }

    protected abstract void displayHealth();
    protected abstract void render();
    public boolean isDead() {
        return getHealthPoints() <= 0;
    }

}
