package org.daniel.elysium;

import org.daniel.elysium.debugUtils.DebugPrint;
import org.daniel.elysium.interfaces.MenuOptionCLI;
import org.daniel.elysium.menus.SecretTopUpCode;
import org.daniel.elysium.menus.games.BlackjackCLI;

import java.util.Scanner;

/**
 * Factory class responsible for creating menu options in the CLI.
 * <p>
 * This class provides the appropriate menu instance based on the user's selection.
 * </p>
 */
public class MenuFactory {

    /**
     * Retrieves the selected menu option based on user input.
     *
     * @param scanner the {@code Scanner} instance used to capture user input
     * @return a {@code MenuOptionCLI} instance corresponding to the selected menu, or {@code null} if exiting
     */
    protected MenuOptionCLI getMenu(Scanner scanner) {
        switch (getPlayerOption(scanner)) {
            case 1 -> {
                return new BlackjackCLI();
            }
            case 2 -> {
                return null; // Placeholder for Baccarat CLI
            }
            case 3 -> {
                return null; // Placeholder for Ultimate Texas Hold'em CLI
            }
            case 2684 -> {
                return new SecretTopUpCode();
            }
            case 0 -> {
                return null; // Exit option
            }
            default -> {
                DebugPrint.println("Invalid Option, Try again!.");
            }
        }
        return null;
    }

    /**
     * Prompts the player to select a game option.
     *
     * @param scanner the {@code Scanner} instance used to capture user input
     * @return the selected option as an integer
     */
    private int getPlayerOption(Scanner scanner) {
        DebugPrint.print("Choose game: Blackjack (1), Baccarat (2), Ultimate TH (3), Quit (0): ");
        return scanner.nextInt();
    }
}

