package shapes;

import main.UIManager;
import math.LinAlg;
import math.Matrix;
import objects.Camera;

import java.awt.*;
import java.awt.geom.Path2D;

import static math.LinAlg.squareDis;

public class Triangle3D {

    public static void draw(Graphics2D g, LinAlg.Triangle t, Color c) {
        if (!Camera.visible(t.centroid())) return;
        Matrix centroid = t.centroid();
        double s = 256 * squareDis(centroid, Camera.getTransformedPos());
        double brightness = Math.min(1, UIManager.brightnessSlider.getVal() / s);
        g.setColor(new Color((int) (brightness * c.getRed()), (int) (brightness * c.getGreen()), (int) (brightness * c.getBlue())));

        Matrix p1 = Camera.getProjection(t.p1);
        Matrix p2 = Camera.getProjection(t.p2);
        Matrix p3 = Camera.getProjection(t.p3);

        Path2D.Double triangle = new Path2D.Double();
        triangle.moveTo(p1.get(0, 0), p1.get(0, 1));
        triangle.lineTo(p2.get(0, 0), p2.get(0, 1));
        triangle.lineTo(p3.get(0, 0), p3.get(0, 1));
        triangle.closePath();

        g.fill(triangle);
    }

}
