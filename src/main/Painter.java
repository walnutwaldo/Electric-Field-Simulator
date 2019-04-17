package main;

import UI.SideBar;
import UI.UIComponent;
import editing.ChargeSelector;
import math.Matrix;
import objects.Camera;
import objects.FixedPointCharge;
import objects.MovingCharge;
import objects.Positionable;
import shapes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.Comparator;
import java.util.PriorityQueue;

import static math.LinAlg.LineSeg;
import static math.LinAlg.getDis;

public class Painter extends JPanel {

    public static final int MIN_BRIGHTNESS = 100000;
    public static final int MAX_BRIGHTNESS = 1000000;

    private static final double MAX_LINE_SEG_LENGTH = 2;

    private static final double GRID_STEP = 5;

    public double maxDim;

    public Graphics2D g;

    public int getWidth() {
        return super.getWidth() - SideBar.width;
    }

    public int getHeight() {
        return super.getHeight();
    }

    void addLineSeg(Matrix p1, Matrix p2, PriorityQueue<Positionable> pq) {
        Matrix curr = new Matrix(p1);
        Matrix unit = Matrix.scale(Matrix.normalize(Matrix.subtract(p2, p1)), MAX_LINE_SEG_LENGTH);
        while (true) {
            double dis = getDis(curr, p2);
            if (dis < MAX_LINE_SEG_LENGTH) {
                pq.add(new LineSeg(curr, p2));
                break;
            } else {
                Matrix next = Matrix.add(curr, unit);
                pq.add(new LineSeg(curr, next));
                curr = next;
            }
        }
    }

    void addGrid(PriorityQueue<Positionable> pq) {
        double gs = UIManager.gridSizeSlider.getVal();
        double mx = GRID_STEP * Math.floor(gs / GRID_STEP);
        for (double i = -mx; i <= mx; i += GRID_STEP) {
            addLineSeg(new Matrix(new double[][]{{i, -gs, 0}}), new Matrix(new double[][]{{i, gs, 0}}), pq);
            addLineSeg(new Matrix(new double[][]{{-gs, i, 0}}), new Matrix(new double[][]{{gs, i, 0}}), pq);
        }
        if (mx != gs)
            addLineSeg(new Matrix(new double[][]{{-gs, -gs, 0}}), new Matrix(new double[][]{{-gs, gs, 0}}), pq);
        if (mx != gs) addLineSeg(new Matrix(new double[][]{{gs, -gs, 0}}), new Matrix(new double[][]{{gs, gs, 0}}), pq);
        if (mx != gs) addLineSeg(new Matrix(new double[][]{{-gs, gs, 0}}), new Matrix(new double[][]{{gs, gs, 0}}), pq);
        if (mx != gs)
            addLineSeg(new Matrix(new double[][]{{-gs, -gs, 0}}), new Matrix(new double[][]{{gs, -gs, 0}}), pq);
    }

    void addBox(PriorityQueue<Positionable> pq) {
        double gs = UIManager.gridSizeSlider.getVal();
        for (int i = 0; i < 3; i++)
            for (double a = -gs; a <= gs; a += 2 * gs)
                for (double b = -gs; b <= gs; b += 2 * gs) {
                    Matrix p1 = new Matrix(new double[][]{{a, b, -gs}});
                    p1.set(0, 2, p1.get(0, i));
                    p1.set(0, i, -gs);
                    Matrix p2 = new Matrix(new double[][]{{a, b, gs}});
                    p2.set(0, 2, p2.get(0, i));
                    p2.set(0, i, gs);
                    addLineSeg(p1, p2, pq);
                }
    }

    private void drawTab() {
        AffineTransform at = g.getTransform();
        g.setClip(0, 0, getWidth(), getHeight());
        g.setColor(Color.GRAY);
        double tabProtrusion = SideBar.getTabProtrusion();
        g.translate(getWidth() + SideBar.TAB_RADIUS - tabProtrusion, getHeight() / 2);
        g.fill(new Ellipse2D.Double(-SideBar.TAB_RADIUS, -SideBar.TAB_RADIUS, 2 * SideBar.TAB_RADIUS, 2 * SideBar.TAB_RADIUS));
        if (WindowManager.mouseUI.onTab) {
            if (WindowManager.mouseUI.downTab) g.setColor(new Color(240, 240, 240));
            else g.setColor(new Color(220, 220, 220));
        } else g.setColor(new Color(200, 200, 200));
        if (SideBar.showingBar) TabArrow.draw(g, TabArrow.RIGHT);
        else TabArrow.draw(g, TabArrow.LEFT);
        g.setTransform(at);
        g.setClip(0, 0, getWidth(), getHeight());
    }

