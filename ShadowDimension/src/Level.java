import bagel.*;
import bagel.util.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Level {
    protected final Font INSTRUCTION_FONT = new Font("res/frostbite.ttf", INSTRUCTION_FONT_SIZE);
    protected final Font TITLE_FONT = new Font("res/frostbite.ttf", TITLE_FONT_SIZE);
    private final static String START_MESSAGE = "PRESS SPACE TO START";
    private final static String END_MESSAGE = "GAME OVER";
    protected final static int INSTRUCTION_FONT_SIZE = 40;
    protected final static int TITLE_FONT_SIZE = 75;
    protected final static int TITLE_X = 260;
    protected final static int TITLE_Y = 250;
    protected final static int INSTRUCTION_Y = 250;
    protected final static int INSTRUCTION_Y_OFFSET = 190;

    private boolean levelStarted;
    private boolean levelEnded;
    private boolean levelNext;
    private boolean gameOver;

    private ArrayList<SinkHole> sinkholes = new ArrayList<>();
    private Player player;
    private Point topLeft;
    private Point bottomRight;

    private Timer levelTransition;

    public Level() {
        levelStarted = levelEnded = levelNext = gameOver = false;
    }

    /**
     * Method used to read file and create objects.
     */
    protected void readCSV(String fileName){
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String line;
            while((line = reader.readLine()) != null){
                String[] sections = line.split(",");
                switch (sections[0]) {
                    case "Fae":
                        player = new Player(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;
                    case "Sinkhole":
                        sinkholes.add(new SinkHole(Integer.parseInt(sections[1]), Integer.parseInt(sections[2])));
                        break;
                    case "TopLeft":
                        topLeft = new Point(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;
                    case "BottomRight":
                        bottomRight = new Point(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;
                }
                readComponents(sections);
            }
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Performs a state update.
     * allows the game to start when space is pressed.
     */
    protected void update(Input input) {
        if (input.wasPressed(Keys.SPACE)) {
            levelStarted = true;
        }
        if (!levelStarted) {
            drawStartScreen();
        } else if (!levelEnded && !gameOver) {
            updateScreen(input);

            for (Iterator<SinkHole> iterator = sinkholes.iterator(); iterator.hasNext(); ) {
                SinkHole currSinkhole = iterator.next();
                currSinkhole.update(this);
                if (!currSinkhole.isActive()) {
                    iterator.remove();
                }
            }

            player.update(input, this);

            if (player.hasWon(this)) {
                levelEnded = true;
                levelTransition.startCountDown();
            }
            if (player.isDead()) {
                gameOver = true;
            }
        }

        if (levelEnded && levelTransition.isState()) {
            playLevelTransition();
        } else if (levelEnded) {
            levelNext = true;
        } else if (gameOver) {
            drawEndScreen();
        }
    }

    /**
     * Method used to draw the start of the screen before space bar is pressed.
     */
    private void drawStartScreen() {
        INSTRUCTION_FONT.drawString(START_MESSAGE,
                (Window.getWidth()/2.0 - (INSTRUCTION_FONT.getWidth(START_MESSAGE)/2.0)), INSTRUCTION_Y + INSTRUCTION_Y_OFFSET);
        drawInstructionMessage();
    }

    /**
     * Method used to draw the end of the screen after player has either won or loses.
     */
    protected void drawEndScreen() {
        if (player.isDead()) {
            TITLE_FONT.drawString(END_MESSAGE,
                    (Window.getWidth()/2.0 - (TITLE_FONT.getWidth(END_MESSAGE)/2.0)), (Window.getHeight()/2.0));
        } else {
            drawWinScreen();
        }
    }

    protected abstract void drawInstructionMessage();
    protected abstract void drawBackground();
    protected abstract void updateScreen(Input input);
    protected abstract void drawWinScreen();
    protected abstract void playLevelTransition();
    protected abstract void readComponents(String[] sections);

    public boolean levelNext() {
        return levelNext;
    }
    public void setLevelTransition(Timer levelTransition) {
        this.levelTransition = levelTransition;
    }
    public Player getPlayer() {
        return player;
    }
    public Point getTopLeftBoundary() {
        return topLeft;
    }
    public Point getBottomRightBoundary() {
        return bottomRight;
    }
    public Timer getLevelTransition() {
        return levelTransition;
    }
    public ArrayList<SinkHole> getSinkHoles() {
        return sinkholes;
    }
}
