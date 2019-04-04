package objects;

import math.Matrix;

import java.util.List;

import static math.LinAlg.*;

public class MovingCharge implements Positionable {

    public static final double RADIUS = 0.1;
    public static final double MIN_DIS = 0.1;
    public static final double SPEED = 300;
    public static final double MAX_STEP_DIS = 0.1;

    private Matrix pos;
    private Matrix velocity;

    private boolean finished;

    private long lastT;

    public MovingCharge(double x, double y, double z) {
        pos = new Matrix(new double[][]{{x, y, z}});
        velocity = new Matrix(new double[][]{{0, 0, 0}});
        lastT = -1;
        finished = false;
    }

    public MovingCharge(Matrix p) {
        pos = p;
        velocity = new Matrix(new double[][]{{0, 0, 0}});
        lastT = -1;
        finished = false;
    }

    public void update(List<FixedPointCharge> fixedCharges) {
        if (lastT == -1) {
            lastT = System.currentTimeMillis();
            return;
        }
        long currT = System.currentTimeMillis();
        double dt = (double) (currT - lastT) / 1000;
        lastT = currT;

        Matrix initPos = pos;
        while (dt > 0 && !finished) {
            Matrix E = new Matrix(new double[][]{{0, 0, 0}});
            for (FixedPointCharge fpc : fixedCharges) {
                Matrix a = Matrix.scale(Matrix.normalize(Matrix.subtract(pos, fpc.getPos())),
                        fpc.getCharge() / squareDis(pos, fpc.getPos()));
                E = Matrix.add(E, a);
            }
            double ddt = Math.min(dt, MAX_STEP_DIS / (E.length() * SPEED));
            Matrix newPos = Matrix.add(pos, Matrix.scale(E, SPEED * ddt));
            Matrix extrapPos = Matrix.add(pos, Matrix.scale(E, SPEED * dt));

            for (FixedPointCharge fpc : fixedCharges)
                if (getDis(fpc.getPos(), new LineSeg(pos, extrapPos)) < MIN_DIS)
                    finished = true;

            pos = newPos;
            dt -= ddt;
        }
    }

    public Matrix getPos() {
        return pos.clone();
    }

    public double getDisTo(Matrix m) {
        return Matrix.subtract(m, pos).length() - MovingCharge.RADIUS;
    }

    public boolean isFinished() {
        return finished;
    }

}