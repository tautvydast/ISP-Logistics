package com.blender.grape.storage.dialog;

import com.blender.grape.storage.controller.CommodityController;
import com.blender.grape.users.ui.UIUtilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangeStatusDialog extends JDialog {
    private JPanel contentPane;
    private JButton confirmButton;
    private JButton cancelButton;
    private JTextArea textArea1;
    private JButton buttonOK;

    private int type;
    private int commodityId;
    private ComListDialog parent;

    private CommodityController commodityController;

    public ChangeStatusDialog(int type, int commodityId, ComListDialog parent) {
        this.type = type;
        this.commodityId = commodityId;
        this.parent = parent;
        commodityController = new CommodityController();
        setContentPane(contentPane);
        setModal(true);
        switch (type) {
            case 0:
                setTitle("Write-off commodity");
                break;
            case 1:
                setTitle("Set commodity for export");
                break;
        }
        getRootPane().setDefaultButton(buttonOK);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                confirm();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                close();
            }
        });
        pack();
    }

    private void confirm() {
        if (commodityController.updateStatus(commodityId, type == 0? 1 : 2, textArea1.getText())) {
            parent.setTableContents();
            close();
        } else {
            UIUtilities.showWindow(new AlertDialog("Error", "Failed to update status"));
        }
    }

    private void close() {
        setVisible(false);
        dispose();
    }
}
