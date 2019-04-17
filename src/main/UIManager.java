package main;

import UI.Button;
import UI.Checkbox;
import UI.*;
import editing.ChargeAdder;
import editing.ChargeSelector;
import objects.FixedPointCharge;
import objects.MovingCharge;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UIManager {

    private static final Font H1 = new Font("Havana", Font.BOLD, 15);
    private static final Font H2 = new Font("Havana", Font.PLAIN, 12);
    private static final Font INFO_FONT = new Font("Courier New", Font.PLAIN, 12);

    public static Header uiHeader;
    public static Header displayHeader;
    public static Header simulationHeader;

    public static Header brightnessHeader;
    public static Header mouseWheelSensitivityHeader;
    public static Header xSensitivityHeader;
    public static Header ySensitivityHeader;
    public static Header movingChargeHeader;
    public static Header speedHeader;
    public static Header chargeHeader;
    public static Header xPosHeader;
    public static Header yPosHeader;
    public static Header zPosHeader;

    public static Slider brightnessSlider;
    public static Slider mouseWheelSensitivitySlider;
    public static Slider xSensitivitySlider;
    public static Slider ySensitivitySlider;
    public static Slider movingChargeSlider;
    public static Slider speedSlider;
    public static Slider gridSizeSlider;
    public static Slider chargeSlider;
    public static Slider xPosSlider;
    public static Slider yPosSlider;
    public static Slider zPosSlider;

    public static Checkbox gridCheckbox;
    public static Checkbox boxCheckbox;
    public static Button editButton;
    public static Button newChargeButton;
    public static Button deleteChargeButton;
    private static HorizontalLayout checkboxesLayout;

    private static List<UIComponent> settingsUI;

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

        chargeHeader = new Header("Charge: ", H2, Color.WHITE);
        xPosHeader = new Header("X: ", H2, Color.WHITE);
        yPosHeader = new Header("Y: ", H2, Color.WHITE);
        zPosHeader = new Header("Z: ", H2, Color.WHITE);
    }

    private static void generateSliders() {
        brightnessSlider = new Slider(Painter.MIN_BRIGHTNESS, Painter.MAX_BRIGHTNESS, Slider.LINEAR);
        mouseWheelSensitivitySlider = new Slider(MouseUI.MIN_WHEEL_SENSITIVITY, MouseUI.MAX_WHEEL_SENSITIVITY, Slider.LOGARITHMIC);
        xSensitivitySlider = new Slider(MouseUI.MIN_X_SENSITIVITY, MouseUI.MAX_X_SENSITIVITY, Slider.LOGARITHMIC);
        ySensitivitySlider = new Slider(MouseUI.MIN_Y_SENSITIVITY, MouseUI.MAX_Y_SENSITIVITY, Slider.LOGARITHMIC);
        movingChargeSlider = new Slider(SimulationManager.MIN_MOVING_CHARGES, SimulationManager.MAX_MOVING_CHARGES, Slider.LOGARITHMIC);
        speedSlider = new Slider(MovingCharge.MIN_SPEED, MovingCharge.MAX_SPEED, Slider.LINEAR);
        gridSizeSlider = new Slider(SimulationManager.MIN_GRID_SIZE, SimulationManager.MAX_GRID_SIZE, Slider.LINEAR);
        chargeSlider = new Slider(FixedPointCharge.MIN_CHARGE, FixedPointCharge.MAX_CHARGE, Slider.LINEAR, new Runnable() {
            @Override
            public void run() {
                ChargeSelector.selectedCharge.setCharge(UIManager.chargeSlider.getVal());
                UIManager.chargeHeader.text = "Charge: " + Double.toString(Math.round(ChargeSelector.selectedCharge.getCharge() * 10) / 10.0);
            }
        });
        xPosSlider = new Slider(-SimulationManager.MAX_GRID_SIZE, SimulationManager.MAX_GRID_SIZE, Slider.LINEAR, new Runnable() {
            @Override
            public void run() {
                ChargeSelector.selectedCharge.setX(UIManager.xPosSlider.getVal());
                UIManager.xPosHeader.text = "X: " + Double.toString(Math.round(ChargeSelector.selectedCharge.getX() * 10) / 10.0);
            }
        });
        yPosSlider = new Slider(-SimulationManager.MAX_GRID_SIZE, SimulationManager.MAX_GRID_SIZE, Slider.LINEAR, new Runnable() {
            @Override
            public void run() {
                ChargeSelector.selectedCharge.setY(UIManager.yPosSlider.getVal());
                UIManager.yPosHeader.text = "Y: " + Double.toString(Math.round(ChargeSelector.selectedCharge.getY() * 10) / 10.0);
            }
        });
        zPosSlider = new Slider(-SimulationManager.MAX_GRID_SIZE, SimulationManager.MAX_GRID_SIZE, Slider.LINEAR, new Runnable() {
            @Override
            public void run() {
                ChargeSelector.selectedCharge.setZ(UIManager.zPosSlider.getVal());
                UIManager.zPosHeader.text = "Z: " + Double.toString(Math.round(ChargeSelector.selectedCharge.getZ() * 10) / 10.0);
            }
        });
    }

    private static void generateButtons() {
        gridCheckbox = new Checkbox("Grid", H2, true);
        boxCheckbox = new Checkbox("Box", H2, true);
        checkboxesLayout = new HorizontalLayout();
        checkboxesLayout.add(gridCheckbox);
        checkboxesLayout.add(boxCheckbox);

        editButton = new Button("EDIT", H2, new Runnable() {
            @Override
            public void run() {
                ChargeSelector.toggle();
            }
        });
        newChargeButton = new Button("ADD NEW CHARGE", H2, new Runnable() {
            @Override
            public void run() {
                ChargeAdder.addNewCharge();
            }
        });
        deleteChargeButton = new Button("DELETE", H2, new Runnable() {
            @Override
            public void run() {
                SimulationManager.remove(ChargeSelector.selectedCharge);
                ChargeSelector.selectedCharge = null;
            }
        });
    }

    private static void generateComponents() {
        generateHeaders();
        generateSliders();
        generateButtons();
    }

    private static List<UIComponent> createSettingsUI() {
        if (settingsUI == null) {
            settingsUI = new ArrayList<UIComponent>(20);
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
            settingsUI.add(checkboxesLayout);
            settingsUI.add(gridSizeSlider);

            settingsUI.add(simulationHeader);
            settingsUI.add(movingChargeHeader);
            settingsUI.add(movingChargeSlider);
            settingsUI.add(speedHeader);
            settingsUI.add(speedSlider);
        }
        return settingsUI;
    }

    private static List<UIComponent> createEditUI() {
        List<UIComponent> editUI = new ArrayList<UIComponent>();
        editUI.add(editButton);
        if (ChargeSelector.editing) {
            editUI.add(newChargeButton);
            if (ChargeSelector.selectedCharge != null) {
                editUI.add(chargeHeader);
                editUI.add(chargeSlider);
                editUI.add(xPosHeader);
                editUI.add(xPosSlider);
                editUI.add(yPosHeader);
                editUI.add(yPosSlider);
                editUI.add(zPosHeader);
                editUI.add(zPosSlider);
                editUI.add(deleteChargeButton);
            }
        }
        return editUI;
    }

    private static List<UIComponent> createInfoUI() {
        List<UIComponent> infoUI = new ArrayList<UIComponent>();
        infoUI.add(new Header("Made by Walden Yan", INFO_FONT, Color.WHITE));
        infoUI.add(new Header("Avon High School, CT", INFO_FONT, Color.WHITE));
        infoUI.add(new Header("AP Computer Science Principles", INFO_FONT, Color.WHITE));
        Header h = new Header("Create Project (2019)", INFO_FONT, Color.WHITE);
        h.topMargin = 0;
        infoUI.add(h);
        infoUI.add(new Header("", INFO_FONT, Color.WHITE));
        infoUI.add(new Header("Language: Java", INFO_FONT, Color.WHITE));
        return infoUI;
    }

    public static void init() {
        generateComponents();
    }

    public static List<UIComponent> getUIComponents() {
        if (SideBar.currentOption == SideBar.SETTINGS) return createSettingsUI();
        else if (SideBar.currentOption == SideBar.EDIT) return createEditUI();
        else if (SideBar.currentOption == SideBar.INFO) return createInfoUI();
        return new ArrayList<UIComponent>();
    }
}
