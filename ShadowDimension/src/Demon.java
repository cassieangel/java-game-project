import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.Keys;
import bagel.util.*;

import java.util.ArrayList;
import java.util.Random;

/** represents a demon, that can attacks and inflict damage on player
 */
public class Demon extends Character {
    private Keys INC_TIME = Keys.L;
    private Keys DEC_TIME = Keys.K;

    private final Image DEMON_LEFT = new Image("res/demon/demonLeft.png");
    private final Image DEMON_RIGHT = new Image("res/demon/demonRight.png");
    private final Image DEMON_INVINCIBLE_LEFT = new Image("res/demon/demonInvincibleLeft.png");
    private final Image DEMON_INVINCIBLE_RIGHT = new Image("res/demon/demonInvincibleRight.png");
    private final static int INVINCIBLE_COOLDOWN = 1500;

    private final Font FONT = new Font("res/frostbite.ttf", FONT_SIZE);
    private final static int FONT_SIZE = 15;
    private final static int HEALTH_OFFSET = 6;

    protected final static int INITIAL_HEALTH_POINTS = 40;
    protected final static double SPEED_MIN = 0.2;
    protected final static double SPEED_MAX = 0.7;
    protected final static int ATTACK_RANGE = 150;
    protected final static int DAMAGE_POINTS = 10;
    private Timer invincibleCoolDown;
    private Rectangle hitBox;
    private boolean isAggressive;
    private double originalSpeed;
    private int oldHealthPoints;
    private int attackRange;
    private int xDirection;
    private int yDirection;

    private ArrayList<Fire> fires = new ArrayList<>();

    public Demon(double x, double y) {
        super(x, y, Math.random()*(SPEED_MAX-SPEED_MIN)+SPEED_MIN, DAMAGE_POINTS, INITIAL_HEALTH_POINTS);
        originalSpeed = getSpeed();
        attackRange = ATTACK_RANGE;
        invincibleCoolDown = new Timer(INVINCIBLE_COOLDOWN);

        setOldHealthPoints(getHealthPoints());
        randomDirection();
        setCurrentImage();
        isAggressive = randomAggressiveness();

        Point centrePoint = computeCentrePoint();
        Point hitBoxTopLeft = new Point(centrePoint.x-attackRange/2, centrePoint.y-attackRange/2);
        setObjectBox(getCurrentImage().getBoundingBoxAt(centrePoint));
        hitBox = new Rectangle(hitBoxTopLeft, attackRange, attackRange);
        setName(getClass().getSimpleName());
    }

    private void setCurrentImage() {
        if (invincibleCoolDown.isState()) {
            if (xDirection == 1) {
                setCurrentImage(DEMON_INVINCIBLE_RIGHT);
            } else {
                setCurrentImage(DEMON_INVINCIBLE_LEFT);
            }
        } else if (xDirection == 1) {
            setCurrentImage(DEMON_RIGHT);
        } else {
            setCurrentImage(DEMON_LEFT);
        }
    }

    /** method that creates random direction for demon to move
     */
    private void randomDirection() {
        Random random = new Random();
        do {
            // left(-1, 0), right(1, 0), up(0, -1) and down(0, 1)
            boolean generateDirection = random.nextBoolean();
            int direction;
            if (generateDirection == true) {
                direction = 1;
            } else {
                direction = -1;
            }
            xDirection = direction * random.nextInt(2);
            yDirection = direction * random.nextInt(2);

        } while (Math.abs(xDirection) == Math.abs(yDirection));
    }

    /** method that creates if demon is aggressive (boolean = 1) or passive (boolean = 0)
     */
    private boolean randomAggressiveness() {
        Random random = new Random();
        return random.nextBoolean();
    }

