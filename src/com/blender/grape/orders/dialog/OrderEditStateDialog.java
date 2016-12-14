package com.blender.grape.orders.dialog;

import com.blender.grape.UIConstants;
import com.blender.grape.resources.DialogResources;
import com.blender.grape.orders.sql.OrdersUtil;
import com.blender.grape.orders.structs.*;
import com.blender.grape.users.ui.SimpleButton;

import javax.swing.*;
import java.awt.*;

/**
 * Created by sponkau on 13/12/2016.
 */
public class OrderEditStateDialog extends JDialog {
    private final JComboBox<orderState> orderStateComboBox;
    private final JComboBox<order> orderComboBox;

    public OrderEditStateDialog(String title) {
        orderStateComboBox = new JComboBox<>();
        orderComboBox = new JComboBox<>();
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
        java.util.List<order> allOrders = OrdersUtil.getAllOrders();
        for(order s : allOrders)
        {
            orderComboBox.addItem(s);
        }
        getContentPane().add(editPanel());
        pack();
    }

    private JPanel editPanel() {
        JPanel statePanel = new JPanel();
        statePanel.setBorder(UIConstants.BORDER);
        statePanel.setLayout(new BoxLayout(statePanel, BoxLayout.Y_AXIS));
        statePanel.add(editStatePanel(), BorderLayout.CENTER);
        statePanel.add(createButtonPanel(), BorderLayout.SOUTH);
        return statePanel;
    }

    private JPanel editStatePanel() {
        JPanel editStatePanel = new JPanel();
        editStatePanel.setLayout(new BoxLayout(editStatePanel, BoxLayout.Y_AXIS));

        JPanel orderChoosePanel = new JPanel(new BorderLayout());
        orderChoosePanel.add(new JLabel(DialogResources.getString("ORDER_STATE_CHOOSE")));
        editStatePanel.add(orderChoosePanel);
        editStatePanel.add(orderComboBox);

        JPanel orderStatePanel = new JPanel(new BorderLayout());
        orderStatePanel.add(new JLabel(DialogResources.getString("ORDER_STATE_CHANGE")));
        editStatePanel.add(orderStatePanel);
        editStatePanel.add(orderStateComboBox);

        return editStatePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel eastButtonPanel = new JPanel(new BorderLayout());
        eastButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        eastButtonPanel.add(new SimpleButton("OK", this::saveState), BorderLayout.WEST);
        eastButtonPanel.add(new SimpleButton("CANCEL", this::cancel), BorderLayout.EAST);
        buttonPanel.add(eastButtonPanel, BorderLayout.EAST);
        return buttonPanel;
    }

    private void saveState() {
        Object stateId = orderStateComboBox.getSelectedItem();
        int ordrState = ((orderState)stateId).getID();
        Object orderId = orderComboBox.getSelectedItem();
        int currentState = ((order)orderId).getBusena();


        OrdersUtil.editOrderState(currentState, ordrState);
        setVisible(false);
        dispose();
    }

    private void cancel() {
        setVisible(false);
        dispose();
    }
}
