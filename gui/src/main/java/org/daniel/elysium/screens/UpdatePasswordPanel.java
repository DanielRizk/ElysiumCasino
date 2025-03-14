package org.daniel.elysium.screens;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.assets.LogoAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.elements.fields.StyledPasswordField;
import org.daniel.elysium.elements.labels.LogoLabel;
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
 * Represents the user update password panel where new users can change their password.
 */
public class UpdatePasswordPanel extends JPanel implements Resettable {
    private final StyledPasswordField oldPasswordField;
    private final StyledPasswordField passwordField;
    private final StyledPasswordField repeatPasswordField;
    private final StyledButton changePassButton;
    private final StyledButton backButton;
    private final StateManager stateManager;

    /**
     * Constructs a {@code UpdatePasswordPanel} with input fields for registration.
     *
     * @param stateManager The application's {@link StateManager} instance.
     */
    public UpdatePasswordPanel(StateManager stateManager) {
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
        LogoLabel logoLabel = new LogoLabel(LogoAsset.LOGO_SHADE, logoDimension);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        inputPanel.add(logoLabel, gbc);

        // Create and add old password input password field
        oldPasswordField = new StyledPasswordField("Old Password");
        gbc.gridy = 1;
        inputPanel.add(oldPasswordField, gbc);

        // Create and add password input password field
        passwordField = new StyledPasswordField("New Password");
        gbc.gridy = 2;
        inputPanel.add(passwordField, gbc);

        // Create and add repeat password input password field
        repeatPasswordField = new StyledPasswordField("Repeat Password");
        gbc.gridy = 3;
        inputPanel.add(repeatPasswordField, gbc);

        // Add spacing between inputs and buttons
        gbc.insets = new Insets(40, 0, 10, 0);

        // Create and add change button
        changePassButton = new StyledButton("Change", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        gbc.gridy = 4;
        inputPanel.add(changePassButton, gbc);

        // Revert original spacing
        gbc.insets = new Insets(10, 0, 10, 0);

        // Create and add back button
        backButton = new StyledButton("Back", ButtonAsset.BUTTON_DARK_BLUE_SHARP);
        gbc.gridy = 5;
        inputPanel.add(backButton, gbc);

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
        changePassButton.addActionListener(e -> {
            if (stateManager.isUserLoggedIn()) {
                change();
            }
        });

        // Add keystroke to register button (Enter key)
        KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(enterKey, "registerAction");
        getActionMap().put("registerAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePassButton.doClick();
            }
        });

        // Back button action -> switch to login panel
        backButton.addActionListener(e -> {
            if (stateManager.isUserLoggedIn()){
                stateManager.switchPanel("Profile");
            }
        });
    }

    /**
     * Handles the change of the password logic by validating user input and updating the current user.
     */
    private void change() {
        String oldPassword = oldPasswordField.getPassword().trim();
        String newPassword = passwordField.getPassword();
        String repeatPassword = repeatPasswordField.getPassword();
        UserDAO userDAO = new UserDAO();

        // Validate username
        if (oldPassword.isEmpty() || oldPassword.equals("Old Password")) {
            new Toast(stateManager.getFrame(), "Please enter your old password", 3000).setVisible(true);
            return;
        }

        // Check if old pass is the same as the user input
        if (!oldPassword.equals(stateManager.getProfile().getPass())) {
            new Toast(stateManager.getFrame(), "Old password does not match", 3000).setVisible(true);
            return;
        }

        // Check if old pass is the same as the new pass
        if (oldPassword.equals(newPassword)) {
            new Toast(stateManager.getFrame(), "New pass cannot be the same as the old pass", 3000).setVisible(true);
            return;
        }

        // Validate password
        if (newPassword.isEmpty() || newPassword.equals("New Password")) {
            new Toast(stateManager.getFrame(), "Please enter your new Password", 3000).setVisible(true);
            return;
        }

        // Validate repeat password field
        if (repeatPassword.isEmpty() || repeatPassword.equals("Repeat Password")) {
            new Toast(stateManager.getFrame(), "Please repeat your new Password", 3000).setVisible(true);
            return;
        }

        // Check if passwords match
        if (!newPassword.equals(repeatPassword)) {
            new Toast(stateManager.getFrame(), "New passwords do not match, Try again.", 3000).setVisible(true);
            return;
        }

        // Update user password
        UserProfile profile = userDAO.changePassword(stateManager.getProfile().getName(), oldPassword, newPassword);
        stateManager.setProfile(profile);
        new Toast(stateManager.getFrame(), "Password has been updated", 3000).setVisible(true);
        stateManager.switchPanel("Profile");
    }

    @Override
    public void onRestart() {
        // No need to do anything here
    }

    @Override
    public void reset() {
        this.oldPasswordField.setText("");
        this.passwordField.setText("");
        this.repeatPasswordField.setText("");
    }
}
