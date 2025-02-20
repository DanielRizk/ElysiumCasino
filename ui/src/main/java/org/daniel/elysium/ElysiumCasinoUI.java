package org.daniel.elysium;

import org.daniel.elysium.managers.EventManager;
import org.daniel.elysium.managers.ScreenManager;
import org.daniel.elysium.screens.LoginPanel;
import org.daniel.elysium.screens.MainMenuPanel;
import org.daniel.elysium.screens.RegisterPanel;

import javax.swing.*;
import java.awt.*;


public class ElysiumCasinoUI {
    private JFrame frame;
    private ScreenManager screenManager;
    private EventManager eventManager;

    public ElysiumCasinoUI() {
        frame = new JFrame("Elysium Casino");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1200, 800));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(true);

        JPanel mainPanel = new JPanel(new CardLayout());
        screenManager = new ScreenManager(mainPanel);
        eventManager = new EventManager(screenManager);

        screenManager.addScreen(new LoginPanel(screenManager, eventManager), "Login");
        screenManager.addScreen(new MainMenuPanel(screenManager, eventManager), "MainMenu");
        screenManager.addScreen(new RegisterPanel(screenManager, eventManager), "Register");

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


