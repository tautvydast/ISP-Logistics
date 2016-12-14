package com.blender.grape.storage.misc;

import com.blender.grape.users.ui.UIUtilities;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Properties;

/**
 * Created by Tautvydas Traška, IFF-4/2.
 */
public class DatePicker extends JDialog {
    private JTextField parentTextField;

    public DatePicker(JTextField parentTextField) {
        setLocationRelativeTo(parentTextField);
        this.parentTextField = parentTextField;
        setModal(true);
    }

    public void createCalendar() {

        setTitle("Calendar");
        setLayout(new FlowLayout());

        SqlDateModel dateModel = new SqlDateModel();

        Integer year = Calendar.getInstance().get(Calendar.YEAR),
                month = Calendar.getInstance().get(Calendar.MONTH),
                day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        dateModel.setDate(year, month, day);
        dateModel.setSelected(true);

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);

        JDatePickerImpl datePick = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        JButton buttonOK = new JButton("OK");
        buttonOK.addActionListener((ActionEvent e) -> {
            parentTextField.setText(datePick.getModel().getValue().toString());
            setVisible(false);
            remove(datePanel);
            remove(buttonOK);
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                remove(datePanel);
                remove(buttonOK);
                setEnabled(true);
            }
        });

        add(datePanel);
        add(buttonOK);

        pack();
        setVisible(true);
    }

}
