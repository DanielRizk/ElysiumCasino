package org.daniel.elysium;

/**
 * The entry point of the application.
 * <p>
 * This class initializes and starts the CLI-based application by
 * creating an instance of {@code CLILauncher} and invoking its launch method.
 * </p>
 */
public class Main {
    public static void main(String[] args) {
        CLILauncher launcher = new CLILauncher();
        launcher.launch();
    }
}
