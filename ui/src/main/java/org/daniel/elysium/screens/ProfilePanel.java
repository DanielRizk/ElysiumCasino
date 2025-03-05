package org.daniel.elysium.screens;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.LogoAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.elements.fields.StyledTextField;
import org.daniel.elysium.elements.notifications.StyledConfirmDialog;
import org.daniel.elysium.elements.notifications.StyledNotificationDialog;
import org.daniel.elysium.elements.panels.BackgroundPanel;
import org.daniel.elysium.interfaces.Resettable;
import org.daniel.elysium.user.database.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Represents the Profile panel where users can manage their account
 * to view balance, change password, or delete the account.
 */
public class ProfilePanel extends JPanel implements Resettable {
    private final StyledTextField balanceLabel;
    private final StyledTextField nameLabel;
    private final StyledButton changePasswordButton;
    private final StyledButton deleteAccountButton;
    private final StyledButton backButton;
    private final StateManager stateManager;

    /**
     * Constructs a {@code ProfilePanel} with the required UI components.
     *
     * @param stateManager The application's {@link StateManager} instance.
     */
    public ProfilePanel(StateManager stateManager) {
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

        // Create and add Name text field
        nameLabel = new StyledTextField("Name: " + stateManager.getProfile().getName(), false);
        gbc.gridy = 1;
        inputPanel.add(nameLabel, gbc);

        // Create and add Balance text field
        balanceLabel = new StyledTextField("Balance: " + stateManager.getProfile().getBalance(), false);
        gbc.gridy = 2;
        inputPanel.add(balanceLabel, gbc);

        // Add spacing between label and buttons
        gbc.insets = new Insets(40, 0, 10, 0);

        // Create and add login button
        changePasswordButton = new StyledButton("Change Password");
        gbc.gridy = 3;
        inputPanel.add(changePasswordButton, gbc);

        // Revert original spacing
        gbc.insets = new Insets(10, 0, 10, 0);

        // Create and add register button
        deleteAccountButton = new StyledButton("Delete Account");
        gbc.gridy = 4;
        inputPanel.add(deleteAccountButton, gbc);

        // Create and add quit button
        backButton = new StyledButton("Back");
        gbc.gridy = 5;
        inputPanel.add(backButton, gbc);

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
        // change pass button action -> go to change pass panel
        changePasswordButton.addActionListener(e -> stateManager.switchPanel("ChangePass"));

        // Add keystroke to top up your balance (Ctrl + Shift + T)
        KeyStroke topUpKey = KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(topUpKey, "topUpAction");
        getActionMap().put("topUpAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                topUpBalance();
            }
        });

        // Register button action -> switch to register panel
        deleteAccountButton.addActionListener(e -> deleteAccount());

        // Back button action -> switch to login panel
        backButton.addActionListener(e -> stateManager.switchPanel("MainMenu"));
    }

    /**
     * Deletes the user's account after confirmation.
     * <p>
     * This method displays a confirmation dialog asking the user if they want to delete their account.
     * If the user confirms, their account is deleted from the database, their profile is removed,
     * and they are redirected to the login screen.
     */

    private void deleteAccount(){
        StyledConfirmDialog dialog = new StyledConfirmDialog(stateManager.getFrame(), "Are you sure you want to delete you account ?");
        dialog.setVisible(true);
        if (dialog.isConfirmed()){
            UserDAO userDAO = new UserDAO();
            userDAO.deleteUser(stateManager.getProfile().getName());
            stateManager.setProfile(null);
            stateManager.switchPanel("Login");

            // Set initial focus to the main panel
            stateManager.getFrame().addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowOpened(java.awt.event.WindowEvent e) {
                    stateManager.getFrame().getComponent(0).requestFocusInWindow();
                }
            });
        }
    }

    /** Helper method to set and update the name label */
    private void updateNameLabel(){
        nameLabel.setText("Name: " + stateManager.getProfile().getName());
    }

    /** Helper method to set and update the balance label */
    private void updateBalanceLabel(){
        balanceLabel.setText("Balance: " + stateManager.getProfile().getBalance());
    }

    /**
     * Increases the user's balance by 10,000 and displays a notification.
     * <p>
     * This method adds 10,000 to the user's balance, updates the balance display,
     * and shows a notification dialog informing the user about the balance increase.
     */
    private void topUpBalance(){
        stateManager.getProfile().increaseBalanceBy(10000);
        StyledNotificationDialog dialog = new StyledNotificationDialog(stateManager.getFrame(), "Your balance has increased by 10000");
        dialog.setVisible(true);
        updateBalanceLabel();
    }

    @Override
    public void reset() {
        // Nothing to do here
    }

    @Override
    public void onRestart() {
        updateNameLabel();
        updateBalanceLabel();
    }
}
