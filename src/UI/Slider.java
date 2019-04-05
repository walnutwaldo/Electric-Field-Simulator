package UI;

import java.awt.*;

public class Slider extends UIComponent {

    public static final int TOP_MARGIN = 10;
    public static final int LEFT_MARGIN = 10;
    public static final int WIDTH = SideBar.MAX_WIDTH - 20;
    public static final int HEIGHT = 10;

    public static final int SLIDER_WIDTH = 7;
    public static final int SLIDER_HEIGHT = 15;

    public double sliderLoc;

    public Slider() {
        super(TOP_MARGIN, LEFT_MARGIN, WIDTH, HEIGHT);
        sliderLoc = 0.5;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(leftMargin, topMargin, width, height);
        g.setColor(new Color(200, 200, 200));
        g.fillRect(leftMargin + (int) (sliderLoc * WIDTH) - SLIDER_WIDTH / 2, topMargin + HEIGHT / 2 - SLIDER_HEIGHT / 2, SLIDER_WIDTH, SLIDER_HEIGHT);
    }

    private boolean between(int a, int b, int c) {
        return a >= b && a <= c;
    }

    public boolean onSlider(int x, int y) {
        return between(x - (leftMargin + (int) (sliderLoc * WIDTH) - SLIDER_WIDTH / 2), 0, SLIDER_WIDTH) &&
                between(y - (topMargin + HEIGHT / 2 - SLIDER_HEIGHT / 2), 0, SLIDER_HEIGHT);
    }
}
