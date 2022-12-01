import bagel.Image;

/** represents a navec, special aggressive demon
 */
public class Navec extends Demon {
    private final Image NAVEC_LEFT = new Image("res/navec/navecLeft.png");
    private final Image NAVEC_RIGHT = new Image("res/navec/navecRight.png");
    private final Image NAVEC_INVINCIBLE_LEFT = new Image("res/navec/navecInvincibleLeft.png");
    private final Image NAVEC_INVINCIBLE_RIGHT = new Image("res/navec/navecInvincibleRight.png");

    public Navec(double x, double y) {
        super(x, y);
        setHealthPoints(INITIAL_HEALTH_POINTS*2); // 40*2=80
        setMaxHealthPoints(INITIAL_HEALTH_POINTS*2);
        setAttackRange(ATTACK_RANGE*4/3); // 150*4/3=200
        setDamagePoints(DAMAGE_POINTS*2); // 10*2=20
        setAggressive(true);
        setObjectBox(NAVEC_RIGHT.getBoundingBoxAt(computeCentrePoint()));
        setName(getClass().getSimpleName());
    }

    @Override
    protected void render() {
        if (getInvincibleCoolDown().isState()) {
            if (getXDirection() == 1) {
                setCurrentImage(NAVEC_INVINCIBLE_RIGHT);
            } else {
                setCurrentImage(NAVEC_INVINCIBLE_LEFT);
            }
        } else if (getXDirection() == 1) {
            setCurrentImage(NAVEC_RIGHT);
        } else {
            setCurrentImage(NAVEC_LEFT);
        }
        setObjectBox(getCurrentImage().getBoundingBoxAt(computeCentrePoint()));
        getCurrentImage().drawFromTopLeft(getX(), getY());
    }

    @Override
    protected Fire createFire(Player player) {
        return new FireNavec(getHitBox().centre().x, getHitBox().centre().y,
                player.getObjectBox().centre().x, player.getObjectBox().centre().y, this);
    }
}
