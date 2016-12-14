package com.blender.grape.storage.dialog;

import com.blender.grape.resources.DialogResources;
import com.blender.grape.storage.controller.*;
import com.blender.grape.storage.model.*;
import com.blender.grape.storage.table.model.CommodityTableModel;
import com.blender.grape.users.structures.user.User;
import com.blender.grape.users.ui.UIUtilities;
import org.apache.poi.xwpf.usermodel.*;
import sun.rmi.transport.proxy.CGIHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ComListDialog extends JDialog {
    private JPanel contentPane;
    private JButton closeButton;
    private JButton writeOffButton;
    private JButton setForExportButton;
    private JComboBox comboBox1;
    private JTable table1;
    private JTextField textField1;
    private JButton goButton;
    private JButton clearButton;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JButton generateReportButton;
    private JButton buttonOK;

    private List<Commodity> commodityList;

    enum View {ALL, IN_WAREHOUSE, WRITTEN_OFF, READY_FOR_EXPORT};
    enum ViewCategory {ALL, BY_SECTION, BY_CATEGORY, BY_MANUFACTURER, BY_SHIPMENT};
    View currentView;
    ViewCategory currentViewCategory;

    int stateValue;
    int type;
    int typeId;

    private ShipmentController shipmentController;
    private CommodityController commodityController;
    private SimpleUserController driverController;
    private ManufacturerController manufacturerController;
    private CategoryController categoryController;
    private SectionController sectionController;

    public ComListDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Commodities");

        initialize();

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                close();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                clear();
            }
        });
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                search();
            }
        });
        writeOffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                changeStatusDialog(0);
            }
        });
        setForExportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                changeStatusDialog(1);
            }
        });
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                switch (comboBox1.getSelectedIndex()) {
                    case 0:
                        currentView = View.ALL;
                        break;
                    case 1:
                        currentView = View.IN_WAREHOUSE;
                        break;
                    case 2:
                        currentView = View.WRITTEN_OFF;
                        break;
                    case 3:
                        currentView = View.READY_FOR_EXPORT;
                        break;
                }
                setTableContents();
            }
        });
        comboBox2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                switch (comboBox2.getSelectedIndex()) {
                    case 0:
                        currentViewCategory = ViewCategory.ALL;
                        break;
                    case 1:
                        currentViewCategory = ViewCategory.BY_SECTION;
                        break;
                    case 2:
                        currentViewCategory = ViewCategory.BY_CATEGORY;
                        break;
                    case 3:
                        currentViewCategory = ViewCategory.BY_MANUFACTURER;
                        break;
                    case 4:
                        currentViewCategory = ViewCategory.BY_SHIPMENT;
                        break;
                }
                initCB3();
                setTableContents();
            }
        });
        comboBox3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (comboBox3.getItemCount() > 1) {
                    setTableContents();
                }
            }
        });
        textField1.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    search();
                }
            }
        });
        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                generateReport();
            }
        });
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

        currentView = View.ALL;
        currentViewCategory = ViewCategory.ALL;

        initCB1();
        initCB2();
        initCB3();

        setTableContents();
    }

    private void initCB1() {
        comboBox1.addItem("-");
        comboBox1.addItem("In warehouse");
        comboBox1.addItem("Written-off");
        comboBox1.addItem("Ready for export");
    }

    private void initCB2() {
        comboBox2.addItem("-");
        comboBox2.addItem("Section");
        comboBox2.addItem("Category");
        comboBox2.addItem("Manufacturer");
        comboBox2.addItem("Shipment");
    }

    private void initCB3() {
        switch (currentViewCategory) {
            case ALL:
                comboBox3.removeAllItems();
                comboBox3.addItem("-");
                break;
            case BY_SECTION:
                comboBox3.removeAllItems();
                comboBox3.addItem("-");
                for (Section s : sectionController.getList()) {
                    comboBox3.addItem(s);
                }
                break;
            case BY_CATEGORY:
                comboBox3.removeAllItems();
                comboBox3.addItem("-");
                for (Category c : categoryController.getList()) {
                    comboBox3.addItem(c);
                }
                break;
            case BY_MANUFACTURER:
                comboBox3.removeAllItems();
                comboBox3.addItem("-");
                for (Manufacturer m : manufacturerController.getList()) {
                    comboBox3.addItem(m);
                }
                break;
            case BY_SHIPMENT:
                comboBox3.removeAllItems();
                comboBox3.addItem("-");
                for (Shipment s : shipmentController.getList()) {
                    comboBox3.addItem(s);
                }
                break;
        }
    }

    public void setTableContents() {
        identifyFilters();
        commodityList = commodityController.getList(stateValue, type, typeId, "");
        refreshList();
    }

    private void identifyFilters() {
        stateValue = -1;
        type = -1;
        typeId = -1;

        switch (currentView) {
            case IN_WAREHOUSE:
                stateValue = 0;
                break;
            case WRITTEN_OFF:
                stateValue = 1;
                break;
            case READY_FOR_EXPORT:
                stateValue = 2;
        }

        switch (currentViewCategory) {
            case BY_MANUFACTURER:
                type = 0;
                if (comboBox3.getSelectedIndex() != 0) {
                    Manufacturer manufacturer = (Manufacturer) comboBox3.getSelectedItem();
                    typeId = manufacturer.getId();
                }
                break;
            case BY_CATEGORY:
                type = 1;
                if (comboBox3.getSelectedIndex() != 0) {
                    Category category = (Category) comboBox3.getSelectedItem();
                    typeId = category.getId();
                }
                break;
            case BY_SHIPMENT:
                type = 2;
                if (comboBox3.getSelectedIndex() != 0) {
                    Shipment shipment = (Shipment) comboBox3.getSelectedItem();
                    typeId = shipment.getId();
                }
                break;
            case BY_SECTION:
                type = 3;
                if (comboBox3.getSelectedIndex() != 0) {
                    Section section = (Section) comboBox3.getSelectedItem();
                    typeId = section.getId();
                }
                break;
        }
    }

    private void search() {
        commodityList = commodityController.getList(stateValue, type, typeId, textField1.getText());
        refreshList();
    }

    private void changeStatusDialog(int type) {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow != -1) {
            CommodityTableModel tableModel = (CommodityTableModel) table1.getModel();
            Commodity commodity = tableModel.getItemModel(selectedRow);

            if (commodity.getState().equals(1+"") && type == 0) {
                UIUtilities.showWindow(new AlertDialog("Error", "Already written-off"));
                return;
            }

            if (commodity.getState().equals(2+"") && type == 1) {
                UIUtilities.showWindow(new AlertDialog("Error", "Already set for export"));
                return;
            }

            if (commodity.getState().equals(1+"") && type == 1) {
                UIUtilities.showWindow(new AlertDialog("Error", "Can't export this!"));
                return;
            }

            ChangeStatusDialog changeStatusDialog = new ChangeStatusDialog(type, commodity.getId(), this);
            UIUtilities.showWindow(changeStatusDialog);
        } else {
            UIUtilities.showWindow(new AlertDialog("Error", "Nothing selected"));
        }
    }

    private void refreshList() {
        table1.setModel(new CommodityTableModel(commodityList));
    }

    private void generateReport() {
        if (!commodityList.isEmpty()) {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            DateFormat dateFormatTitle = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String filename = "commodities_" + dateFormat.format(date) + ".docx";
            try (FileOutputStream out = new FileOutputStream(new File(filename))) {
                XWPFDocument document = new XWPFDocument();

                XWPFParagraph titleParagraph = document.createParagraph();
                XWPFRun title = titleParagraph.createRun();
                title.setBold(true);
                title.setText("Commodities " + dateFormatTitle.format(date));

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
            UIUtilities.showWindow(new AlertDialog("Error", "Commodities' list is empty"));
        }
    }

    private void clear() {
        stateValue = -1;
        type = -1;
        typeId = -1;
        comboBox1.setSelectedIndex(0);
        comboBox2.setSelectedIndex(0);
        textField1.setText("");
        initCB3();
        commodityList = commodityController.getList(stateValue, type, typeId, "");
        refreshList();
    }

    private void close() {
        setVisible(false);
        dispose();
    }
}
