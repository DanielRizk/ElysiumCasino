package org.daniel.elysium;

import org.daniel.elysium.assets.AssetManager;
import org.daniel.elysium.assets.BackgroundAsset;
import org.daniel.elysium.assets.LogoAsset;
import org.daniel.elysium.debugUtils.DebugLevel;
import org.daniel.elysium.debugUtils.DebugPrint;
import org.daniel.elysium.games.baccarat.BaccaratPanel;
import org.daniel.elysium.games.blackjack.BlackjackPanel;
import org.daniel.elysium.games.ultimateTH.UltimatePanel;
import org.daniel.elysium.screens.*;
import org.daniel.elysium.user.database.DatabaseConnection;
import org.daniel.elysium.user.profile.UserProfile;

import javax.swing.*;
import java.awt.*;

/**
 * The main launcher for the Elysium Casino application.
 * Initializes the database, creates the main frame, and sets up the StateManager
 * to handle panel navigation.
 */
public class Launcher {

    // Instance reference to the splash screen
    private JWindow splashScreen;
    // Instance reference to the main frame
    private JFrame frame;


    /**
     * Initializes the launcher, displays the splash screen, and starts loading the application.
     * The actual initialization is performed in the background using a {@link SwingWorker}.
     * Once initialization is complete, the splash screen is hidden, and the main application is displayed.
     */
    public Launcher() {

        // Show splash screen while initializing the application
        showSplashScreen();

        // Load the application in the background
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                initializeApplication();
                return null;
            }

            @Override
            protected void done() {
                hideSplashScreen(); // Hide the splash screen
                showMainApplication(); // Show the main app
            }
        };

        worker.execute();
    }

    /**
     * Displays the splash screen with a loading message and an image.
     * The splash screen remains visible until the application finishes initializing.
     */
    private void showSplashScreen() {
        splashScreen = new JWindow();

        JLabel splashImage = new JLabel(AssetManager.getScaledIcon(BackgroundAsset.SPLASH_SCREEN, new Dimension(600, 500)));
        splashImage.setLayout(new BorderLayout());

        // Add a loading text
        JLabel loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setOpaque(false);
        splashImage.add(loadingLabel, BorderLayout.SOUTH);


        splashScreen.getContentPane().add(splashImage);
        splashScreen.setSize(600, 500); // Adjust splash screen size
        splashScreen.setLocationRelativeTo(null); // Center on screen
        splashScreen.setVisible(true);
    }

    /**
     * Hides and disposes of the splash screen once the application has finished loading.
     */
    private void hideSplashScreen() {
        if (splashScreen != null) {
            splashScreen.setVisible(false);
            splashScreen.dispose();
        }
    }

    /**
     * Initializes the core components of the application, including:
     * - Connecting to the database
     * - Setting the application theme based on the OS
     * - Creating the main application frame
     * - Registering all UI panels with the {@link StateManager}
     * - Setting up the application's entry point
     */
    private void initializeApplication() {
        // Perform startup tasks
        DatabaseConnection.initializeDatabase();
        DebugPrint.getInstance(DebugLevel.DEBUG); // Set to Disabled in production

        // Set the look and feel to match the OS
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFrame.setDefaultLookAndFeelDecorated(true); // Allows the OS theme to apply decorations
        } catch (Exception e) {
            DebugPrint.println(e);
        }

        // Creating main Frame
        frame = new JFrame("Elysium Casino");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1600, 1200));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.pack();

        // Set app icon
        ImageIcon icon = AssetManager.getIcon(LogoAsset.APP_ICON);
        frame.setIconImage(icon.getImage());

        // Creating main panel to hold all other panels
        JPanel mainPanel = new JPanel(new CardLayout());

        // Creating stateManager and registering all panels
        StateManager stateManager = createStateManager(mainPanel, frame);

        // Set the entry point of the app (initial panel)
        stateManager.switchPanel("UltimateTH");

        // Set initial focus to the main panel
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                mainPanel.requestFocusInWindow();
            }
        });

        // Add main panel and set frame to visible
        frame.add(mainPanel);
    }

    /**
     * Makes the main application window visible after the splash screen is closed.
     */
    private void showMainApplication() {
        frame.setVisible(true);
    }


    /**
     * Creates and configures the {@link StateManager}, registering all available UI panels.
     * This method ensures that all screens are accessible through the state manager.
     *
     * @param mainPanel The main container panel that will hold all registered UI panels.
     * @param frame     The main application frame.
     * @return The configured {@link StateManager} instance.
     */
    private static StateManager createStateManager(JPanel mainPanel, JFrame frame) {
        StateManager stateManager = new StateManager(mainPanel, frame);

        //TODO: Test user for testing
        stateManager.setProfile(new UserProfile("Test", "Test", 10000));

        // TODO: make a unique background for each game.
        stateManager.registerPanel("Login", new LoginPanel(stateManager));
        stateManager.registerPanel("Register", new RegisterPanel(stateManager));
        stateManager.registerPanel("MainMenu", new MainMenuPanel(stateManager));
        stateManager.registerPanel("Profile", new ProfilePanel(stateManager));
        stateManager.registerPanel("ChangePass", new UpdatePasswordPanel(stateManager));
        stateManager.registerPanel("Blackjack", new BlackjackPanel(stateManager));
        stateManager.registerPanel("Baccarat", new BaccaratPanel(stateManager));
        stateManager.registerPanel("UltimateTH", new UltimatePanel(stateManager));

        return stateManager;
    }
}
