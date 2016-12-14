package com.blender.grape.users.ui.dialogs;

import com.blender.grape.UIConstants;
import com.blender.grape.resources.DialogResources;
import com.blender.grape.users.ui.PermissionsTableModel;
import com.blender.grape.users.ui.SimpleButton;
import com.blender.grape.users.sql.RolesUtil;
import com.blender.grape.users.structures.Permission;
import com.blender.grape.users.structures.role.Role;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
public class RoleEditDialog extends JDialog {
    private final JTextField roleNameField;
    private Map<Permission, Boolean> permissions;
    private Role role;
    private Runnable okAction;

    public RoleEditDialog(String title, Role role) {
        this.role = role;
        this.roleNameField = new JTextField();
        this.permissions = new HashMap<>();
        roleNameField.setText(role.getName());
        initialize(title);
    }

    public RoleEditDialog(String title) {
        this.roleNameField = new JTextField();
        this.permissions = new HashMap<>();
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

    public void setOkAction(Runnable okAction) {
        this.okAction = okAction;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(UIConstants.BORDER);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(createTitlePanel());
        contentPanel.add(roleNameField);
        contentPanel.add(new JScrollPane(createPermissionsTable()));
        contentPanel.add(createButtonPanel());
        return contentPanel;
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(new JLabel(DialogResources.getString("ROLE_MANAGEMENT_FIELD_NAME")), BorderLayout.WEST);
        return titlePanel;
    }

    private JTable createPermissionsTable() {
        createPermissions();
        JTable permissionsTable = new JTable();
        permissionsTable.setModel(new PermissionsTableModel(permissions));
        permissionsTable.setCellSelectionEnabled(false);
        permissionsTable.setColumnSelectionAllowed(false);
        permissionsTable.setRowSelectionAllowed(true);
        return permissionsTable;
    }

    private void createPermissions() {
        for (Permission permission : Permission.values()) {
            permissions.put(permission, role != null && role.getPermissions().contains(permission));
        }
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(new SimpleButton("OK", this::ok));
        buttonPanel.add(new SimpleButton("CANCEL", this::cancel));
        return buttonPanel;
    }

    private void ok() {
        String roleName = roleNameField.getText();
        if (Objects.equals(roleName, "")) {
            showLoginIsEmptyWarning();
        } else {
            Set<Permission> newPermissions = permissions.entrySet().stream()
                    .filter(Map.Entry::getValue)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());
            if (role == null) {
                role = new Role(-1, roleName, newPermissions);
                RolesUtil.addRole(role);
            } else {
                role.setName(roleName);
                role.setPermissions(newPermissions);
                RolesUtil.editRole(role);
            }
            okAction.run();
            cancel();
        }
    }

    private void cancel() {
        setVisible(false);
        dispose();
    }

    private void showLoginIsEmptyWarning() {
        String message = DialogResources.getString("EDIT_ROLE_NAME_VALIDATE");
        JOptionPane.showMessageDialog(this, message, message, JOptionPane.WARNING_MESSAGE);
    }
}
