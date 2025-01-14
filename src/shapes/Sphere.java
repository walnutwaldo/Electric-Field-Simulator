package shapes;

import editing.ChargeSelector;
import main.UIManager;
import math.Conics;
import math.Matrix;
import objects.Camera;
import objects.MovingCharge;

import java.awt.*;

import static math.LinAlg.solveLinSystem;
import static math.LinAlg.squareDis;

public class Sphere {

    private static double getFade(Matrix pos, double rad) {
        if (ChargeSelector.editing) return 1;
        double minD = MovingCharge.FADE_DIS;
        for (int i = 0; i < 3; i++)
            minD = Math.min(minD, UIManager.gridSizeSlider.getVal() + rad - Math.abs(pos.get(0, i)));
        return Math.max(0, Math.min(1, minD / MovingCharge.FADE_DIS));
    }

    private static Matrix getEllipse(Matrix pos, double r) {
        Matrix p_prime = Matrix.subtract(pos, Camera.getTransformedPos());
        double d = p_prime.length();
        double r_prime = r * Math.sqrt(d * d + r * r) / d;
        double d_prime = Math.sqrt(d * d - r * r - r_prime * r_prime);
        Matrix A = Matrix.scale(p_prime, d_prime / d);
        double b = Math.sqrt(Math.pow(A.get(0, 0), 2) + Math.pow(A.get(0, 1), 2));

        double a1 = A.get(0, 0);
        double a2 = -A.get(0, 1) * r_prime / b;
        double a3 = -A.get(0, 0) * A.get(0, 2) * r_prime / (d_prime * b);
        double a4 = A.get(0, 2);
        double a5 = b * r_prime / d_prime;
        double a6 = Math.tan(Camera.FOV / 2) * A.get(0, 1);
        double a7 = Math.tan(Camera.FOV / 2) * A.get(0, 0) * r_prime / b;
        double a8 = -Math.tan(Camera.FOV / 2) * A.get(0, 1) * A.get(0, 2) * r_prime / (d_prime * b);

        Matrix linSystem = new Matrix(new double[][]{
                {2 * a1 * a2, a2 * a4, 0, a2 * a6 + a1 * a7, a4 * a7, -2 * a6 * a7},
                {2 * a1 * a3, a3 * a4 + a1 * a5, 2 * a4 * a5, a3 * a6 + a1 * a8, a5 * a6 + a4 * a8, -2 * a6 * a8},
                {2 * a2 * a3, a2 * a5, 0, a2 * a8 + a3 * a7, a5 * a7, -2 * a7 * a8},
                {a1 * a1 + a3 * a3, a1 * a4 + a3 * a5, a4 * a4 + a5 * a5, a1 * a6 + a3 * a8, a4 * a6 + a5 * a8, -a6 * a6 - a8 * a8},
                {a2 * a2 - a3 * a3, -a3 * a5, -a5 * a5, a2 * a7 - a3 * a8, -a5 * a8, -a7 * a7 + a8 * a8}
        });
        Matrix ellipse = solveLinSystem(linSystem);
        return ellipse;
    }

    public static void fill(Graphics2D g, Matrix pos, double r, Color c) {
        double fade = getFade(Matrix.mult(pos, Camera.getTransformationMatrix().inv()), r);
        if (!Camera.visible(Matrix.add(pos, new Matrix(new double[][]{{0, r / Math.sin(Camera.FOV / 2), 0}}))))
            return;
        double brightness = Math.min(1, (UIManager.brightnessSlider.getVal() / squareDis(pos, Camera.getTransformedPos())) / 256);
        g.setColor(new Color((int) (brightness * c.getRed()), (int) (brightness * c.getGreen()), (int) (brightness * c.getBlue()), (int) (fade * c.getAlpha())));

        Conics.fillEllipse(g, getEllipse(pos, r));
    }

    public static void draw(Graphics2D g, Matrix pos, double r, Color c) {
        g.setColor(c);
        Conics.drawEllipse(g, getEllipse(pos, r));
    }

}
