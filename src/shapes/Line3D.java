package shapes;

import main.UIManager;
import math.Matrix;
import objects.Camera;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import static math.LinAlg.*;

public class Line3D {

    private static final float LINE_THICKNESS = 0.0015f;

    private static final int[] DX = new int[]{-1, 0, 1, 0};
    private static final int[] DY = new int[]{0, 1, 0, -1};

    public static LineSeg getVisibleSeg(Matrix point1, Matrix point2) {
        if (Camera.visible(point1) && Camera.visible(point2)) return new LineSeg(point1, point2);
        ArrayList<Matrix> ints = new ArrayList<Matrix>();
        for (int i = 0; i < 4; i++) {
            Matrix normal = new Matrix(new double[][]{{DX[i] * Math.sin(Camera.FOV / 2 + Math.PI / 2),
                    Math.cos(Camera.FOV / 2 + Math.PI / 2), DY[i] * Math.sin(Camera.FOV / 2 + Math.PI / 2)}});
            Matrix inters = intersection(new LineSeg(point1, point2), new Plane(Camera.getTransformedPos(), normal));
            if (Camera.visible(inters)) ints.add(inters);
        }
        if (Camera.visible(point1)) return new LineSeg(point1, ints.get(0));
        if (Camera.visible(point2)) return new LineSeg(point2, ints.get(0));
        else if (!ints.isEmpty()) {
            for (int i = ints.size() - 1; i > 0; i--) if (approx(ints.get(i), ints.get(i - 1))) ints.remove(i);
            return new LineSeg(ints.get(0), ints.get(1));
        }
        return new LineSeg(null, null);
    }

    public static void draw(Graphics2D g, LineSeg ls) {
        Matrix cameraMatrix = Camera.getTransformationMatrix();
        Matrix p1 = Matrix.mult(ls.pnt1, cameraMatrix);
        Matrix p2 = Matrix.mult(ls.pnt2, cameraMatrix);
        LineSeg vis = getVisibleSeg(p1, p2);

        int brightness = (int) Math.min(255, UIManager.brightnessSlider.getVal() / squareDis(Matrix.scale(Matrix.add(p1, p2), 0.5), Camera.getTransformedPos()));
        g.setColor(new Color(brightness, brightness, brightness));

        if (vis.pnt1 == null) return;
        p1 = Camera.getProjection(vis.pnt1);
        p2 = Camera.getProjection(vis.pnt2);
        g.setStroke(new BasicStroke(LINE_THICKNESS));
        g.draw(new Line2D.Double(p1.get(0, 0), p1.get(0, 1), p2.get(0, 0), p2.get(0, 1)));
    }

}
