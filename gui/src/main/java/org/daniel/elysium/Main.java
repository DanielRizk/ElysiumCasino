package org.daniel.elysium;

import javax.swing.*;

/**
 * The entry point of the application.
 * Initializes and launches the application using Swing's event dispatch thread.
 */
public class Main {

    /**
     * The main method that starts the application.
     * Uses {@link SwingUtilities#invokeLater(Runnable)} to ensure GUI components
     * are created on the Event Dispatch Thread (EDT) for thread safety.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(UILauncher::new);
    }
}
