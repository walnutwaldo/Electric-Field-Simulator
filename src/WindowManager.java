import javax.swing.*;

public class WindowManager {

    private static final String TITLE = "Electric Field Simulator";
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    private static JFrame frame;
    private static Painter painter;
    private static MouseUI mouseUI;

    public static void init() {
        frame = new JFrame(TITLE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);

        painter = new Painter();
        frame.add(painter);

        mouseUI = new MouseUI();
        frame.addMouseMotionListener(mouseUI);
        frame.addMouseListener(mouseUI);
        frame.addMouseWheelListener(mouseUI);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void update() {
        frame.repaint();
    }

}
