package org.daniel.elysium.screens.panels;

import org.daniel.elysium.events.handlers.RegisterEventHandler;
import org.daniel.elysium.screens.models.BackgroundPanel;
import org.daniel.elysium.constants.Asset;
import org.daniel.elysium.elements.StyledButton;
import org.daniel.elysium.elements.StyledPasswordField;
import org.daniel.elysium.elements.StyledTextField;
import org.daniel.elysium.managers.AssetManager;
import org.daniel.elysium.events.EventManager;
import org.daniel.elysium.screens.ScreenManager;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    public RegisterPanel(RegisterEventHandler eventHandler) {
        setLayout(new BorderLayout());

        BackgroundPanel backgroundPanel =  new BackgroundPanel(Asset.BACKGROUND);
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel logoLabel = new JLabel(AssetManager.getScaledIcon(Asset.LOGO_SHADE, 430, 350));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        inputPanel.add(logoLabel, gbc);

        StyledTextField usernameField = new StyledTextField("Username", 15);
        gbc.gridy = 1;
        inputPanel.add(usernameField, gbc);

        StyledPasswordField passwordField = new StyledPasswordField("Password", 15);
        gbc.gridy = 2;
        inputPanel.add(passwordField, gbc);

        StyledPasswordField repeatPasswordField = new StyledPasswordField("Repeat Password", 15);
        gbc.gridy = 3;
        inputPanel.add(repeatPasswordField, gbc);

        gbc.insets = new Insets(40, 0, 10, 0);

        StyledButton registerButton = new StyledButton("Register", Asset.BUTTON_DB_SHARP);
        gbc.gridy = 5;
        inputPanel.add(registerButton, gbc);

        gbc.insets = new Insets(10, 0, 10, 0);

        StyledButton quitButton = new StyledButton("Quit", Asset.BUTTON_DB_SHARP);
        gbc.gridy = 6;
        inputPanel.add(quitButton, gbc);

        eventHandler.addRegisterButtonListener(registerButton);
        eventHandler.addQuitButtonListener(quitButton);

        backgroundPanel.add(inputPanel, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);

    }
}
