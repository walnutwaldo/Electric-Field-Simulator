package objects;

import editing.ChargeSelector;
import math.Matrix;
import shapes.Sphere;

import java.awt.*;

public class FixedPointCharge implements Positionable {

    public static final int MIN_CHARGE = -25;
    public static final int MAX_CHARGE = 25;

    public static final double RADIUS = 1;

    private Matrix pos;
    private double charge;

    public FixedPointCharge() {
    }

    public FixedPointCharge(double x, double y, double z, double _charge) {
        pos = new Matrix(new double[][]{{x, y, z}});
        charge = _charge;
    }

    public double getX() {
        return pos.get(0, 0);
    }

    public void setX(double x) {
        pos.set(0, 0, x);
    }

    public double getY() {
        return pos.get(0, 1);
    }

    public void setY(double y) {
        pos.set(0, 1, y);
    }

    public double getZ() {
        return pos.get(0, 2);
    }

    public void setZ(double z) {
        pos.set(0, 2, z);
    }

    public Matrix getPos() {
        return pos.clone();
    }

    public double getDisTo(Matrix m) {
        return Matrix.subtract(m, pos).length() - FixedPointCharge.RADIUS;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    @Override
    public void draw(Graphics2D g) {
        Sphere.fill(g, Matrix.mult(getPos(), Camera.getTransformationMatrix()), FixedPointCharge.RADIUS, Color.WHITE);
        if (ChargeSelector.currCharge == this) {
            if (ChargeSelector.on) {
                if (ChargeSelector.down)
                    Sphere.fill(g, Matrix.mult(getPos(), Camera.getTransformationMatrix()), FixedPointCharge.RADIUS, new Color(0, 240, 0, 100));
                Sphere.fill(g, Matrix.mult(getPos(), Camera.getTransformationMatrix()), FixedPointCharge.RADIUS, new Color(0, 250, 0, 100));
            }
        }
        if (ChargeSelector.selectedCharge == this)
            Sphere.draw(g, Matrix.mult(getPos(), Camera.getTransformationMatrix()), FixedPointCharge.RADIUS, new Color(255, 0, 0));
    }
}