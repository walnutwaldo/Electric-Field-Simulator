package editing;

import main.SimulationManager;
import main.UIManager;
import main.WindowManager;
import math.LinAlg;
import math.Matrix;
import objects.Camera;
import objects.FixedPointCharge;

import java.awt.event.MouseEvent;

public class ChargeSelector {

    public static boolean down;
    public static boolean on;
    public static FixedPointCharge currCharge;
    public static FixedPointCharge selectedCharge;

    public static boolean editing;

    private static LinAlg.Line getRay(MouseEvent e) {
        return new LinAlg.Line(Camera.getTransformedPos(), new Matrix(new double[][]{{
                Math.tan(Camera.FOV / 2) * (e.getX() - WindowManager.painter.getWidth() / 2) / (WindowManager.painter.maxDim / 2),
                1 - Camera.getDis(),
                Math.tan(Camera.FOV / 2) * (WindowManager.painter.getHeight() / 2 - e.getY()) / (WindowManager.painter.maxDim / 2),
        }}));
    }

    public static FixedPointCharge getFPC(MouseEvent e) {
        if (!editing) return null;
        LinAlg.Line l = getRay(e);
        FixedPointCharge curr = null;
        for (FixedPointCharge fpc : SimulationManager.getFixedCharges()) {
            Matrix pos = Matrix.mult(fpc.getPos(), Camera.getTransformationMatrix());
            if (LinAlg.getDis(pos, l) < FixedPointCharge.RADIUS) {
                if (curr == null || LinAlg.getDis(fpc.getPos(), Camera.getPos()) < LinAlg.getDis(curr.getPos(), Camera.getPos()))
                    curr = fpc;
            }
        }
        return curr;
    }

    public static void updateCharge(MouseEvent e) {
        FixedPointCharge fpc = getFPC(e);
        if (!down) {
            currCharge = fpc;
            on = (fpc != null);
        } else
            on = (fpc == currCharge);
    }

    public static void moveUpdate(MouseEvent e) {
        updateCharge(e);
    }

    public static void pressUpdate(MouseEvent e) {
        updateCharge(e);
        if (on) down = true;
    }

    public static void selectCharge(FixedPointCharge newCharge) {
        selectedCharge = newCharge;
        UIManager.chargeSlider.setVal(newCharge.getCharge());
        UIManager.xPosSlider.setVal(newCharge.getX());
        UIManager.yPosSlider.setVal(newCharge.getY());
        UIManager.zPosSlider.setVal(newCharge.getZ());
    }

    public static void releaseUpdate(MouseEvent e) {
        updateCharge(e);
        if (down) {
            down = false;
            if (getFPC(e) == currCharge) {
                if (selectedCharge == currCharge) selectedCharge = null;
                else {
                    selectCharge(currCharge);
                }
            }
        }
    }

    public static void clickUpdate(MouseEvent e) {
        updateCharge(e);
        if (e.getX() < WindowManager.painter.getWidth()) selectedCharge = currCharge;
    }

    public static void toggle() {
        editing = !editing;
        if (!editing) {
            selectedCharge = null;
            on = false;
            down = false;
        }
        if (editing) UIManager.editButton.text = "STOP EDITING";
        else UIManager.editButton.text = "EDIT";
    }
}
