package org.daniel.elysium;

import org.daniel.elysium.debugUtils.DebugLevel;
import org.daniel.elysium.debugUtils.DebugPrint;
import org.daniel.elysium.screens.blackjack.BlackjackPanel;
import org.daniel.elysium.screens.panels.LoginPanel;
import org.daniel.elysium.screens.panels.MainMenuPanel;
import org.daniel.elysium.screens.panels.RegisterPanel;
import org.daniel.elysium.user.database.DatabaseConnection;
import org.daniel.elysium.user.profile.UserProfile;

import javax.swing.*;
import java.awt.*;


public class Launcher {

    public Launcher() {
        DatabaseConnection.initializeDatabase();
        DebugPrint.getInstance(DebugLevel.DEBUG);


        JFrame frame = new JFrame("Elysium Casino");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1800, 1200));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);

        JPanel mainPanel = new JPanel(new CardLayout());

        StateManager stateManager = new StateManager(mainPanel, frame);
        stateManager.registerPanel("Login", () -> new LoginPanel(stateManager));
        stateManager.registerPanel("MainMenu", () -> new MainMenuPanel(stateManager));
        stateManager.registerPanel("Register", () -> new RegisterPanel(stateManager));
        stateManager.registerPanel("Blackjack", () -> new BlackjackPanel(stateManager));

        stateManager.setProfile(new UserProfile("test", "test", 1000));
        stateManager.switchPanel("Blackjack");

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


