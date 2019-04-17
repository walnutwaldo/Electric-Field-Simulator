package UI;

import main.WindowManager;

import java.awt.*;

public class Slider extends UIComponent {

    public static final int HEIGHT = 7;
    public static final int LINEAR = 0;
    public static final int LOGARITHMIC = 1;

    public static final int SLIDER_WIDTH = 7;
    public static final int SLIDER_HEIGHT = 12;

    private double sliderLoc;
    private double min, max;
    private int type;

    private Runnable update;

    public Slider(double _min, double _max, int _type) {
        super(HEIGHT);
        sliderLoc = 0.5;
        min = _min;
        max = _max;
        type = _type;
    }

    public Slider(double _min, double _max, int _type, Runnable _update) {
        super(HEIGHT);
        sliderLoc = 0.5;
        min = _min;
        max = _max;
        type = _type;
        update = _update;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(leftMargin, topMargin, width, height);
        g.setColor(new Color(180, 180, 180));
        if (this == WindowManager.mouseUI.currentSlider) {
            if (WindowManager.mouseUI.downSlider) g.setColor(new Color(220, 220, 220));
            else if (WindowManager.mouseUI.onSlider) g.setColor(new Color(200, 200, 200));
        }
        g.fillRect(leftMargin + (int) (sliderLoc * width) - SLIDER_WIDTH / 2, topMargin + height / 2 - SLIDER_HEIGHT / 2, SLIDER_WIDTH, SLIDER_HEIGHT);
    }

    private static final int LEEWAY = 1;

    private boolean between(double a, double b, double c) {
        return a >= b - LEEWAY && a <= c + LEEWAY;
    }

    public boolean onSlider(int x, int y) {
        return between(x - leftMargin - sliderLoc * width, -SLIDER_WIDTH / 2, SLIDER_WIDTH / 2) &&
                between(y - topMargin - height / 2, -SLIDER_HEIGHT / 2, SLIDER_HEIGHT / 2);
    }

    public boolean onBar(int x, int y) {
        return between(x - leftMargin, 0, width) &&
                between(y - topMargin, 0, height);
    }

    public double getVal() {
        if (type == LINEAR) return min + (max - min) * sliderLoc;
        else if (type == LOGARITHMIC) return min * Math.pow((max / min), sliderLoc);
        else return -1;
    }

    public void setSlider(double s) {
        sliderLoc = s;
        if (update != null) update.run();
    }

    public void setVal(double v) {
        if (type == LINEAR) sliderLoc = (v - min) / (max - min);
        else sliderLoc = Math.log(v / min) / Math.log(max / min);
        if (update != null) update.run();
    }
}
