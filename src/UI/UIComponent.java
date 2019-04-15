package UI;

import java.awt.*;

public abstract class UIComponent {

    public static final int TOP_MARGIN = 10;
    public static final int LEFT_MARGIN = 10;
    public static final int WIDTH = SideBar.MAX_WIDTH - 20;

    public int topMargin = TOP_MARGIN, leftMargin = LEFT_MARGIN, width = WIDTH, height;

    public UIComponent(int _height) {
        height = _height;
    }

    public abstract void draw(Graphics2D g);
}
