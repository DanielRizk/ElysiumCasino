package org.daniel.elysium.screens.panels;

import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.elements.notifications.StyledConfirmDialog;
import org.daniel.elysium.elements.notifications.Toast;
import org.daniel.elysium.elements.panels.BackgroundPanel;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.elements.fields.StyledPasswordField;
import org.daniel.elysium.elements.fields.StyledTextField;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.screens.ScreenManager;
import org.daniel.elysium.user.database.UserDAO;
import org.daniel.elysium.user.profile.UserProfile;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class RegisterPanel extends JPanel {
    StyledTextField usernameField;
    StyledPasswordField passwordField;
    StyledPasswordField repeatPasswordField;
    private final ScreenManager screenManager;
    public RegisterPanel(ScreenManager screenManager) {
        this.screenManager = screenManager;
        setLayout(new BorderLayout());

        BackgroundPanel backgroundPanel =  new BackgroundPanel(BackgroundAsset.BACKGROUND);
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel logoLabel = new JLabel(AssetManager.getScaledIcon(BackgroundAsset.LOGO_SHADE, 430, 350));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        inputPanel.add(logoLabel, gbc);

        usernameField = new StyledTextField("Username", 15);
        gbc.gridy = 1;
        inputPanel.add(usernameField, gbc);

        passwordField = new StyledPasswordField("Password", 15);
        gbc.gridy = 2;
        inputPanel.add(passwordField, gbc);

        repeatPasswordField = new StyledPasswordField("Repeat Password", 15);
        gbc.gridy = 3;
        inputPanel.add(repeatPasswordField, gbc);

        gbc.insets = new Insets(40, 0, 10, 0);

        StyledButton registerButton = new StyledButton("Register", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        gbc.gridy = 5;
        inputPanel.add(registerButton, gbc);

        gbc.insets = new Insets(10, 0, 10, 0);

        StyledButton quitButton = new StyledButton("Quit", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        gbc.gridy = 6;
        inputPanel.add(quitButton, gbc);

        registerButton.addActionListener(e ->{
            register();
        });

        quitButton.addActionListener(e ->{
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            StyledConfirmDialog dialog = new StyledConfirmDialog(frame, "Are you sure you want to quit?");
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                System.exit(0);
            }
        });

        backgroundPanel.add(inputPanel, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);

    }

    private UserProfile register(){
        String userName = usernameField.getText().trim();
        String password = Arrays.toString(passwordField.getPassword());
        String repeatPassword = Arrays.toString(repeatPasswordField.getPassword());
        UserDAO userDAO = new UserDAO();
        if (userName.isEmpty() || userName.equals("Username")){
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            new Toast(frame, "Please enter your Username", 3000).setVisible(true);
            return null;
        }
        if (userDAO.getUserByUsername(userName) != null){
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            new Toast(frame, "Username already exists", 3000).setVisible(true);
            return null;
        }
        if (password.isEmpty() || password.equals("Password")){
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            new Toast(frame, "Please enter your Password", 3000).setVisible(true);
            return null;
        }
        if (repeatPassword.isEmpty() || repeatPassword.equals("Repeat Password")){
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            new Toast(frame, "Please repeat your Password", 3000).setVisible(true);
            return null;
        }
        if (!password.equals(repeatPassword)){
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            new Toast(frame, "Passwords do not match, Try again.", 3000).setVisible(true);
            return null;
        }

        UserProfile profile = userDAO.addUser(userName, password, 10000);
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new Toast(frame, "Welcome " + userName + ", Your balance is: " + profile.getBalance(), 3000).setVisible(true);
        screenManager.showScreen("MainMenu");
        return profile;
    }
}
