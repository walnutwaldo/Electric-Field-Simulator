package main;

import UI.SideBar;
import UI.UIComponent;
import math.Matrix;
import objects.Camera;
import objects.FixedPointCharge;
import objects.MovingCharge;
import objects.Positionable;

import static math.LinAlg.*;
import static math.Conics.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class Painter extends JPanel {

    private static final int MIN_BRIGHTNESS = 100000;
    private static final int MAX_BRIGHTNESS = 1000000;

    private static final int[] DX = new int[]{-1, 0, 1, 0};
    private static final int[] DY = new int[]{0, 1, 0, -1};

    private static final double GRID_STEP = 5;
    private static double brightness_coeff = 500000;

    private Camera camera;

    private double maxDim;
    private Matrix cameraMatrix;
    private Matrix cameraPos;

    private PriorityQueue<Positionable> pq = new PriorityQueue<Positionable>(new Comparator<Positionable>() {
        public int compare(Positionable o1, Positionable o2) {
            return (int) Math.signum(o2.getDisTo(camera.getPos()) - o1.getDisTo(camera.getPos()));
        }
    });

    public Painter(Camera _camera) {
        super();
        camera = _camera;
    }

    public int getWidth() {
        return super.getWidth() - SideBar.width;
    }

    public int getHeight() {
        return super.getHeight();
    }

    private void drawEllipse(Matrix ellipse, Graphics2D g) {
        Matrix ellipseData = getEllipseData(ellipse);
        double theta = ellipseData.get(0, 0);
        double a = ellipseData.get(0, 1);
        double b = ellipseData.get(0, 2);
        double h = ellipseData.get(0, 3);
        double k = ellipseData.get(0, 4);
        g.rotate(-theta, getWidth() / 2, getHeight() / 2);
        g.fillOval((int) Math.round(getWidth() / 2 + (h - a) * maxDim / 2), (int) Math.round(getHeight() / 2 - (k + b) * maxDim / 2),
                (int) Math.round(a * maxDim), (int) Math.round(b * maxDim));
        g.rotate(theta, getWidth() / 2, getHeight() / 2);
    }

    private void drawSphere(Graphics2D g, Matrix pos, double r) {
        if (!visible(pos)) return;
        int brightness = (int) Math.min(255, brightness_coeff / squareDis(pos, cameraPos));
        g.setColor(new Color(brightness, brightness, brightness));

        Matrix p_prime = Matrix.subtract(pos, cameraPos);
        double d = p_prime.length();
        double r_prime = r * Math.sqrt(d * d + r * r) / d;
        double d_prime = Math.sqrt(d * d - r * r - r_prime * r_prime);
        Matrix A = Matrix.scale(p_prime, d_prime / d);
        double b = Math.sqrt(Math.pow(A.get(0, 0), 2) + Math.pow(A.get(0, 1), 2));

        double a1 = A.get(0, 0);
        double a2 = -A.get(0, 1) * r_prime / b;
        double a3 = -A.get(0, 0) * A.get(0, 2) * r_prime / (d_prime * b);
        double a4 = A.get(0, 2);
        double a5 = b * r_prime / d_prime;
        double a6 = Math.tan(Camera.FOV / 2) * A.get(0, 1);
        double a7 = Math.tan(Camera.FOV / 2) * A.get(0, 0) * r_prime / b;
        double a8 = -Math.tan(Camera.FOV / 2) * A.get(0, 1) * A.get(0, 2) * r_prime / (d_prime * b);

        Matrix linSystem = new Matrix(new double[][]{
                {2 * a1 * a2, a2 * a4, 0, a2 * a6 + a1 * a7, a4 * a7, -2 * a6 * a7},
                {2 * a1 * a3, a3 * a4 + a1 * a5, 2 * a4 * a5, a3 * a6 + a1 * a8, a5 * a6 + a4 * a8, -2 * a6 * a8},
                {2 * a2 * a3, a2 * a5, 0, a2 * a8 + a3 * a7, a5 * a7, -2 * a7 * a8},
                {a1 * a1 + a3 * a3, a1 * a4 + a3 * a5, a4 * a4 + a5 * a5, a1 * a6 + a3 * a8, a4 * a6 + a5 * a8, -a6 * a6 - a8 * a8},
                {a2 * a2 - a3 * a3, -a3 * a5, -a5 * a5, a2 * a7 - a3 * a8, -a5 * a8, -a7 * a7 + a8 * a8}
        });
        Matrix ellipse = solveLinSystem(linSystem);
        drawEllipse(ellipse, g);
    }

    private void drawLine(Graphics2D g, LineSeg ls) {
        Matrix p1 = Matrix.mult(ls.pnt1, cameraMatrix);
        Matrix p2 = Matrix.mult(ls.pnt2, cameraMatrix);
        LineSeg vis = getVisibleSeg(p1, p2);

        int brightness = (int) Math.min(255, brightness_coeff / squareDis(Matrix.scale(Matrix.add(p1, p2), 0.5), cameraPos));
        g.setColor(new Color(brightness, brightness, brightness));

        if (vis.pnt1 == null) return;
        p1 = toScreen(vis.pnt1);
        p2 = toScreen(vis.pnt2);
        g.drawLine((int) Math.round(p1.get(0, 0)), (int) Math.round(p1.get(0, 1)),
                (int) Math.round(p2.get(0, 0)), (int) Math.round(p2.get(0, 1)));
    }

    private boolean visible(Matrix point) {
        if (point == null) return false;
        return point.get(0, 1) > -Camera.getDis() - EPSILON &&
                Math.abs(point.get(0, 2)) < Math.tan(Camera.FOV / 2) * (Camera.getDis() + point.get(0, 1)) + EPSILON &&
                Math.abs(point.get(0, 0)) < Math.tan(Camera.FOV / 2) * (Camera.getDis() + point.get(0, 1)) + EPSILON;
    }

    private LineSeg getVisibleSeg(Matrix point1, Matrix point2) {
        if (visible(point1) && visible(point2)) return new LineSeg(point1, point2);
        List<Matrix> ints = new ArrayList<Matrix>();
        for (int i = 0; i < 4; i++) {
            Matrix normal = new Matrix(new double[][]{{DX[i] * Math.sin(Camera.FOV / 2 + Math.PI / 2),
                    Math.cos(Camera.FOV / 2 + Math.PI / 2), DY[i] * Math.sin(Camera.FOV / 2 + Math.PI / 2)}});
            Matrix inters = intersection(new LineSeg(point1, point2), new Plane(cameraPos, normal));
            if (visible(inters)) ints.add(inters);
        }
        if (visible(point1)) return new LineSeg(point1, ints.get(0));
        if (visible(point2)) return new LineSeg(point2, ints.get(0));
        else if (!ints.isEmpty()) {
            for (int i = ints.size() - 1; i > 0; i--) if (approx(ints.get(i), ints.get(i - 1))) ints.remove(i);
            return new LineSeg(ints.get(0), ints.get(1));
        }
        return new LineSeg(null, null);
    }

    void drawGrid(Graphics2D g) {
        List<LineSeg> list = new ArrayList<LineSeg>();
        for (double i = -SimulationManager.GRID_SIZE; i <= SimulationManager.GRID_SIZE; i += GRID_STEP) {
            for (double j = -SimulationManager.GRID_SIZE; j <= SimulationManager.GRID_SIZE; j += GRID_STEP) {
                Matrix centerPos = new Matrix(new double[][]{{i, j, 0}});
                loop:
                for (int k = 0; k < 4; k++) {
                    Matrix newp = new Matrix(new double[][]{{i + DX[k] * GRID_STEP, j + DY[k] * GRID_STEP, 0}});
                    for (int dim = 0; dim < 3; dim++)
                        if (Math.abs(newp.get(0, dim)) > SimulationManager.GRID_SIZE) continue loop;

                    pq.add(new LineSeg(centerPos, newp));
                }
            }
        }
    }

    private Matrix screenScale(Matrix proj) {
        Matrix res = new Matrix(1, 2);
        res.set(0, 0, (double) getWidth() / 2 + proj.get(0, 0) * maxDim / 2);
        res.set(0, 1, (double) getHeight() / 2 - proj.get(0, 1) * maxDim / 2);
        return res;
    }

    private Matrix toScreen(Matrix pos) {
        Matrix proj = Camera.getProjection(pos);
        return screenScale(proj);
    }

    private void drawTab(Graphics2D g) {
        g.setColor(Color.GRAY);
        g.translate(getWidth() + SideBar.TAB_RADIUS - SideBar.getTabProtrusion(), getHeight() / 2);
        g.fillOval(-SideBar.TAB_RADIUS, -SideBar.TAB_RADIUS, 2 * SideBar.TAB_RADIUS, 2 * SideBar.TAB_RADIUS);
        g.setStroke(new BasicStroke(5));
        if (WindowManager.mouseUI.onTab) g.setColor(Color.WHITE);
        else g.setColor(new Color(200, 200, 200));
        if (SideBar.showingBar) {
            g.setClip(-2 * SideBar.TAB_RADIUS / 3, -SideBar.TAB_RADIUS / 2, SideBar.TAB_RADIUS / 2, SideBar.TAB_RADIUS);
            g.rotate(Math.PI / 4, 0, 0);
            int shift = (int) (Math.sqrt(0.5) * (5 + SideBar.TAB_RADIUS / 6));
            g.drawRect(-100 - shift, shift, 100, 100);
            g.rotate(-Math.PI / 4, 0, 0);
        } else {
            g.setClip(-2 * SideBar.TAB_RADIUS / 3, -SideBar.TAB_RADIUS / 2, SideBar.TAB_RADIUS / 2, SideBar.TAB_RADIUS);
            g.rotate(Math.PI / 4, -2 * SideBar.TAB_RADIUS / 3, 0);
            int shift = (int) (Math.sqrt(0.5) * 5);
            g.drawRect(-2 * SideBar.TAB_RADIUS / 3 + shift, -100 - shift, 100, 100);
            g.rotate(-Math.PI / 4, -2 * SideBar.TAB_RADIUS / 3, 0);
        }
        g.translate(-getWidth() - SideBar.TAB_RADIUS + SideBar.getTabProtrusion(), -getHeight() / 2);
        g.setClip(0, 0, getWidth(), getHeight());
    }

    private void drawSideBar(Graphics2D g) {
        drawTab(g);
        g.setClip(getWidth(), 0, SideBar.width, getHeight());
        g.translate(getWidth(), 0);
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, SideBar.width, getHeight());
        int totalDY = 0;
        for (UIComponent uic : SideBar.getUIComponents()) {
            uic.draw(g);
            totalDY += uic.height + uic.topMargin;
            g.translate(0, uic.height + uic.topMargin);
        }
        g.translate(-getWidth(), -totalDY);
        g.setClip(0, 0, getWidth(), getHeight());
    }

    public void paintComponent(Graphics _g) {
        brightness_coeff = (MAX_BRIGHTNESS - MIN_BRIGHTNESS) * SideBar.brightnessSlider.sliderLoc + MIN_BRIGHTNESS;

        Graphics2D g = (Graphics2D) _g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.clearRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        cameraMatrix = Camera.getTransformationMatrix();
        cameraPos = new Matrix(new double[][]{{0, -Camera.getDis(), 0}});
        maxDim = Math.max(getWidth(), getHeight());
        drawGrid(g);
        g.setColor(Color.WHITE);
        for (FixedPointCharge fpc : SimulationManager.getFixedCharges()) pq.add(fpc);
        for (MovingCharge mc : SimulationManager.getMovingCharges()) pq.add(mc);
        while (!pq.isEmpty()) {
            Object o = pq.poll();
            if (o instanceof FixedPointCharge)
                drawSphere(g, Matrix.mult(((FixedPointCharge) o).getPos(), cameraMatrix), FixedPointCharge.RADIUS);
            if (o instanceof MovingCharge)
                drawSphere(g, Matrix.mult(((MovingCharge) o).getPos(), cameraMatrix), MovingCharge.RADIUS);
            if (o instanceof LineSeg)
                drawLine(g, (LineSeg) o);
        }

        drawSideBar(g);
    }

}