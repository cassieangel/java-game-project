import bagel.DrawOptions;
import bagel.Image;

public class Fire extends GameObject implements DamageInflictable {
    private Image FIRE_DEMON = new Image("res/demon/demonFire.png");

    private final double direction;
    private final DrawOptions rotation;
    private Demon owner;
    private boolean active;

    private final double TOP_LEFT_RAD = 0;
    private final double TOP_RIGHT_RAD = Math.PI/2;
    private final double BOTTOM_LEFT_RAD = 3*Math.PI/2;
    private final double BOTTOM_RIGHT_RAD = Math.PI;

    public Fire(double demonX, double demonY, double playerX, double playerY, Demon owner) {
        super(demonX, demonY);
        this.owner = owner;
        setCurrentImage(FIRE_DEMON);
        direction = computeDirection(demonX, demonY, playerX, playerY);
        rotation = new DrawOptions().setRotation(direction);
        active = true;
    }

    @Override
    public void update(Level level) {
        if (active) {
            getCurrentImage().draw(getX(), getY(), rotation);
            level.getPlayer().receiveDamage(this);
            active = false;
        }

    }

    private double computeDirection(double demonX, double demonY, double playerX, double playerY) {
        double xChange = playerX-demonX;
        double yChange = playerY-demonY;

        double ownerWidth = owner.getCurrentImage().getWidth();
        double ownerHeight = owner.getCurrentImage().getHeight();

        double fireWidth = getCurrentImage().getWidth();
        double fireHeight = getCurrentImage().getHeight();

        double yEdge = (ownerHeight + fireWidth)/2;
        double xEdge = (ownerWidth + fireHeight)/2;

        if (xChange <= 0 && yChange <= 0) {
            setY(getY() - yEdge);
            setX(getX() - xEdge);
            return TOP_LEFT_RAD;
        } else if (xChange <= 0 && yChange > 0) {
            setY(getY() + yEdge);
            setX(getX() - xEdge);
            return BOTTOM_LEFT_RAD;
        } else if (xChange > 0 && yChange <= 0) {
            setY(getY() - yEdge);
            setX(getX() + xEdge);
            return TOP_RIGHT_RAD;
        } else {
            setY(getY() + yEdge);
            setX(getX() + xEdge);
            return BOTTOM_RIGHT_RAD;
        }
    }

    @Override
    public void printDamageLog(Character character) {
        System.out.println(owner.getName() + " inflicts " + owner.getDamagePoints() + " damage points on " +
                character.getName() + ". " + character.getName() + "'s current health: " +
                character.getHealthPoints() + "/" + character.getMaxHealthPoints());
    }

    public Demon getOwner() {
        return owner;
    }

}
