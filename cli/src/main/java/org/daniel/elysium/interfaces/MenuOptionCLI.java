package org.daniel.elysium.interfaces;

import org.daniel.elysium.user.profile.UserProfile;

/**
 * Represents a command-line menu option.
 * <p>
 * This interface defines a method to start a menu option using a given user profile.
 * Implementing classes will provide specific functionality for each menu option.
 * </p>
 */
public interface MenuOptionCLI {

    /**
     * Starts the menu option with the given user profile.
     *
     * @param profile the {@code UserProfile} of the current user
     */
    void start(UserProfile profile);
}

