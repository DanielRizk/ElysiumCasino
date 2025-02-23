package org.daniel.elysium.screens.panels;

import org.daniel.elysium.events.handlers.MainMenuEventHandler;
import org.daniel.elysium.screens.models.BackgroundPanel;
import org.daniel.elysium.constants.Asset;
import org.daniel.elysium.elements.StyledButton;
import org.daniel.elysium.managers.AssetManager;
import org.daniel.elysium.events.EventManager;
import org.daniel.elysium.screens.ScreenManager;

import javax.swing.*;
import java.awt.*;

// MainMenuPanel.java
public class MainMenuPanel extends JPanel {
    public MainMenuPanel(MainMenuEventHandler eventHandler) {
        setLayout(new BorderLayout());

        BackgroundPanel backgroundPanel = new BackgroundPanel(Asset.BACKGROUND);
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel logoLabel = new JLabel(AssetManager.getScaledIcon(Asset.LOGO_SHADE, 430, 350));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        buttonPanel.add(logoLabel, gbc);

        StyledButton blackjackButton = new StyledButton("BlackJack", Asset.BUTTON_DB_ROUND);
        gbc.gridy = 1;
        buttonPanel.add(blackjackButton, gbc);

        StyledButton baccaratButton = new StyledButton("Baccarat", Asset.BUTTON_DB_ROUND);
        gbc.gridy = 2;
        buttonPanel.add(baccaratButton, gbc);

        StyledButton ultimateTHButton = new StyledButton("Ultimate TH", Asset.BUTTON_DB_ROUND);
        gbc.gridy = 3;
        buttonPanel.add(ultimateTHButton, gbc);

        StyledButton profileButton = new StyledButton("Profile", Asset.BUTTON_G_ROUND);
        gbc.gridy = 4;
        buttonPanel.add(profileButton, gbc);

        StyledButton logoutButton = new StyledButton("Logout", Asset.BUTTON_G_ROUND);
        gbc.gridy = 5;
        buttonPanel.add(logoutButton, gbc);

        eventHandler.addLogoutButtonListener(logoutButton);
        eventHandler.addBlackjackButtonListener(blackjackButton);

        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);
    }
}
