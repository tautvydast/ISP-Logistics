package com.blender.grape.orders.dialog;

import com.blender.grape.UIConstants;
import com.blender.grape.orders.sql.OrdersUtil;
import com.blender.grape.orders.structs.*;
import com.blender.grape.resources.DialogResources;
import com.blender.grape.users.sql.UsersUtils;
import com.blender.grape.users.structures.user.Administrator;
import com.blender.grape.users.structures.user.GoodsReceiver;
import com.blender.grape.users.structures.user.Manager;
import com.blender.grape.users.structures.user.User;
import com.blender.grape.users.ui.SimpleButton;
import com.blender.grape.users.ui.dialogs.UserManagementDialog;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.List;
import java.util.*;

/**
 * Created by sponkau on 15/12/2016.
 */
public class OrderLogDialog extends JDialog {
    private final JDatePickerImpl exportDatePicker;
    private final JDatePickerImpl arrivalDatePicker;
    private final UtilDateModel model;
    private final UtilDateModel model1;
    private final Properties p;
    private final JDatePanelImpl exportDate;
    private final JDatePanelImpl arrivalDate;
    private JTable ordersTable;
    private OrdersTableModel ordersTableModel;

    public OrderLogDialog(String title) {
        model = new UtilDateModel();
        model1 = new UtilDateModel();
        p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        exportDate = new JDatePanelImpl(model, p);
        arrivalDate = new JDatePanelImpl(model1, p);
        exportDatePicker = new JDatePickerImpl(exportDate, new DateLabelFormatter());
        arrivalDatePicker = new JDatePickerImpl(arrivalDate, new DateLabelFormatter());
        initialize(title);
    }

    private void initialize(String title) {
        setTitle(title);
        setModal(true);
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        getContentPane().add(createContentPanel());
        pack();
    }

    private JTable createOrderTable() {
        ordersTable = new JTable();
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ordersTable.setCellSelectionEnabled(false);
        ordersTable.setRowSelectionAllowed(true);
        return ordersTable;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(UIConstants.BORDER);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(createOrderPanel(), BorderLayout.CENTER);
        contentPanel.add(new JScrollPane(createOrderTable()), BorderLayout.CENTER);
        contentPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        return contentPanel;
    }

    private JPanel createOrderPanel() {
        JPanel createOrderPanel = new JPanel();
        createOrderPanel.setLayout(new BoxLayout(createOrderPanel, BoxLayout.Y_AXIS));

        JPanel exportDatePanel = new JPanel(new BorderLayout());
        exportDatePanel.add(new JLabel(DialogResources.getString("ORDER_DATE_FROM")));
        createOrderPanel.add(exportDatePanel);
        createOrderPanel.add(exportDatePicker);

        JPanel arrivalDatePanel = new JPanel(new BorderLayout());
        arrivalDatePanel.add(new JLabel(DialogResources.getString("ORDER_DATE_TO")));
        createOrderPanel.add(arrivalDatePanel);
        createOrderPanel.add(arrivalDatePicker);

        return createOrderPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel eastButtonPanel = new JPanel(new BorderLayout());
        eastButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        eastButtonPanel.add(new SimpleButton("OK", this::saveOrder), BorderLayout.WEST);
        eastButtonPanel.add(new SimpleButton("CANCEL", this::cancel), BorderLayout.EAST);
        buttonPanel.add(eastButtonPanel, BorderLayout.EAST);
        return buttonPanel;
    }

    private void saveOrder() {

        fillWithOrders(exportDatePicker.getJFormattedTextField().getText(), arrivalDatePicker.getJFormattedTextField().getText());
    }

    private void cancel() {
        setVisible(false);
        dispose();
    }

    private void fillWithOrders(String from, String to) {
        java.util.List<order> orders = OrdersUtil.getOrdersLog(from, to);
        ordersTableModel = new OrdersTableModel(orders);
        ordersTable.setModel(ordersTableModel);
    }

    private class OrdersTableModel extends AbstractTableModel {
        private static final int ORDER_NUMBER = 0;
        private static final int ORDER_CONTENT_LOG = 1;
        private static final int ORDER_EXPORT_DATE_LOG = 2;
        private static final int ORDER_ARRIVAL_DATE_LOG = 3;
        private static final int ORDER_DESTINATION_LOG = 4;
        private static final int ORDER_MANAGER_LOG = 5;
        private static final int ORDER_STATE_LOG = 6;
        private static final int ORDER_TRANSPORT_LOG = 7;
        private final String[] column_names = {"ORDER_NUMBER", "ORDER_CONTENT_LOG", "ORDER_EXPORT_DATE_LOG",
                "ORDER_ARRIVAL_DATE_LOG", "ORDER_DESTINATION_LOG", "ORDER_MANAGER_LOG", "ORDER_STATE_LOG",
                "ORDER_TRANSPORT_LOG"};
        private final java.util.List<order> orders;

        public OrdersTableModel(java.util.List<order> orders) {
            this.orders = orders;
        }

        @Override
        public int getRowCount() {
            return orders.size();
        }


        @Override
        public int getColumnCount() {
            return column_names.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            order Order = orders.get(rowIndex);
            switch (columnIndex) {
                case ORDER_NUMBER:
                    return " " + Order.getNumber();
                case ORDER_CONTENT_LOG:
                    return " " + Order.getContent();
                case ORDER_EXPORT_DATE_LOG:
                    return " " + Order.getExportDate();
                case ORDER_ARRIVAL_DATE_LOG:
                    return " " + Order.getArrivalDate();
                case ORDER_DESTINATION_LOG:
                    return " " + Order.getDestination();
                case ORDER_MANAGER_LOG:
                    return " " + Order.getManager();
                case ORDER_STATE_LOG:
                    return " " + Order.getState();
                case ORDER_TRANSPORT_LOG:
                    return " " + Order.getTransport();
            }
            return "";
        }

        @Override
        public String getColumnName(int column) {
            return DialogResources.getString(column_names[column]);
        }

    }
}
