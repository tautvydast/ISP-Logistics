package com.blender.grape.users.ui.dialogs;

import com.blender.grape.UIConstants;
import com.blender.grape.resources.DialogResources;
import com.blender.grape.resources.Icons;
import com.blender.grape.users.PermissionService;
import com.blender.grape.users.ui.SimpleButton;
import com.blender.grape.users.ui.UIUtilities;
import com.blender.grape.users.sql.RolesUtil;
import com.blender.grape.users.structures.Permission;
import com.blender.grape.users.structures.role.NoRole;
import com.blender.grape.users.structures.role.Role;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
public class RoleManagementDialog extends JDialog {
    private RolesTableModel rolesTableModel;
    private JTable rolesTable;

    public RoleManagementDialog() {
        initialize();
    }

    private void initialize() {
        setTitle(DialogResources.getString("ROLE_MANAGEMENT"));
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(createContentPanel());
        pack();
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(UIConstants.BORDER);
        contentPanel.add(createControlButtonsPanel(), BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(createRolesTable()), BorderLayout.CENTER);
        contentPanel.add(createCloseButtonPanel(), BorderLayout.SOUTH);
        return contentPanel;
    }

    private JPanel createControlButtonsPanel() {
        JPanel controlButtonsPanel = new JPanel();
        controlButtonsPanel.setLayout(new BoxLayout(controlButtonsPanel, BoxLayout.X_AXIS));
        controlButtonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        SimpleButton addRole = new SimpleButton("ROLE_MANAGEMENT_ADD_ROLE", this::addRole, Icons.ADD_ROLE);
        addRole.setEnabled(PermissionService.hasRightTo(Permission.CREATE_ROLE));
        controlButtonsPanel.add(addRole);

        SimpleButton editRole = new SimpleButton("ROLE_MANAGEMENT_EDIT_ROLE", this::editRole, Icons.EDIT_ROLE);
        editRole.setEnabled(PermissionService.hasRightTo(Permission.EDIT_ROLE));
        controlButtonsPanel.add(editRole);

        SimpleButton removeRole = new SimpleButton("ROLE_MANAGEMENT_REMOVE_ROLE", this::removeRole, Icons.REMOVE_ROLE);
        removeRole.setEnabled(PermissionService.hasRightTo(Permission.REMOVE_ROLE));
        controlButtonsPanel.add(removeRole);

        return controlButtonsPanel;
    }

    private void addRole() {
        RoleEditDialog addRoleDialog = new RoleEditDialog(DialogResources.getString("EDIT_ROLE_ADD_TITLE"));
        addRoleDialog.setOkAction(this::fillWithRoles);
        UIUtilities.showWindow(addRoleDialog);
        fillWithRoles();
    }

    private void editRole() {
        int selectedRow = rolesTable.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < rolesTable.getRowCount()) {
            Role role = rolesTableModel.getRole(selectedRow);
            RoleEditDialog editRoleDialog = new RoleEditDialog(DialogResources.getString("EDIT_ROLE_EDIT_TITLE"), role);
            editRoleDialog.setOkAction(this::fillWithRoles);
            UIUtilities.showWindow(editRoleDialog);
        }
    }

    private void removeRole() {
        SwingUtilities.invokeLater(() -> {
            int selectedRow = rolesTable.getSelectedRow();
            String message = DialogResources.getString("ROLE_MANAGEMENT_REMOVE_ROLE_CONFIRM");
            if (selectedRow >= 0 && selectedRow < rolesTable.getRowCount()) {
                if (JOptionPane.showConfirmDialog(this, message, "", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    Role role = rolesTableModel.getRole(selectedRow);
                    RolesUtil.removeRole(role);
                    rolesTableModel.removeRole(selectedRow);
                    rolesTable.updateUI();
                }
            }
        });
    }

    private JTable createRolesTable() {
        rolesTable = new JTable();
        rolesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rolesTable.setCellSelectionEnabled(false);
        rolesTable.setRowSelectionAllowed(true);
        fillWithRoles();
        return rolesTable;
    }

    private JPanel createCloseButtonPanel() {
        JPanel closeButtonPanel = new JPanel(new BorderLayout());
        closeButtonPanel.add(new SimpleButton("CLOSE", this::close));
        return closeButtonPanel;
    }

    private void fillWithRoles() {
        List<Role> roles = RolesUtil.getAllRoles();
        roles.remove(new NoRole());
        rolesTableModel = new RolesTableModel(roles);
        rolesTable.setModel(rolesTableModel);
    }

    private void close() {
        setVisible(false);
        dispose();
    }

    private class RolesTableModel extends AbstractTableModel {
        private final List<Role> roles;

        RolesTableModel(List<Role> roles) {
            this.roles = roles;
        }

        @Override
        public int getRowCount() {
            return roles.size();
        }

        @Override
        public int getColumnCount() {
            return 1;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return roles.get(rowIndex);
        }

        @Override
        public String getColumnName(int column) {
            return DialogResources.getString("ROLE_MANAGEMENT_COLUMN_NAME");
        }

        public Role getRole(int rowIndex) {
            return roles.get(rowIndex);
        }

        public void removeRole(int rowIndex) {
            roles.remove(rowIndex);
        }
    }
}