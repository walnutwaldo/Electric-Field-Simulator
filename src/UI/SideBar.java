package UI;

public class SideBar {

    public static final int MAX_WIDTH = 250;
    public static final double TAB_RADIUS = 30;
    public static final int OPTIONS_HEIGHT = 40;
    private static final double TAB_COEFFICIENT = 5;
    public static int width = 0;

    public static final int NUM_OPTIONS = 4;
    public static final int EDIT = 0;
    public static final int SETTINGS = 3;

    public static int currentOption = 0;

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
            tabInit = (int) Math.max(0, tabInit - (currT - tabPressTime) / TAB_COEFFICIENT);
            tabPressTime = currT;
        }
    }

    public static void hideTab() {
        if (showingTab) {
            showingTab = false;
            long currT = System.currentTimeMillis();
            tabInit = (int) Math.min(TAB_RADIUS, tabInit + (currT - tabPressTime) / TAB_COEFFICIENT);
            tabPressTime = currT;
        }
    }

    public static int getTabProtrusion() {
        if (showingTab)
            return (int) Math.min(TAB_RADIUS, tabInit + (System.currentTimeMillis() - tabPressTime) / TAB_COEFFICIENT);
        else return (int) Math.max(0, tabInit - (System.currentTimeMillis() - tabPressTime) / TAB_COEFFICIENT);
    }

}
