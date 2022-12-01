import bagel.*;

/** represents a SinkHole, which cannot be passed through by characters, and removed upon contact with player
 */
public class SinkHole extends GameObject implements DamageInflictable {
    private final static Image SINKHOLE = new Image("res/sinkhole.png");
    protected static final int DAMAGE_POINTS = 30;
    private boolean active;

    /** creates a SinkHole object
     * @param x top left x coordinate of the sinkHole
     * @param y top left y coordinate of the sinkHole
     */
    public SinkHole(double x, double y) {
        super(x, y);
        setCurrentImage(SINKHOLE);
        setObjectBox(getCurrentImage().getBoundingBoxAt(computeCentrePoint()));
        active = true;
    }

    @Override
    public void update(Level level) {
        getCurrentImage().drawFromTopLeft(getX(), getY());

        // collision with player, sinkhole is removed
        if (level.getPlayer().checkCollision(this)) {
            level.getPlayer().receiveDamage(this);
            active = false;
        }
    }

    @Override
    public void printDamageLog(Character character) {
        if (character.getHealthPoints()*100/character.getMaxHealthPoints() < 0) {
            System.out.println("Sinkhole inflicts " + DAMAGE_POINTS + " damage points on " + character.getName() + ". " +
                    character.getName() + "'s current health: " + 0 + "/" + character.getMaxHealthPoints());
        } else {
            System.out.println("Sinkhole inflicts " + DAMAGE_POINTS + " damage points on " + character.getName() + ". " +
                    character.getName() + "'s current health: " + character.getHealthPoints() + "/" +
                    character.getMaxHealthPoints());
        }
    }

    public boolean isActive() {
        return active;
    }
}
