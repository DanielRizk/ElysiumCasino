package org.daniel.elysium.screens.panels;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.elements.notifications.StyledNotificationDialog;
import org.daniel.elysium.elements.panels.BackgroundPanel;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    private final StyledButton blackjackButton;
    private final StyledButton baccaratButton;
    private final StyledButton ultimateTHButton;
    private final StyledButton profileButton;
    private final StyledButton logoutButton;
    private final StateManager stateManager;

    public MainMenuPanel(StateManager stateManager) {
       this.stateManager = stateManager;
        setLayout(new BorderLayout());

        // Set the background
        BackgroundPanel backgroundPanel = new BackgroundPanel(BackgroundAsset.BACKGROUND);
        // inputPanel to hold other elements
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        // Create a grid to organize elements
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create and add the game logo
        Dimension logoDimension = new Dimension(430, 350);
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

        // Create and add ultimate_th button
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

        // Add action listeners
        registerButtonsActions();

        // add inputPanel and background to main panel
        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);
    }

    /**
     * Register and activate buttons action listeners
     */
    private void registerButtonsActions(){
        // Blackjack button action -> start and go to blackjack
        blackjackButton.addActionListener(e -> {
            if (stateManager.isUserLoggedIn() &&
                    stateManager.getProfile().getBalance() > StateManager.MIN_BET){
                stateManager.switchPanel("Blackjack");
            }
        });

        // Baccarat button action -> start and go to baccarat
        baccaratButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            StyledNotificationDialog dialog = new StyledNotificationDialog(frame, "Coming soon");
            dialog.setVisible(true);
            /*
            if (stateManager.isUserLoggedIn() &&
                    stateManager.getProfile().getBalance() > StateManager.MIN_BET){
                stateManager.switchPanel("Baccarat");
            }*/
        });

        // Ultimate_TH button action -> start and go to ultimate_th
        ultimateTHButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            StyledNotificationDialog dialog = new StyledNotificationDialog(frame, "Coming soon");
            dialog.setVisible(true);
            /*
            if (stateManager.isUserLoggedIn() &&
                    stateManager.getProfile().getBalance() > StateManager.MIN_BET){
                stateManager.switchPanel("Ultimate TH");
            }*/
        });

        // Profile button action -> go to user profile page
        profileButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            StyledNotificationDialog dialog = new StyledNotificationDialog(frame, "Coming soon");
            dialog.setVisible(true);
            /*
            if (stateManager.isUserLoggedIn() &&
                    stateManager.getProfile().getBalance() > StateManager.MIN_BET){
                stateManager.switchPanel("Profile");
            }*/
        });

        // Logout button action -> go to login page
        logoutButton.addActionListener(e -> {
            stateManager.setProfile(null);
            stateManager.switchPanel("Login");
        });
    }
}
