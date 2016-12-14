package com.blender.grape.storage.dialog;

import com.blender.grape.storage.controller.CategoryController;
import com.blender.grape.storage.model.Category;
import com.blender.grape.users.ui.UIUtilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CategoryEditDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JTextArea textArea1;

    private CategoryController categoryController;
    private int actionType;

    private ManagementDialog parentDialog;

    public CategoryEditDialog(int type, Category model, ManagementDialog parentDialog) {
        this.parentDialog = parentDialog;
        categoryController = new CategoryController();
        this.actionType = type;
        if (model != null) {
            categoryController.setModel(model);
        }
        setContentPane(contentPane);
        setModal(true);
        setTitle("Category editor");
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
                            categoryController.setModel(new Category(-1, textField1.getText(), textArea1.getText(),
                                    "", 0));
                            if (categoryController.insertIntoDatabase()) {
                                parentDialog.updateView();
                                close();
                            } else {
                                UIUtilities.showWindow(new AlertDialog("Error", "There was an error creating new category."));
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
                            categoryController.setModel(new Category(categoryController.getModel().getId(), textField1.getText(),
                                    textArea1.getText(), categoryController.getModel().getCreationDate(),
                                    categoryController.getModel().getCommodityCount()));
                            if (categoryController.updateDatabaseModel()) {
                                parentDialog.updateView();
                                close();
                            } else {
                                UIUtilities.showWindow(new AlertDialog("Error", "There was an error updating a category."));
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
        textField1.setText(categoryController.getModel().getTitle());
        textArea1.setText(categoryController.getModel().getDescription()+"");
    }

    private String inputStatus() {
        if (textField1.getText().equals("") || textArea1.getText().equals("")) {
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
