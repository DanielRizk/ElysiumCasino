package org.daniel.elysium.screens.panels;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.ButtonAsset;
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

/**
 * Represents the user registration panel where new users can create an account.
 */
public class RegisterPanel extends JPanel {
    private final StyledTextField usernameField;
    private final StyledPasswordField passwordField;
    private final StyledPasswordField repeatPasswordField;
    private final StyledButton registerButton;
    private final StyledButton backButton;
    private final StyledButton quitButton;
    private final StateManager stateManager;

    /**
     * Constructs a {@code RegisterPanel} with input fields for registration.
     *
     * @param stateManager The application's {@link StateManager} instance.
     */
    public RegisterPanel(StateManager stateManager) {
        this.stateManager = stateManager;
        setLayout(new BorderLayout());

        // Set the background
        BackgroundPanel backgroundPanel = new BackgroundPanel(BackgroundAsset.BACKGROUND);

        // Panel to hold input fields and buttons
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

        // Create and add repeat password input password field
        repeatPasswordField = new StyledPasswordField("Repeat Password");
        gbc.gridy = 3;
        inputPanel.add(repeatPasswordField, gbc);

        // Add spacing between inputs and buttons
        gbc.insets = new Insets(40, 0, 10, 0);

        // Create and add register button
        registerButton = new StyledButton("Register", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        gbc.gridy = 5;
        inputPanel.add(registerButton, gbc);

        // Revert original spacing
        gbc.insets = new Insets(10, 0, 10, 0);

        // Create and add back button
        backButton = new StyledButton("Back", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        gbc.gridy = 6;
        inputPanel.add(backButton, gbc);

        // Create and add quit button
        quitButton = new StyledButton("Quit", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        gbc.gridy = 7;
        inputPanel.add(quitButton, gbc);

        // Register button actions
        registerButtonsActions();

        // Add input panel and background to the main panel
        backgroundPanel.add(inputPanel, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);
    }

    /**
     * Registers action listeners for register, back, and quit buttons.
     * Also maps the Enter key to trigger the register button.
     */
    private void registerButtonsActions() {
        // Register button action -> create user account and go to main menu
        registerButton.addActionListener(e -> {
            stateManager.setProfile(register());
            if (stateManager.isUserLoggedIn()) {
                stateManager.switchPanel("MainMenu");
            }
        });

        // Add keystroke to register button (Enter key)
        KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(enterKey, "registerAction");
        getActionMap().put("registerAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerButton.doClick();
            }
        });

        // Back button action -> switch to login panel
        backButton.addActionListener(e -> stateManager.switchPanel("Login"));

        // Quit button action -> confirm before quitting
        quitButton.addActionListener(e -> quit());
    }

    /**
     * Handles the registration logic by validating user input and creating a new user.
     *
     * @return The newly created {@link UserProfile}, or {@code null} if registration fails.
     */
    private UserProfile register() {
        String userName = usernameField.getText().trim();
        String password = passwordField.getPassword();
        String repeatPassword = repeatPasswordField.getPassword();
        UserDAO userDAO = new UserDAO();

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        // Validate username
        if (userName.isEmpty() || userName.equals("Username")) {
            new Toast(frame, "Please enter your Username", 3000).setVisible(true);
            return null;
        }

        // Check if username already exists
        if (userDAO.getUserByUsername(userName) != null) {
            new Toast(frame, "Username already exists", 3000).setVisible(true);
            return null;
        }

        // Validate password
        if (password.isEmpty() || password.equals("Password")) {
            new Toast(frame, "Please enter your Password", 3000).setVisible(true);
            return null;
        }

        // Validate repeat password field
        if (repeatPassword.isEmpty() || repeatPassword.equals("Repeat Password")) {
            new Toast(frame, "Please repeat your Password", 3000).setVisible(true);
            return null;
        }

        // Check if passwords match
        if (!password.equals(repeatPassword)) {
            new Toast(frame, "Passwords do not match, Try again.", 3000).setVisible(true);
            return null;
        }

        // Create the user
        UserProfile profile = userDAO.addUser(userName, password, 10000);
        new Toast(frame, "Welcome " + userName + ", Your balance is: " + profile.getBalance(), 3000).setVisible(true);
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
}
