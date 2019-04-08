package shapes;

import UI.SideBar;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class TabArrow {

    private static final double WIDTH = 1 / 2.0 * SideBar.TAB_RADIUS;
    private static final double HEIGHT = 2 * WIDTH;
    private static final double THICKNESS = 2.5 / 9 * SideBar.TAB_RADIUS;

    private static final double LEFT_SHIFT = (SideBar.TAB_RADIUS - WIDTH) / 2;
    private static final double RIGHT_SHIFT =
            (Math.pow(SideBar.TAB_RADIUS, 2) - 2 * Math.pow(WIDTH, 2)) / (2 * WIDTH + 2 * SideBar.TAB_RADIUS);
    private static final double[] SHIFT = new double[]{LEFT_SHIFT, RIGHT_SHIFT};

    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    public static void draw(Graphics2D g, int direction) {
        if (direction != LEFT && direction != RIGHT)
            throw new IllegalArgumentException("direction is not valid");
        g.translate(-SHIFT[direction], 0);
        Shape initClip = g.getClip();
        g.setClip(new Rectangle2D.Double(-WIDTH, -HEIGHT / 2, WIDTH, HEIGHT));
        g.rotate(Math.PI / 4, 0, 0);
        if (direction == LEFT) {
            g.fill(new Rectangle2D.Double(-WIDTH / Math.sqrt(2), -WIDTH / Math.sqrt(2), THICKNESS / Math.sqrt(2), WIDTH * Math.sqrt(2)));
            g.fill(new Rectangle2D.Double(
                    -WIDTH / Math.sqrt(2), (WIDTH - THICKNESS) / Math.sqrt(2), WIDTH * Math.sqrt(2), THICKNESS / Math.sqrt(2)));
        } else if (direction == RIGHT) {
            g.fill(new Rectangle2D.Double(-WIDTH * Math.sqrt(2), 0, WIDTH * Math.sqrt(2), THICKNESS / Math.sqrt(2)));
            g.fill(new Rectangle2D.Double(-THICKNESS / Math.sqrt(2), 0, THICKNESS / Math.sqrt(2), WIDTH * Math.sqrt(2)));
        }
        g.rotate(-Math.PI / 4, 0, 0);
        g.setClip(initClip);
        g.translate(SHIFT[direction], 0);
    }

}
