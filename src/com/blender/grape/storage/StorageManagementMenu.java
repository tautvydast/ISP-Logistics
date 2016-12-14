package com.blender.grape.storage;

import com.blender.grape.MenuButton;
import com.blender.grape.UIConstants;
import com.blender.grape.resources.DialogResources;
import com.blender.grape.storage.dialog.ComListDialog;
import com.blender.grape.storage.dialog.ManagementDialog;
import com.blender.grape.storage.dialog.RegisterDialog;
import com.blender.grape.users.ui.UIUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tautvydas Traška, IFF-4/2.
 */
public class StorageManagementMenu extends JFrame {
    public StorageManagementMenu() throws HeadlessException {
        initialize();
    }

    private void initialize() {
        setTitle(DialogResources.getString("STORAGE_MANAGEMENT_MENU_TITLE"));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(createContentPanel());
        pack();
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(UIConstants.BORDER);

        MenuButton usersManagement = new MenuButton("STORAGE_MANAGEMENT_BUTTON_COMLIST", this::openComListDialog);
//        usersManagement.setEnabled(PermissionService.isUserManager());
        contentPanel.add(usersManagement);

        MenuButton rolesManagement = new MenuButton("STORAGE_MANAGEMENT_BUTTON_REGISTER", this::openRegisterDialog);
//        rolesManagement.setEnabled(PermissionService.isRoleManager());
        contentPanel.add(rolesManagement);

        MenuButton menuStatic = new MenuButton("STORAGE_MANAGEMENT_BUTTON_MANAGE", this::openManagementDialog);
//        menuStatic.setEnabled(PermissionService.hasRightTo(Permission.MANAGE_WAREHOUSE));
        contentPanel.add(menuStatic);

        contentPanel.add(new MenuButton("CLOSE", this::exit));

        return contentPanel;
    }

    private void openComListDialog() {
        ComListDialog comListDialog = new ComListDialog();
        UIUtilities.showWindow(comListDialog);
    }

    private void openRegisterDialog() {
        RegisterDialog registerDialog = new RegisterDialog();
        UIUtilities.showWindow(registerDialog);
    }

    private void openManagementDialog() {
        ManagementDialog managementDialog = new ManagementDialog();
        UIUtilities.showWindow(managementDialog);
    }

    private void exit() {
        setVisible(false);
        dispose();
    }
}
