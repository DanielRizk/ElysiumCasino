package org.daniel.elysium.interfaces;

/**
 * Interface for UI panels that need to be reset or restarted.
 * This interface defines methods for resetting the state and handling restarts, allowing for a consistent approach to reinitializing components.
 */
public interface Resettable {

    /**
     * Resets the panel to its initial state.
     * This method should clear any data or state to bring the component back to its default configuration.
     */
    void reset();

    /**
     * Handles actions required when a panel needs to be restarted.
     * This method is intended for reinitializing panels or performing necessary cleanup and setup when restarting.
     */
    void onRestart();
}
