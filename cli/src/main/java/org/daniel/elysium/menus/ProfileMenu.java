package org.daniel.elysium.menus;

import org.daniel.elysium.cliUtils.CmdHelper;
import org.daniel.elysium.debugUtils.DebugPrint;
import org.daniel.elysium.interfaces.MenuOptionCLI;
import org.daniel.elysium.user.database.UserDAO;
import org.daniel.elysium.user.profile.UserProfile;

import java.util.Scanner;

/**
 * A console-based menu that allows users to modify their profile settings,
 * including changing their password or deleting their account.
 */
public class ProfileMenu implements MenuOptionCLI {
    private final Scanner scanner;
    private int exitCode = 0;

    /**
     * Constructs a {@code ProfileMenu} with a provided {@link Scanner}
     * for handling console input.
     *
     * @param scanner the {@code Scanner} to use for reading user input
     */
    public ProfileMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Starts the profile menu loop, where users can:
     * <ul>
     *     <li>Change their password</li>
     *     <li>Delete their account</li>
     *     <li>Return to the previous menu</li>
     * </ul>
     * If the user chooses "Back (0)", the method returns immediately
     * to break out of the menu loop.
     *
     * @param profile the current user's {@link UserProfile}
     */
    @Override
    public void start(UserProfile profile) {
        // A continuous loop until user chooses 0 (Back) or account deletion triggers an exit.
        while (true) {
            // Clear the console (if supported) for a cleaner UI
            CmdHelper.clearCMD();

            // Get the user's menu option
            switch (getPlayerOption(scanner)) {
                case 0 -> {
                    CmdHelper.clearCMD();
                    return;
                }
                case 1 -> {
                    changePassword(profile);
                }
                case 2 -> {
                    // If the user deletes the account, set exit code and return
                    if (deleteAccount(profile)) {
                        exitCode = 1;
                        return;
                    }
                }
                default -> {
                    // Handle invalid menu selections
                    DebugPrint.println("Invalid Option, Try again!.");
                }
            }
        }
    }

    /**
     * Returns an exit code to indicate if the user has deleted the account or not.
     * Typically, 0 = normal exit, 1 = account deletion occurred.
     *
     * @return an integer representing the exit code
     */
    @Override
    public int exitCode() {
        return exitCode;
    }

    /**
     * Prompts the user to change their password.
     * <p>
     * Verifies the old password, ensures new password is different
     * and then updates the database via {@link UserDAO}.
     *
     * @param profile the current user's {@link UserProfile}
     */
    private void changePassword(UserProfile profile) {
        // Scanner fix: consume leftover newline from previous input
        scanner.nextLine();

        UserDAO userDAO = profile.getUser();

        // Ask for the old password and check if it matches
        DebugPrint.print("Enter Old Password: ");
        String oldPass = scanner.nextLine().trim();

        if (!oldPass.equals(profile.getPass())) {
            DebugPrint.println("Old password does not match, try again!");
            CmdHelper.haltCMD();
            return;
        }

        // Prompt for new password
        DebugPrint.print("Enter Password: ");
        String pass = scanner.nextLine().trim();

        // Ensure new password is not identical to old password
        if (oldPass.equals(pass)) {
            DebugPrint.println("New pass cannot be the same as the old pass, try again!");
            CmdHelper.haltCMD();
            return;
        }

        // Confirm the new password
        DebugPrint.print("Enter Password again: ");
        String passAgain = scanner.nextLine().trim();

        if (!pass.equals(passAgain)) {
            DebugPrint.println("Passwords did not match, try again.");
            CmdHelper.haltCMD();
            return;
        }

        // If all checks pass, update the password in the database
        userDAO.changePassword(profile.getName(), oldPass, pass);
        DebugPrint.println("Password has been updated");
        CmdHelper.haltCMD();
    }

    /**
     * Asks the user to confirm account deletion.
     * If confirmed, deletes the user from the database.
     *
     * @param profile the current user's {@link UserProfile}
     * @return {@code true} if the user confirmed deletion (account is deleted),
     *         {@code false} otherwise
     */
    private boolean deleteAccount(UserProfile profile) {
        // Prompt for confirmation
        DebugPrint.print("Are you sure you want to delete your account? Yes (0), No (1): ");
        if (scanner.nextInt() == 0) {
            // Consume leftover newline
            scanner.nextLine();

            // Delete the user from the database
            UserDAO userDAO = profile.getUser();
            userDAO.deleteUser(profile.getName());

            DebugPrint.println("Account deleted!, Press any key to continue");
            CmdHelper.haltCMD();  // Wait for user input before clearing/moving on
            return true;
        }
        return false;
    }

    /**
     * Prompts the player to select an option from the menu.
     * <p>
     * Possible options:
     * <ul>
     *     <li>1: Change password</li>
     *     <li>2: Delete account</li>
     *     <li>0: Go back</li>
     * </ul>
     *
     * @param scanner the {@code Scanner} instance used to capture user input
     * @return the selected option as an integer
     */
    private int getPlayerOption(Scanner scanner) {
        DebugPrint.print("Change password (1), Delete account (2), Back (0): ");
        return scanner.nextInt();
    }
}
