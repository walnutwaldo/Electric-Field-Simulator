package main;

import UI.Header;
import UI.MouseUI;
import UI.Slider;
import UI.UIComponent;
import objects.MovingCharge;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UIManager {

    private static List<UIComponent> uiComponents;
    public static Header brightnessHeader;
    public static Header mouseWheelSensitivityHeader;
    public static Header xSensitivityHeader;
    public static Header ySensitivityHeader;
    public static Header movingChargeHeader;
    public static Header speedHeader;
    public static Slider brightnessSlider;
    public static Slider mouseWheelSensitivitySlider;
    public static Slider xSensitivitySlider;
    public static Slider ySensitivitySlider;
    public static Slider movingChargeSlider;
    public static Slider speedSlider;

    public static void init() {
        uiComponents = new ArrayList<UIComponent>();
        brightnessHeader = new Header("Brightness", new Font("Havana", Font.BOLD, 12), Color.WHITE);
        uiComponents.add(brightnessHeader);
        brightnessSlider = new Slider(Painter.MIN_BRIGHTNESS, Painter.MAX_BRIGHTNESS, Slider.LINEAR);
        uiComponents.add(brightnessSlider);

        mouseWheelSensitivityHeader = new Header("Mouse Wheel Sensitivity", new Font("Havana", Font.BOLD, 12), Color.WHITE);
        uiComponents.add(mouseWheelSensitivityHeader);
        mouseWheelSensitivitySlider = new Slider(MouseUI.MIN_WHEEL_SENSITIVITY, MouseUI.MAX_WHEEL_SENSITIVITY, Slider.LOGARITHMIC);
        uiComponents.add(mouseWheelSensitivitySlider);

        xSensitivityHeader = new Header("X Sensitivity", new Font("Havana", Font.BOLD, 12), Color.WHITE);
        uiComponents.add(xSensitivityHeader);
        xSensitivitySlider = new Slider(MouseUI.MIN_X_SENSITIVITY, MouseUI.MAX_X_SENSITIVITY, Slider.LOGARITHMIC);
        uiComponents.add(xSensitivitySlider);

        ySensitivityHeader = new Header("Y Sensitivity", new Font("Havana", Font.BOLD, 12), Color.WHITE);
        uiComponents.add(ySensitivityHeader);
        ySensitivitySlider = new Slider(MouseUI.MIN_Y_SENSITIVITY, MouseUI.MAX_Y_SENSITIVITY, Slider.LOGARITHMIC);
        uiComponents.add(ySensitivitySlider);

        movingChargeHeader = new Header("Number of Moving Charges", new Font("Havana", Font.BOLD, 12), Color.WHITE);
        uiComponents.add(movingChargeHeader);
        movingChargeSlider = new Slider(SimulationManager.MIN_MOVING_CHARGES, SimulationManager.MAX_MOVING_CHARGES, Slider.LOGARITHMIC);
        uiComponents.add(movingChargeSlider);

        speedHeader = new Header("Speed", new Font("Havana", Font.BOLD, 12), Color.WHITE);
        uiComponents.add(speedHeader);
        speedSlider = new Slider(MovingCharge.MIN_SPEED, MovingCharge.MAX_SPEED, Slider.LINEAR);
        uiComponents.add(speedSlider);
    }

    public static List<UIComponent> getUIComponents() {
        return uiComponents;
    }
}
