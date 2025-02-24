package org.daniel.elysium.screens.panels;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.elements.fields.StyledTextField;
import org.daniel.elysium.elements.notifications.StyledConfirmDialog;
import org.daniel.elysium.elements.notifications.Toast;
import org.daniel.elysium.elements.panels.BackgroundPanel;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.elements.fields.StyledPasswordField;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.user.database.UserDAO;
import org.daniel.elysium.user.profile.UserProfile;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

// LoginPanel.java
public class LoginPanel extends JPanel {
    private final StyledTextField usernameField;
    private final StyledPasswordField passwordField;
    private final StateManager stateManager;
    public LoginPanel(StateManager stateManager) {
        this.stateManager = stateManager;
        setLayout(new BorderLayout());

        BackgroundPanel backgroundPanel = new BackgroundPanel(BackgroundAsset.BACKGROUND);
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

        usernameField = new StyledTextField("Username");
        gbc.gridy = 1;
        inputPanel.add(usernameField, gbc);

        passwordField = new StyledPasswordField("Password");
        gbc.gridy = 2;
        inputPanel.add(passwordField, gbc);

        gbc.insets = new Insets(40, 0, 10, 0);

        StyledButton loginButton = new StyledButton("Login");
        gbc.gridy = 4;
        inputPanel.add(loginButton, gbc);

        gbc.insets = new Insets(10, 0, 10, 0);
        StyledButton registerButton = new StyledButton("Register");
        gbc.gridy = 5;
        inputPanel.add(registerButton, gbc);

        StyledButton quitButton = new StyledButton("Quit");
        gbc.gridy = 6;
        inputPanel.add(quitButton, gbc);

        loginButton.addActionListener(e -> {
            stateManager.setProfile(login());
            if (stateManager.isUserLoggedIn()){
                stateManager.switchPanel("MainMenu");
            }
        });

        quitButton.addActionListener(e ->{
            quit();
        });

        registerButton.addActionListener(e ->{
            stateManager.switchPanel("Register");
        });

        backgroundPanel.add(inputPanel, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);
    }

    private UserProfile login(){
        String userName = usernameField.getText().trim();
        String password = passwordField.getPassword();
        UserDAO userDAO = new UserDAO();
        if (userName.isEmpty() || userName.equals("Username")){
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            new Toast(frame, "Please enter your Username", 3000).setVisible(true);
            return null;
        }
        if (password.isEmpty() || password.equals("Password")){
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            new Toast(frame, "Please enter your Password", 3000).setVisible(true);
            return null;
        }
        UserProfile profile = userDAO.getUserByUsername(userName);
        if (profile == null){
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            new Toast(frame, "Username does not exists", 3000).setVisible(true);
            return null;
        }
        if (!profile.getPass().equals(password)){
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            new Toast(frame, "Wrong password, Try again", 3000).setVisible(true);
            return null;
        }
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new Toast(frame, "Welcome " + userName + ", Your balance is: " + profile.getBalance(), 3000).setVisible(true);
        return profile;
    }

    private void quit(){
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        StyledConfirmDialog dialog = new StyledConfirmDialog(frame, "Are you sure you want to quit?");
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            System.exit(0);
        }
    }
}
