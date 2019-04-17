package UI;

import main.WindowManager;

import java.awt.*;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Button extends UIComponent {

    public static final int HEIGHT = 30;

    public String text;
    protected Font font;

    protected Runnable runnable;

    public Button(String _text, Font _font, Runnable _runnable) {
        super(HEIGHT);
        text = _text;
        font = _font;
        runnable = _runnable;
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(leftMargin, topMargin);
        g.setColor(new Color(220, 220, 220));
        if (WindowManager.mouseUI.onButton && this == WindowManager.mouseUI.currentButton) {
            if (WindowManager.mouseUI.downButton) g.setColor(new Color(200, 200, 200));
            else g.setColor(new Color(210, 210, 210));
        }
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.BLACK);
        g.setFont(font);
        LineMetrics lm = font.getLineMetrics(text, g.getFontRenderContext());
        Rectangle2D rect = font.getStringBounds(text, g.getFontRenderContext());
        g.drawString(text, (int) ((WIDTH - rect.getWidth()) / 2), (int) ((HEIGHT + lm.getAscent() - lm.getDescent()) / 2));
        g.setTransform(at);
    }

    private static final int LEEWAY = 1;

    private boolean between(double a, double b, double c) {
        return a >= b - LEEWAY && a <= c + LEEWAY;
    }

    public boolean isOn(int x, int y) {
        return between(x - leftMargin, 0, WIDTH) &&
                between(y - topMargin, 0, HEIGHT);
    }

    public void press() {
        runnable.run();
    }

}
