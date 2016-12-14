package com.blender.grape.storage.dialog;

import com.blender.grape.storage.controller.ManufacturerController;
import com.blender.grape.storage.model.Manufacturer;
import com.blender.grape.users.ui.UIUtilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class ManufacturerEditDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;

    private ManufacturerController manufacturerController;
    private int actionType;

    private ManagementDialog parentDialog;

    public ManufacturerEditDialog(int type, Manufacturer model, ManagementDialog parentDialog) {
        this.parentDialog = parentDialog;
        manufacturerController = new ManufacturerController();
        this.actionType = type;
        if (model != null) {
            manufacturerController.setModel(model);
        }
        setContentPane(contentPane);
        setModal(true);
        setTitle("Manufacturer editor");
        getRootPane().setDefaultButton(buttonOK);
        initialize();
        this.pack();
    }

    private void initialize() {
        switch (actionType) {
            case 0:
                // add new
                buttonOK.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        if (inputStatus().equals("")) {
                            manufacturerController.setModel(new Manufacturer(-1, textField1.getText(), textField2.getText(),
                                    "", textField3.getText()));
                            if (manufacturerController.insertIntoDatabase()) {
                                parentDialog.updateView();
                                close();
                            } else {
                                UIUtilities.showWindow(new AlertDialog("Error", "There was an error creating new manufacturer."));
                            }
                        } else {
                            UIUtilities.showWindow(new AlertDialog("Error", inputStatus()));
                        }
                    }
                });
                break;
            case 1:
                // edit
                setValues();
                buttonOK.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        if (inputStatus().equals("")) {
                            manufacturerController.setModel(new Manufacturer(manufacturerController.getModel().getId(),
                                    textField1.getText(), textField2.getText(),manufacturerController.getModel().getCreationDate(),
                                    textField3.getText()));
                            if (manufacturerController.updateDatabaseModel()) {
                                parentDialog.updateView();
                                close();
                            } else {
                                UIUtilities.showWindow(new AlertDialog("Error", "There was an error updating a manufacturer."));
                            }
                        } else {
                            UIUtilities.showWindow(new AlertDialog("Error", inputStatus()));
                        }
                    }
                });
                break;
        }

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                close();
            }
        });
    }

    private void setValues() {
        textField1.setText(manufacturerController.getModel().getName());
        textField2.setText(manufacturerController.getModel().getCountry()+"");
        textField3.setText(manufacturerController.getModel().getPhoneNumber());
    }

    private String inputStatus() {
        if (textField1.getText().equals("") || textField2.getText().equals("") || textField3.getText().equals("")) {
            return "One or more fields are empty";
        }
        /*if (!Pattern.matches("^[1-9][0-9]*$", textField3.getText())) {
            return "False input for capacity";
        }*/
        return "";
    }

    private void close() {
        setVisible(false);
        dispose();
    }

}
