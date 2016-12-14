package com.blender.grape.users.ui.dialogs;

import com.blender.grape.UIConstants;
import com.blender.grape.resources.DialogResources;
import com.blender.grape.users.ui.SimpleButton;
import com.blender.grape.users.sql.RolesUtil;
import com.blender.grape.users.sql.UsersUtils;
import com.blender.grape.users.structures.role.Role;
import com.blender.grape.users.structures.user.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

/**
 * @author Tautvydas Ramanauskas IFF-4/2
 */
public class UserEditDialog extends JDialog {
    private final JTextField nameField;
    private final JTextField passwordField;
    private final JTextField loginField;
    private final JTextField lastNameField;
    private final JTextField emailField;
    private final JTextField positionField;
    private final JTextField phoneField;
    private final JTextField addressField;
    private final JTextField personCodeField;
    private final JComboBox<Role> roleSelectionComboBox;
    private User user;
    private Runnable okAction;

    public UserEditDialog(String title, User user) {
        this(title);
        this.user = user;
        loginField.setText(user.getLogin());
        passwordField.setText(user.getPassword().getPassword());
        nameField.setText(user.getName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmail());
        positionField.setText(user.getPosition());
        phoneField.setText("" + user.getPhone());
        addressField.setText(user.getAddress());
        personCodeField.setText("" + user.getPersonCode());
    }

    public UserEditDialog(String title) {
        loginField = new JTextField();
        passwordField = new JTextField();
        nameField = new JTextField();
        lastNameField = new JTextField();
        emailField = new JTextField();
        positionField = new JTextField();
        phoneField = new JTextField();
        addressField = new JTextField();
        personCodeField = new JTextField();
        roleSelectionComboBox = new JComboBox<>();
        init(title);
    }

    public void setOkAction(Runnable okAction) {
        this.okAction = okAction;
    }

    private void init(String title) {
        setTitle(title);
        setModal(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().add(createContentPanel());
        List<Role> allRoles = RolesUtil.getAllRoles();
        ComboBoxModel<Role> model = roleSelectionComboBox.getModel();
        allRoles.forEach(((DefaultComboBoxModel<Role>) model)::addElement);
        pack();
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(UIConstants.BORDER);
        contentPanel.add(createUserInfoPanel(), BorderLayout.CENTER);
        contentPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        return contentPanel;
    }

    private JPanel createUserInfoPanel() {
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));

        JPanel loginPanel = new JPanel(new BorderLayout());
        loginPanel.add(new JLabel(DialogResources.getString("EDIT_USER_FIELD_LOGIN")));
        userInfoPanel.add(loginPanel);
        userInfoPanel.add(loginField);

        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.add(new JLabel(DialogResources.getString("EDIT_USER_FIELD_PASSWORD")));
        userInfoPanel.add(passwordPanel);
        userInfoPanel.add(passwordField);

        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.add(new JLabel(DialogResources.getString("EDIT_USER_FIELD_NAME")));
        userInfoPanel.add(namePanel);
        userInfoPanel.add(nameField);

        JPanel lastNamePanel = new JPanel(new BorderLayout());
        lastNamePanel.add(new JLabel(DialogResources.getString("EDIT_USER_FIELD_LAST_NAME")));
        userInfoPanel.add(lastNamePanel);
        userInfoPanel.add(lastNameField);

        JPanel emailPanel = new JPanel(new BorderLayout());
        emailPanel.add(new JLabel(DialogResources.getString("EDIT_USER_FIELD_EMAIL")));
        userInfoPanel.add(emailPanel);
        userInfoPanel.add(emailField);

        JPanel positionPanel = new JPanel(new BorderLayout());
        positionPanel.add(new JLabel(DialogResources.getString("EDIT_USER_FIELD_POSITION")));
        userInfoPanel.add(positionPanel);
        userInfoPanel.add(positionField);

        JPanel phoneNumberPanel = new JPanel(new BorderLayout());
        phoneNumberPanel.add(new JLabel(DialogResources.getString("EDIT_USER_FIELD_PHONE_NUMBER")));
        userInfoPanel.add(phoneNumberPanel);
        userInfoPanel.add(phoneField);

        JPanel addressPanel = new JPanel(new BorderLayout());
        addressPanel.add(new JLabel(DialogResources.getString("EDIT_USER_FIELD_ADDRESS")));
        userInfoPanel.add(addressPanel);
        userInfoPanel.add(addressField);

        JPanel personsCodePanel = new JPanel(new BorderLayout());
        personsCodePanel.add(new JLabel(DialogResources.getString("EDIT_USER_FIELD_PERSONS_CODE")));
        userInfoPanel.add(personsCodePanel);
        userInfoPanel.add(personCodeField);

        JPanel rolePanel = new JPanel(new BorderLayout());
        rolePanel.add(new JLabel(DialogResources.getString("EDIT_USER_FIELD_ROLE")));
        userInfoPanel.add(rolePanel);
        userInfoPanel.add(roleSelectionComboBox);

        return userInfoPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel eastButtonPanel = new JPanel(new BorderLayout());
        eastButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        eastButtonPanel.add(new SimpleButton("OK", this::ok), BorderLayout.WEST);
        eastButtonPanel.add(new SimpleButton("CANCEL", this::cancel), BorderLayout.EAST);
        buttonPanel.add(eastButtonPanel, BorderLayout.EAST);
        return buttonPanel;
    }

    private void ok() {
        if (Objects.equals(loginField.getText(), "")) {
            showLoginIsEmptyWarning();
        } else if (Objects.equals(passwordField.getText(), "")) {
            showPasswordIsEmptyWarning();
        } else if (!emailField.getText().contains("@")) {
            showIncorrectEmailWarning();
        } else {
            if (user == null) {
                user = new User();
                readFields();
                UsersUtils.addUser(user);
            } else {
                readFields();
                UsersUtils.editUser(user);
            }
            okAction.run();
            cancel();
        }
    }

    private void cancel() {
        setVisible(false);
        dispose();
    }

    private void readFields() {
        user.setLogin(loginField.getText());
        user.getPassword().setPassword(passwordField.getText());
        user.setName(nameField.getText());
        user.setLastName(lastNameField.getText());
        user.setEmail(emailField.getText());
        user.setPosition(positionField.getText());
        user.setPhone(phoneField.getText());
        user.setAddress(addressField.getText());
        user.setPersonCode(phoneField.getText());
        user.setRole((Role) roleSelectionComboBox.getSelectedItem());
    }

    private void showLoginIsEmptyWarning() {
        String message = DialogResources.getString("EDIT_USER_LOGIN_VALIDATE");
        JOptionPane.showMessageDialog(this, message, message, JOptionPane.WARNING_MESSAGE);
    }

    private void showPasswordIsEmptyWarning() {
        String message = DialogResources.getString("EDIT_USER_PASSWORD_VALIDATE");
        JOptionPane.showMessageDialog(this, message, message, JOptionPane.WARNING_MESSAGE);
    }

    private void showIncorrectEmailWarning() {
        String message = DialogResources.getString("EDIT_USER_EMAIL_VALIDATE");
        JOptionPane.showMessageDialog(this, message, message, JOptionPane.WARNING_MESSAGE);
    }
}
