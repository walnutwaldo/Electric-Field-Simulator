import objects.Camera;

import javax.swing.*;

public class WindowManager {

    private static final String TITLE = "Electric Field Simulator";
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    public static Camera camera;
    public static JFrame frame;
    public static Painter painter;
    public static MouseUI mouseUI;

    public static void init() {
        frame = new JFrame(TITLE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);

        camera = new Camera();
        painter = new Painter(camera);
        frame.add(painter);

        mouseUI = new MouseUI();
        painter.addMouseMotionListener(mouseUI);
        painter.addMouseListener(mouseUI);
        painter.addMouseWheelListener(mouseUI);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void update() {
        frame.repaint();
    }

}
