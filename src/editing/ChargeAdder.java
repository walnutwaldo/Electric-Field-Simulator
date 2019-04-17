package editing;

import main.SimulationManager;
import objects.FixedPointCharge;

public class ChargeAdder {

    public static void addNewCharge() {
        FixedPointCharge newCharge = new FixedPointCharge(0, 0, 0, 5);
        SimulationManager.addCharge(newCharge);
        ChargeSelector.selectCharge(newCharge);
    }

}
