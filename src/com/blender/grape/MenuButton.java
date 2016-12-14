package com.blender.grape;

import com.blender.grape.resources.DialogResources;
import com.blender.grape.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
public class MenuButton extends JButton {
    public MenuButton(String textKey, Runnable action) {
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setHorizontalTextPosition(CENTER);
        setVerticalTextPosition(CENTER);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
        setText(DialogResources.getString(textKey));
        setToolTipText(DialogResources.getString(textKey));
        setIcon(Icons.MENU_BUTTON);
    }
}
