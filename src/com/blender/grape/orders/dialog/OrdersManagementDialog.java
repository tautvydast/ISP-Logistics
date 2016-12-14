package com.blender.grape.orders.dialog;

import javax.swing.*;

import com.blender.grape.UIConstants;
import com.blender.grape.resources.DialogResources;
import com.blender.grape.users.ui.SimpleButton;
import com.blender.grape.users.ui.UIUtilities;

import java.awt.*;

/**
 * Created by sponkau on 10/12/2016.
 */
public class OrdersManagementDialog extends JDialog {

    public OrdersManagementDialog() {
        initialize();
    }

    private void initialize() {
        setTitle(DialogResources.getString("ORDER_MANAGEMENT"));
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(createContentPanel());
        pack();
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setPreferredSize(new Dimension(200, 200));
        contentPanel.setBorder(UIConstants.BORDER);
        contentPanel.add(createControlButtonsPanel(), BorderLayout.NORTH);
        contentPanel.add(createCloseButtonPanel(), BorderLayout.SOUTH);
        return contentPanel;
    }

    private JPanel createControlButtonsPanel() {
        JPanel controlButtonsPanel = new JPanel(new BorderLayout());
        controlButtonsPanel.setLayout(new BoxLayout(controlButtonsPanel, BoxLayout.Y_AXIS));

        SimpleButton createOrder = new SimpleButton("ORDER_CREATE_ORDER_MENU", this::createOrder);
        SimpleButton editOrderStatus = new SimpleButton("ORDER_EDIT_ORDER_STATUS", this::editOrderStatus);
        SimpleButton orderLog = new SimpleButton("ORDER_GET_LOG", this::orderLog);
        SimpleButton manageTransport = new SimpleButton("ORDER_MANAGE_TRANSPORT", this::manageTransport);
        controlButtonsPanel.add(createOrder);
        controlButtonsPanel.add(editOrderStatus);
        controlButtonsPanel.add(orderLog);
        controlButtonsPanel.add(manageTransport);

        return controlButtonsPanel;
    }

    private void createOrder() {
        OrderCreateDialog orderCreateDialog = new OrderCreateDialog(DialogResources.getString("ORDER_CREATE_ORDER_DIALOG"));
        UIUtilities.showWindow(orderCreateDialog);
    }

    private void editOrderStatus() {
        OrderEditStateDialog orderEditStateDialog = new OrderEditStateDialog(DialogResources.getString("ORDER_EDIT_ORDER_STATUS_DIALOG"));
        UIUtilities.showWindow(orderEditStateDialog);
    }

    private void orderLog() {
        OrderCreateDialog orderCreateDialog = new OrderCreateDialog(DialogResources.getString("ORDER_CREATE_ORDER_DIALOG"));
        UIUtilities.showWindow(orderCreateDialog);
    }

    private void manageTransport() {
        ManageTransportDialog manageTransportDialog = new ManageTransportDialog(DialogResources.getString("ORDER_CREATE_ORDER_DIALOG"));
        UIUtilities.showWindow(manageTransportDialog);
    }


    private JPanel createCloseButtonPanel() {
        JPanel closeButtonPanel = new JPanel(new BorderLayout());
        closeButtonPanel.add(new SimpleButton("CLOSE", this::close));
        return closeButtonPanel;
    }

    private void close() {
        setVisible(false);
        dispose();
    }

}
