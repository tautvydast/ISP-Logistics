package com.blender.grape.orders.dialog;

import com.blender.grape.UIConstants;
import com.blender.grape.orders.sql.TransportUtil;
import com.blender.grape.orders.structs.orderState;
import com.blender.grape.orders.structs.transport;
import com.blender.grape.resources.DialogResources;
import com.blender.grape.users.ui.SimpleButton;

import javax.swing.*;
import java.awt.*;

/**
 * Created by sponkau on 14/12/2016.
 */
public class TransportDeleteDialog extends JDialog {
    private final JComboBox<transport> transportComboBox;

    public TransportDeleteDialog(String title) {
        transportComboBox = new JComboBox<>();
        initialize(title);
    }

    private void initialize(String title) {
        setTitle(title);
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        java.util.List<transport> allTransport = TransportUtil.getAllTransportNotOnOrder();
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
        statePanel.add(deleteTruckPanel(), BorderLayout.CENTER);
        statePanel.add(createButtonPanel(), BorderLayout.SOUTH);
        return statePanel;
    }

    private JPanel deleteTruckPanel() {
        JPanel deleteTruckPanel = new JPanel();
        deleteTruckPanel.setLayout(new BoxLayout(deleteTruckPanel, BoxLayout.Y_AXIS));

        JPanel truckChoosePanel = new JPanel(new BorderLayout());
        truckChoosePanel.add(new JLabel(DialogResources.getString("TRANSPORT_CHOOSE_DELETE")));
        deleteTruckPanel.add(truckChoosePanel);
        deleteTruckPanel.add(transportComboBox);

        return deleteTruckPanel;
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
        int Truck = ((transport)orderId).getID();

        TransportUtil.deleteTransport(Truck);
        setVisible(false);
        dispose();
    }

    private void cancel() {
        setVisible(false);
        dispose();
    }
}
