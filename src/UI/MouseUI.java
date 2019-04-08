package UI;

import main.UIManager;
import main.WindowManager;
import objects.Camera;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MouseUI implements MouseMotionListener, MouseListener, MouseWheelListener {

    private boolean mouseDown;
    private int lastX, lastY;

    public static final double MIN_X_SENSITIVITY = 0.001;
    public static final double MAX_X_SENSITIVITY = 0.1;
    public static final double MIN_Y_SENSITIVITY = 0.001;
    public static final double MAX_Y_SENSITIVITY = 0.1;
    public static final double MIN_WHEEL_SENSITIVITY = 0.05;
    public static final double MAX_WHEEL_SENSITIVITY = 10;

    private static final int TAB_ACTIVATION_DIS = 5;

    public boolean onTab;
    public boolean onSlider;
    public Slider currentSlider;

    private double clamp(double a, double b, double c) {
        return Math.max(b, Math.min(a, c));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int newX = e.getX();
        int newY = e.getY();
        int screenWidth = WindowManager.painter.getWidth();
        if (onSlider)
            currentSlider.sliderLoc =
                    clamp((double) (e.getX() - screenWidth - currentSlider.LEFT_MARGIN) / currentSlider.WIDTH,
                            0, 1);
        if (mouseDown) {
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
        int newX = e.getX();
        int newY = e.getY();
        int screenWidth = WindowManager.painter.getWidth();
        int screenHeight = WindowManager.painter.getHeight();
        onTab = newX < screenWidth && Math.pow(newX - screenWidth, 2) + Math.pow(newY - screenHeight / 2, 2) <= Math.pow(SideBar.TAB_RADIUS, 2);
        if (e.getX() >= screenWidth) {
            int xPos = e.getX();
            int yPos = e.getY();
            xPos -= screenWidth;
            onSlider = false;
            for (UIComponent uic : UIManager.getUIComponents()) {
                if (uic instanceof Slider && (((Slider) uic).onSlider(xPos, yPos) || ((Slider) uic).onBar(xPos, yPos))) {
                    onSlider = true;
                    currentSlider = (Slider) uic;
                    break;
                }
                yPos -= uic.topMargin + uic.height;
            }
        }
        if (newX >= screenWidth - TAB_ACTIVATION_DIS || newX <= TAB_ACTIVATION_DIS || newY <= TAB_ACTIVATION_DIS || newY >= screenHeight - TAB_ACTIVATION_DIS || onTab) {
            SideBar.showTab();
        } else if (newX <= screenWidth - SideBar.TAB_RADIUS) SideBar.hideTab();
        if (onTab || onSlider) WindowManager.painter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        else WindowManager.painter.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        lastX = newX;
        lastY = newY;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int screenWidth = WindowManager.painter.getWidth();
        int screenHeight = WindowManager.painter.getHeight();
        if (e.getX() >= screenWidth) {
            int xPos = e.getX();
            int yPos = e.getY();
            xPos -= screenWidth;
            for (UIComponent uic : UIManager.getUIComponents()) {
                if (uic instanceof Slider && (((Slider) uic).onBar(xPos, yPos) || ((Slider) uic).onSlider(xPos, yPos))) {
                    onSlider = true;
                    currentSlider = (Slider) uic;
                    currentSlider.sliderLoc =
                            clamp((double) (e.getX() - screenWidth - currentSlider.LEFT_MARGIN) / currentSlider.WIDTH,
                                    0, 1);
                    break;
                }
                yPos -= uic.topMargin + uic.height;
            }
        } else if (Math.pow(e.getX() - screenWidth, 2) + Math.pow(e.getY() - screenHeight / 2, 2) <= Math.pow(SideBar.TAB_RADIUS, 2)) {
            SideBar.toggle();
        } else {
            mouseDown = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
        onSlider = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

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