import bagel.Image;
import bagel.util.*;

/** represents a game object, which has a pair of coordinates, can be rendered to the screen and can collide with other
 * game objects.
 */
public abstract class GameObject {
    private double x;
    private double y;
    private Rectangle objectBox;
    private Image currentImage;

    /** creates a game object with its coordinates.
     */
    public GameObject(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /** method computes the centre point of the current image and returns the point of the object.
     */
    public Point computeCentrePoint() {
        return new Point(x+getCurrentImage().getWidth()/2, y+getCurrentImage().getHeight()/2);
    }

    /** method checks if there's a collision with another game object and returns a boolean.
     */
    protected boolean checkCollision (GameObject object) {
        if (object!=null) {
            return object.getObjectBox().intersects(getObjectBox());
        }
        return false;
    }

    public abstract void update(Level level);
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public Rectangle getObjectBox() {
        return objectBox;
    }
    public Image getCurrentImage() {
        return currentImage;
    }

    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setObjectBox(Rectangle objectBox) {
        this.objectBox = objectBox;
    }
    public void setCurrentImage(Image currentImage) {
        this.currentImage = currentImage;
    }
}