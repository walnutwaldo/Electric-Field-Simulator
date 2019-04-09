package shapes;

import UI.SideBar;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class InfoIcon {

    private static final double RADIUS = 0.32 * SideBar.OPTIONS_HEIGHT;
    private static final double CIRCLE_THICKNESS = 0.05 * SideBar.OPTIONS_HEIGHT;

    private static final double WIDTH = 0.07 * SideBar.OPTIONS_HEIGHT;
    private static final double DOT_WIDTH = 0.08 * SideBar.OPTIONS_HEIGHT;
    private static final double HEIGHT = 0.28 * SideBar.OPTIONS_HEIGHT;
    private static final double OVERHANG = 0.04 * SideBar.OPTIONS_HEIGHT;
    private static final double MINI_H = 0.03 * SideBar.OPTIONS_HEIGHT;
    private static final double GAP = 0.04 * SideBar.OPTIONS_HEIGHT;

    public static void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(SideBar.OPTIONS_HEIGHT / 2, SideBar.OPTIONS_HEIGHT / 2);
        g.setStroke(new BasicStroke((float) CIRCLE_THICKNESS));
        g.draw(new Ellipse2D.Double(-RADIUS, -RADIUS, 2 * RADIUS, 2 * RADIUS));
        double totalH = HEIGHT + GAP + DOT_WIDTH;
        g.fill(new Ellipse2D.Double(-DOT_WIDTH / 2, -totalH / 2, DOT_WIDTH, DOT_WIDTH));
        g.fill(new Rectangle2D.Double(-WIDTH / 2 - OVERHANG, -totalH / 2 + DOT_WIDTH + GAP, OVERHANG + WIDTH, MINI_H));
        g.fill(new Rectangle2D.Double(-WIDTH / 2, -totalH / 2 + DOT_WIDTH + GAP, WIDTH, HEIGHT));
        g.fill(new Rectangle2D.Double(-WIDTH / 2 - OVERHANG, totalH / 2 - MINI_H, 2 * OVERHANG + WIDTH, MINI_H));
        g.setTransform(at);
    }

}
