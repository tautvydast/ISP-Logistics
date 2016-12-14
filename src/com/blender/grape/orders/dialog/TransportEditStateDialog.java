package com.blender.grape.orders.dialog;

import com.blender.grape.UIConstants;
import com.blender.grape.resources.DialogResources;
import com.blender.grape.orders.sql.TransportUtil;
import com.blender.grape.orders.structs.orderState;
import com.blender.grape.orders.structs.transport;
import com.blender.grape.users.ui.SimpleButton;

import javax.swing.*;
import java.awt.*;

/**
 * Created by sponkau on 14/12/2016.
 */
public class TransportEditStateDialog extends JDialog {
    private final JComboBox<orderState> transportStateComboBox;
    private final JComboBox<transport> transportComboBox;

    public TransportEditStateDialog(String title) {
        transportStateComboBox = new JComboBox<>();
        transportComboBox = new JComboBox<>();
        initialize(title);
    }

    private void initialize(String title) {
        setTitle(title);
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        java.util.List<orderState> allStates = TransportUtil.getAllTransportStates();
        for(orderState s : allStates)
        {
            transportStateComboBox.addItem(s);
        }
        java.util.List<transport> allTransport = TransportUtil.getAllTransport();
        for(transport s : allTransport)
        {
            transportComboBox.addItem(s);
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
        orderChoosePanel.add(new JLabel(DialogResources.getString("TRANSPORT_STATE_CHOOSE")));
        editStatePanel.add(orderChoosePanel);
        editStatePanel.add(transportComboBox);

        JPanel orderStatePanel = new JPanel(new BorderLayout());
        orderStatePanel.add(new JLabel(DialogResources.getString("TRANSPORT_STATE_CHANGE")));
        editStatePanel.add(orderStatePanel);
        editStatePanel.add(transportStateComboBox);

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
        Object stateId = transportStateComboBox.getSelectedItem();
        int State = ((orderState)stateId).getID();
        Object orderId = transportComboBox.getSelectedItem();
        int Truck = ((transport)orderId).getID();

        TransportUtil.editTransportState(Truck, State);
        setVisible(false);
        dispose();
    }

    private void cancel() {
        setVisible(false);
        dispose();
    }
}
