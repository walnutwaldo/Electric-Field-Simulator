package main;

import UI.MouseUI;

import javax.swing.*;

public class WindowManager {

    private static final String TITLE = "Electric Field Simulator";
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    public static JFrame frame;
    public static Painter painter;
    public static MouseUI mouseUI;

    public static void init() {
        frame = new JFrame(TITLE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);

        painter = new Painter();
        frame.add(painter);

        mouseUI = new MouseUI();
        painter.addMouseMotionListener(mouseUI);
        painter.addMouseListener(mouseUI);
        painter.addMouseWheelListener(mouseUI);
    }

    public static void show() {
        frame.setVisible(true);
    }

    public static void update() {
        frame.repaint();
    }

}
