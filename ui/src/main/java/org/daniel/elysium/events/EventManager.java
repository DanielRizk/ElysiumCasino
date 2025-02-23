package org.daniel.elysium.events;

import org.daniel.elysium.screens.ScreenManager;

import javax.swing.*;

public abstract class EventManager {
    protected ScreenManager screenManager;

    public EventManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }





    public void aaddRegisterButtonListener(JButton registerButton) {
        registerButton.addActionListener(e -> screenManager.showScreen("Register"));
    }
}
