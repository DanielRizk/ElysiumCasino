package org.daniel.elysium;

import org.daniel.elysium.user.profile.UserProfile;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Manages the application's state, including user profile and panel navigation.
 * Handles switching between different UI panels dynamically.
 */
public class StateManager {

    /** The container where panels are displayed. */
    private final Container container;

    /** The current user's profile, or {@code null} if no user is logged in. */
    private UserProfile profile = null;

    /** The currently displayed panel. */
    private JPanel currentPanel = null;

    /** A mapping of panel keys to their corresponding suppliers for dynamic instantiation. */
    private final Map<String, Supplier<JPanel>> panelSuppliers;

    /** The main application frame. */
    private final JFrame frame;

    /** The minimum bet allowed in the game. */
    public static final int MIN_BET = 10;

    /**
     * Constructs a StateManager with the specified container and frame.
     *
     * @param container The container in which panels will be switched.
     * @param frame     The main application frame.
     */
    public StateManager(Container container, JFrame frame) {
        this.container = container;
        this.panelSuppliers = new HashMap<>();
        this.frame = frame;
    }

    /**
     * Registers a new panel with a given key for dynamic switching.
     *
     * @param key           The unique key to identify the panel.
     * @param panelSupplier The supplier that provides an instance of the panel when needed.
     */
    public void registerPanel(String key, Supplier<JPanel> panelSupplier) {
        panelSuppliers.put(key, panelSupplier);
    }

    /**
     * Switches the displayed panel to the one associated with the given key.
     *
     * @param key The key of the panel to switch to.
     * @throws IllegalArgumentException If no panel is registered with the given key.
     */
    public void switchPanel(String key) {
        Supplier<JPanel> supplier = panelSuppliers.get(key);
        if (supplier == null) {
            throw new IllegalArgumentException("No panel registered with key: " + key);
        }

        // Remove the current panel from the container.
        if (currentPanel != null) {
            container.remove(currentPanel);
        }

        // Create a new instance of the requested panel.
        currentPanel = supplier.get();

        // Add the new panel to the container.
        container.add(currentPanel, BorderLayout.CENTER);
        container.revalidate();
        container.repaint();
    }

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
}
