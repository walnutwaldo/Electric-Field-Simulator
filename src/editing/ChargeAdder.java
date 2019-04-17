package editing;

import main.SimulationManager;
import objects.FixedPointCharge;

public class ChargeAdder {

    public static void addNewCharge() {
        SimulationManager.addCharge(new FixedPointCharge(0, 0, 0, 5));
    }

}
