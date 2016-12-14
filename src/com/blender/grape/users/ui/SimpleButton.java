package com.blender.grape.users.ui;

import com.blender.grape.resources.DialogResources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
public class SimpleButton extends JButton {
    private final static Dimension SIZE = new Dimension(100, 20);

    public SimpleButton(String textKey, Runnable action, Icon icon) {
        this(textKey, action);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setIcon(scaleIcon(icon));
        setText(null);
        setToolTipText(DialogResources.getString(textKey));
    }

    public SimpleButton(String textKey, Runnable action) {
        setPreferredSize(SIZE);
        setToolTipText(DialogResources.getString(textKey));
        setAction(new AbstractAction(DialogResources.getString(textKey)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    private Icon scaleIcon(Icon icon) {
        Image image = ((ImageIcon) icon).getImage();
        Image scaledImage = image.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}
