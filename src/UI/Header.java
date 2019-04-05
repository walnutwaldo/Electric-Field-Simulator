package UI;

import java.awt.*;

public class Header extends UIComponent {

    private static final int LEFT_MARGIN = 10;
    private static final int TOP_MARGIN = 10;
    public static final int WIDTH = SideBar.MAX_WIDTH - 20;

    private Color color;
    public String text;
    private Font font;

    public Header(String _text, Font _font, Color _color) {
        super(LEFT_MARGIN, TOP_MARGIN, WIDTH, _font.getSize());
        text = _text;
        font = _font;
        color = _color;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, leftMargin, topMargin + height);
    }
}
