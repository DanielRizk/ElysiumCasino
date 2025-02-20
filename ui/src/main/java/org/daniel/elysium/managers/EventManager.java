package org.daniel.elysium.managers;

import javax.swing.*;

public class EventManager {
    private ScreenManager screenManager;

    public EventManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    public void addLoginButtonListener(JButton loginButton) {
        loginButton.addActionListener(e -> screenManager.showScreen("MainMenu"));
    }

    public void addQuitButtonListener(JButton quitButton) {
        quitButton.addActionListener(e -> System.exit(0));
    }

    public void addLogoutButtonListener(JButton logoutButton) {
        logoutButton.addActionListener(e -> screenManager.showScreen("Login"));
    }

    public void addRegisterButtonListener(JButton registerButton) {
        registerButton.addActionListener(e -> screenManager.showScreen("Register"));
    }
}
