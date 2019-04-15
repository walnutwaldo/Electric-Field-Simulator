package UI;

import java.awt.*;

public class Header extends UIComponent {

    public static final int CENTER = 1;
    public static final int LEFT = 0;

    private Color color;
    public String text;
    private Font font;
    private int alignment;

    public Header(String _text, Font _font, Color _color, int _alignment) {
        super(_font.getSize());
        text = _text;
        font = _font;
        color = _color;
        alignment = _alignment;
    }

    public Header(String _text, Font _font, Color _color) {
        super(_font.getSize());
        text = _text;
        font = _font;
        color = _color;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int w = fm.stringWidth(text);
        if (alignment == LEFT) g.drawString(text, leftMargin, topMargin + height);
        else if (alignment == CENTER) g.drawString(text, leftMargin + (width - w) / 2, topMargin + height);
    }
}
