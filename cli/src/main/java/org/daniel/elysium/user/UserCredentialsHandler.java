package org.daniel.elysium.user;

import org.daniel.elysium.cliUtils.CmdHelper;
import org.daniel.elysium.debugUtils.DebugPrint;
import org.daniel.elysium.user.database.DatabaseConnection;
import org.daniel.elysium.user.database.UserDAO;
import org.daniel.elysium.user.profile.UserProfile;

import java.util.Scanner;

/**
 * Handles user authentication and registration for the casino system.
 * <p>
 * This class manages user login, registration, and interaction with the database.
 * It prompts users to log in, create a new account, or exit the system.
 * </p>
 */
public class UserCredentialsHandler {

    /**
     * Initializes the database connection when the handler is created.
     */
    public UserCredentialsHandler() {
        DatabaseConnection.initializeDatabase();
    }

    /**
     * Prompts the user to log in, register, or exit.
     *
     * @param scanner the {@code Scanner} instance used to capture user input
     * @return a {@code UserProfile} object if authentication is successful, or {@code null} if the user exits
     */
    public UserProfile getUser(Scanner scanner) {
        switch (getPlayerOption(scanner)) {
            case 1 -> {
                return login(scanner);
            }
            case 2 -> {
                return register(scanner);
            }
            case 0 -> {
                return null; // User chose to exit
            }
            default -> {
                DebugPrint.println("Invalid Option, Try again!.");
            }
        }
        return new UserProfile(null, null, 0, 0);
    }

    /**
     * Prints a welcome message to the console.
     */
    private void printWelcomeMessage() {
        CmdHelper.clearCMD();
        DebugPrint.println("********************************************************");
        DebugPrint.println("********************************************************");
        DebugPrint.println("              WELCOME TO Elysium Casino                 ");
        DebugPrint.println("********************************************************");
        DebugPrint.println("********************************************************");
        DebugPrint.println();
    }

    /**
     * Prompts the user to choose an option: login, register, or exit.
     *
     * @param scanner the {@code Scanner} instance used to capture user input
     * @return the user's selected option as an integer
     */
    private int getPlayerOption(Scanner scanner) {
        printWelcomeMessage();
        DebugPrint.print("Login (1), Register (2), Quit (0): ");
        return scanner.nextInt();
    }

    /**
     * Handles user login by prompting for credentials and verifying them against the database.
     *
     * @param scanner the {@code Scanner} instance used to capture user input
     * @return a {@code UserProfile} object if login is successful, or a null-equivalent profile if authentication fails
     */
    private UserProfile login(Scanner scanner) {
        UserDAO userDAO = new UserDAO();
        UserProfile nullPlayer = new UserProfile(null, null, 0, 0);
        scanner.nextLine(); // Consume the newline character

        DebugPrint.print("Enter your username: ");
        String username = scanner.nextLine().trim();
        DebugPrint.print("Enter your pass: ");
        String pass = scanner.nextLine().trim();

        UserProfile player = userDAO.getUserByUsername(username);
        if (player != null) {
            if (player.getPass().equals(pass)) {
                CmdHelper.clearCMD();
                DebugPrint.println("Welcome " + username + ", your balance is: " + player.getBalance());
                return player;
            } else {
                DebugPrint.println("Wrong password, try again!");
                CmdHelper.haltCMD();
                return nullPlayer;
            }
        } else {
            DebugPrint.println("Username does not exist, make sure to spell correctly.");
            CmdHelper.haltCMD();
            return nullPlayer;
        }
    }

    /**
     * Handles user registration by collecting credentials and adding the user to the database.
     *
     * @param scanner the {@code Scanner} instance used to capture user input
     * @return a {@code UserProfile} object if registration is successful, or a null-equivalent profile if it fails
     */
    private UserProfile register(Scanner scanner) {
        scanner.nextLine(); // Consume the newline character
        UserDAO userDAO = new UserDAO();
        UserProfile nullPlayer = new UserProfile(null, null, 0, 0);

        DebugPrint.print("Enter username: ");
        String name = scanner.nextLine().trim();
        if (userDAO.getUserByUsername(name) != null) {
            DebugPrint.println("This username already exists, please choose a different username.");
            CmdHelper.haltCMD();
            return nullPlayer;
        }

        DebugPrint.print("Enter Password: ");
        String pass = scanner.nextLine().trim();
        DebugPrint.print("Enter Password again: ");
        String passAgain = scanner.nextLine().trim();
        if (!pass.equals(passAgain)) {
            DebugPrint.println("Passwords did not match, try again.");
            CmdHelper.haltCMD();
            return nullPlayer;
        }

        UserProfile player = userDAO.addUser(name, pass, 10000);
        if (player != null) {
            CmdHelper.clearCMD();
            DebugPrint.println("Welcome " + name + ", your balance is: " + player.getBalance());
            return player;
        } else {
            return nullPlayer;
        }
    }
}

