package org.daniel.elysium.cliUtils;

import org.daniel.elysium.debugUtils.DebugPrint;

import java.io.IOException;

/**
 * A utility class for handling command-line interface (CLI) operations.
 * <p>
 * Provides methods for clearing the console and halting execution until user input.
 * </p>
 */
public class CmdHelper {

    /**
     * Clears the command-line interface (CLI).
     * <p>
     * Uses ANSI escape codes to clear the console screen.
     * Works on most UNIX-based systems but may not function in some Windows environments.
     * </p>
     */
    public static void clearCMD() {
        try {
            DebugPrint.print("\033[H\033[2J");
            DebugPrint.flush();
        } catch (Exception e) {
            DebugPrint.println(e, true);
        }
    }

    /**
     * Halts execution and waits for user input before proceeding.
     * <p>
     * Displays a message prompting the user to press any key, then waits for input.
     * Note: {@code System.in.read()} captures a single byte and may not behave as expected in all terminals.
     * </p>
     */
    public static void haltCMD() {
        DebugPrint.println("Press any key to continue...");
        try {
            DebugPrint.println(System.in.read(), true);
        } catch (IOException e) {
            DebugPrint.println(e, true);
        }
    }
}

