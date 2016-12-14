package com.blender.grape;

import com.blender.grape.resources.DialogResources;
import com.blender.grape.storage.StorageManagementMenu;
import com.blender.grape.users.PermissionService;
import com.blender.grape.users.ui.UIUtilities;
import com.blender.grape.users.ui.dialogs.LogViewDialog;
import com.blender.grape.users.ui.dialogs.RoleManagementDialog;
import com.blender.grape.users.ui.dialogs.UserManagementDialog;
import com.blender.grape.users.structures.Permission;

import javax.swing.*;
import java.awt.*;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
public class MainMenu extends JFrame {
    public MainMenu() throws HeadlessException {
        initialize();
    }

    private void initialize() {
        setTitle(DialogResources.getString("MAIN_MENU"));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(createContentPanel());
        pack();
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(UIConstants.BORDER);

        MenuButton usersManagement = new MenuButton("MAIN_MENU_USERS_MANAGEMENT", this::openManageUsersDialog);
        usersManagement.setEnabled(PermissionService.isUserManager());
        contentPanel.add(usersManagement);

        MenuButton rolesManagement = new MenuButton("MAIN_MENU_ROLES_MANAGEMENT", this::openManageRolesDialog);
        rolesManagement.setEnabled(PermissionService.isRoleManager());
        contentPanel.add(rolesManagement);

        MenuButton menuStatic = new MenuButton("MAIN_MENU_STATIC", this::openStorageManagementMenu);
        menuStatic.setEnabled(PermissionService.hasRightTo(Permission.MANAGE_WAREHOUSE));
        contentPanel.add(menuStatic);

        MenuButton menuDynamic = new MenuButton("MAIN_MENU_DYNAMIC", this::openDynamicDialog);
        menuDynamic.setEnabled(PermissionService.hasRightTo(Permission.MANAGE_ORDERS));
        contentPanel.add(menuDynamic);

        MenuButton log = new MenuButton("MAIN_MENU_LOG", this::openLogDialog);
        log.setEnabled(PermissionService.hasRightTo(Permission.OPEN_LOG));
        contentPanel.add(log);

        contentPanel.add(new MenuButton("EXIT", this::exit));
        return contentPanel;
    }

    private void openManageUsersDialog() {
        UserManagementDialog userManagementDialog = new UserManagementDialog();
        UIUtilities.showWindow(userManagementDialog);
    }

    private void openManageRolesDialog() {
        RoleManagementDialog roleManagementDialog = new RoleManagementDialog();
        UIUtilities.showWindow(roleManagementDialog);
    }

    private void openStorageManagementMenu() {
        StorageManagementMenu storageManagementMenu = new StorageManagementMenu();
        UIUtilities.showWindow(storageManagementMenu);
    }

    private void openSaticDialog() {
        System.out.println("Static");
    }

    private void openDynamicDialog() {
        System.out.println("Dynamic");
    }

    private void openLogDialog() {
        LogViewDialog logViewDialog = new LogViewDialog();
        UIUtilities.showWindow(logViewDialog);
    }

    private void exit() {
        setVisible(false);
        dispose();
        System.exit(0);
    }
}
