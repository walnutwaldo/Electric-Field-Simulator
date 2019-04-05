package UI;

import java.awt.*;

public abstract class UIComponent {

    public int topMargin, leftMargin, width, height;

    public UIComponent(int topMargin, int leftMargin, int width, int height) {
        this.topMargin = topMargin;
        this.leftMargin = leftMargin;
        this.width = width;
        this.height = height;
    }

    public abstract void draw(Graphics2D g);
}
