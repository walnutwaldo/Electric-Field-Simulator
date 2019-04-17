package UI;

import editing.ChargeSelector;
import main.UIManager;
import main.WindowManager;
import objects.Camera;

import java.awt.*;
import java.awt.event.*;

public class MouseUI implements MouseMotionListener, MouseListener, MouseWheelListener {

    private int lastX, lastY;

    public static final double MIN_X_SENSITIVITY = 0.0005;
    public static final double MAX_X_SENSITIVITY = 0.2;
    public static final double MIN_Y_SENSITIVITY = 0.0005;
    public static final double MAX_Y_SENSITIVITY = 0.2;
    public static final double MIN_WHEEL_SENSITIVITY = 0.05;
    public static final double MAX_WHEEL_SENSITIVITY = 10;

    private static final int TAB_ACTIVATION_DIS = 5;

    public boolean downSimulation;
    public boolean downTab;
    public boolean downSlider;
    public boolean downButton;
    public boolean downOption;

    public boolean onSimulation;
    public boolean onTab;
    public boolean onSlider;
    public boolean onButton;
    public boolean onOption;

    public int currOption;
    public Slider currentSlider;
    public Button currentButton;

    private double clamp(double a, double b, double c) {
        return Math.max(b, Math.min(a, c));
    }

    public boolean down() {
        return downOption || downTab || downSimulation || downSlider || downButton;
    }

    private void updateTab(MouseEvent e) {
        if (downSimulation) {
            SideBar.hideTab();
            return;
        }
        int newX = e.getX();
        int newY = e.getY();
        int screenWidth = WindowManager.painter.getWidth();
        int screenHeight = WindowManager.painter.getHeight();
        boolean onTabZone = Math.pow(newX - screenWidth, 2) + Math.pow(newY - screenHeight / 2, 2) <= Math.pow(SideBar.TAB_RADIUS, 2);
        if (newX >= screenWidth - TAB_ACTIVATION_DIS || newX <= TAB_ACTIVATION_DIS || newY <= TAB_ACTIVATION_DIS || newY >= screenHeight - TAB_ACTIVATION_DIS || onTabZone) {
            SideBar.showTab();
        } else if (newX <= screenWidth - SideBar.TAB_RADIUS) SideBar.hideTab();
    }

    private void processUIC(UIComponent uic, int xPos, int yPos) {
        if (uic instanceof Slider && (((Slider) uic).onSlider(xPos, yPos) || ((Slider) uic).onBar(xPos, yPos))) {
            if (!down()) currentSlider = (Slider) uic;
            if (uic == currentSlider && (!down() || downSlider)) onSlider = true;
        }
        if (uic instanceof Button && ((Button) uic).isOn(xPos, yPos)) {
            if (!down()) currentButton = (Button) uic;
            if (uic == currentButton && (!down() || downButton)) onButton = true;
        }
        if (uic instanceof HorizontalLayout) for (UIComponent uic2 : ((HorizontalLayout) uic).getComponents()) {
            processUIC(uic2, xPos, yPos);
            xPos -= uic2.leftMargin + uic2.width;
        }
    }

    private void updateOns(MouseEvent e) {
        int newX = e.getX();
        int newY = e.getY();
        int screenWidth = WindowManager.painter.getWidth();
        int screenHeight = WindowManager.painter.getHeight();
        if (!down() || downTab)
            onTab = newX < screenWidth && Math.pow(newX - screenWidth - SideBar.TAB_RADIUS + SideBar.getTabProtrusion(), 2) + Math.pow(newY - screenHeight / 2, 2) <= Math.pow(SideBar.TAB_RADIUS, 2);
        if (!down() || downSimulation)
            onSimulation = newX < screenWidth && !onTab;
        onOption = false;
        onSlider = false;
        onButton = false;
        if (newX >= screenWidth && newY <= SideBar.OPTIONS_HEIGHT) {
            int newOption = (newX - screenWidth) / SideBar.OPTIONS_HEIGHT;
            if (!down()) currOption = newOption;
            if (currOption < SideBar.NUM_OPTIONS && currOption == newOption && (!down() || downOption) && currOption != SideBar.currentOption)
                onOption = true;
        } else if (newX >= screenWidth && newY > SideBar.OPTIONS_HEIGHT) {
            int xPos = newX;
            int yPos = newY - SideBar.OPTIONS_HEIGHT;
            xPos -= screenWidth;
            for (UIComponent uic : UIManager.getUIComponents()) {
                processUIC(uic, xPos, yPos);
                yPos -= uic.topMargin + uic.height;
            }
        }
        if (onTab || onSlider || onOption || onButton || ChargeSelector.on) {
            WindowManager.painter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else WindowManager.painter.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        ChargeSelector.moveUpdate(e);
        if (ChargeSelector.down) return;
        int newX = e.getX();
        int newY = e.getY();
        int screenWidth = WindowManager.painter.getWidth();
        updateOns(e);
        updateTab(e);
        if (downSlider) {
            currentSlider.setSlider(
                    clamp((double) (e.getX() - screenWidth - currentSlider.LEFT_MARGIN) / currentSlider.WIDTH,
                            0, 1));
        }
        if (downSimulation) {
            int dx = newX - lastX;
            int dy = newY - lastY;
            lastX = e.getX();
            lastY = e.getY();
            Camera.increaseTilt(UIManager.ySensitivitySlider.getVal() * dy);
            Camera.increaseTheta(UIManager.xSensitivitySlider.getVal() * dx);
        }
        lastX = newX;
        lastY = newY;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        ChargeSelector.moveUpdate(e);
        int newX = e.getX();
        int newY = e.getY();
        updateOns(e);
        updateTab(e);
        lastX = newX;
        lastY = newY;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        ChargeSelector.clickUpdate(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        updateOns(e);
        int screenWidth = WindowManager.painter.getWidth();
        downOption = onOption;
        downSlider = onSlider;
        downButton = onButton;
        downTab = onTab;
        downSimulation = onSimulation;
        if (downSlider)
            currentSlider.setSlider(
                    clamp((double) (e.getX() - screenWidth - currentSlider.LEFT_MARGIN) / currentSlider.WIDTH,
                            0, 1));
        ChargeSelector.pressUpdate(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        updateOns(e);
        downSimulation = false;
        downSlider = false;
        if (downOption) {
            if (onOption) SideBar.currentOption = currOption;
            downOption = false;
            onOption = false;
        }
        if (downTab) {
            if (onTab) SideBar.toggle();
            downTab = false;
            onTab = false;
        }
        if (downButton) {
            if (onButton) currentButton.press();
            downButton = false;
        }
        updateOns(e);
        updateTab(e);
        ChargeSelector.releaseUpdate(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        updateTab(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        SideBar.showTab();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int screenWidth = WindowManager.painter.getWidth();
        if (e.getX() >= screenWidth) {

        } else {
            Camera.increaseDis(UIManager.mouseWheelSensitivitySlider.getVal() * e.getPreciseWheelRotation());
        }
    }
}