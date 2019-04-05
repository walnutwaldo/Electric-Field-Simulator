package UI;

import java.util.ArrayList;
import java.util.List;

public class SideBar {

    public static final int MAX_WIDTH = 250;
    public static final int TAB_RADIUS = 30;
    private static final double TAB_COEFF = 5;
    public static int width = 0;

    public static boolean showingTab = false;
    public static boolean showingBar = false;

    private static long tabPressTime = 0;
    private static int tabInit = 0;

    public static void open() {
        showingBar = true;
        width = MAX_WIDTH;
    }

    public static void close() {
        showingBar = false;
        width = 0;
    }

    public static void toggle() {
        if (showingBar) close();
        else open();
    }

    public static void showTab() {
        if (!showingTab) {
            showingTab = true;
            long currT = System.currentTimeMillis();
            tabInit = (int) Math.max(0, tabInit - (currT - tabPressTime) / TAB_COEFF);
            tabPressTime = currT;
        }
    }

    public static void hideTab() {
        if (showingTab) {
            showingTab = false;
            long currT = System.currentTimeMillis();
            tabInit = (int) Math.min(TAB_RADIUS, tabInit + (currT - tabPressTime) / TAB_COEFF);
            tabPressTime = currT;
        }
    }

    public static int getTabProtrusion() {
        if (showingTab)
            return (int) Math.min(TAB_RADIUS, tabInit + (System.currentTimeMillis() - tabPressTime) / TAB_COEFF);
        else return (int) Math.max(0, tabInit - (System.currentTimeMillis() - tabPressTime) / TAB_COEFF);
    }

}
