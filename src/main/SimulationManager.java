package main;

import objects.FixedPointCharge;
import objects.MovingCharge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimulationManager {

    public static final int MIN_MOVING_CHARGES = 10;
    public static final int MAX_MOVING_CHARGES = 20000;
    public static final double MIN_GRID_SIZE = 10;
    public static final double MAX_GRID_SIZE = 30;

    private static List<FixedPointCharge> fixedCharges;
    private static List<MovingCharge> movingCharges;

    private static boolean lockedMovingCharges;

    public static void init() {
        fixedCharges = new ArrayList<FixedPointCharge>();
        fixedCharges.add(new FixedPointCharge(10, 10, 5, 5));
        fixedCharges.add(new FixedPointCharge(-10, 10, -5, -5));
        fixedCharges.add(new FixedPointCharge(-10, -10, 5, 5));
        fixedCharges.add(new FixedPointCharge(10, -10, -5, -5));

        movingCharges = new ArrayList<MovingCharge>();
    }

    public static List<FixedPointCharge> getFixedCharges() {
        return new ArrayList<FixedPointCharge>(fixedCharges);
    }

    public static List<MovingCharge> getMovingCharges() {
        while (lockedMovingCharges) {
        }
        lockedMovingCharges = true;
        List<MovingCharge> ret = new ArrayList<MovingCharge>(movingCharges);
        lockedMovingCharges = false;
        return ret;
    }

    public static void update() {
        List<MovingCharge> newList = new ArrayList<MovingCharge>();
        for (MovingCharge mc : movingCharges) {
            MovingCharge mc2 = new MovingCharge(mc);
            mc2.update();
            if (!mc2.isFinished()) newList.add(mc2);
        }

        while (newList.size() < UIManager.movingChargeSlider.getVal()) {
            double x = (Math.random() - 0.5) * 2 * UIManager.gridSizeSlider.getVal();
            double y = (Math.random() - 0.5) * 2 * UIManager.gridSizeSlider.getVal();
            double z = (Math.random() - 0.5) * 2 * UIManager.gridSizeSlider.getVal();
            newList.add(new MovingCharge(x, y, z));
        }

        Collections.shuffle(newList);
        List<MovingCharge> list2 = new ArrayList<MovingCharge>();
        for (int i = 0; i < Math.min(newList.size(), UIManager.movingChargeSlider.getVal()); i++)
            list2.add(newList.get(i));

        while (lockedMovingCharges) {
        }
        lockedMovingCharges = true;
        movingCharges = list2;
        lockedMovingCharges = false;
    }

    public static void addCharge(FixedPointCharge fixedPointCharge) {
        fixedCharges.add(fixedPointCharge);
    }
}