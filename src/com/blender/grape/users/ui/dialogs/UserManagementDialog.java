package com.blender.grape.users.ui.dialogs;

import com.blender.grape.UIConstants;
import com.blender.grape.resources.DialogResources;
import com.blender.grape.resources.Icons;
import com.blender.grape.users.PermissionService;
import com.blender.grape.users.structures.LogEntry;
import com.blender.grape.users.ui.SimpleButton;
import com.blender.grape.users.ui.UIUtilities;
import com.blender.grape.users.sql.UsersUtils;
import com.blender.grape.users.structures.Permission;
import com.blender.grape.users.structures.user.Administrator;
import com.blender.grape.users.structures.user.GoodsReceiver;
import com.blender.grape.users.structures.user.Manager;
import com.blender.grape.users.structures.user.User;
import com.blender.grape.utils.DestinationFolderChooser;
import org.apache.poi.xwpf.usermodel.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
public class UserManagementDialog extends JDialog {
    private static final String REPORT_FILE = "Users Overview.docx";
    private JTable usersTable;
    private UsersTableModel usersTableModel;

    public UserManagementDialog() {
        initialize();
    }

    private void initialize() {
        setModal(true);
        setResizable(false);
        setTitle(DialogResources.getString("USER_MANAGEMENT_TITLE"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().add(createContentPanel());
        pack();
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(UIConstants.BORDER);
        contentPanel.add(createButtonPanel(), BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(createUserTable()), BorderLayout.CENTER);
        contentPanel.add(createReportPanel(), BorderLayout.EAST);
        contentPanel.add(createCloseButtonPanel(), BorderLayout.SOUTH);
        return contentPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        SimpleButton addUser = new SimpleButton("USER_MANAGEMENT_ADD_USER", this::addUser, Icons.ADD_USER);
        addUser.setEnabled(PermissionService.hasRightTo(Permission.CREATE_USERS));
        buttonPanel.add(addUser);

        SimpleButton editUser = new SimpleButton("USER_MANAGEMENT_EDIT_USER", this::editUser, Icons.EDIT_USER);
        editUser.setEnabled(PermissionService.hasRightTo(Permission.EDIT_USERS));
        buttonPanel.add(editUser);

        SimpleButton deleteUser = new SimpleButton("USER_MANAGEMENT_DELETE_USER", this::deleteUser, Icons.REMOVE_USER);
        deleteUser.setEnabled(PermissionService.hasRightTo(Permission.REMOVE_USERS));
        buttonPanel.add(deleteUser);

        SimpleButton editPermissions = new SimpleButton("USER_MANAGEMENT_EDIT_PERMISSIONS", this::editPermissions, Icons.EDIT_USER_PERMISSIONS);
        editPermissions.setEnabled(PermissionService.hasRightTo(Permission.EDIT_USER_PERMISSIONS));
        buttonPanel.add(editPermissions);

        return buttonPanel;
    }

    private JTable createUserTable() {
        usersTable = new JTable();
        fillWithUsers();
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        usersTable.setCellSelectionEnabled(false);
        usersTable.setRowSelectionAllowed(true);
        return usersTable;
    }

    private JPanel createReportPanel() {
        JPanel reportPanel = new JPanel(new BorderLayout());
        reportPanel.add(new SimpleButton("GENERATE_REPORT", this::report));
        return reportPanel;
    }

    private JPanel createCloseButtonPanel() {
        JPanel bottomButtonPanel = new JPanel(new BorderLayout());
        bottomButtonPanel.add(new SimpleButton("CLOSE", this::close));
        return bottomButtonPanel;
    }

    private void addUser() {
        UserEditDialog addUserDialog = new UserEditDialog(DialogResources.getString("EDIT_USER_ADD_TITLE"));
        addUserDialog.setOkAction(this::fillWithUsers);
        UIUtilities.showWindow(addUserDialog);
    }

    private void editUser() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < usersTable.getRowCount()) {
            User user = usersTableModel.getUser(selectedRow);
            UserEditDialog userEditDialog = new UserEditDialog(DialogResources.getString("EDIT_USER_EDIT_TITLE"), user);
            userEditDialog.setOkAction(this::fillWithUsers);
            UIUtilities.showWindow(userEditDialog);
        }
    }

