import bagel.*;
import bagel.util.Rectangle;

/** represents a player on the game
 */
public class Player extends Character implements DamageInflictable {
    private final static Image PLAYER_LEFT = new Image("res/fae/faeLeft.png");
    private final static Image PLAYER_RIGHT = new Image("res/fae/faeRight.png");
    private final static Image ATTACK_PLAYER_LEFT = new Image("res/fae/faeAttackLeft.png");
    private final static Image ATTACK_PLAYER_RIGHT = new Image("res/fae/faeAttackRight.png");

    private final Font FONT = new Font("res/frostbite.ttf", FONT_SIZE);
    private final static int FONT_SIZE = 30;
    private final static int HEALTH_X = 20;
    private final static int HEALTH_Y = 25;

    private final static double MOVEMENT_SPEED = 2;
    private final static int DAMAGE_POINTS = 20;
    private final static int INITIAL_HEALTH_POINTS = 100;
    private final static int INVINCIBLE_COOLDOWN = 3000;
    private static final int ATTACK_DURATION = 1000;
    private static final int ATTACK_COOLDOWN = 2000;
    private Keys ATTACK_KEY = Keys.A;
    private Timer invincibleCoolDown;
    private Timer attackCoolDown;
    private Timer attackState;
    private String NAME = "Fae";
    private boolean faceRight;

    public Player(double x, double y) {
        super(x, y, MOVEMENT_SPEED, DAMAGE_POINTS, INITIAL_HEALTH_POINTS);
        faceRight = true;
        setCurrentImage(PLAYER_RIGHT);
        setObjectBox(PLAYER_RIGHT.getBoundingBoxAt(computeCentrePoint()));
        setName(NAME);
        invincibleCoolDown = new Timer(INVINCIBLE_COOLDOWN);
        attackState = new Timer(ATTACK_DURATION);
        attackCoolDown = new Timer(ATTACK_COOLDOWN);
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
        FONT.drawString(Math.round(percentageHP) + "%", HEALTH_X, HEALTH_Y, colour);
    }

    /** method when player enters attack state when attack key is pressed.
     * starts countdown for attack stage and cooldown after where player cannot attack during it.
     */
    private void enterAttackState(Input input) {
        if (!attackCoolDown.isState() && input.isDown(ATTACK_KEY)) {
            attackState.startCountDown();
        }
        if (attackState.isState()) {
            attackState.countDown();
            if (!attackState.isState()) {
                attackCoolDown.startCountDown();
            }
        }
        if (attackCoolDown.isState()) {
            attackCoolDown.countDown();
        }
    }

    @Override
    protected void render() {
        if (attackState.isState()) {    // if sailor is in attack state
            if (faceRight) {
                setCurrentImage(ATTACK_PLAYER_RIGHT);
            } else {
                setCurrentImage(ATTACK_PLAYER_LEFT);
            }
        } else if (faceRight) {
            setCurrentImage(PLAYER_RIGHT);
        } else {
            setCurrentImage(PLAYER_LEFT);
        }
        setObjectBox(getCurrentImage().getBoundingBoxAt(computeCentrePoint()));
        getCurrentImage().drawFromTopLeft(getX(), getY());
    }

    /** method to move back to old position.
     */
    private void moveBack() {
        setX(getOldX());
        setY(getOldY());
    }

    /** method for update player's logic to move (move back when collide with obstacle
     * or when it is out of the screen. also sets invincible state when player is attacked
     * and enters attack state when attacking.
     */
    public void update(Input input, Level level) {
        // check collision with wall or trees depending on level
        boolean collide = false;
        if (level instanceof Level0) {
            collide = checkCollision(((Level0)level).getWalls());
        } else if (level instanceof Level1) {
            collide = checkCollision(((Level1) level).getTrees());
        }

        // move if player is not out of bound / collide with obstacles
        if (!(outOfBound(level) || collide)) {
            if (input.isDown(Keys.UP)){
                setOldY(getY());
                move(0, -getSpeed());
            } else if (input.isDown(Keys.DOWN)) {
                setOldY(getY());
                move(0, getSpeed());
            } else if (input.isDown(Keys.LEFT)) {
                faceRight = false;
                setOldX(getX());
                move(-getSpeed(),0);
            } else if (input.isDown(Keys.RIGHT)) {
                faceRight = true;
                setOldX(getX());
                move(getSpeed(),0);
            }
        } else {
            moveBack();
        }

        // countdown for invisible state
        if (invincibleCoolDown.isState()){
            invincibleCoolDown.countDown();
        }

        enterAttackState(input);
        update(level);
    }

    /** method to update screen (render player and display player's health)
     */
    @Override
    public void update(Level level) {
        render();
        displayHealth();
    }

    /** method to check whether damage is received by player when attacker
     * (can be sinkhole, fire) attacks the player. also sets health points for player and print logs.
     */
    public boolean receiveDamage(DamageInflictable attacker) {
        if (attacker instanceof SinkHole) {
            SinkHole sinkhole = (SinkHole) attacker;
            if (checkCollision(sinkhole)) {
                setHealthPoints(getHealthPoints() - sinkhole.DAMAGE_POINTS);
                sinkhole.printDamageLog(this);
                return true;
            }
        }

        if (attacker instanceof Fire) {
            Fire fire = (Fire) attacker;
            Rectangle FireBox = new Rectangle(fire.getX(), fire.getY(), fire.getCurrentImage().getWidth(),
                    fire.getCurrentImage().getHeight());
            if (getObjectBox().intersects(FireBox) && !invincibleCoolDown.isState()) {
                invincibleCoolDown.startCountDown();
                setHealthPoints(getHealthPoints() - fire.getOwner().getDamagePoints());
                fire.printDamageLog(this);
                return true;
            }
        }

        if (attacker instanceof FireNavec) {
            FireNavec fire = (FireNavec) attacker;
            Rectangle FireBox = new Rectangle(fire.getX(), fire.getY(), fire.getCurrentImage().getWidth(),
                    fire.getCurrentImage().getHeight());
            if (getObjectBox().intersects(FireBox) && !invincibleCoolDown.isState()) {
                invincibleCoolDown.startCountDown();
                setHealthPoints(getHealthPoints() - fire.getOwner().getDamagePoints());
                fire.printDamageLog(this);
                return true;
            }
        }
        return false;
    }

    /** method to check whether player has won and returns boolean.
     */
    public boolean hasWon(Level level) {
        if (level instanceof Level0) {
            return (getX() >= Level0.WIN_X) && (getY() > Level0.WIN_Y);
        } else if (level instanceof Level1) {
            return ((Level1)level).getNavec().isDead();
        }
        return false;
    }

    @Override
    public void printDamageLog(Character character) {
        System.out.println("Fae inflicts " + getDamagePoints() + " damage points on " + character.getName() + ". " +
                character.getName() + "'s" + " current health: " + character.getHealthPoints() + "/" +
                character.getMaxHealthPoints());
    }

    public Timer getAttackState() {
        return attackState;
    }
}
