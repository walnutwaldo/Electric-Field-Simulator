package objects;

import main.SimulationManager;
import math.Matrix;

import java.util.List;

import static math.LinAlg.*;

public class MovingCharge implements Positionable {

    public static final double RADIUS = 0.1;
    public static final double MIN_DIS = 0.1;
    public static final double SPEED = 300;

    private Matrix pos;
    private Matrix velocity;

    private boolean finished;

    private long lastT;

    public MovingCharge(MovingCharge mc){
        pos = new Matrix(mc.pos);
        velocity = new Matrix(mc.velocity);
        lastT = mc.lastT;
        finished = mc.finished;
    }

    public MovingCharge(double x, double y, double z) {
        pos = new Matrix(new double[][]{{x, y, z}});
        velocity = new Matrix(new double[][]{{0, 0, 0}});
        lastT = -1;
        finished = false;
    }

    public MovingCharge(Matrix p) {
        pos = new Matrix(p);
        velocity = new Matrix(new double[][]{{0, 0, 0}});
        lastT = -1;
        finished = false;
    }

    public void update() {
        if (lastT == -1) {
            lastT = System.currentTimeMillis();
            return;
        }
        long currT = System.currentTimeMillis();
        double dt = (double) (currT - lastT) / 1000;
        lastT = currT;

        velocity = new Matrix(new double[][]{{0, 0, 0}});
        for (FixedPointCharge fpc : SimulationManager.getFixedCharges()) {
            Matrix a = Matrix.scale(Matrix.normalize(Matrix.subtract(pos, fpc.getPos())),
                    fpc.getCharge() / squareDis(pos, fpc.getPos()));
            velocity = Matrix.add(velocity, a);
        }
        Matrix newPos = Matrix.add(pos, Matrix.scale(velocity, SPEED * dt));

        for (FixedPointCharge fpc : SimulationManager.getFixedCharges())
            if (getDis(fpc.getPos(), new LineSeg(pos, newPos)) < MIN_DIS)
                finished = true;
        for(int dim = 0; dim < 3; dim++) if(Math.abs(newPos.get(0, dim)) > SimulationManager.GRID_SIZE) finished = true;
        pos = newPos;
    }

    public Matrix getPos() {
        double dt = (double) (System.currentTimeMillis() - lastT) / 1000;
        Matrix newP = Matrix.add(pos, Matrix.scale(velocity, SPEED * dt));

        for(int dim = 0; dim < 3; dim++) if(Math.abs(newP.get(0, dim)) > SimulationManager.GRID_SIZE) finished = true;
        if(finished) return pos;
        return newP;
    }

    public double getDisTo(Matrix m) {
        return Matrix.subtract(m, pos).length() - MovingCharge.RADIUS;
    }

    public boolean isFinished() {
        return finished;
    }

}