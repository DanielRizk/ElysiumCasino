package org.daniel.elysium.events.handlers;

import org.daniel.elysium.events.EventManager;
import org.daniel.elysium.screens.ScreenManager;

import javax.swing.*;

public class MainMenuEventHandler extends EventManager {
    public MainMenuEventHandler(ScreenManager screenManager) {
        super(screenManager);
    }

    public void addLogoutButtonListener(JButton logoutButton) {
        logoutButton.addActionListener(e -> screenManager.showScreen("Login"));
    }

    public void addBlackjackButtonListener(JButton blackjackButton){
        blackjackButton.addActionListener(e -> screenManager.showScreen("Blackjack"));
    }
}
