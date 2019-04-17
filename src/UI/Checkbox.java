package UI;

import main.WindowManager;

import java.awt.*;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;

public class Checkbox extends Button {

    private static final int GAP = 5;
    private static final int BOX_MARGIN = 3;
    private static final int WIDTH = 90;

    public static final int HEIGHT = 15;

    private boolean checked;

    public Checkbox(String _text, Font _font, boolean defaultChecked) {
        super(_text, _font, null);
        runnable = new Runnable() {
            @Override
            public void run() {
                checked = !checked;
            }
        };
        checked = defaultChecked;
        width = WIDTH;
        height = HEIGHT;
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(leftMargin, topMargin);
        g.setColor(new Color(250, 250, 250));
        if (WindowManager.mouseUI.onButton && this == WindowManager.mouseUI.currentButton) {
            if (WindowManager.mouseUI.downButton) g.setColor(new Color(230, 230, 230));
            else g.setColor(new Color(240, 240, 240));
        }
        g.fillRect(0, 0, HEIGHT, HEIGHT);
        if (checked) {
            g.setColor(new Color(50, 50, 50));
            if (WindowManager.mouseUI.onButton && this == WindowManager.mouseUI.currentButton) {
                if (WindowManager.mouseUI.downButton) g.setColor(new Color(30, 30, 30));
                else g.setColor(new Color(40, 40, 40));
            }
            g.fillRect(BOX_MARGIN, BOX_MARGIN, HEIGHT - 2 * BOX_MARGIN, HEIGHT - 2 * BOX_MARGIN);
        }
        g.setColor(Color.WHITE);
        g.setFont(font);
        LineMetrics lm = font.getLineMetrics(text, g.getFontRenderContext());
        g.drawString(text, HEIGHT + GAP, (int) ((HEIGHT + lm.getAscent() - lm.getDescent()) / 2));
        g.setTransform(at);
    }

    private static final int LEEWAY = 1;

    private boolean between(double a, double b, double c) {
        return a >= b - LEEWAY && a <= c + LEEWAY;
    }

    public boolean isOn(int x, int y) {
        return between(x - leftMargin, 0, HEIGHT) &&
                between(y - topMargin, 0, HEIGHT);
    }

    public boolean isChecked() {
        return checked;
    }

}
