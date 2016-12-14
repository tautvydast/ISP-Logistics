package com.blender.grape.utils;

import com.blender.grape.resources.DialogResources;
import com.blender.grape.users.ui.UIUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ProgressDialog extends JDialog {
    private static final int PROGRESS_BAR_WIDTH = 200;
    private JProgressBar progressBar;
    private JLabel message;
    private Runnable runnable;

    public ProgressDialog(Runnable runnable, String message) {
        init(runnable, message);
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public void setVisible(boolean visible) {
        if (visible) {
            progressBar.setIndeterminate(true);
            UIUtilities.showWindow(this);
        } else {
            progressBar.setIndeterminate(false);
        }
        super.setVisible(visible);
    }

    private void init(Runnable runnable, String message) {
        setupControls();
        setupComponent();
        setupEventHandlers();
        setMessage(message);
        setRunnable(runnable);
    }

    private void setupControls() {
        progressBar = new JProgressBar();
        Dimension preferredSize = progressBar.getPreferredSize();
        preferredSize.width = PROGRESS_BAR_WIDTH;
        progressBar.setPreferredSize(preferredSize);
        message = new JLabel(" ");
    }

    private void setupComponent() {
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = GridBagConstraints.RELATIVE;
        gc.anchor = GridBagConstraints.NORTHWEST;
        contentPane.add(message, gc);
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(progressBar, gc);

        setTitle("");
        pack();
    }

    private void setupEventHandlers() {
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent event) {
                final Thread task = new Thread(runnable);
                task.start();
                new Thread() {
                    public void run() {
                        try {
                            task.join();
                        } catch (InterruptedException e) {
                            JOptionPane.showMessageDialog(ProgressDialog.this,
                                    DialogResources.getString("PROGRESS_DIALOG_FAILED", e.getMessage()),
                                    DialogResources.getString("ERROR"),
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        SwingUtilities.invokeLater(() -> setVisible(false));
                    }
                }.start();
            }
        });
    }
}