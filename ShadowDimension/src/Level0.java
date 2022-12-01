import bagel.Input;
import bagel.Image;
import bagel.Window;

import java.util.ArrayList;

public class Level0 extends Level {
    private final Image LEVEL0_BACKGROUND = new Image("res/background0.png");
    private final static String INSTRUCTION_MESSAGE = "USE ARROW KEYS TO FIND GATE";
    private final static String WIN_MESSAGE = "LEVEL COMPLETE!";
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static String WORLD_FILE0 = "res/level0.csv";

    private static final int WIN_DISPLAY_TIME = 3000;
    protected static final int WIN_X = 950;
    protected static final int WIN_Y = 670;

    private ArrayList<Obstacle> walls = new ArrayList<>();

    public Level0() {
        readCSV(WORLD_FILE0);
        setLevelTransition(new Timer(WIN_DISPLAY_TIME));
    }

    @Override
    protected void readComponents(String[] sections) {
        switch (sections[0]) {
            case "Wall":
                walls.add(new Obstacle("Wall", Integer.parseInt(sections[1]), Integer.parseInt(sections[2])));
                break;
        }
    }

    @Override
    protected void drawInstructionMessage() {
        TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
        INSTRUCTION_FONT.drawString(INSTRUCTION_MESSAGE, (Window.getWidth()/2.0 - (INSTRUCTION_FONT.getWidth(INSTRUCTION_MESSAGE)/2.0)),
                (INSTRUCTION_Y + INSTRUCTION_Y_OFFSET*4/3));
    }

    @Override
    protected void drawBackground() {
        LEVEL0_BACKGROUND.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
    }

    /**
     * Method used to get other elements on screen that are not in common between different levels.
     */
    @Override
    public void updateScreen(Input input) {
        drawBackground();
        for (int i=0; i<walls.size(); i++) {
            walls.get(i).update(this);
        }
    }

    @Override
    protected void drawWinScreen() {
        TITLE_FONT.drawString(WIN_MESSAGE,
                (Window.getWidth()/2.0 - (TITLE_FONT.getWidth(WIN_MESSAGE)/2.0)), (Window.getHeight()/2.0));
    }

    @Override
    protected void playLevelTransition() {
        getLevelTransition().countDown();
        drawEndScreen();
    }

    public ArrayList<Obstacle> getWalls() {
        return walls;
    }
}
