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
        // Initialization
        DatabaseConnection.initializeDatabase();
        DebugPrint.getInstance(DebugLevel.DEBUG); // Set to Disabled in production

        // Creating main Frame
        JFrame frame = new JFrame("Elysium Casino");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1800, 1200));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.pack();

        // Creating main panel to hold all other panels
        JPanel mainPanel = new JPanel(new CardLayout());

        // Creating stateManager and register all panels
        StateManager stateManager = createStateManager(mainPanel, frame);

        // Set entry point to the app
        stateManager.switchPanel("Blackjack");

        // Set initial focus to the main panel
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                mainPanel.requestFocusInWindow();
            }
        });

        // Add main panel and set frame to visible
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Create StateManager and register all panels
     */
    private static StateManager createStateManager(JPanel mainPanel, JFrame frame) {
        StateManager stateManager = new StateManager(mainPanel, frame);
        stateManager.registerPanel("Login", () -> new LoginPanel(stateManager));
        stateManager.registerPanel("MainMenu", () -> new MainMenuPanel(stateManager));
        stateManager.registerPanel("Register", () -> new RegisterPanel(stateManager));
        stateManager.registerPanel("Blackjack", () -> new BlackjackPanel(stateManager));

        // Test user for testing
        stateManager.setProfile(new UserProfile("Test", "Test", 1000));
        return stateManager;
    }
}


