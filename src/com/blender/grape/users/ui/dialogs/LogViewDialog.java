package com.blender.grape.users.ui.dialogs;

import com.blender.grape.UIConstants;
import com.blender.grape.resources.DialogResources;
import com.blender.grape.users.sql.LogUtil;
import com.blender.grape.users.structures.LogEntry;
import com.blender.grape.users.ui.SimpleButton;
import org.apache.poi.xwpf.usermodel.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
public class LogViewDialog extends JDialog {
    private static final String REPORT_FILE = "Log Overview.docx";
    private static final int VISIBLE_ROW_COUNT = 20;
    private static final int WIDTH = 500;
    private final JList<LogEntry> logList;

    public LogViewDialog() throws HeadlessException {
        logList = new JList<>();
        initialize();
    }

    private void initialize() {
        setTitle(DialogResources.getString("LOG_OVERVIEW_TITLE"));
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().add(createContentPanel());
        pack();
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(UIConstants.BORDER);
        contentPanel.add(new JScrollPane(createLogList()), BorderLayout.CENTER);
        contentPanel.add(createReportPanel(), BorderLayout.EAST);
        contentPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        return contentPanel;
    }

    private JList createLogList() {
        logList.setEnabled(false);
        logList.setFixedCellWidth(WIDTH);
        logList.setVisibleRowCount(VISIBLE_ROW_COUNT);
        logList.setModel(new MyModel());
        return logList;
    }

    private JPanel createReportPanel() {
        JPanel reportPanel = new JPanel(new BorderLayout());
        reportPanel.add(new SimpleButton("GENERATE_REPORT", this::report));
        return reportPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(new SimpleButton("CLOSE", this::close));
        return buttonPanel;
    }

    private void report() {
        try (FileOutputStream out = new FileOutputStream(new File(REPORT_FILE))) {
            XWPFDocument document = new XWPFDocument();

            XWPFParagraph titleParagraph = document.createParagraph();
            XWPFRun title = titleParagraph.createRun();
            title.setBold(true);
            title.setText(DialogResources.getString("LOG_OVERVIEW_TITLE"));

            XWPFTable table = document.createTable();
            XWPFTableRow titleRow = table.getRow(0);
            titleRow.getCell(0).setText(DialogResources.getString("LOG_REPORT_DATE"));
            titleRow.createCell().setText(DialogResources.getString("LOG_REPORT_USER"));
            titleRow.createCell().setText(DialogResources.getString("LOG_REPORT_PERMISSION"));

            for (int i = 0; i < logList.getVisibleRowCount(); i++) {
                LogEntry logEntry = logList.getModel().getElementAt(i);
                XWPFTableRow logRow = table.createRow();
                logRow.getCell(0).setText(logEntry.getDateTime().toString());
                logRow.getCell(1).setText(logEntry.getUser().getLogin());
                logRow.getCell(2).setText(logEntry.getPermission().toString());
            }

            document.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        setVisible(false);
        dispose();
    }

    private class MyModel extends AbstractListModel<LogEntry> {
        private final java.util.List<LogEntry> logEntries;

        MyModel() {
            logEntries = LogUtil.getLog();
        }

        @Override
        public int getSize() {
            return logEntries.size();
        }

        @Override
        public LogEntry getElementAt(int index) {
            return logEntries.get(index);
        }
    }
}
