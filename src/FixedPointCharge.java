import math.Matrix;

public class FixedPointCharge {

    public static final double RADIUS = 1;

    private Matrix pos;
    private int charge;

    public FixedPointCharge() {
    }

    public FixedPointCharge(double x, double y, double z, int _charge) {
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

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }
}