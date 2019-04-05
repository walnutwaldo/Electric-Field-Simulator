package UI;

import main.WindowManager;
import objects.Camera;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MouseUI implements MouseMotionListener, MouseListener, MouseWheelListener {

    private boolean mouseDown;
    private int lastX, lastY;

    private static final double X_SENSITIVITY = 0.01;
    private static final double Y_SENSITIVITY = 0.01;
    private static final double WHEEL_SENSITIVITY = 0.4;

    public boolean onTab;

    private boolean onSlider;
    private Slider currentSlider;
    private double initSliderLoc;
    private int pressX;

    private double clamp(double a, double b, double c) {
        return Math.max(b, Math.min(a, c));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int newX = e.getX();
        int newY = e.getY();
        int screenWidth = WindowManager.painter.getWidth();
        int screenHeight = WindowManager.painter.getHeight();
        if (onSlider) {
            double dx = e.getX() - pressX;
            currentSlider.sliderLoc = clamp(initSliderLoc + (dx) / currentSlider.width, 0, 1);
        }
        if (mouseDown) {
            int dx = newX - lastX;
            int dy = newY - lastY;
            lastX = e.getX();
            lastY = e.getY();
            Camera.increaseTilt(Y_SENSITIVITY * dy);
            Camera.increaseTheta(X_SENSITIVITY * dx);
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
        if (newX >= screenWidth - 5 || onTab) {
            SideBar.showTab();
        } else if (newX <= screenWidth - SideBar.TAB_RADIUS) SideBar.hideTab();
        if (onTab) WindowManager.painter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
            for (UIComponent uic : SideBar.getUIComponents()) {
                if (uic instanceof Slider && ((Slider) uic).onSlider(xPos, yPos)) {
                    onSlider = true;
                    currentSlider = (Slider) uic;
                    initSliderLoc = currentSlider.sliderLoc;
                    break;
                }
                yPos -= uic.topMargin + uic.height;
            }
        } else if (Math.pow(e.getX() - screenWidth, 2) + Math.pow(e.getY() - screenHeight / 2, 2) <= Math.pow(SideBar.TAB_RADIUS, 2)) {
            SideBar.toggle();
        } else {
            mouseDown = true;
        }
        pressX = e.getX();
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

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int screenWidth = WindowManager.painter.getWidth();
        if (e.getX() >= screenWidth) {

        } else {
            Camera.increaseDis(WHEEL_SENSITIVITY * e.getPreciseWheelRotation());
        }
    }
}