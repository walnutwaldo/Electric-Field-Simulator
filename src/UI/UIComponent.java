package UI;

import java.awt.*;

public abstract class UIComponent {

    public int topMargin, leftMargin, width, height;

    public UIComponent(int _topMargin, int _leftMargin, int _width, int _height) {
        topMargin = _topMargin;
        leftMargin = _leftMargin;
        width = _width;
        height = _height;
    }

    public abstract void draw(Graphics2D g);
}
