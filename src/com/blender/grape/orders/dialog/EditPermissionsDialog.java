package com.blender.grape.orders.dialog;

import com.blender.grape.UIConstants;
import com.blender.grape.resources.DialogResources;
import com.blender.grape.users.ui.PermissionsTableModel;
import com.blender.grape.users.ui.SimpleButton;
import com.blender.grape.users.sql.UsersUtils;
import com.blender.grape.users.structures.Permission;
import com.blender.grape.users.structures.user.User;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
public class EditPermissionsDialog extends JDialog {
    private final User user;
    private final Map<Permission, Boolean> permissions;
    private Runnable okAction;

    public EditPermissionsDialog(User user) {
        this.user = user;
        this.permissions = new HashMap<>();
        initialize();
    }

    private void initialize() {
        setTitle(DialogResources.getString("EDIT_PERMISSIONS_TITLE"));
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().add(createContentPanel());
        pack();
    }

    public void setOKAction(Runnable okAction) {
        this.okAction = okAction;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(UIConstants.BORDER);
        contentPanel.add(new JScrollPane(createPermissionsTable()), BorderLayout.CENTER);
        contentPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        return contentPanel;
    }

    private JTable createPermissionsTable() {
        createPermissions();
        JTable table = new JTable();
        table.setModel(new PermissionsTableModel(permissions));
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        int max = Stream.of(Permission.values()).map(p -> p.toString().length()).max(Integer::compare).orElse(0);
        table.getColumnModel().getColumn(PermissionsTableModel.PERMISSION_COLUMN).setPreferredWidth(max * 10);
        return table;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(new SimpleButton("OK", this::ok));
        buttonPanel.add(new SimpleButton("CANCEL", this::cancel));
        return buttonPanel;
    }

    private void createPermissions() {
        Set<Permission> userPermissions = user.getPermissions();
        Set<Permission> rolePermissions = user.getRole().getPermissions();
        for (Permission permission : Permission.values()) {
            boolean userContains = userPermissions.contains(permission);
            boolean roleContains = rolePermissions.contains(permission);
            this.permissions.put(permission, roleContains ? null : userContains);
        }
    }

    private void ok() {
        Set<Permission> newPermissions = new HashSet<>();
        for (Map.Entry<Permission, Boolean> entry : permissions.entrySet()) {
            Boolean value = entry.getValue();
            if (value == null || value) {
                newPermissions.add(entry.getKey());
            }
        }
        UsersUtils.updatePermissions(user, newPermissions);
        okAction.run();
        cancel();
    }

    private void cancel() {
        setVisible(false);
        dispose();
    }
}
