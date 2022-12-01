import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;

import java.util.ArrayList;
import java.util.Iterator;

public class Level1 extends Level {
    private final Image LEVEL1_BACKGROUND = new Image("res/background1.png");
    private static final String INSTRUCTION_MESSAGE1 = "PRESS A TO ATTACK";
    private static final String INSTRUCTION_MESSAGE2 = "DEFEAT NAVEC TO WIN";
    private static final String WIN_MESSAGE = "CONGRATULATIONS!";
    private static final String WORLD_FILE1 = "res/level1.csv";

    private Keys INC_TIMESCALE = Keys.L;
    private Keys DEC_TIMESCALE = Keys.K;
    private int timeScaleCount = 0;

    private ArrayList<Obstacle> trees = new ArrayList<>();
    private ArrayList<Demon> demons = new ArrayList<>();
    private Navec navec;

    Integer winScreenTime = Integer.MAX_VALUE; //infinity because win screen for final level will always be there

    public Level1() {
        readCSV(WORLD_FILE1);
        setLevelTransition(new Timer(winScreenTime));
    }

    @Override
    protected void readComponents(String[] sections) {
        switch (sections[0]) {
            case "Tree":
                trees.add(new Obstacle("Tree", Integer.parseInt(sections[1]), Integer.parseInt(sections[2])));
                break;

            case "Demon":
                demons.add(new Demon(Integer.parseInt(sections[1]), Integer.parseInt(sections[2])));
                break;

            case "Navec":
                navec = new Navec(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                break;
        }
    }

    @Override
    protected void drawInstructionMessage() {
        INSTRUCTION_FONT.drawString(INSTRUCTION_MESSAGE1, (Window.getWidth()/2.0 -
                        (INSTRUCTION_FONT.getWidth(INSTRUCTION_MESSAGE1)/2.0)), (INSTRUCTION_Y + INSTRUCTION_Y_OFFSET*4/3));
        INSTRUCTION_FONT.drawString(INSTRUCTION_MESSAGE2, (Window.getWidth()/2.0 -
                        (INSTRUCTION_FONT.getWidth(INSTRUCTION_MESSAGE2)/2.0)), (INSTRUCTION_Y + INSTRUCTION_Y_OFFSET*5/3));
    }

    @Override
    protected void drawBackground() {
        LEVEL1_BACKGROUND.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
    }

    /**
     * Method used to get other elements on screen that are not in common between different levels.
     * Also update character's speed (demons and navec) when timescale is changed.
     */
    @Override
    public void updateScreen(Input input) {
        drawBackground();
        for (int i=0; i<trees.size(); i++) {
            trees.get(i).update(this);
        }

        // timescale changes
        if (input.wasReleased(INC_TIMESCALE) && (timeScaleCount < 3)) {
            timeScaleCount += 1;
            System.out.println("Sped up, Speed: " + timeScaleCount);
        } else if (input.wasReleased(DEC_TIMESCALE) && (timeScaleCount > -3)) {
            timeScaleCount -= 1;
            System.out.println("Slowed down, Speed: " + timeScaleCount);
        }

        // update for demon
        for (Iterator<Demon> iterator = demons.iterator(); iterator.hasNext();) {
            Demon currEnemy = iterator.next();

            if (timeScaleCount > 0) {
                currEnemy.setSpeed(currEnemy.getOriginalSpeed()*Math.pow(1+0.5, Math.abs(timeScaleCount)));
            } else if (timeScaleCount < 0) {
                currEnemy.setSpeed(currEnemy.getOriginalSpeed()*Math.pow(1-0.5, Math.abs(timeScaleCount)));
            } else if (timeScaleCount == 0) {
                currEnemy.setSpeed(currEnemy.getOriginalSpeed());
            }

            currEnemy.update(this);

            // remove demon if it is already dead
            if (currEnemy.isDead()) {
                iterator.remove();
            }
        }

        // update for navec
        if (timeScaleCount > 0) {
            getNavec().setSpeed(getNavec().getOriginalSpeed()*Math.pow(1+0.5, Math.abs(timeScaleCount)));
        } else if (timeScaleCount < 0) {
            getNavec().setSpeed(getNavec().getOriginalSpeed()*Math.pow(1-0.5, Math.abs(timeScaleCount)));
        } else if (timeScaleCount == 0) {
            getNavec().setSpeed(getNavec().getOriginalSpeed());
        }
        navec.update(this);
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

    public ArrayList<Obstacle> getTrees() {
        return trees;
    }
    public Navec getNavec() {
        return navec;
    }
}
