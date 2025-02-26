package org.daniel.elysium;

import org.daniel.elysium.user.profile.UserProfile;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class StateManager {
    private final Container container;
    private UserProfile profile = null;
    private JPanel currentPanel = null;
    private final Map<String, Supplier<JPanel>> panelSuppliers;
    private final JFrame frame;

    public StateManager(Container container, JFrame frame) {
        this.container = container;
        this.panelSuppliers = new HashMap<>();
        this.frame = frame;
    }

    public void registerPanel(String key, Supplier<JPanel> panelSupplier) {
        panelSuppliers.put(key, panelSupplier);
    }

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

    public boolean isUserLoggedIn(){
        return profile != null;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public JFrame getFrame(){
        return frame;
    }
}
