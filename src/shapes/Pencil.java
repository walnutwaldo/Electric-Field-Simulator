package shapes;


import UI.SideBar;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class Pencil {

    private static final double GAP = 0.05 * SideBar.OPTIONS_HEIGHT;
    private static final double BODY_WIDTH = 0.5 * SideBar.OPTIONS_HEIGHT;
    private static final double ERASER_WIDTH = 0.1 * SideBar.OPTIONS_HEIGHT;
    private static final double WIDTH = 0.15 * SideBar.OPTIONS_HEIGHT;
    private static final double TIP_WIDTH = 0.2 * SideBar.OPTIONS_HEIGHT;

    public static void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate((double) SideBar.OPTIONS_HEIGHT / 2, (double) SideBar.OPTIONS_HEIGHT / 2);
        g.rotate(Math.PI / 4);
        double totalH = 2 * GAP + BODY_WIDTH + ERASER_WIDTH + TIP_WIDTH;
        g.fill(new Rectangle2D.Double(-WIDTH / 2, -totalH / 2, WIDTH, ERASER_WIDTH));
        g.fill(new Rectangle2D.Double(-WIDTH / 2, ERASER_WIDTH + GAP - totalH / 2, WIDTH, BODY_WIDTH));
        Path2D.Double triangle = new Path2D.Double();
        triangle.moveTo(-WIDTH / 2, totalH / 2 - TIP_WIDTH);
        triangle.lineTo(WIDTH / 2, totalH / 2 - TIP_WIDTH);
        triangle.lineTo(0, totalH / 2);
        triangle.closePath();
        g.fill(triangle);
        g.setTransform(at);
    }

}
