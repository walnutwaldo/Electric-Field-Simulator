package main;

import UI.*;
import javafx.geometry.Side;
import objects.MovingCharge;

import java.awt.*;
import java.net.SocketImpl;
import java.util.ArrayList;
import java.util.List;

public class UIManager {

    private static final Font H1 = new Font("Havana", Font.BOLD, 15);
    private static final Font H2 = new Font("Havana", Font.PLAIN, 12);

    private static List<UIComponent> editUI;
    private static List<UIComponent> settingsUI;

    public static Header uiHeader;
    public static Header displayHeader;
    public static Header simulationHeader;

    public static Header brightnessHeader;
    public static Header mouseWheelSensitivityHeader;
    public static Header xSensitivityHeader;
    public static Header ySensitivityHeader;
    public static Header movingChargeHeader;
    public static Header speedHeader;
    public static Header gridSizeHeader;

    public static Slider brightnessSlider;
    public static Slider mouseWheelSensitivitySlider;
    public static Slider xSensitivitySlider;
    public static Slider ySensitivitySlider;
    public static Slider movingChargeSlider;
    public static Slider speedSlider;
    public static Slider gridSizeSlider;

    private static void generateHeaders() {
        uiHeader = new Header("User Interface", H1, Color.WHITE, Header.CENTER);
        displayHeader = new Header("Display", H1, Color.WHITE, Header.CENTER);
        simulationHeader = new Header("Simulation", H1, Color.WHITE, Header.CENTER);

        brightnessHeader = new Header("Brightness", H2, Color.WHITE);
        mouseWheelSensitivityHeader = new Header("Mouse Wheel Sensitivity", H2, Color.WHITE);
        xSensitivityHeader = new Header("X Sensitivity", H2, Color.WHITE);
        ySensitivityHeader = new Header("Y Sensitivity", H2, Color.WHITE);
        movingChargeHeader = new Header("Number of Moving Charges", H2, Color.WHITE);
        speedHeader = new Header("Speed", H2, Color.WHITE);
        gridSizeHeader = new Header("Grid Size", H2, Color.WHITE);
    }

    private static void generateSliders() {
        brightnessSlider = new Slider(Painter.MIN_BRIGHTNESS, Painter.MAX_BRIGHTNESS, Slider.LINEAR);
        mouseWheelSensitivitySlider = new Slider(MouseUI.MIN_WHEEL_SENSITIVITY, MouseUI.MAX_WHEEL_SENSITIVITY, Slider.LOGARITHMIC);
        xSensitivitySlider = new Slider(MouseUI.MIN_X_SENSITIVITY, MouseUI.MAX_X_SENSITIVITY, Slider.LOGARITHMIC);
        ySensitivitySlider = new Slider(MouseUI.MIN_Y_SENSITIVITY, MouseUI.MAX_Y_SENSITIVITY, Slider.LOGARITHMIC);
        movingChargeSlider = new Slider(SimulationManager.MIN_MOVING_CHARGES, SimulationManager.MAX_MOVING_CHARGES, Slider.LOGARITHMIC);
        speedSlider = new Slider(MovingCharge.MIN_SPEED, MovingCharge.MAX_SPEED, Slider.LINEAR);
        gridSizeSlider = new Slider(SimulationManager.MIN_GRID_SIZE, SimulationManager.MAX_GRID_SIZE, Slider.LINEAR);
    }

    private static void generateComponents() {
        generateHeaders();
        generateSliders();
    }

    private static void orderComponents() {
        settingsUI.add(uiHeader);
        settingsUI.add(xSensitivityHeader);
        settingsUI.add(xSensitivitySlider);
        settingsUI.add(ySensitivityHeader);
        settingsUI.add(ySensitivitySlider);
        settingsUI.add(mouseWheelSensitivityHeader);
        settingsUI.add(mouseWheelSensitivitySlider);

        settingsUI.add(displayHeader);
        settingsUI.add(brightnessHeader);
        settingsUI.add(brightnessSlider);
        settingsUI.add(gridSizeHeader);
        settingsUI.add(gridSizeSlider);

        settingsUI.add(simulationHeader);
        settingsUI.add(movingChargeHeader);
        settingsUI.add(movingChargeSlider);
        settingsUI.add(speedHeader);
        settingsUI.add(speedSlider);
    }

    public static void init() {
        editUI = new ArrayList<UIComponent>();
        settingsUI = new ArrayList<UIComponent>();
        generateComponents();
        orderComponents();
    }

    public static List<UIComponent> getUIComponents() {
        if (SideBar.currentOption == 1) return settingsUI;
        else return editUI;

    }
}
