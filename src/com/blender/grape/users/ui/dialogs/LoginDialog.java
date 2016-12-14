package com.blender.grape.users.ui.dialogs;

import com.blender.grape.MainMenu;
import com.blender.grape.UIConstants;
import com.blender.grape.resources.DialogResources;
import com.blender.grape.users.ui.SimpleButton;
import com.blender.grape.users.ui.UIUtilities;
import com.blender.grape.users.sql.UsersUtils;
import com.blender.grape.users.structures.user.Administrator;
import com.blender.grape.users.structures.user.GoodsReceiver;
import com.blender.grape.users.structures.user.Manager;

import javax.swing.*;
import java.awt.*;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
public class LoginDialog extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginDialog() {
        usernameField = new JTextField("Admin");
        passwordField = new JPasswordField("Admin");
    }

    public void initialize() {
        setTitle(DialogResources.getString("LOGIN"));
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        UIUtilities.showWindow(this);
        getContentPane().add(createContentPanel());
        pack();
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(UIConstants.BORDER);
        contentPanel.add(createFieldsPanel(), BorderLayout.CENTER);
        contentPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        return contentPanel;
    }

    private JPanel createFieldsPanel() {
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.add(new JLabel(DialogResources.getString("USERNAME")));
        fieldsPanel.add(usernameField);
        fieldsPanel.add(new JLabel(DialogResources.getString("PASSWORD")));
        fieldsPanel.add(passwordField);
        return fieldsPanel;
    }

    private JPanel createButtonPanel() {
        JPanel loginButtonPanel = new JPanel();
        SimpleButton loginButton = new SimpleButton("LOGIN", this::login);
        getRootPane().setDefaultButton(loginButton);
        loginButtonPanel.add(loginButton);

        JPanel exitButtonPanel = new JPanel();
        exitButtonPanel.add(new SimpleButton("CANCEL", () -> System.exit(0)));

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.add(loginButtonPanel, BorderLayout.WEST);
        buttonPanel.add(exitButtonPanel, BorderLayout.EAST);
        return buttonPanel;
    }

    private void login() {
        String username = usernameField.getText();
        char[] password = passwordField.getPassword();
        if (Administrator.isAdmin(username, password) ||
                Manager.isManager(username, password) ||
                GoodsReceiver.isGoodsReceiver(username, password) ||
                UsersUtils.containsLogin(username, new String(password))) {
            openMainMenu();
            closeLoginDialog();
        } else {
            showWrongLoginPasswordWarning();
        }
    }

    private void openMainMenu() {
        MainMenu mainMenu = new MainMenu();
        UIUtilities.showWindow(mainMenu);
    }

    private void closeLoginDialog() {
        setVisible(false);
        dispose();
    }

    private void showWrongLoginPasswordWarning() {
        String title = DialogResources.getString("LOGIN_FAILED_TITLE");
        String message = DialogResources.getString("LOGIN_FAILED");
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }
}
