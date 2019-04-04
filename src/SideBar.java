import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SideBar {

    private static final int TIMER_DELAY = 5;

    public static final int MAX_WIDTH = 250;
    public static final int TAB_RADIUS = 30;
    public static int width = 0;

    public static boolean showingTab = false;
    public static boolean showingBar = false;
    public static int tabProtrusion = 0;

    private static Timer t;

    public static void init() {
        t = new Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showingBar)
                    width = Math.min(MAX_WIDTH, width + 25);
                else if (!showingBar)
                    width = Math.max(0, width - 25);
                if (showingTab)
                    tabProtrusion = Math.min(TAB_RADIUS, tabProtrusion + 10);
                else
                    tabProtrusion = Math.max(0, tabProtrusion - 10);
            }
        });
        t.start();
    }

    public static void open() { showingBar = true; }

    public static void close() { showingBar = false; }

    public static void toggle() { showingBar = !showingBar; }

    public static void showTab() { showingTab = true; }

    public static void hideTab() { showingTab = false; }

}
