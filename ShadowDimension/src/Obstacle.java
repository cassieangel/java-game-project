import bagel.*;

/** represents a obstacle which can be a wall or tree,
 * which cannot be passed through by characters
 */
public class Obstacle extends GameObject {
    private final Image WALL_IMAGE = new Image("res/wall.png");
    private final Image TREE_IMAGE = new Image("res/tree.png");

    /** creates a new obstacle object with its coordinate and set their image and box
     */
    public Obstacle(String name, int x, int y) {
        super(x, y);
        if (name.equals("Wall")) {
            setCurrentImage(WALL_IMAGE);
            setObjectBox(WALL_IMAGE.getBoundingBoxAt(computeCentrePoint()));
        } else if (name.equals("Tree")) {
            setCurrentImage(TREE_IMAGE);
            setObjectBox(TREE_IMAGE.getBoundingBoxAt(computeCentrePoint()));
        }
    }

    @Override
    public void update(Level level) {
        getCurrentImage().drawFromTopLeft(getX(), getY());
    }
}
