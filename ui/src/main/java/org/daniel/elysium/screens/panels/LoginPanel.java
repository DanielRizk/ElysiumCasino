package org.daniel.elysium.screens.panels;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.elements.fields.StyledPasswordField;
import org.daniel.elysium.elements.fields.StyledTextField;
import org.daniel.elysium.elements.notifications.StyledConfirmDialog;
import org.daniel.elysium.elements.notifications.Toast;
import org.daniel.elysium.elements.panels.BackgroundPanel;
import org.daniel.elysium.user.database.UserDAO;
import org.daniel.elysium.user.profile.UserProfile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class LoginPanel extends JPanel {
    private final StyledTextField usernameField;
    private final StyledPasswordField passwordField;
    private final StyledButton loginButton;
    private final StyledButton registerButton;
    private final StyledButton quitButton;
    private final StateManager stateManager;

    public LoginPanel(StateManager stateManager) {
        this.stateManager = stateManager;
        setLayout(new BorderLayout());

        // Set the background
        BackgroundPanel backgroundPanel = new BackgroundPanel(BackgroundAsset.BACKGROUND);
        // inputPanel to hold other elements
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);

        // Create a grid to organize elements
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Create and add the game logo
        Dimension logoDimension = new Dimension(430, 350);
        JLabel logoLabel = new JLabel(AssetManager.getScaledIcon(BackgroundAsset.LOGO_SHADE, logoDimension));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        inputPanel.add(logoLabel, gbc);

        // Create and add username input text field
        usernameField = new StyledTextField("Username");
        gbc.gridy = 1;
        inputPanel.add(usernameField, gbc);

        // Create and add password input password field
        passwordField = new StyledPasswordField("Password");
        gbc.gridy = 2;
        inputPanel.add(passwordField, gbc);

        // add more space between inputs and buttons
        gbc.insets = new Insets(40, 0, 10, 0);

        // Create and add login button
        loginButton = new StyledButton("Login");
        gbc.gridy = 4;
        inputPanel.add(loginButton, gbc);

        // Revert original spaces
        gbc.insets = new Insets(10, 0, 10, 0);

        // Create and add register button
        registerButton = new StyledButton("Register");
        gbc.gridy = 5;
        inputPanel.add(registerButton, gbc);

        // Create and add quit button
        quitButton = new StyledButton("Quit");
        gbc.gridy = 6;
        inputPanel.add(quitButton, gbc);

        // Add action listeners
        registerButtonsActions();

        // add inputPanel and background to main panel
        backgroundPanel.add(inputPanel, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);
    }

    /**
     * Register and activate buttons action listeners
     */
    private void registerButtonsActions(){
        // Login button action -> login and go to main menu
        loginButton.addActionListener(e -> {
            stateManager.setProfile(login());
            if (stateManager.isUserLoggedIn()){
                stateManager.switchPanel("MainMenu");
            }
        });

        // Add keystroke to login button - Enter Key -
        KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(enterKey, "loginAction");
        getActionMap().put("loginAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton.doClick();
            }
        });

        // Register button action -> go to register page
        registerButton.addActionListener(e ->{
            stateManager.switchPanel("Register");
        });

        // Quit button action -> confirm and quit
        quitButton.addActionListener(e ->{
            quit();
        });
    }

    /**
     * Login logic and procedure
     */
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

    /**
     * Quit confirmation
     */
    private void quit(){
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        StyledConfirmDialog dialog = new StyledConfirmDialog(frame, "Are you sure you want to quit?");
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            System.exit(0);
        }
    }
}
