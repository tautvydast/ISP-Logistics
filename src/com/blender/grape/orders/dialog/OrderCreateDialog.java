package com.blender.grape.orders.dialog;

import com.blender.grape.UIConstants;
import com.blender.grape.resources.DialogResources;
import com.blender.grape.orders.sql.OrdersUtil;
import com.blender.grape.orders.structs.*;
import com.blender.grape.users.ui.SimpleButton;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;


/**
 * Created by sponkau on 10/12/2016.
 */
public class OrderCreateDialog extends JDialog {
    private final JTextField destinationField;
    private final JTextField orderAmountField;
    private final JComboBox<orderState> orderStateComboBox;
    private final JComboBox<orderContent> orderContentComboBox;
    private final JComboBox<orderTransport> orderTransportComboBox;
    private final JDatePickerImpl exportDatePicker;
    private final JDatePickerImpl arrivalDatePicker;
    private final UtilDateModel model;
    private final UtilDateModel model1;
    private final Properties p;
    private final JDatePanelImpl exportDate;
    private final JDatePanelImpl arrivalDate;

    public OrderCreateDialog(String title) {
        destinationField = new JTextField();
        orderAmountField = new JTextField();
        orderStateComboBox = new JComboBox<>();
        orderContentComboBox = new JComboBox<>();
        orderTransportComboBox = new JComboBox<>();

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
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        java.util.List<orderState> allStates = OrdersUtil.getAllOrderStates();
        for(orderState s : allStates)
        {
            orderStateComboBox.addItem(s);
        }

        java.util.List<orderContent> allContent = OrdersUtil.getAvailableContent();
        for(orderContent s : allContent)
        {
            orderContentComboBox.addItem(s);
        }

        java.util.List<orderTransport> allTransport = OrdersUtil.getAvailableTransport();
        for(orderTransport s : allTransport)
        {
            orderTransportComboBox.addItem(s);
        }
        getContentPane().add(createContentPanel());
        pack();
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(UIConstants.BORDER);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(createOrderPanel(), BorderLayout.CENTER);
        contentPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        return contentPanel;
    }

    private JPanel createOrderPanel() {
        JPanel createOrderPanel = new JPanel();
        createOrderPanel.setLayout(new BoxLayout(createOrderPanel, BoxLayout.Y_AXIS));

        JPanel exportDatePanel = new JPanel(new BorderLayout());
        exportDatePanel.add(new JLabel(DialogResources.getString("ORDER_EXPORT_DATE")));
        createOrderPanel.add(exportDatePanel);
        createOrderPanel.add(exportDatePicker);

        JPanel arrivalDatePanel = new JPanel(new BorderLayout());
        arrivalDatePanel.add(new JLabel(DialogResources.getString("ORDER_ARRIVAL_DATE")));
        createOrderPanel.add(arrivalDatePanel);
        createOrderPanel.add(arrivalDatePicker);

        JPanel destinationPanel = new JPanel(new BorderLayout());
        destinationPanel.add(new JLabel(DialogResources.getString("ORDER_DESTINATION")));
        createOrderPanel.add(destinationPanel);
        createOrderPanel.add(destinationField);

        JPanel orderStatePanel = new JPanel(new BorderLayout());
        orderStatePanel.add(new JLabel(DialogResources.getString("ORDER_STATE")));
        createOrderPanel.add(orderStatePanel);
        createOrderPanel.add(orderStateComboBox);

        JPanel orderContentPanel = new JPanel(new BorderLayout());
        orderContentPanel.add(new JLabel(DialogResources.getString("ORDER_CONTENT")));
        createOrderPanel.add(orderContentPanel);
        createOrderPanel.add(orderContentComboBox);

        JPanel orderAmountPanel = new JPanel(new BorderLayout());
        orderAmountPanel.add(new JLabel(DialogResources.getString("ORDER_AMOUNT")));
        createOrderPanel.add(orderAmountPanel);
        createOrderPanel.add(orderAmountField);

        JPanel orderTransportPanel = new JPanel(new BorderLayout());
        orderTransportPanel.add(new JLabel(DialogResources.getString("ORDER_TRANSPORT")));
        createOrderPanel.add(orderTransportPanel);
        createOrderPanel.add(orderTransportComboBox);

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
        Object stateId = orderStateComboBox.getSelectedItem();
        int ordrState = ((orderState)stateId).getID();
        Object contentId = orderContentComboBox.getSelectedItem();
        int ordrContent = ((orderContent)contentId).getPrekes_kodas();
        Object transportId = orderTransportComboBox.getSelectedItem();
        int ordrTransport = ((orderTransport)transportId).getID();

        order Uzsakymas = new order(Integer.parseInt(orderAmountField.getText()), 4, ordrState,
                ordrContent, ordrTransport, destinationField.getText(),
                exportDatePicker.getJFormattedTextField().getText(),
                arrivalDatePicker.getJFormattedTextField().getText());

        OrdersUtil.createOrder(Uzsakymas);
        setVisible(false);
        dispose();
    }

    private void cancel() {
        setVisible(false);
        dispose();
    }

}
