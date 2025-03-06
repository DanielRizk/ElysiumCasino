package org.daniel.elysium;

import org.daniel.elysium.interfaces.Resettable;
import org.daniel.elysium.user.profile.UserProfile;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Manages the application's state, including user profile and panel navigation.
 * Supports both dynamic panel creation and cached panel switching using CardLayout.
 */
public class StateManager {

    /** The container where panels are displayed. */
    private final Container container;

    /** CardLayout container for persistent panels. */
    private final JPanel cardPanel;
    private final CardLayout cardLayout;

    /** The current user's profile, or {@code null} if no user is logged in. */
    private UserProfile profile = null;

    /** The currently displayed panel (for the disposable method). */
    private JPanel currentPanel = null;

    /** Stores panel suppliers for lazy initialization (disposable method). */
    private final Map<String, Supplier<JPanel>> panelSuppliers;

    /** Stores cached panel instances for CardLayout switching. */
    private final Map<String, JPanel> cachedPanels;

    /** The main application frame. */
    private final JFrame frame;

    /** Defines the new game start mode. [Auto, Manual] */
    private boolean autoStartNewGame = true;

    /**
     * Constructs a StateManager with the specified container and frame.
     *
     * @param container The container in which panels will be switched.
     * @param frame     The main application frame.
     */
    public StateManager(Container container, JFrame frame) {
        this.container = container;
        this.frame = frame;
        this.panelSuppliers = new HashMap<>();
        this.cachedPanels = new HashMap<>();

        // Set up CardLayout for the alternative panel switching method
        this.cardLayout = new CardLayout();
        this.cardPanel = new JPanel(cardLayout);
        container.add(cardPanel, BorderLayout.CENTER);
    }

    /* ==============================
       METHOD 1: DISPOSABLE PANEL APPROACH
       - Loads a new panel every time (fresh UI, but slower)
       ============================== */

    /**
     * Registers a new panel supplier for dynamic (disposable) panel switching.
     *
     * @param key           The unique key to identify the panel.
     * @param panelSupplier The supplier that provides a fresh instance of the panel when needed.
     */
    public void registerDisposablePanel(String key, Supplier<JPanel> panelSupplier) {
        panelSuppliers.put(key, panelSupplier);
    }

    /**
     * Switches the displayed panel using the disposable approach.
     * Creates a fresh instance every time a panel is loaded.
     *
     * @param key The key of the panel to switch to.
     * @throws IllegalArgumentException If no panel is registered with the given key.
     */
    public void switchDisposablePanel(String key) {
        Supplier<JPanel> supplier = panelSuppliers.get(key);
        if (supplier == null) {
            throw new IllegalArgumentException("No panel registered with key: " + key);
        }

        JPanel newPanel = supplier.get(); // Always creates a fresh instance

        SwingUtilities.invokeLater(() -> {
            if (currentPanel != null) {
                container.remove(currentPanel);
            }

            currentPanel = newPanel;
            container.add(currentPanel, BorderLayout.CENTER);
            container.revalidate();
            container.repaint();
        });
    }

    /* ==============================
       METHOD 2: CARD LAYOUT PANEL APPROACH
       - Stores panels in memory for faster switching
       ============================== */

    /**
     * Registers a new panel for CardLayout-based switching.
     * Unlike the disposable approach, this stores a single instance of the panel.
     *
     * @param key    The unique key to identify the panel.
     * @param panel  The panel instance to store in CardLayout.
     */
    public void registerPanel(String key, JPanel panel) {
        cachedPanels.put(key, panel);
        cardPanel.add(panel, key);
    }

    /**
     * Switches the displayed panel using the CardLayout approach.
     * This method does NOT create a new panel instance but instead shows a cached one.
     *
     * @param key The key of the panel to switch to.
     * @throws IllegalArgumentException If no panel is registered with the given key.
     */
    public void switchPanel(String key) {

        // Ensure it implements Resettable before casting
        if (currentPanel instanceof Resettable usedPanel) {
            usedPanel.reset();
        }

        if (!cachedPanels.containsKey(key)) {
            throw new IllegalArgumentException("No panel registered with key: " + key);
        }

        SwingUtilities.invokeLater(() -> {
            // Get the panel that will be shown
            currentPanel = cachedPanels.get(key);

            if (currentPanel instanceof Resettable resettablePanel) {
                resettablePanel.onRestart(); // Call method AFTER the UI switches
            }

            // Switch the panel
            cardLayout.show(cardPanel, key);
        });
    }

    /* ==============================
       COMMON METHODS
       ============================== */

    /**
     * Returns the current user's profile.
     *
     * @return The {@link UserProfile} of the logged-in user, or {@code null} if no user is logged in.
     */
    public UserProfile getProfile() {
        return profile;
    }

    /**
     * Sets the user profile for the current session.
     *
     * @param profile The {@link UserProfile} to set.
     */
    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    /**
     * Returns the main application frame.
     *
     * @return The application's {@link JFrame}.
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Checks if a user is currently logged in.
     *
     * @return {@code true} if a user is logged in, otherwise {@code false}.
     */
    public boolean isUserLoggedIn() {
        return profile != null;
    }

    /**
     * Checks whether the game is set to automatically start a new round after the current game ends.
     *
     * @return {@code true} if auto-start is enabled, otherwise {@code false}
     */
    public boolean isAutoStartNewGame() {
        return autoStartNewGame;
    }

    /**
     * Sets whether the game should automatically start a new round after the current game ends.
     *
     * @param autoStartNewGame {@code true} to enable auto-start, {@code false} to disable it
     */
    public void setAutoStartNewGame(boolean autoStartNewGame) {
        this.autoStartNewGame = autoStartNewGame;
    }

}
