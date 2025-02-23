package org.daniel.elysium.events.handlers;

import org.daniel.elysium.events.EventManager;
import org.daniel.elysium.screens.ScreenManager;

import javax.swing.*;

public class LoginEventHandler extends EventManager {

    public LoginEventHandler(ScreenManager screenManager) {
        super(screenManager);
    }

    public void addLoginButtonListener(JButton loginButton) {
        loginButton.addActionListener(e -> screenManager.showScreen("MainMenu"));
    }

    public void addRegisterButtonListener(JButton registerButton) {
        registerButton.addActionListener(e -> screenManager.showScreen("Register"));
    }

    public void addQuitButtonListener(JButton quitButton) {
        quitButton.addActionListener(e -> System.exit(0));
    }
}
