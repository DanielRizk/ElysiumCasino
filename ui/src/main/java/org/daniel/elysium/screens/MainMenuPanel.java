package org.daniel.elysium.screens;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.elements.notifications.StyledNotificationDialog;
import org.daniel.elysium.elements.panels.BackgroundPanel;
import org.daniel.elysium.games.baccarat.BaccaratController;
import org.daniel.elysium.games.blackjack.BlackjackController;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code MainMenuPanel} class represents the main menu screen where users can navigate
 * to different game modes, view their profile, or log out.
 */
public class MainMenuPanel extends JPanel {
    private final StyledButton blackjackButton;
    private final StyledButton baccaratButton;
    private final StyledButton ultimateTHButton;
    private final StyledButton profileButton;
    private final StyledButton logoutButton;
    private final StateManager stateManager;

    /**
     * Constructs the {@code MainMenuPanel}, initializes buttons and UI components.
     *
     * @param stateManager The application's {@link StateManager} instance.
     */
    public MainMenuPanel(StateManager stateManager) {
        this.stateManager = stateManager;
        setLayout(new BorderLayout());

        // Set the background
        BackgroundPanel backgroundPanel = new BackgroundPanel(BackgroundAsset.BACKGROUND);

        // Panel to hold menu buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        // Create a grid to organize elements
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create and add the game logo
        Dimension logoDimension = new Dimension(600, 500);
        JLabel logoLabel = new JLabel(AssetManager.getScaledIcon(BackgroundAsset.LOGO_SHADE, logoDimension));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        buttonPanel.add(logoLabel, gbc);

        // Create and add blackjack button
        blackjackButton = new StyledButton("BlackJack");
        gbc.gridy = 1;
        buttonPanel.add(blackjackButton, gbc);

        // Create and add baccarat button
        baccaratButton = new StyledButton("Baccarat");
        gbc.gridy = 2;
        buttonPanel.add(baccaratButton, gbc);

        // Create and add Ultimate Texas Hold'em button
        ultimateTHButton = new StyledButton("Ultimate TH");
        gbc.gridy = 3;
        buttonPanel.add(ultimateTHButton, gbc);

        // Create and add profile button
        profileButton = new StyledButton("Profile", ButtonAsset.BUTTON_GREY_ROUND);
        gbc.gridy = 4;
        buttonPanel.add(profileButton, gbc);

        // Create and add logout button
        logoutButton = new StyledButton("Logout", ButtonAsset.BUTTON_GREY_ROUND);
        gbc.gridy = 5;
        buttonPanel.add(logoutButton, gbc);

        // Register button actions
        registerButtonsActions();

        // Add input panel and background to the main panel
        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);
    }

    /**
     * Registers action listeners for menu buttons to handle navigation and interactions.
     */
    private void registerButtonsActions() {
        // Blackjack button action -> start and go to blackjack if user has sufficient balance
        blackjackButton.addActionListener(e -> {
            if (stateManager.isUserLoggedIn() &&
                    stateManager.getProfile().getBalance() > BlackjackController.MIN_BET) {
                stateManager.switchPanel("Blackjack");
            } else {
                StyledNotificationDialog dialog = new StyledNotificationDialog(
                        stateManager.getFrame(),
                        "You don't have enough balance to start the game. "
                );

                dialog.setVisible(true);
            }
        });

        // Baccarat button action -> start and go to baccarat if user has sufficient balance
        baccaratButton.addActionListener(e -> {
            if (stateManager.isUserLoggedIn() &&
                    stateManager.getProfile().getBalance() > BaccaratController.MIN_BET) {
                stateManager.switchPanel("Baccarat");
            } else {
                StyledNotificationDialog dialog = new StyledNotificationDialog(
                        stateManager.getFrame(),
                        "You don't have enough balance to start the game. "
                );

                dialog.setVisible(true);
            }
        });

        // Ultimate Texas Hold'em button action -> display "Coming soon" notification
        ultimateTHButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            StyledNotificationDialog dialog = new StyledNotificationDialog(frame, "Coming soon");
            dialog.setVisible(true);
            /*
            if (stateManager.isUserLoggedIn() &&
                    stateManager.getProfile().getBalance() > StateManager.MIN_BET) {
                stateManager.switchPanel("Ultimate TH");
            }*/
        });

        // Profile button action -> display "Coming soon" notification
        profileButton.addActionListener(e -> {
            if (stateManager.isUserLoggedIn()) {
                stateManager.switchPanel("Profile");
            }
        });

        // Logout button action -> log out the user and return to log in screen
        logoutButton.addActionListener(e -> {
            stateManager.setProfile(null);
            stateManager.switchPanel("Login");
        });
    }
}
