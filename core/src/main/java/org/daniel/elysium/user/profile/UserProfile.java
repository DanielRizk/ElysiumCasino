package org.daniel.elysium.user.profile;

import org.daniel.elysium.user.database.UserDAO;

/**
 * Represents a user's profile, including credentials and balance.
 */
public class UserProfile {
    private final String name;
    private String pass;
    private double balance;
    private int gameMode;
    private final UserDAO userDAO;

    /**
     * Creates a new user profile with the specified name, password, and balance.
     *
     * @param name     The username.
     * @param pass     The password.
     * @param balance  The initial account balance.
     * @param gameMode The integer defining which mode each game runs with.
     */
    public UserProfile(String name, String pass, double balance, int gameMode) {
        this.name = name;
        this.pass = pass;
        this.balance = balance;
        this.gameMode = gameMode;
        this.userDAO = new UserDAO();
    }

    /**
     * Returns the user object.
     *
     * @return The username as {@code UserDAO}.
     */
    public UserDAO getUser() {
        return userDAO;
    }

    /**
     * Returns the username.
     *
     * @return The username.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the user's password.
     *
     * @return The password.
     */
    public String getPass() {
        return pass;
    }

    /**
     * Returns the user's current balance.
     *
     * @return The account balance.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets the user's balance.
     *
     * @param balance The new balance to set.
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Returns the user's current game mode.
     *
     * @return The account game mode.
     */
    public int getGameMode() {
        return gameMode;
    }

    /**
     * Sets the user's game mode.
     *
     * @param gameMode The new game mode to set.
     */
    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
        userDAO.updateGameMode(this, this.name, this.gameMode);
    }

    /**
     * Sets the user's password.
     *
     * @param pass The new password to set.
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * Increases the user's balance by a specified amount and updates the database.
     *
     * @param amount The amount to add to the balance.
     */
    public void increaseBalanceBy(double amount) {
        this.balance += amount;
        userDAO.updateBalance(this, this.name, this.balance);
    }

    /**
     * Decreases the user's balance by a specified amount and updates the database.
     *
     * @param amount The amount to subtract from the balance.
     */
    public void decreaseBalanceBy(double amount) {
        this.balance -= amount;
        userDAO.updateBalance(this, this.name, this.balance);
    }
}
