package org.daniel.elysium;

import org.daniel.elysium.debugUtils.DebugLevel;
import org.daniel.elysium.debugUtils.DebugPrint;
import org.daniel.elysium.events.handlers.LoginEventHandler;
import org.daniel.elysium.events.handlers.MainMenuEventHandler;
import org.daniel.elysium.events.handlers.RegisterEventHandler;
import org.daniel.elysium.models.BJCard;
import org.daniel.elysium.models.Card;
import org.daniel.elysium.models.Shoe;
import org.daniel.elysium.screens.ScreenManager;
import org.daniel.elysium.screens.blackjack.BlackjackPanel;
import org.daniel.elysium.screens.panels.LoginPanel;
import org.daniel.elysium.screens.panels.MainMenuPanel;
import org.daniel.elysium.screens.panels.RegisterPanel;
import org.daniel.elysium.user.database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class Launcher {

    public Launcher() {
        DatabaseConnection.initializeDatabase();
        DebugPrint.getInstance(DebugLevel.DEBUG);


        JFrame frame = new JFrame("Elysium Casino");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1800, 1200));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(true);

        JPanel mainPanel = new JPanel(new CardLayout());
        ScreenManager screenManager = new ScreenManager(mainPanel);

        /*
        *
        StateManager stateManager = new StateManager(frame.getContentPane());
        stateManager.registerPanel("Login", () -> new LoginPanel(stateManager));
        stateManager.registerPanel("MainMenu", () -> new MainMenuPanel(stateManager));
        stateManager.registerPanel("Register", () -> new RegisterPanel(stateManager));
        stateManager.registerPanel("Blackjack", () -> new BlackjackPanel(stateManager));

        stateManager.switchPanel("Login");
        *
        * */

        screenManager.addScreen(new LoginPanel(screenManager), "Login");
        screenManager.addScreen(new MainMenuPanel(new MainMenuEventHandler(screenManager)), "MainMenu");
        screenManager.addScreen(new RegisterPanel(screenManager), "Register");
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


