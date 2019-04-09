package shapes;

import UI.SideBar;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Gear {

    private static final int NUM_TEETH = 8;
    private static final double R_OUTER = 0.275 * SideBar.OPTIONS_HEIGHT;
    private static final double R_INNER = 0.125 * SideBar.OPTIONS_HEIGHT;
    private static final double TEETH_WIDTH = 0.12 * SideBar.OPTIONS_HEIGHT;
    private static final double TEETH_PROTRUSION = 0.075 * SideBar.OPTIONS_HEIGHT;

    public static void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(SideBar.OPTIONS_HEIGHT / 2, SideBar.OPTIONS_HEIGHT / 2);
        g.setStroke(new BasicStroke((float) (R_OUTER - R_INNER)));
        g.draw(new Ellipse2D.Double(-(R_OUTER + R_INNER) / 2, -(R_OUTER + R_INNER) / 2, R_OUTER + R_INNER, R_OUTER + R_INNER));
        for (int i = 0; i < NUM_TEETH; i++) {
            g.fill(new Rectangle2D.Double(-TEETH_WIDTH / 2, -R_OUTER - TEETH_PROTRUSION, TEETH_WIDTH, R_OUTER + TEETH_PROTRUSION - R_INNER));
            g.rotate(Math.PI * 2 / NUM_TEETH);
        }
        g.setTransform(at);
    }

}
