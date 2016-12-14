package com.blender.grape.utils;

import com.blender.grape.resources.DialogResources;
import com.blender.grape.users.ui.UIUtilities;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Created by Tautvydas Ramanauskas IF-4/12
 */
public class DestinationFolderChooser extends JDialog {
    public void initialize() {
        setTitle(DialogResources.getString("FOLDER_CHOOSER_TITLE"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        createFileChooser();
        UIUtilities.showWindow(this);
    }

    private void createFileChooser() {
        JFileChooser destinationFolderChooser = new JFileChooser();
        destinationFolderChooser.setCurrentDirectory(new java.io.File("."));
        destinationFolderChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        destinationFolderChooser.setAcceptAllFileFilterUsed(false);
        destinationFolderChooser.setMultiSelectionEnabled(false);
        destinationFolderChooser.setFileHidingEnabled(true);
        destinationFolderChooser.addActionListener(e -> {
            switch (e.getActionCommand()) {
                case JFileChooser.APPROVE_SELECTION:
                    File selectedFolder = destinationFolderChooser.getSelectedFile();
                case JFileChooser.CANCEL_SELECTION:
                    setVisible(false);
                    dispose();
                    break;
            }
        });
        getContentPane().add(destinationFolderChooser);
        setSize(destinationFolderChooser.getPreferredSize());
    }
}