    private void drawOptionsBar() {
        AffineTransform at = g.getTransform();
        g.setColor(new Color(100, 100, 100));
        g.fillRect(0, 0, SideBar.width, getHeight());
        g.setColor(new Color(130, 130, 130));
        g.fillRect(SideBar.currentOption * SideBar.OPTIONS_HEIGHT, 0, SideBar.OPTIONS_HEIGHT, SideBar.OPTIONS_HEIGHT + 1);
        if (WindowManager.mouseUI.onOption) {
            if (WindowManager.mouseUI.downOption) g.setColor(new Color(80, 80, 80));
            else g.setColor(new Color(90, 90, 90));
            g.fillRect(WindowManager.mouseUI.currOption * SideBar.OPTIONS_HEIGHT, 0, SideBar.OPTIONS_HEIGHT, SideBar.OPTIONS_HEIGHT + 1);
        }
        for (int i = 0; i < SideBar.NUM_OPTIONS; i++) {
            g.setColor(Color.WHITE);
            if (i == SideBar.SETTINGS) Gear.draw(g);
            else if (i == SideBar.EDIT) Pencil.draw(g);
            else if (i == SideBar.INFO) InfoIcon.draw(g);
            g.translate(SideBar.OPTIONS_HEIGHT, 0);
        }
        g.setTransform(at);
    }

    private void drawSideBar() {
        drawTab();
        AffineTransform at = g.getTransform();
        g.setClip(getWidth(), 0, SideBar.width, getHeight());
        g.translate(getWidth(), 0);
        drawOptionsBar();
        g.translate(0, SideBar.OPTIONS_HEIGHT);
        g.setColor(new Color(130, 130, 130));
        g.fillRect(0, 0, SideBar.width, getHeight() - SideBar.OPTIONS_HEIGHT);
        int totalDY = 0;
        for (UIComponent uic : UIManager.getUIComponents()) {
            uic.draw(g);
            totalDY += uic.height + uic.topMargin;
            g.translate(0, uic.height + uic.topMargin);
        }
        g.setTransform(at);
        g.setClip(0, 0, getWidth(), getHeight());
    }

    private void drawSimulation() {
        AffineTransform at = g.getTransform();
        g.translate((double) getWidth() / 2, (double) getHeight() / 2);
        g.scale(maxDim / 2, -maxDim / 2);

        g.clearRect(-1, -1, 2, 2);
        g.setColor(Color.BLACK);
        g.fillRect(-1, -1, 2, 2);

        PriorityQueue<Positionable> pq = new PriorityQueue<Positionable>(new Comparator<Positionable>() {
            public int compare(Positionable o1, Positionable o2) {
                return (int) Math.signum(o2.getDisTo(Camera.getPos()) - o1.getDisTo(Camera.getPos()));
            }
        });
        if (UIManager.gridCheckbox.isChecked()) addGrid(pq);
        if (UIManager.boxCheckbox.isChecked()) addBox(pq);
        for (FixedPointCharge fpc : SimulationManager.getFixedCharges()) pq.add(fpc);
        for (MovingCharge mc : SimulationManager.getMovingCharges()) pq.add(mc);
        while (!pq.isEmpty()) {
            Positionable o = pq.poll();
            o.draw(g);
        }
        g.setTransform(at);
    }

    private void drawEditUI() {
        g.setFont(new Font("Havana", Font.BOLD, 20));
        g.setColor(Color.RED);
        g.drawString("EDIT MODE", 10, 30);
    }

    private void init(Graphics _g) {
        maxDim = Math.max(getWidth(), getHeight());
        g = (Graphics2D) _g;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public void paintComponent(Graphics _g) {
        init(_g);
        drawSimulation();
        drawSideBar();
        if(ChargeSelector.editing) drawEditUI();
    }

}