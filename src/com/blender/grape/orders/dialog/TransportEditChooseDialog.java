package com.blender.grape.orders.dialog;

import com.blender.grape.UIConstants;
import com.blender.grape.orders.sql.TransportUtil;
import com.blender.grape.orders.structs.transport;
import com.blender.grape.resources.DialogResources;
import com.blender.grape.users.ui.SimpleButton;
import com.blender.grape.users.ui.UIUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * Created by sponkau on 14/12/2016.
 */
public class TransportEditChooseDialog extends JDialog {
    private final JComboBox<transport> transportComboBox;

    public TransportEditChooseDialog(String title) {
        transportComboBox = new JComboBox<>();
        initialize(title);
    }

    private void initialize(String title) {
        setTitle(title);
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        java.util.List<transport> allTransport = TransportUtil.getAllTransportFull();
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
        statePanel.add(chooseTruckPanel(), BorderLayout.CENTER);
        statePanel.add(createButtonPanel(), BorderLayout.SOUTH);
        return statePanel;
    }

    private JPanel chooseTruckPanel() {
        JPanel chooseTruckPanel = new JPanel();
        chooseTruckPanel.setLayout(new BoxLayout(chooseTruckPanel, BoxLayout.Y_AXIS));

        JPanel truckChoosePanel = new JPanel(new BorderLayout());
        truckChoosePanel.add(new JLabel(DialogResources.getString("TRANSPORT_CHOOSE_EDIT")));
        chooseTruckPanel.add(truckChoosePanel);
        chooseTruckPanel.add(transportComboBox);

        return chooseTruckPanel;
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
        Object orderId = transportComboBox.getSelectedItem();
        transport Truck = ((transport)orderId).getTransport();
        //transport Truck = transportComboBox.getSelectedItem();

        TransportEditDialog transportEditDialog = new TransportEditDialog(
                DialogResources.getString("ORDER_CREATE_ORDER_DIALOG"),Truck);
        UIUtilities.showWindow(transportEditDialog);
        setVisible(false);
        dispose();
    }

    private void cancel() {
        setVisible(false);
        dispose();
    }
}
