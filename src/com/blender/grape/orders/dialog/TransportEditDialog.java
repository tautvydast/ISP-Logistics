package com.blender.grape.orders.dialog;

import com.blender.grape.UIConstants;
import com.blender.grape.orders.sql.TransportUtil;
import com.blender.grape.orders.structs.DateLabelFormatter;
import com.blender.grape.orders.structs.orderState;
import com.blender.grape.orders.structs.transport;
import com.blender.grape.resources.DialogResources;
import com.blender.grape.users.ui.SimpleButton;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Created by sponkau on 14/12/2016.
 */
public class TransportEditDialog extends JDialog {
    private final JTextField idField;
    private final JTextField markeField;
    private final JTextField modelisField;
    private final JTextField talpaField;
    private final JTextField galiaField;
    private final JTextField bakasField;
    private final JTextField gedimasField;
    private final JTextField spalvaField;
    private final JTextField sanaudosField;
    private final JComboBox<orderState> transportStateComboBox;
    private final JDatePickerImpl technikineDatePicker;
    private final UtilDateModel model;
    private final Properties p;
    private final JDatePanelImpl technikineDate;

    public TransportEditDialog(String title,transport Car) {
        idField = new JTextField();
        idField.setVisible(false);
        markeField = new JTextField();
        modelisField = new JTextField();
        talpaField = new JTextField();
        galiaField = new JTextField();
        bakasField = new JTextField();
        gedimasField = new JTextField();
        spalvaField = new JTextField();
        sanaudosField = new JTextField();
        transportStateComboBox = new JComboBox<>();

        model = new UtilDateModel();
        p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        model.setDate(2015,10,12);
        technikineDate = new JDatePanelImpl(model, p);
        technikineDatePicker = new JDatePickerImpl(technikineDate, new DateLabelFormatter());
        initialize(title,Car);
    }

    private void initialize(String title, transport Car) {
        setTitle(title);
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        idField.setText(Integer.toString(Car.getID()));
        markeField.setText(Car.getMarke());
        modelisField.setText(Car.getModelis());
        talpaField.setText(Car.getTalpa());
        galiaField.setText(Car.getGalia());
        bakasField.setText(Car.getBakas());
        gedimasField.setText(Car.getGedimas());
        spalvaField.setText(Car.getSpalva());
        sanaudosField.setText(Car.getSanaudos());
        transportStateComboBox.setSelectedItem(Car.getBusenaStr());

        java.util.List<orderState> allStates = TransportUtil.getAllTransportStates();
        for(orderState s : allStates)
        {
            transportStateComboBox.addItem(s);
        }

        getContentPane().add(createContentPanel());
        pack();
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(UIConstants.BORDER);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(addTransportPanel(), BorderLayout.CENTER);
        contentPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        return contentPanel;
    }

    private JPanel addTransportPanel() {
        JPanel addTransportPanel = new JPanel();
        addTransportPanel.setLayout(new BoxLayout(addTransportPanel, BoxLayout.Y_AXIS));

        JPanel markePanel = new JPanel(new BorderLayout());
        markePanel.add(new JLabel(DialogResources.getString("TRANSPORT_MAKE")));
        addTransportPanel.add(markePanel);
        addTransportPanel.add(markeField);

        JPanel modelisPanel = new JPanel(new BorderLayout());
        modelisPanel.add(new JLabel(DialogResources.getString("TRANSPORT_MODEL")));
        addTransportPanel.add(modelisPanel);
        addTransportPanel.add(modelisField);

        JPanel talpaPanel = new JPanel(new BorderLayout());
        talpaPanel.add(new JLabel(DialogResources.getString("TRANSPORT_TALPA")));
        addTransportPanel.add(talpaPanel);
        addTransportPanel.add(talpaField);

        JPanel galiaPanel = new JPanel(new BorderLayout());
        galiaPanel.add(new JLabel(DialogResources.getString("TRANSPORT_GALIA")));
        addTransportPanel.add(galiaPanel);
        addTransportPanel.add(galiaField);

        JPanel bakasPanel = new JPanel(new BorderLayout());
        bakasPanel.add(new JLabel(DialogResources.getString("TRANSPORT_BAKAS")));
        addTransportPanel.add(bakasPanel);
        addTransportPanel.add(bakasField);

        JPanel gedimasPanel = new JPanel(new BorderLayout());
        gedimasPanel.add(new JLabel(DialogResources.getString("TRANSPORT_GEDIMAS")));
        addTransportPanel.add(gedimasPanel);
        addTransportPanel.add(gedimasField);

        JPanel spalvaPanel = new JPanel(new BorderLayout());
        spalvaPanel.add(new JLabel(DialogResources.getString("TRANSPORT_SPALVA")));
        addTransportPanel.add(spalvaPanel);
        addTransportPanel.add(spalvaField);

        JPanel sanaudosPanel = new JPanel(new BorderLayout());
        sanaudosPanel.add(new JLabel(DialogResources.getString("TRANSPORT_SANAUDOS")));
        addTransportPanel.add(sanaudosPanel);
        addTransportPanel.add(sanaudosField);

        JPanel technikinePanel = new JPanel(new BorderLayout());
        technikinePanel.add(new JLabel(DialogResources.getString("TRANSPORT_TA")));
        addTransportPanel.add(technikinePanel);
        addTransportPanel.add(technikineDatePicker);

        JPanel orderStatePanel = new JPanel(new BorderLayout());
        orderStatePanel.add(new JLabel(DialogResources.getString("TRANSPORT_STATE")));
        addTransportPanel.add(orderStatePanel);
        addTransportPanel.add(transportStateComboBox);

        return addTransportPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel eastButtonPanel = new JPanel(new BorderLayout());
        eastButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        eastButtonPanel.add(new SimpleButton("OK", this::saveTransport), BorderLayout.WEST);
        eastButtonPanel.add(new SimpleButton("CANCEL", this::cancel), BorderLayout.EAST);
        buttonPanel.add(eastButtonPanel, BorderLayout.EAST);
        return buttonPanel;
    }

    private void saveTransport() {
        Object stateId = transportStateComboBox.getSelectedItem();
        int ordrState = ((orderState)stateId).getID();

        transport Car = new transport(Integer.parseInt(idField.getText()), markeField.getText(),modelisField.getText(),talpaField.getText(),
                galiaField.getText(),bakasField.getText(),gedimasField.getText(),spalvaField.getText(),
                sanaudosField.getText(),technikineDatePicker.getJFormattedTextField().getText(),ordrState,"");

        TransportUtil.editTransport(Car);
        setVisible(false);
        dispose();
    }

    private void cancel() {
        setVisible(false);
        dispose();
    }
}
