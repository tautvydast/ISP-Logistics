package com.blender.grape.storage.dialog;

import com.blender.grape.storage.controller.SectionController;
import com.blender.grape.storage.model.Section;
import com.blender.grape.storage.model.SimpleUserModel;
import com.blender.grape.users.structures.user.User;
import com.blender.grape.users.ui.UIUtilities;
import com.blender.grape.users.sql.SQLUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class SectionEditDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JComboBox comboBox1;

    private SectionController sectionController;
    private int actionType;
    private ArrayList<User> comboBoxUsers;

    private ManagementDialog parentDialog;
    private Section model;

    public SectionEditDialog(int type, Section model, ManagementDialog parentDialog) {
        this.parentDialog = parentDialog;
        this.model = model;
        sectionController = new SectionController();
        this.actionType = type;
        if (model != null) {
            sectionController.setModel(model);
        }
        setContentPane(contentPane);
        setModal(true);
        setTitle("Section editor");
        getRootPane().setDefaultButton(buttonOK);
        initialize();
        this.pack();
    }

    private void initialize() {
        switch (actionType) {
            case 0:
                // add new
                setComboBox(-1);
                buttonOK.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        if (inputStatus().equals("")) {
                            SimpleUserModel managerModel = (SimpleUserModel) comboBox1.getSelectedItem();
                            sectionController.setModel(new Section(-1, textField1.getText(),
                                    Integer.parseInt(textField2.getText()), 0, textField3.getText(), null,
                                    managerModel));
                            if (sectionController.insertIntoDatabase()) {
                                parentDialog.updateView();
                                close();
                            } else {
                                UIUtilities.showWindow(new AlertDialog("Error", "There was an error creating new section."));
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
                            SimpleUserModel managerModel = (SimpleUserModel) comboBox1.getSelectedItem();
                            sectionController.setModel(new Section(sectionController.getModel().getId(), textField1.getText(),
                                    Integer.parseInt(textField2.getText()), sectionController.getModel().getCommodityAmount(),
                                    textField3.getText(), null, managerModel));
                            if (sectionController.updateDatabaseModel()) {
                                parentDialog.updateView();
                                close();
                            } else {
                                UIUtilities.showWindow(new AlertDialog("Error", "There was an error updating a section."));
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
        textField1.setText(sectionController.getModel().getTitle());
        textField2.setText(sectionController.getModel().getCapacity()+"");
        textField3.setText(sectionController.getModel().getResponsiblePerson());
        setComboBox(sectionController.getModel().getManagerModel().getId());
    }

    private void setComboBox(int managerId) {
        int listId = 0;
        boolean found = false;
        String query = "SELECT * FROM gp_vartotojas";
        try (ResultSet rs = SQLUtil.executeQueryWithResult(query)) {
            while (rs != null && rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("vardas");
                String lastName = rs.getString("pavarde");
                SimpleUserModel model = new SimpleUserModel(id, name, lastName);
                comboBox1.addItem(model);
                if (id == managerId) {
                    found = true;
                }
                if (!found) {
                    listId++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (managerId == -1) {
            comboBox1.setSelectedIndex(0);
        } else {
            comboBox1.setSelectedIndex(listId);
        }
    }

    private String inputStatus() {
        if (textField1.getText().equals("") || textField2.getText().equals("") || textField3.getText().equals("")) {
            return "One or more fields are empty";
        }
        if (!Pattern.matches("^[1-9][0-9]*$", textField2.getText())) {
            return "False input for capacity";
        }
        if (model != null && model.getCommodityAmount() > Integer.parseInt(textField2.getText())) {
            return "Capacity should be bigger than " + (model.getCommodityAmount() - 1);
        }
        return "";
    }

    private void close() {
        setVisible(false);
        dispose();
    }

}
