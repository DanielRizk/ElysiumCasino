package org.daniel.elysium.screens.panels;

import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.ButtonAsset;
import org.daniel.elysium.events.handlers.MainMenuEventHandler;
import org.daniel.elysium.elements.panels.BackgroundPanel;
import org.daniel.elysium.elements.buttons.StyledButton;
import org.daniel.elysium.assets.AssetManager;

import javax.swing.*;
import java.awt.*;

// MainMenuPanel.java
public class MainMenuPanel extends JPanel {
    public MainMenuPanel(MainMenuEventHandler eventHandler) {
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

        StyledButton blackjackButton = new StyledButton("BlackJack", ButtonAsset.BUTTON_DARK_BLUE_ROUND);
        gbc.gridy = 1;
        buttonPanel.add(blackjackButton, gbc);

        StyledButton baccaratButton = new StyledButton("Baccarat", ButtonAsset.BUTTON_DARK_BLUE_ROUND);
        gbc.gridy = 2;
        buttonPanel.add(baccaratButton, gbc);

        StyledButton ultimateTHButton = new StyledButton("Ultimate TH", ButtonAsset.BUTTON_DARK_BLUE_ROUND);
        gbc.gridy = 3;
        buttonPanel.add(ultimateTHButton, gbc);

        StyledButton profileButton = new StyledButton("Profile", ButtonAsset.BUTTON_GREY_ROUND);
        gbc.gridy = 4;
        buttonPanel.add(profileButton, gbc);

        StyledButton logoutButton = new StyledButton("Logout", ButtonAsset.BUTTON_GREY_ROUND);
        gbc.gridy = 5;
        buttonPanel.add(logoutButton, gbc);

        eventHandler.addLogoutButtonListener(logoutButton);
        eventHandler.addBlackjackButtonListener(blackjackButton);

        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);
    }
}
