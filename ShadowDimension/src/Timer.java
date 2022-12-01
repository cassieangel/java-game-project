/** represents a timer, for countdown whenever there is a countdown needed
 */
public class Timer {
    private static int FRAMES_SEC = 60;
    private static int SECS_TO_MS = 1000;

    private int counter;
    private boolean state;
    private final int timer;

    public Timer(int countDown) {
        state = false;
        counter = 0;
        this.timer = countDown;
    }

    /** method to count the number of times a frame has been rendered
     * and change state of timer to false when countdown has ended.
     */
    public void countDown() {
        if (counter < timer/SECS_TO_MS*FRAMES_SEC) {
            counter++;
        } else {
            state = false;
            counter = 0;
        }
    }

    public void startCountDown() {
        state = true;
    }
    public boolean isState() {
        return state;
    }
}
