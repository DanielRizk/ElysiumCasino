package org.daniel.elysium;

import org.daniel.elysium.debugUtils.DebugLevel;
import org.daniel.elysium.debugUtils.DebugPrint;
import org.daniel.elysium.interfaces.MenuOptionCLI;
import org.daniel.elysium.user.UserCredentialsHandler;
import org.daniel.elysium.user.profile.UserProfile;

import java.util.Scanner;

/**
 * Handles the command-line interface (CLI) launch process.
 * <p>
 * This class manages user authentication and menu navigation in a loop,
 * ensuring that a valid user session is established before accessing the menu.
 * </p>
 */
public class CLILauncher {

    /**
     * Starts the CLI application.
     * <p>
     * The method initializes the debugging system, handles user login,
     * and continuously presents menus to the user until they log out or exit.
     * </p>
     */
    public void launch() {
        DebugPrint.getInstance(DebugLevel.PRINT);
        Scanner scanner = new Scanner(System.in);

        UserCredentialsHandler userLoginHandler = new UserCredentialsHandler();
        UserProfile profile;

        while (true) {
            profile = userLoginHandler.getUser(scanner);
            if (profile != null && profile.getName() != null) {
                MenuFactory factory = new MenuFactory();
                while (true) {
                    MenuOptionCLI menu = factory.getMenu(scanner);
                    if (menu != null) {
                        menu.start(profile);
                    } else {
                        break;
                    }
                }
            } else if (profile == null) {
                break;
            }
        }
        scanner.close();
    }
}

