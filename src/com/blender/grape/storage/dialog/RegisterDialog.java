package com.blender.grape.storage.dialog;

import com.blender.grape.storage.controller.*;
import com.blender.grape.storage.misc.DatePicker;
import com.blender.grape.storage.model.*;
import com.blender.grape.storage.table.model.CommodityTableModel;
import com.blender.grape.users.ui.UIUtilities;
import org.apache.poi.xwpf.usermodel.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton buttonRegister;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JComboBox comboBox1;
    private JTable table1;
    private JButton addButton;
    private JButton deleteSelectedButton;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JTextField textField8;
    private JTextField textField9;
    private JTextField textField10;
    private JTextField textField11;
    private JTextField textField12;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JButton clearAllButton;
    private JButton editSelectedButton;
    private JButton generateReportButton;

    private ShipmentController shipmentController;
    private CommodityController commodityController;
    private SimpleUserController driverController;
    private ManufacturerController manufacturerController;
    private CategoryController categoryController;
    private SectionController sectionController;

    private List<Commodity> commodityList;

    public RegisterDialog() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Shipment registration");
        getRootPane().setDefaultButton(buttonCancel);
        buttonRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                registerAction();
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                close();
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                addCommodityToList();
            }
        });
        editSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                editSelected();
            }
        });
        deleteSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                deleteFromList();
            }
        });
        clearAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                clearAll();
            }
        });
        textField1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                DatePicker datePicker = new DatePicker(textField1);
                datePicker.createCalendar();
            }
        });
        textField6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                DatePicker datePicker = new DatePicker(textField6);
                datePicker.createCalendar();
            }
        });
        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                generateReport();
            }
        });
        initialize();
        this.pack();
    }

    private void initialize() {
        shipmentController = new ShipmentController();
        commodityController = new CommodityController();
        driverController = new SimpleUserController();
        manufacturerController = new ManufacturerController();
        categoryController = new CategoryController();
        sectionController = new SectionController();

        commodityList = new ArrayList<>();

        setDriverList();
        setManifacturerList();
        setCategoryList();
        setSectionList();

        fillFieldsWithFakeData();

        refreshList();
    }

    private void fillFieldsWithFakeData() {
        textField1.setText("2017-01-01");
        textField2.setText("20");
        textField3.setText("Latvia");
        textField4.setText("Preke1");
        textField5.setText("Estonia");
        textField6.setText("2016-07-07");
        textField7.setText("2300");
        textField8.setText("50");
        textField9.setText("20x20x20");
        textField10.setText("Red");
        textField11.setText("");
        textField12.setText("");
    }

    private void addCommodityToList() {
        if (checkCommodityInput().equals("")) {

            int id = -1;
            String title = textField4.getText();
            String country = textField5.getText();
            String dateOfManufacture = textField6.getText();
            float value = Float.parseFloat(textField7.getText());
            int quantity = Integer.parseInt(textField8.getText());
            String size = textField9.getText();
            String color = textField10.getText();
            String purpose = textField11.getText();
            String comment = textField12.getText();

            Manufacturer manufacturer = (Manufacturer) comboBox2.getSelectedItem();
            Category category = (Category) comboBox3.getSelectedItem();
            Section section = (Section) comboBox4.getSelectedItem();

            Commodity commodity = new Commodity(id, title, country, dateOfManufacture, "", comment, value, quantity, size, color,
                    purpose, manufacturer, category, new Shipment(-1, "", -1, "", null, null),section);

            commodityList.add(commodity);

            section.decreaseCapacity(quantity);
            category.increaseCommodityCount(quantity);

            refreshList();
        } else {
            UIUtilities.showWindow(new AlertDialog("Error", checkCommodityInput()));
        }
    }

    private void registerAction() {
        if (!commodityList.isEmpty()) {
            if (checkShipmentInput().equals("")) {

                int id = shipmentController.getNextShipmentId();
                String arrivalDate = textField1.getText();
                int weight = Integer.parseInt(textField2.getText());
                String country = textField3.getText();

                SimpleUserModel driver = (SimpleUserModel) comboBox1.getSelectedItem();

                Shipment shipment = new Shipment(id, arrivalDate, weight, country, driver, commodityList);

                if (shipmentController.registerShipment(shipment)) {
                    if (commodityController.addListToDatabase(commodityList, shipment.getId())) {
                        for (int i = 0; i < comboBox4.getItemCount(); i++) {
                            Section section = (Section) comboBox4.getItemAt(i);
                            sectionController.setModel(section);
                            if (!sectionController.updateDatabaseModel()) {
                                UIUtilities.showWindow(new AlertDialog("Error", "Registration was interrupted"));
                            }
                        }
                        for (int i = 0; i < comboBox3.getItemCount(); i++) {
                            Category category = (Category) comboBox3.getItemAt(i);
                            categoryController.setModel(category);
                            if (!categoryController.updateDatabaseModel()) {
                                UIUtilities.showWindow(new AlertDialog("Error", "Registration was interrupted"));
                            }
                        }
                        UIUtilities.showWindow(new AlertDialog("Success", "Registration successful"));
                        close();
                    } else {
                        UIUtilities.showWindow(new AlertDialog("Error", "Failed to register commodities"));
                    }
                } else {
                    UIUtilities.showWindow(new AlertDialog("Error", "Failed to register shipment"));
                }
            } else {
                UIUtilities.showWindow(new AlertDialog("Error", checkShipmentInput()));
            }
        } else {
            UIUtilities.showWindow(new AlertDialog("Error", "Commodities' list is empty"));
        }
    }

    private void deleteFromList() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow != -1) {
            CommodityTableModel tableModel = (CommodityTableModel) table1.getModel();
            Commodity commodity = tableModel.getItemModel(selectedRow);
            Section section = (Section) comboBox4.getSelectedItem();
            section.increaseCapacity(commodity.getAmount());
            Category category = (Category) comboBox3.getSelectedItem();
            category.decreaseCommodityCount(commodity.getAmount());
            commodityList.remove(selectedRow);
            refreshList();
            setAddButtonAction();
        } else {
            UIUtilities.showWindow(new AlertDialog("Error", "Nothing selected"));
        }
    }

    private void editSelected() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow != -1) {
            Commodity c = commodityList.get(selectedRow);

            textField4.setText(c.getTitle());
            textField5.setText(c.getOriginCountry());
            textField6.setText(c.getCreationDate());
            textField7.setText(c.getValue() + "");
            textField8.setText(c.getAmount() + "");
            textField9.setText(c.getSize() + "");
            textField10.setText(c.getColor());
            textField11.setText(c.getPurpose());
            textField12.setText(c.getComment());

            setManufactureCB(c.getManufacturerModel().getId());
            setCategoryCB(c.getCategoryModel().getId());
            setSectionCB(c.getSectionModel().getId());

            removeAddButtonActionListeners();
            addButton.setText("Update");
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    updateCommodity(selectedRow, c);
                }
            });
        } else {
            UIUtilities.showWindow(new AlertDialog("Error", "Nothing selected"));
        }
    }

    private void updateCommodity(int listId, Commodity commodity) {
        CommodityTableModel tableModel = (CommodityTableModel) table1.getModel();
        Commodity oldCommodity = tableModel.getItemModel(listId);
        int oldQuantity = oldCommodity.getAmount();

        int quantity = Integer.parseInt(textField8.getText());

        int diff = oldQuantity - quantity;

        if (checkCommodityInputOnUpdate(diff).equals("")) {

            String title = textField4.getText();
            String country = textField5.getText();
            String dateOfManufacture = textField6.getText();
            float value = Float.parseFloat(textField7.getText());
            String size = textField9.getText();
            String color = textField10.getText();
            String purpose = textField11.getText();
            String comment = textField12.getText();

            Manufacturer manufacturer = (Manufacturer) comboBox2.getSelectedItem();
            Category category = (Category) comboBox3.getSelectedItem();
            Section section = (Section) comboBox4.getSelectedItem();

            if (diff > 0) {
                section.increaseCapacity(diff);
                category.decreaseCommodityCount(diff);
            }
            if (diff < 0) {
                section.decreaseCapacity(diff);
                category.increaseCommodityCount(diff);
            }

            Commodity c = new Commodity(commodity.getId(), title, country, dateOfManufacture, "", comment, value, quantity, size, color,
                    purpose, manufacturer, category, new Shipment(-1, "", -1, "", null, null), section);

            commodityList.set(listId, c);

            refreshList();

            setAddButtonAction();
        } else {
            UIUtilities.showWindow(new AlertDialog("Error", checkCommodityInputOnUpdate(diff)));
        }
    }

    private void removeAddButtonActionListeners() {
        for (ActionListener al : addButton.getActionListeners()) {
            addButton.removeActionListener(al);
        }
    }

    private void setAddButtonAction() {
        removeAddButtonActionListeners();
        addButton.setText("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                addCommodityToList();
            }
        });
    }

    private void setManufactureCB(int id) {
        for (int i = 0; i < comboBox2.getItemCount(); i++) {
            Manufacturer item = (Manufacturer) comboBox2.getItemAt(i);
            if (item.getId() == id) {
                comboBox2.setSelectedIndex(i);
                return;
            }
        }
    }

    private void setCategoryCB(int id) {
        for (int i = 0; i < comboBox3.getItemCount(); i++) {
            Category item = (Category) comboBox3.getItemAt(i);
            if (item.getId() == id) {
                comboBox3.setSelectedIndex(i);
                return;
            }
        }
    }

    private void setSectionCB(int id) {
        for (int i = 0; i < comboBox4.getItemCount(); i++) {
            Section item = (Section) comboBox4.getItemAt(i);
            if (item.getId() == id) {
                comboBox4.setSelectedIndex(i);
                return;
            }
        }
    }

    private String checkCommodityInput() {
        if (textField4.getText().equals("") || textField5.getText().equals("") || textField6.getText().equals("") ||
                textField7.getText().equals("") || textField8.getText().equals("")) {
            return "One or more required commodity fields are empty";
        }
        if (!Pattern.matches("^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$", textField6.getText())) {
            return "False input for manufacture date";
        }
        if (!Pattern.matches("[+-]?([0-9]*[.])?[0-9]+", textField7.getText())) {
            return "False input for value";
        }
        if (!Pattern.matches("^[1-9][0-9]*$", textField8.getText())) {
            return "False input for quantity";
        }
        Section section = (Section) comboBox4.getSelectedItem();
        if (section.getCapacity() - Integer.parseInt(textField8.getText()) < 0) {
            return "Not enough space in selected section";
        }
        if (Integer.parseInt(textField8.getText()) <= 0) {
            return "Quantity can't be zero or less";
        }
        return "";
    }

    private String checkCommodityInputOnUpdate(int sectionSizeDiff) {
        if (textField4.getText().equals("") || textField5.getText().equals("") || textField6.getText().equals("") ||
                textField7.getText().equals("") || textField8.getText().equals("")) {
            return "One or more required commodity fields are empty";
        }
        if (!Pattern.matches("^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$", textField6.getText())) {
            return "False input for manufacture date";
        }
        if (!Pattern.matches("[+-]?([0-9]*[.])?[0-9]+", textField7.getText())) {
            return "False input for value";
        }
        if (!Pattern.matches("^[1-9][0-9]*$", textField8.getText())) {
            return "False input for quantity";
        }
        Section section = (Section) comboBox4.getSelectedItem();
        if ((section.getCapacity() + sectionSizeDiff) < 0) {
            return "Not enough space in selected section";
        }
        if (Integer.parseInt(textField8.getText()) <= 0) {
            return "Quantity can't be zero or less";
        }
        return "";
    }

    private String checkShipmentInput() {
        if (textField1.getText().equals("") || textField2.getText().equals("") || textField3.getText().equals("")) {
            return "One or more shipment fields are empty";
        }
        if (!Pattern.matches("^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$", textField1.getText())) {
            return "False input for shipment date";
        }
        if (!Pattern.matches("^[1-9][0-9]*$", textField2.getText())) {
            return "False input for shipment weight";
        }
        return "";
    }

    private void clearAll() {
        for (int i = 0; i < comboBox4.getItemCount(); i++) {
            Section section = (Section) comboBox4.getItemAt(i);
            for (Commodity c : commodityList) {
                if (section.getId() == c.getSectionModel().getId()) {
                    section.increaseCapacity(c.getAmount());
                }
            }
        }

        for (int i = 0; i < comboBox3.getItemCount(); i++) {
            Category category = (Category) comboBox3.getItemAt(i);
            for (Commodity c : commodityList) {
                if (category.getId() == c.getCategoryModel().getId()) {
                    category.decreaseCommodityCount(c.getAmount());
                }
            }
        }

        setAddButtonAction();
        commodityList.clear();
        refreshList();
    }

    private void refreshList() {
        table1.setModel(new CommodityTableModel(commodityList));
    }

    private void generateReport() {
        if (!commodityList.isEmpty()) {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            DateFormat dateFormatTitle = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String filename = "shipment_" + dateFormat.format(date) + ".docx";
            try (FileOutputStream out = new FileOutputStream(new File(filename))) {
                XWPFDocument document = new XWPFDocument();

                XWPFParagraph titleParagraph = document.createParagraph();
                XWPFRun title = titleParagraph.createRun();
                title.setBold(true);
                title.setText("Shipment " + dateFormatTitle.format(date));

                String[] column_names = {"Title", "Origin country", "Manufacture date", "Value", "Amount",
                        "Manufacturer", "Category", "Section"};

                XWPFTable table = document.createTable();
                XWPFTableRow titleRow = table.getRow(0);
                titleRow.getCell(0).setText(column_names[0]);
                for (int i = 1; i < column_names.length; i++) {
                    titleRow.createCell().setText(column_names[i]);
                }

                CommodityTableModel tableModel = (CommodityTableModel) table1.getModel();
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    Commodity commodity = tableModel.getItemModel(row);
                    XWPFTableRow logRow = table.createRow();

                    int ind = 0;
                    for (int i = 0; i < tableModel.getColumnCount(); i++) {
                        if (!(i == 3 || i == 6 || i == 7 || i == 8)) {
                            logRow.getCell(ind).setText(tableModel.getValueAt(row, i).toString());
                            ind++;
                        }
                    }
                }

                document.write(out);
                UIUtilities.showWindow(new AlertDialog("Success", "Report generated as '" + filename + "'"));
            } catch (IOException e) {
                UIUtilities.showWindow(new AlertDialog("Error", "Failed to create report '" + filename + "'"));
                e.printStackTrace();
            }
        } else {
            UIUtilities.showWindow(new AlertDialog("Error", "The list is empty"));
        }
    }

    private void setDriverList() {
        ArrayList<SimpleUserModel> drivers = (ArrayList<SimpleUserModel>) driverController.getList();
        for (SimpleUserModel u : drivers) {
            comboBox1.addItem(u);
        }
    }

    private void setManifacturerList() {
        ArrayList<Manufacturer> manufacturers = (ArrayList<Manufacturer>) manufacturerController.getList();
        for (Manufacturer m : manufacturers) {
            comboBox2.addItem(m);
        }
    }

    private void setCategoryList() {
        ArrayList<Category> categories = (ArrayList<Category>) categoryController.getList();
        for (Category c : categories) {
            comboBox3.addItem(c);
        }
    }

    private void setSectionList() {
        ArrayList<Section> sections = (ArrayList<Section>) sectionController.getList();
        for (Section s : sections) {
            comboBox4.addItem(s);
        }
    }

    private void close() {
        setVisible(false);
        dispose();
    }

}
