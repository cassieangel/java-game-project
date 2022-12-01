import bagel.Image;

public class FireNavec extends Fire {
    private Image FIRE_NAVEC = new Image("res/navec/navecFire.png");

    public FireNavec(double demonX, double demonY, double playerX, double playerY, Demon owner) {
        super(demonX, demonY, playerX, playerY, owner);
        setCurrentImage(FIRE_NAVEC);
    }
}
