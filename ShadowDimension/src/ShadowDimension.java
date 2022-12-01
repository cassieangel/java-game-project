import bagel.*;
import java.util.ArrayList;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2022
 *
 * Please enter your name below
 * @author Angeline Cassie Ganily
 */

public class ShadowDimension extends AbstractGame {
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;

    private ArrayList<Level> levels = new ArrayList<>();
    private int currentLevel;

    public ShadowDimension(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        levels.add(new Level0());
        levels.add(new Level1());
        currentLevel = 0;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
        if (levels.get(currentLevel).levelNext() && currentLevel < levels.size() - 1) {
            currentLevel++;
        } else {
            levels.get(currentLevel).update(input);
        }
    }
}
