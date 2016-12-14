package com.blender.grape.orders.dialog;

import com.blender.grape.UIConstants;
import com.blender.grape.resources.DialogResources;
import com.blender.grape.users.ui.SimpleButton;
import com.blender.grape.users.ui.UIUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * Created by sponkau on 13/12/2016.
 */
public class ManageTransportDialog  extends JDialog {

    public ManageTransportDialog(String title) {
        initialize(title);
    }

    private void initialize(String title) {
        setTitle(title);
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

        SimpleButton createTransport = new SimpleButton("TRANSPORT_CREATE_MENU", this::createTransport);
        SimpleButton editTransportStatus = new SimpleButton("TRANSPORT_EDIT_STATUS", this::editTransportStatus);
        SimpleButton editTransport = new SimpleButton("TRANSPORT_EDIT", this::editTransport);
        SimpleButton deleteTransport = new SimpleButton("TRANSPORT_DELETE", this::deleteTransport);
        controlButtonsPanel.add(createTransport);
        controlButtonsPanel.add(editTransportStatus);
        controlButtonsPanel.add(editTransport);
        controlButtonsPanel.add(deleteTransport);

        return controlButtonsPanel;
    }

    private void createTransport() {
        TransportCreateDialog transportCreateDialog = new TransportCreateDialog(DialogResources.getString("TRANSPORT_CREATE_DIALOG"));
        UIUtilities.showWindow(transportCreateDialog);
    }

    private void editTransportStatus() {
        TransportEditStateDialog transportEditStateDialog = new TransportEditStateDialog(DialogResources.getString("TRANSPORT_STATUS_EDIT_DIALOG"));
        UIUtilities.showWindow(transportEditStateDialog);
    }

    private void editTransport() {
        TransportEditChooseDialog transportEditDialog = new TransportEditChooseDialog(DialogResources.getString("TRANSPORT_EDIT_DIALOG"));
        UIUtilities.showWindow(transportEditDialog);
    }

    private void deleteTransport() {
        TransportDeleteDialog deleteTransportDialog = new TransportDeleteDialog(DialogResources.getString("TRANSPORT_DELETE_DIALOG"));
        UIUtilities.showWindow(deleteTransportDialog);
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
