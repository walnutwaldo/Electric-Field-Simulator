package UI;

import main.SimulationManager;
import main.WindowManager;
import math.LinAlg;
import math.Matrix;
import objects.Camera;
import objects.FixedPointCharge;

import java.awt.event.MouseEvent;

public class ChargeSelector {

    private static LinAlg.Line getRay(MouseEvent e) {
        return new LinAlg.Line(Camera.getTransformedPos(), new Matrix(new double[][]{{
                Math.tan(Camera.FOV / 2) * (e.getX() - WindowManager.painter.getWidth() / 2) / (WindowManager.painter.maxDim / 2),
                1 - Camera.getDis(),
                Math.tan(Camera.FOV / 2) * (WindowManager.painter.getHeight() / 2 - e.getY()) / (WindowManager.painter.maxDim / 2),
        }}));
    }

    public static FixedPointCharge getFPC(MouseEvent e) {
        LinAlg.Line l = getRay(e);
        for (FixedPointCharge fpc : SimulationManager.getFixedCharges()) {
            Matrix pos = Matrix.mult(fpc.getPos(), Camera.getTransformationMatrix());
            if (LinAlg.getDis(pos, l) < FixedPointCharge.RADIUS) return fpc;
        }
        return null;
    }

}
