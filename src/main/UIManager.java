package main;

import UI.Header;
import UI.MouseUI;
import UI.Slider;
import UI.UIComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UIManager {

    private static List<UIComponent> uiComponents;
    public static Header brightnessHeader;
    public static Header sensitivityHeader;
    public static Header mouseWheelSensitivityHeader;
    public static Header xSensitivityHeader;
    public static Header ySensitivityHeader;
    public static Slider brightnessSlider;
    public static Slider mouseWheelSensitivitySlider;
    public static Slider xSensitivitySlider;
    public static Slider ySensitivitySlider;

    public static void init() {
        uiComponents = new ArrayList<UIComponent>();
        brightnessHeader = new Header("BRIGHTNESS", new Font("Havana", Font.BOLD, 20), Color.WHITE);
        uiComponents.add(brightnessHeader);
        brightnessSlider = new Slider(Painter.MIN_BRIGHTNESS, Painter.MAX_BRIGHTNESS, Slider.LINEAR);
        uiComponents.add(brightnessSlider);

        sensitivityHeader = new Header("SENSITIVITY", new Font("Havana", Font.BOLD, 20), Color.WHITE);
        uiComponents.add(sensitivityHeader);

        mouseWheelSensitivityHeader = new Header("Mouse Wheel", new Font("Havana", Font.BOLD, 15), Color.WHITE);
        uiComponents.add(mouseWheelSensitivityHeader);
        mouseWheelSensitivitySlider = new Slider(MouseUI.MIN_WHEEL_SENSITIVITY, MouseUI.MAX_WHEEL_SENSITIVITY, Slider.LOGARITHMIC);
        uiComponents.add(mouseWheelSensitivitySlider);

        xSensitivityHeader = new Header("X", new Font("Havana", Font.BOLD, 15), Color.WHITE);
        uiComponents.add(xSensitivityHeader);
        xSensitivitySlider = new Slider(MouseUI.MIN_X_SENSITIVITY, MouseUI.MAX_X_SENSITIVITY, Slider.LOGARITHMIC);
        uiComponents.add(xSensitivitySlider);

        ySensitivityHeader = new Header("Y", new Font("Havana", Font.BOLD, 15), Color.WHITE);
        uiComponents.add(ySensitivityHeader);
        ySensitivitySlider = new Slider(MouseUI.MIN_Y_SENSITIVITY, MouseUI.MAX_Y_SENSITIVITY, Slider.LOGARITHMIC);
        uiComponents.add(ySensitivitySlider);
    }

    public static List<UIComponent> getUIComponents() {
        return uiComponents;
    }
}
