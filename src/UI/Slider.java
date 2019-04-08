package UI;

import main.WindowManager;

import java.awt.*;

public class Slider extends UIComponent {

    public static final int TOP_MARGIN = 10;
    public static final int LEFT_MARGIN = 10;
    public static final int WIDTH = SideBar.MAX_WIDTH - 20;
    public static final int HEIGHT = 7;
    public static final int LINEAR = 0;
    public static final int LOGARITHMIC = 1;

    public static final int SLIDER_WIDTH = 7;
    public static final int SLIDER_HEIGHT = 12;

    public double sliderLoc;
    private double min, max;
    private int type;

    public Slider(double _min, double _max, int _type) {
        super(TOP_MARGIN, LEFT_MARGIN, WIDTH, HEIGHT);
        sliderLoc = 0.5;
        min = _min;
        max = _max;
        type = _type;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(leftMargin, topMargin, width, height);
        g.setColor(new Color(180, 180, 180));
        if (WindowManager.mouseUI.onSlider && this == WindowManager.mouseUI.currentSlider)
            g.setColor(new Color(200, 200, 200));
        g.fillRect(leftMargin + (int) (sliderLoc * WIDTH) - SLIDER_WIDTH / 2, topMargin + HEIGHT / 2 - SLIDER_HEIGHT / 2, SLIDER_WIDTH, SLIDER_HEIGHT);
    }

    private static final int LEEWAY = 1;

    private boolean between(double a, double b, double c) {
        return a >= b - LEEWAY && a <= c + LEEWAY;
    }

    public boolean onSlider(int x, int y) {
        return between(x - leftMargin - sliderLoc * WIDTH, -SLIDER_WIDTH / 2, SLIDER_WIDTH / 2) &&
                between(y - topMargin - HEIGHT / 2, -SLIDER_HEIGHT / 2, SLIDER_HEIGHT / 2);
    }

    public boolean onBar(int x, int y) {
        return between(x - leftMargin, 0, WIDTH) &&
                between(y - topMargin, 0, HEIGHT);
    }

    public double getVal() {
        if (type == LINEAR) return min + (max - min) * sliderLoc;
        else if (type == LOGARITHMIC) return min * Math.pow((max / min), sliderLoc);
        else return -1;
    }
}
