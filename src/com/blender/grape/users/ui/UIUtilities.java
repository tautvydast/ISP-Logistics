package com.blender.grape.users.ui;

import java.awt.*;

/**
 * Created by Tautvydas Ramanauskas IF-4/12
 */
public class UIUtilities {
    public static void showWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
        frame.setVisible(true);
    }
}
