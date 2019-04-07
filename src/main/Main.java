package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    private static final int TICKS_PER_SECOND = 25;
    private static final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
    private static final int MAX_FRAME_SKIP = 500;
    private static final int MAX_FRAME_RATE = 60;

    private static long nextGameTick, lastRender;

    private static void update() {
        SimulationManager.update();
    }

    private static void render() {
        WindowManager.update();
    }

    private static void cleanUp() {
    }

    public static void main(String[] args) {
        UIManager.init();
        SimulationManager.init();
        WindowManager.init();
        nextGameTick = System.currentTimeMillis();
        Timer renderTimer = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (System.currentTimeMillis() > lastRender + 1000 / MAX_FRAME_RATE) {
                    lastRender = System.currentTimeMillis();
                    render();
                }
                if (!WindowManager.frame.isVisible()) ((Timer) e.getSource()).stop();
            }
        });
        renderTimer.start();
        while (WindowManager.frame.isVisible()) {
            int loops = 0;
            while (System.currentTimeMillis() > nextGameTick && loops < MAX_FRAME_SKIP) {
                update();
                nextGameTick += SKIP_TICKS;
                loops++;
            }
            //render();
        }
        cleanUp();
        System.exit(0);
    }

}