    private void deleteUser() {
        SwingUtilities.invokeLater(() -> {
            int selectedRow = usersTable.getSelectedRow();
            String message = DialogResources.getString("ROLE_MANAGEMENT_DELETE_USER_CONFIRM");
            if (selectedRow >= 0 && selectedRow < usersTable.getRowCount()) {
                if (JOptionPane.showConfirmDialog(this, message, "", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    User user = usersTableModel.getUser(selectedRow);
                    UsersUtils.removeUser(user);
                    usersTableModel.removeRow(selectedRow);
                    usersTable.updateUI();
                }
            }
        });
    }

    private void editPermissions() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < usersTable.getRowCount()) {
            User user = usersTableModel.getUser(selectedRow);
            EditPermissionsDialog editPermissionsDialog = new EditPermissionsDialog(user);
            editPermissionsDialog.setOKAction(this::fillWithUsers);
            UIUtilities.showWindow(editPermissionsDialog);
        }
    }

    private void report() {
        try (FileOutputStream out = new FileOutputStream(new File(REPORT_FILE))) {
            XWPFDocument document = new XWPFDocument();

            XWPFParagraph titleParagraph = document.createParagraph();
            XWPFRun title = titleParagraph.createRun();
            title.setBold(true);
            title.setText(DialogResources.getString("USER_REPORT_TITLE"));

            XWPFTable table = document.createTable();
            XWPFTableRow titleRow = table.getRow(0);
            titleRow.getCell(0).setText(DialogResources.getString("USER_MANAGEMENT_LOGIN"));
            titleRow.createCell().setText(DialogResources.getString("USER_MANAGEMENT_USER_NAME"));
            titleRow.createCell().setText(DialogResources.getString("USER_MANAGEMENT_USER_LAST_NAME"));
            titleRow.createCell().setText(DialogResources.getString("USER_MANAGEMENT_USER_EMAIL"));
            titleRow.createCell().setText(DialogResources.getString("USER_MANAGEMENT_POSITION"));
            titleRow.createCell().setText(DialogResources.getString("USER_MANAGEMENT_USER_PHONE"));
            titleRow.createCell().setText(DialogResources.getString("USER_MANAGEMENT_ADDRESS"));
            titleRow.createCell().setText(DialogResources.getString("USER_MANAGEMENT_PERSONS_CODE"));
            titleRow.createCell().setText(DialogResources.getString("USER_MANAGEMENT_ROLE"));

            for (int row = 0; row < usersTableModel.getRowCount(); row++) {
                User user = usersTableModel.getUser(row);
                XWPFTableRow logRow = table.createRow();
                logRow.getCell(0).setText(user.getLogin());
                logRow.getCell(1).setText(user.getName());
                logRow.getCell(2).setText(user.getLastName());
                logRow.getCell(3).setText(user.getEmail());
                logRow.getCell(4).setText(user.getPosition());
                logRow.getCell(5).setText(user.getPhone());
                logRow.getCell(6).setText(user.getAddress());
                logRow.getCell(7).setText(user.getPersonCode());
                logRow.getCell(8).setText(user.getRole().toString());
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

    private void fillWithUsers() {
        List<User> users = UsersUtils.getAllUsers();
        users.remove(new Administrator());
        users.remove(new Manager());
        users.remove(new GoodsReceiver());
        usersTableModel = new UsersTableModel(users);
        usersTable.setModel(usersTableModel);
    }

    private class UsersTableModel extends AbstractTableModel {
        private static final int NAME_COLUMN = 0;
        private static final int LAST_NAME_COLUMN = 1;
        private static final int EMAIL_COLUMN = 2;
        private static final int PHONE_COLUMN = 3;
        private final String[] column_names = {"USER_MANAGEMENT_USER_NAME", "USER_MANAGEMENT_USER_LAST_NAME",
                "USER_MANAGEMENT_USER_EMAIL", "USER_MANAGEMENT_USER_PHONE"};
        private final List<User> users;

        public UsersTableModel(List<User> users) {
            this.users = users;
        }

        @Override
        public int getRowCount() {
            return users.size();
        }

        @Override
        public int getColumnCount() {
            return column_names.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            User user = users.get(rowIndex);
            switch (columnIndex) {
                case NAME_COLUMN:
                    return " " + user.getName();
                case LAST_NAME_COLUMN:
                    return " " + user.getLastName();
                case EMAIL_COLUMN:
                    return " " + user.getEmail();
                case PHONE_COLUMN:
                    return " " + user.getPhone();
            }
            return "";
        }

        @Override
        public String getColumnName(int column) {
            return DialogResources.getString(column_names[column]);
        }

        public User getUser(int rowIndex) {
            return users.get(rowIndex);
        }

        public void removeRow(int rowIndex) {
            users.remove(rowIndex);
        }
    }
}
