/** represents entities that can move in the game
 */
public abstract class MovingEntity extends GameObject {
    private double speed;

    /** creates a moving entity with its top left coordinates and its speed
     */
    public MovingEntity(double x, double y, double speed) {
        super(x, y);
        this.speed = speed;
    }

    /** checks if the game object is out of bound (out of the screen) and returns boolean.
     */
    public boolean outOfBound(Level level) {
        return (getX() < level.getTopLeftBoundary().x || getX() > level.getBottomRightBoundary().x ||
                getY() < level.getTopLeftBoundary().y || getY() > level.getBottomRightBoundary().y);
    }

    /** method to make the game object move with increment of input x and y coordinates.
     */
    public void move(double xMove, double yMove) {
        setX(getX() + xMove);
        setY(getY() + yMove);
    }

    public double getSpeed() {
        return speed;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }
}