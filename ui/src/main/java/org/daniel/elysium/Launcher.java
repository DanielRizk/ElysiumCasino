package org.daniel.elysium;

import org.daniel.elysium.events.handlers.BlackjackEventHandler;
import org.daniel.elysium.events.handlers.LoginEventHandler;
import org.daniel.elysium.events.handlers.MainMenuEventHandler;
import org.daniel.elysium.events.handlers.RegisterEventHandler;
import org.daniel.elysium.screens.ScreenManager;
import org.daniel.elysium.screens.blackjack.BlackjackPanel;
import org.daniel.elysium.screens.panels.LoginPanel;
import org.daniel.elysium.screens.panels.MainMenuPanel;
import org.daniel.elysium.screens.panels.RegisterPanel;

import javax.swing.*;
import java.awt.*;


public class Launcher {

    public Launcher() {
        JFrame frame = new JFrame("Elysium Casino");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1800, 1200));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(true);

        JPanel mainPanel = new JPanel(new CardLayout());
        ScreenManager screenManager = new ScreenManager(mainPanel);

        screenManager.addScreen(new LoginPanel(new LoginEventHandler(screenManager)), "Login");
        screenManager.addScreen(new MainMenuPanel(new MainMenuEventHandler(screenManager)), "MainMenu");
        screenManager.addScreen(new RegisterPanel(new RegisterEventHandler(screenManager)), "Register");
        screenManager.addScreen(new BlackjackPanel(screenManager), "Blackjack");
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                mainPanel.requestFocusInWindow();
            }
        });

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}


