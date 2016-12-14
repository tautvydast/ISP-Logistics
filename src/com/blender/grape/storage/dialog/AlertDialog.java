package com.blender.grape.storage.dialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlertDialog extends JDialog {
    private JPanel contentPane;
    private JButton OKButton;
    private JLabel messageLabel;

    public AlertDialog(String title, String message) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(OKButton);
        this.setTitle(title);
        messageLabel.setText(message);
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                dispose();
            }
        });
        this.pack();
    }

}
