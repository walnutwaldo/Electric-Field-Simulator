import java.awt.event.*;

public class MouseUI implements MouseMotionListener, MouseListener, MouseWheelListener {

    private boolean mouseDown;
    private int lastX, lastY;

    private static final double X_SENSITIVITY = 0.01;
    private static final double Y_SENSITIVITY = 0.01;
    private static final double WHEEL_SENSITIVITY = 0.4;

    @Override
    public void mouseDragged(MouseEvent e) {
        int dx = e.getX() - lastX;
        int dy = e.getY() - lastY;
        lastX = e.getX();
        lastY = e.getY();
        Camera.increaseTilt(Y_SENSITIVITY * dy);
        Camera.increaseTheta(X_SENSITIVITY * dx);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDown = true;
        lastX = e.getX();
        lastY = e.getY();
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
        Camera.increaseDis(WHEEL_SENSITIVITY * e.getPreciseWheelRotation());
    }
}