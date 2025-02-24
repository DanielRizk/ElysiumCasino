package org.daniel.elysium.screens.panels;

import org.daniel.elysium.StateManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.elements.panels.BackgroundPanel;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.assets.AssetManager;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(StateManager stateManager) {
        setLayout(new BorderLayout());

        BackgroundPanel backgroundPanel = new BackgroundPanel(BackgroundAsset.BACKGROUND);
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel logoLabel = new JLabel(AssetManager.getScaledIcon(BackgroundAsset.LOGO_SHADE, 430, 350));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        buttonPanel.add(logoLabel, gbc);

        StyledButton blackjackButton = new StyledButton("BlackJack");
        gbc.gridy = 1;
        buttonPanel.add(blackjackButton, gbc);

        StyledButton baccaratButton = new StyledButton("Baccarat");
        gbc.gridy = 2;
        buttonPanel.add(baccaratButton, gbc);

        StyledButton ultimateTHButton = new StyledButton("Ultimate TH");
        gbc.gridy = 3;
        buttonPanel.add(ultimateTHButton, gbc);

        StyledButton profileButton = new StyledButton("Profile", ButtonAsset.BUTTON_GREY_ROUND);
        gbc.gridy = 4;
        buttonPanel.add(profileButton, gbc);

        StyledButton logoutButton = new StyledButton("Logout", ButtonAsset.BUTTON_GREY_ROUND);
        gbc.gridy = 5;
        buttonPanel.add(logoutButton, gbc);

        logoutButton.addActionListener(e -> {
            stateManager.setProfile(null);
            stateManager.switchPanel("Login");
        });

        blackjackButton.addActionListener(e -> {
            if (stateManager.isUserLoggedIn()){
                stateManager.switchPanel("Blackjack");
            }
        });

        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);
    }
}
