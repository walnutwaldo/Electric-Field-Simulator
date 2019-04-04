import objects.Camera;

import java.awt.event.*;

public class MouseUI implements MouseMotionListener, MouseListener, MouseWheelListener {

    private boolean mouseDown;
    private int lastX, lastY;

    private static final double X_SENSITIVITY = 0.01;
    private static final double Y_SENSITIVITY = 0.01;
    private static final double WHEEL_SENSITIVITY = 0.4;

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!mouseDown) return;
        int dx = e.getX() - lastX;
        int dy = e.getY() - lastY;
        lastX = e.getX();
        lastY = e.getY();
        Camera.increaseTilt(Y_SENSITIVITY * dy);
        Camera.increaseTheta(X_SENSITIVITY * dx);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
        int screenWidth = WindowManager.painter.getWidth();
        int screenHeight = WindowManager.painter.getHeight();
        if (lastX >= screenWidth - 5
                || Math.pow(lastX - screenWidth, 2) + Math.pow(lastY - screenHeight / 2, 2) <= Math.pow(SideBar.TAB_RADIUS, 2))
            SideBar.showTab();
        else if (lastX <= screenWidth - SideBar.TAB_RADIUS) SideBar.hideTab();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int screenWidth = WindowManager.painter.getWidth();
        int screenHeight = WindowManager.painter.getHeight();
        if (e.getX() >= screenWidth) {
        } else if (Math.pow(e.getX() - screenWidth, 2) + Math.pow(e.getY() - screenHeight / 2, 2) <= Math.pow(SideBar.TAB_RADIUS, 2)) {
            SideBar.toggle();
        } else {
            mouseDown = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
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