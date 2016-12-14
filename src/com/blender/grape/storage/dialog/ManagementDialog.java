package com.blender.grape.storage.dialog;

import com.blender.grape.storage.controller.CategoryController;
import com.blender.grape.storage.controller.ManufacturerController;
import com.blender.grape.storage.controller.SectionController;
import com.blender.grape.storage.model.Category;
import com.blender.grape.storage.model.Manufacturer;
import com.blender.grape.storage.model.Section;
import com.blender.grape.storage.table.model.CategoryTableModel;
import com.blender.grape.storage.table.model.ManufacturerTableModel;
import com.blender.grape.storage.table.model.SectionTableModel;
import com.blender.grape.users.ui.UIUtilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManagementDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JComboBox comboBox1;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTable contentsTable;

    private SectionController sectionController;
    private ManufacturerController manufacturerController;
    private CategoryController categoryController;

    enum SelectedWindow {SECTIONS, MANUFACTURERS, CATEGORIES};
    private SelectedWindow currentWindow;
    private int currentRow;

    public ManagementDialog() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Manage warehouse");
        initialize();
        this.pack();
    }

    private void initialize() {
        sectionController = new SectionController();
        manufacturerController = new ManufacturerController();
        categoryController = new CategoryController();

        setSectionsView();
        currentRow = contentsTable.getSelectedRow();

        comboBox1.addItem("Sections");
        comboBox1.addItem("Manufacturers");
        comboBox1.addItem("Categories");
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                switch (comboBox1.getSelectedIndex()) {
                    case 0:
                        setSectionsView();
                        break;
                    case 1:
                        setManufacturersView();
                        break;
                    case 2:
                        setCategoriesView();
                        break;
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                dispose();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                addAction();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                updateAction();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                deleteAction();
            }
        });
    }

    public void updateView() {
        switch (currentWindow) {
            case SECTIONS:
                setSectionsView();
                break;
            case MANUFACTURERS:
                setManufacturersView();
                break;
            case CATEGORIES:
                setCategoriesView();
                break;

        }
    }

    private void setSectionsView() {
        currentWindow = SelectedWindow.SECTIONS;
        List<Section> sections = sectionController.getList();
        contentsTable.setModel(new SectionTableModel(sections));
    }

    private void setManufacturersView() {
        currentWindow = SelectedWindow.MANUFACTURERS;
        List<Manufacturer> manufacturers = manufacturerController.getList();
        contentsTable.setModel(new ManufacturerTableModel(manufacturers));
    }

    private void setCategoriesView() {
        currentWindow = SelectedWindow.CATEGORIES;
        List<Category> categories = categoryController.getList();
        contentsTable.setModel(new CategoryTableModel(categories));
    }

    private void addAction() {
        switch(currentWindow) {
            case SECTIONS:
                SectionEditDialog sectionEditDialog = new SectionEditDialog(0, null, this);
                UIUtilities.showWindow(sectionEditDialog);
                break;
            case MANUFACTURERS:
                ManufacturerEditDialog manufacturerEditDialog= new ManufacturerEditDialog(0, null, this);
                UIUtilities.showWindow(manufacturerEditDialog);
                break;
            case CATEGORIES:
                CategoryEditDialog categoryEditDialog = new CategoryEditDialog(0, null, this);
                UIUtilities.showWindow(categoryEditDialog);
                break;
        }
    }

    private void updateAction() {
        currentRow = contentsTable.getSelectedRow();
        if (currentRow == -1) {
            UIUtilities.showWindow(new AlertDialog("Error", "Nothing selected"));
            return;
        }

        if (currentRow != -1) {
            switch (currentWindow) {
                case SECTIONS:
                    SectionTableModel sectionTableModel = (SectionTableModel) contentsTable.getModel();
                    Section sectionUpdateModel = sectionTableModel.getItemModel(currentRow);
                    SectionEditDialog sectionEditDialog = new SectionEditDialog(1, sectionUpdateModel, this);
                    UIUtilities.showWindow(sectionEditDialog);
                    break;
                case MANUFACTURERS:
                    ManufacturerTableModel manufacturerTableModel = (ManufacturerTableModel) contentsTable.getModel();
                    Manufacturer manufacturerUpdateModel = manufacturerTableModel.getItemModel(currentRow);
                    ManufacturerEditDialog manufacturerEditDialog= new ManufacturerEditDialog(1, manufacturerUpdateModel, this);
                    UIUtilities.showWindow(manufacturerEditDialog);
                    break;
                case CATEGORIES:
                    CategoryTableModel categoryTableModel = (CategoryTableModel) contentsTable.getModel();
                    Category categoryUpdateModel = categoryTableModel.getItemModel(currentRow);
                    CategoryEditDialog categoryEditDialog= new CategoryEditDialog(1, categoryUpdateModel, this);
                    UIUtilities.showWindow(categoryEditDialog);
                    break;
            }
        } else {
            UIUtilities.showWindow(new AlertDialog("Error", "Please select something."));
        }
    }

    private void deleteAction() {
        currentRow = contentsTable.getSelectedRow();
        if (currentRow == -1) {
            UIUtilities.showWindow(new AlertDialog("Error", "Nothing selected"));
            return;
        }

        if (currentRow != -1) {
            switch (currentWindow) {
                case SECTIONS:
                    SectionTableModel sectionTableModel = (SectionTableModel) contentsTable.getModel();
                    Section sectionDeleteModel = sectionTableModel.getItemModel(currentRow);
                    if (sectionController.deleteFromDatabase(sectionDeleteModel.getId())) {
                        updateView();
                    } else {
                        UIUtilities.showWindow(new AlertDialog("Error", "Delete failed"));
                    }
                    break;
                case MANUFACTURERS:
                    ManufacturerTableModel manufacturerTableModel = (ManufacturerTableModel) contentsTable.getModel();
                    Manufacturer manufacturerDeleteModel = manufacturerTableModel.getItemModel(currentRow);
                    if (manufacturerController.deleteFromDatabase(manufacturerDeleteModel.getId())) {
                        updateView();
                    } else {
                        UIUtilities.showWindow(new AlertDialog("Error", "Delete failed"));
                    }
                    break;
                case CATEGORIES:
                    CategoryTableModel categoryTableModel = (CategoryTableModel) contentsTable.getModel();
                    Category categoryDeleteModel = categoryTableModel.getItemModel(currentRow);
                    if (categoryController.deleteFromDatabase(categoryDeleteModel.getId())) {
                        updateView();
                    } else {
                        UIUtilities.showWindow(new AlertDialog("Error", "Delete failed"));
                    }
                    break;
            }
        } else {
            UIUtilities.showWindow(new AlertDialog("Error", "Please select something."));
        }
    }

}
