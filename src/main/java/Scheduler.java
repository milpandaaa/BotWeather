import java.util.Timer;


public class Scheduler {
    public static void run() {
        TimerExample example = new TimerExample();
        Timer timer = new Timer();
        timer.schedule(example, 86_400_000L);
    }
}
