package org.daniel.elysium.screens;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.LogoAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.elements.fields.StyledPasswordField;
import org.daniel.elysium.elements.fields.StyledTextField;
import org.daniel.elysium.elements.notifications.StyledConfirmDialog;
import org.daniel.elysium.elements.notifications.Toast;
import org.daniel.elysium.elements.panels.BackgroundPanel;
import org.daniel.elysium.interfaces.Resettable;
import org.daniel.elysium.user.database.UserDAO;
import org.daniel.elysium.user.profile.UserProfile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Represents the login panel where users can enter their credentials
 * to log in, register, or quit the application.
 */
public class LoginPanel extends JPanel implements Resettable {
    private final StyledTextField usernameField;
    private final StyledPasswordField passwordField;
    private final StyledButton loginButton;
    private final StyledButton registerButton;
    private final StyledButton quitButton;
    private final StateManager stateManager;

    /**
     * Constructs a {@code LoginPanel} with the required UI components.
     *
     * @param stateManager The application's {@link StateManager} instance.
     */
    public LoginPanel(StateManager stateManager) {
        this.stateManager = stateManager;
        setLayout(new BorderLayout());

        // Set the background
        BackgroundPanel backgroundPanel = new BackgroundPanel(BackgroundAsset.BACKGROUND_MAIN);

        // Panel to hold input fields and buttons
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);

        // Create a grid to organize elements
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Create and add the game logo
        Dimension logoDimension = new Dimension(600, 500);
        JLabel logoLabel = new JLabel(AssetManager.getScaledIcon(LogoAsset.LOGO_SHADE, logoDimension));
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

        // Add spacing between inputs and buttons
        gbc.insets = new Insets(40, 0, 10, 0);

        // Create and add login button
        loginButton = new StyledButton("Login");
        gbc.gridy = 4;
        inputPanel.add(loginButton, gbc);

        // Revert original spacing
        gbc.insets = new Insets(10, 0, 10, 0);

        // Create and add register button
        registerButton = new StyledButton("Register");
        gbc.gridy = 5;
        inputPanel.add(registerButton, gbc);

        // Create and add quit button
        quitButton = new StyledButton("Quit");
        gbc.gridy = 6;
        inputPanel.add(quitButton, gbc);

        // Register button actions
        registerButtonsActions();

        // Add input panel and background to the main panel
        backgroundPanel.add(inputPanel, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);
    }

    /**
     * Registers action listeners for login, register, and quit buttons.
     * Also maps the Enter key to trigger the login button.
     */
    private void registerButtonsActions() {
        // Login button action -> log in and go to the main menu
        loginButton.addActionListener(e -> {
            stateManager.setProfile(login());
            if (stateManager.isUserLoggedIn()) {
                stateManager.switchPanel("MainMenu");
            }
        });

        // Add keystroke to login button (Enter key)
        KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(enterKey, "loginAction");
        getActionMap().put("loginAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton.doClick();
            }
        });

        // Register button action -> switch to register panel
        registerButton.addActionListener(e -> stateManager.switchPanel("Register"));

        // Quit button action -> confirm before quitting
        quitButton.addActionListener(e -> quit());
    }

    /**
     * Handles the login logic by validating user input and checking credentials.
     *
     * @return The logged-in {@link UserProfile} if successful, otherwise {@code null}.
     */
    private UserProfile login() {
        String userName = usernameField.getText().trim();
        String password = passwordField.getPassword();
        UserDAO userDAO = new UserDAO();

        // Validate username
        if (userName.isEmpty() || userName.equals("Username")) {
            new Toast(stateManager.getFrame(), "Please enter your Username", 3000).setVisible(true);
            return null;
        }

        // Validate password
        if (password.isEmpty() || password.equals("Password")) {
            new Toast(stateManager.getFrame(), "Please enter your Password", 3000).setVisible(true);
            return null;
        }

        // Fetch user profile from database
        UserProfile profile = userDAO.getUserByUsername(userName);
        if (profile == null) {
            new Toast(stateManager.getFrame(), "Username does not exist", 3000).setVisible(true);
            return null;
        }

        // Verify password
        if (!profile.getPass().equals(password)) {
            new Toast(stateManager.getFrame(), "Wrong password, Try again", 3000).setVisible(true);
            return null;
        }

        // Successful login
        new Toast(stateManager.getFrame(), "Welcome " + userName + ", Your balance is: " + profile.getBalance(), 3000).setVisible(true);
        return profile;
    }

    /**
     * Prompts the user with a confirmation dialog before quitting the application.
     */
    private void quit() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        StyledConfirmDialog dialog = new StyledConfirmDialog(frame, "Are you sure you want to quit?");
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            System.exit(0);
        }
    }

    @Override
    public void onRestart() {
        // No need to do anything here
    }

    @Override
    public void reset() {
        this.usernameField.setText("");
        this.passwordField.setText("");
    }
}