    @Override
    protected void displayHealth() {
        double percentageHP = ((double) getHealthPoints()/getMaxHealthPoints()) * 100;
        DrawOptions colour = new DrawOptions();
        if (percentageHP <= RED_BOUNDARY){
            colour.setBlendColour(RED);
        } else if (percentageHP <= ORANGE_BOUNDARY){
            colour.setBlendColour(ORANGE);
        } else {
            colour.setBlendColour(GREEN);
        }
        FONT.drawString(Math.round(percentageHP) + "%", getX(), getY()-HEALTH_OFFSET, colour);
    }

    @Override
    protected void render() {
        setCurrentImage();
        setObjectBox(getCurrentImage().getBoundingBoxAt(computeCentrePoint()));
        getCurrentImage().drawFromTopLeft(getX(), getY());
    }

    @Override
    public void update(Level level) {
        setOldHealthPoints(getHealthPoints());

        // move if only demon is aggressive
        if (isAggressive) {
            move(xDirection*getSpeed(), yDirection*getSpeed());
        }
        render();
        enemiesChangeDirection(level);
        displayHealth();

        // update position and its box when it moves
        Point centrePoint = computeCentrePoint();
        setObjectBox(getCurrentImage().getBoundingBoxAt(centrePoint));
        Point hitBoxTopLeft = new Point(centrePoint.x-attackRange/2, centrePoint.y-attackRange/2);
        hitBox = new Rectangle(hitBoxTopLeft, attackRange, attackRange);

        for (int i=0; i<fires.size(); i++) {
            fires.get(i).update(level);
        }
        shootFire(level); // shoot fire when intersect with player

        receiveDamage(level.getPlayer());
        becomeInvincible();

        if (getHealthPoints() <= 0) {
            isDead();
        }
    }

    /** method that reverse direction for demon when collide with sinkholes, walls, trees
     * or when it is out of bound.
     */
    protected void enemiesChangeDirection(Level level) {
        boolean changeDirectionObstacles = false;
        boolean changeDirectionSinkHoles = checkCollision((level).getSinkHoles());

        if (level instanceof Level0) {
            changeDirectionObstacles = checkCollision(((Level0)level).getWalls());
        } else if (level instanceof Level1) {
            changeDirectionObstacles = checkCollision(((Level1)level).getTrees());
        }
        if (outOfBound(level) || changeDirectionObstacles || changeDirectionSinkHoles) {
            xDirection *= -1;
            yDirection *= -1;
        }
    }

    protected Fire createFire(Player player) {
        return new Fire(hitBox.centre().x, hitBox.centre().y, player.getObjectBox().centre().x,
                player.getObjectBox().centre().y, this);
    }

    /** method to add fire to arraylist when player enters demon's boundary
     */
    protected void shootFire(Level level) {
        if (hitBox.intersects(level.getPlayer().getObjectBox())) {
            getFires().add(createFire(level.getPlayer()));
        }
    }

    /** method to set invisible and start countdown when enemy gets attacked by player
     */
    protected void becomeInvincible() {
        if (oldHealthPoints!=getHealthPoints() && !invincibleCoolDown.isState()) {
            invincibleCoolDown.startCountDown();
        } else if (invincibleCoolDown.isState()) {
            invincibleCoolDown.countDown();
        }
    }

    /** method to set health points and print damage log if damage is received
     */
    protected void receiveDamage(Player player) {
        if (player.getAttackState().isState() && checkCollision(player) && !invincibleCoolDown.isState()) {
            setHealthPoints(getHealthPoints() - player.getDamagePoints());
            player.printDamageLog(this);
        }
    }

    public ArrayList<Fire> getFires() {
        return fires;
    }
    public Rectangle getHitBox() {
        return hitBox;
    }
    public Timer getInvincibleCoolDown() {
        return invincibleCoolDown;
    }
    public double getOriginalSpeed() {
        return originalSpeed;
    }
    public int getXDirection() {
        return xDirection;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }
    public void setAggressive(boolean isAggressive) {
        this.isAggressive = isAggressive;
    }
    public void setOldHealthPoints(int oldHealthPoints) {
        this.oldHealthPoints = oldHealthPoints;
    }

    @Override
    public boolean isDead() {
        return super.isDead();
    }
}

