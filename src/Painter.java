import math.Matrix;

import static math.LinAlg.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;


public class Painter extends JPanel {

    private static final int[] DX = new int[]{-1, 0, 1, 0};
    private static final int[] DY = new int[]{0, 1, 0, -1};

    private static final double GRID_STEP = 5;
    private static final double BRIGHTNESS = 500000;

    private static double maxDim;
    private Matrix cameraMatrix;
    private Matrix cameraPos;

    public int getWidth() { return super.getWidth(); }

    public int getHeight() { return super.getHeight(); }

    private void drawConic(Matrix conic, Graphics2D g) {
        double A = conic.get(0, 0);
        double B = conic.get(0, 1);
        double C = conic.get(0, 2);
        double D = conic.get(0, 3);
        double E = conic.get(0, 4);
        double theta = 0.5 * Math.atan2(B, A - C);
        double c = Math.cos(theta), s = Math.sin(theta);
        double A2 = A * c * c + B * s * c + C * s * s;
        double C2 = A * s * s - B * s * c + C * c * c;
        double D2 = D * c + E * s;
        double E2 = -D * s + E * c;

        double m = D2 * D2 / (4 * A2) + E2 * E2 / (4 * C2) - 1;

        double a = Math.sqrt(m / A2);
        double b = Math.sqrt(m / C2);
        double h = -D2 / (2 * A2);
        double k = -E2 / (2 * C2);
        g.rotate(-theta, getWidth() / 2, getHeight() / 2);

        g.fillOval((int) (getWidth() / 2 + (h - a) * maxDim / 2), (int) (getHeight() / 2 - (k + b) * maxDim / 2),
                (int) (a * maxDim), (int) (b * maxDim));
        g.rotate(theta, getWidth() / 2, getHeight() / 2);
    }

    private void drawSphere(Graphics2D g, Matrix pos, double r) {
        if (!visible(pos)) return;
        int brightness = (int) Math.min(255, BRIGHTNESS / squareDis(pos, cameraPos));
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

        /*final double STEP = 0.1;
        for (double theta = 0; theta < 2 * Math.PI; theta += STEP) {
            Matrix p1 = new Matrix(new double[][]{{a1 + a2 * Math.cos(theta) + a3 * Math.sin(theta), a4 + a5 * Math.sin(theta)}});
            p1.multBy(1.0 / (a6 + a7 * Math.cos(theta) + a8 * Math.sin(theta)));
            p1 = screenScale(p1);
            Matrix p2 = new Matrix(new double[][]{{a1 + a2 * Math.cos(theta + STEP) + a3 * Math.sin(theta + STEP), a4 + a5 * Math.sin(theta + STEP)}});
            p2.multBy(1.0 / (a6 + a7 * Math.cos(theta + STEP) + a8 * Math.sin(theta + STEP)));
            p2 = screenScale(p2);
            g.drawLine((int) p1.get(0, 0), (int) p1.get(0, 1), (int) p2.get(0, 0), (int) p2.get(0, 1));
        }*/

        Matrix linSystem = new Matrix(new double[][]{
                {2 * a1 * a2, a2 * a4, 0, a2 * a6 + a1 * a7, a4 * a7, -2 * a6 * a7},
                {2 * a1 * a3, a3 * a4 + a1 * a5, 2 * a4 * a5, a3 * a6 + a1 * a8, a5 * a6 + a4 * a8, -2 * a6 * a8},
                {2 * a2 * a3, a2 * a5, 0, a2 * a8 + a3 * a7, a5 * a7, -2 * a7 * a8},
                {a1 * a1 + a3 * a3, a1 * a4 + a3 * a5, a4 * a4 + a5 * a5, a1 * a6 + a3 * a8, a4 * a6 + a5 * a8, -a6 * a6 - a8 * a8},
                {a2 * a2 - a3 * a3, -a3 * a5, -a5 * a5, a2 * a7 - a3 * a8, -a5 * a8, -a7 * a7 + a8 * a8}
        });
        Matrix conic = solveLinSystem(linSystem);
        drawConic(conic, g);
    }

    private void drawLine(Graphics2D g, Matrix p1, Matrix p2) {
        LineSeg vis = getVisibleSeg(p1, p2);
        if (vis.pnt1 == null) return;
        p1 = toScreen(vis.pnt1);
        p2 = toScreen(vis.pnt2);
        g.drawLine((int) p1.get(0, 0), (int) p1.get(0, 1), (int) p2.get(0, 0), (int) p2.get(0, 1));
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
        for (double i = -SimulationManager.GRID_SIZE; i <= SimulationManager.GRID_SIZE; i += GRID_STEP) {
            for (double j = -SimulationManager.GRID_SIZE; j <= SimulationManager.GRID_SIZE; j += GRID_STEP) {
                Matrix centerPos = new Matrix(new double[][]{{i, j, 0}});
                centerPos = Matrix.mult(centerPos, cameraMatrix);
                loop: for (int k = 0; k < 4; k++) {
                    Matrix newp = new Matrix(new double[][]{{i + DX[k] * GRID_STEP, j + DY[k] * GRID_STEP, 0}});
                    for (int dim = 0; dim < 3; dim++) if (Math.abs(newp.get(0, dim)) > SimulationManager.GRID_SIZE) continue loop;
                    newp = Matrix.mult(newp, cameraMatrix);
                    int brightness = (int) Math.min(255, BRIGHTNESS / squareDis(centerPos, cameraPos));
                    if (i == 0 && j == 0 || (newp.get(0, 0) == 0 && newp.get(0, 1) == 0))
                        g.setColor(new Color(255, 0, 0, brightness));
                    else
                        g.setColor(new Color(255, 255, 255, brightness));

                    drawLine(g, centerPos, newp);
                }
            }
        }
    }

    private Matrix screenScale(Matrix proj) {
        Matrix res = new Matrix(1, 2);
        res.set(0, 0, getWidth() / 2 + proj.get(0, 0) * maxDim / 2);
        res.set(0, 1, getHeight() / 2 - proj.get(0, 1) * maxDim / 2);
        return res;
    }

    private Matrix toScreen(Matrix pos) {
        Matrix proj = Camera.getProjection(pos);
        return screenScale(proj);
    }

    public void paintComponent(Graphics _g) {
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
        PriorityQueue<Object> pq = new PriorityQueue<Object>(new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                Matrix p1 = null, p2 = null;
                if (o1 instanceof MovingCharge) p1 = ((MovingCharge) o1).getPos();
                else if (o1 instanceof FixedPointCharge) p1 = ((FixedPointCharge) o1).getPos();
                if (o2 instanceof MovingCharge) p2 = ((MovingCharge) o2).getPos();
                else if (o2 instanceof FixedPointCharge) p2 = ((FixedPointCharge) o2).getPos();
                return (int) Math.signum(getDis(Camera.getPos(), p2) - getDis(Camera.getPos(), p1));
            }
        });
        for (FixedPointCharge fpc : SimulationManager.getFixedCharges()) pq.add(fpc);
        for (MovingCharge mc : SimulationManager.getMovingCharges()) pq.add(mc);
        while (!pq.isEmpty()) {
            Object o = pq.poll();
            if (o instanceof FixedPointCharge)
                drawSphere(g, Matrix.mult(((FixedPointCharge) o).getPos(), cameraMatrix), FixedPointCharge.RADIUS);
            if (o instanceof MovingCharge)
                drawSphere(g, Matrix.mult(((MovingCharge) o).getPos(), cameraMatrix), MovingCharge.RADIUS);
        }
    }

}