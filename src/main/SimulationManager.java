package main;

import math.Matrix;
import objects.FixedPointCharge;
import objects.MovingCharge;

import static math.LinAlg.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class SimulationManager {

    public static final double MIN_MOVING_CHARGE_PROXIMITY = 0.001;
    public static final int MAX_MOVING_CHARGES = 250;
    public static final int GRID_SIZE = 20;

    private static List<FixedPointCharge> fixedCharges;
    private static TreeSet<MovingCharge> movingCharges;

    public static void init() {
        fixedCharges = new ArrayList<FixedPointCharge>();
        fixedCharges.add(new FixedPointCharge(10, 10, 0, 5));
        fixedCharges.add(new FixedPointCharge(-10, 10, 0, -5));
        fixedCharges.add(new FixedPointCharge(-10, -10, 0, 5));
        fixedCharges.add(new FixedPointCharge(10, -10, 0, -5));

        movingCharges = new TreeSet<MovingCharge>(new Comparator<MovingCharge>() {
            public int compare(MovingCharge o1, MovingCharge o2) {
                int diff = (int) Math.signum(getDis(WindowManager.camera.getPos(), o1.getPos()) - getDis(WindowManager.camera.getPos(), o2.getPos()));
                if (diff != 0) return diff;
                for (int i = 0; i < 3; i++)
                    if (o1.getPos().get(0, i) != o2.getPos().get(0, i))
                        return (int) Math.signum(o1.getPos().get(0, i) - o2.getPos().get(0, i));
                return 0;
            }
        });
    }

    public static List<FixedPointCharge> getFixedCharges() {
        return fixedCharges;
    }

    public static TreeSet<MovingCharge> getMovingCharges() {
        return movingCharges;
    }

    public static void update() {
        List<MovingCharge> newList = new ArrayList<MovingCharge>();
        for (MovingCharge mc : movingCharges) {
            mc.update(fixedCharges);
            if (!mc.isFinished()) newList.add(mc);
        }
        movingCharges.clear();
        for (MovingCharge mc : newList) {
            boolean add = true;
            double disFromCamera = getDis(WindowManager.camera.getPos(), mc.getPos());
            Matrix lo = Matrix.add(WindowManager.camera.getPos(), new Matrix(new double[][]{{Math.max(0, disFromCamera - MIN_MOVING_CHARGE_PROXIMITY), 0, 0}}));
            Matrix hi = Matrix.add(WindowManager.camera.getPos(), new Matrix(new double[][]{{disFromCamera + MIN_MOVING_CHARGE_PROXIMITY, 0, 0}}));
            for (MovingCharge mc2 : movingCharges.subSet(new MovingCharge(lo), true, new MovingCharge(hi), true))
                if (getDis(mc.getPos(), mc2.getPos()) < MIN_MOVING_CHARGE_PROXIMITY) add = false;
            for (int dim = 0; dim < 3; dim++) if (Math.abs(mc.getPos().get(0, dim)) > GRID_SIZE) add = false;
            if (add) movingCharges.add(mc);
        }

        while (movingCharges.size() < MAX_MOVING_CHARGES) {
            double x = (Math.random() - 0.5) * 2 * GRID_SIZE;
            double y = (Math.random() - 0.5) * 2 * GRID_SIZE;
            double z = (Math.random() - 0.5) * 2 * GRID_SIZE;
            movingCharges.add(new MovingCharge(x, y, z));
        }
    }

}