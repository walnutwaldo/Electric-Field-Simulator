import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    private static final int TIMER_DELAY = 0;

    private static Timer timer;

    private static void update() {
        SimulationManager.update();
    }

    private static void render() {
        WindowManager.update();
    }

    public static void main(String[] args) {
        WindowManager.init();
        SimulationManager.init();
        SideBar.init();
        timer = new Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                render();
            }
        });
        timer.start();
    }

}