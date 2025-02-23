package org.daniel.elysium.events.handlers;

import org.daniel.elysium.events.EventManager;
import org.daniel.elysium.screens.ScreenManager;

import javax.swing.*;

public class RegisterEventHandler extends EventManager {
    public RegisterEventHandler(ScreenManager screenManager) {
        super(screenManager);
    }

    public void addRegisterButtonListener(JButton loginButton) {
        loginButton.addActionListener(e -> screenManager.showScreen("MainMenu"));
    }

    public void addQuitButtonListener(JButton quitButton) {
        quitButton.addActionListener(e -> System.exit(0));
    }
}
