package org.daniel.elysium.user.database;

import org.daniel.elysium.debugUtils.DebugPrint;
import org.daniel.elysium.user.profile.UserProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object (DAO) for managing user-related database operations.
 */
public class UserDAO {

    /**
     * Adds a new user to the database if the username does not already exist.
     *
     * @param username The unique username for the user.
     * @param password The user's password.
     * @param balance  The initial balance for the user.
     * @return A UserProfile object if the user was successfully created, otherwise null.
     */
    public UserProfile addUser(String username, String password, double balance) {
        if (userExists(username)) {
            DebugPrint.println("User already exists with username: " + username, true);
            return null;
        }

        String sql = "INSERT INTO users (username, password, balance, gameMode) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setDouble(3, balance);
            pstmt.setInt(4, 0);
            pstmt.executeUpdate();
            DebugPrint.println("User added successfully!", true);
            return new UserProfile(username, password, balance, 0);
        } catch (SQLException e) {
            DebugPrint.println(e, true);
        }

        return null;
    }

    /**
     * Checks if a user with the given username exists in the database.
     *
     * @param username The username to check.
     * @return True if the user exists, false otherwise.
     */
    private boolean userExists(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if a user with the username exists
        } catch (SQLException e) {
            DebugPrint.println(e, true);
            return false;
        }
    }

    /**
     * Retrieves a user's profile based on the username.
     *
     * @param username The username of the user.
     * @return A UserProfile object if the user is found, otherwise null.
     */
    public UserProfile getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                DebugPrint.println("ID: " + rs.getInt("id"), true);
                DebugPrint.println("Username: " + rs.getString("username"), true);
                DebugPrint.println("Balance: " + rs.getDouble("balance"), true);
                DebugPrint.println("GameMode: " + rs.getInt("gameMode"), true);
                return new UserProfile(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getDouble("balance"),
                        rs.getInt("gameMode"));
            } else {
                DebugPrint.println("User not found.", true);
                return null;
            }
        } catch (SQLException e) {
            DebugPrint.println(e, true);
        }
        return null;
    }

    /**
     * Changes the password for a user if the old password is correct.
     *
     * @param username    The username of the user.
     * @param oldPassword The current password of the user.
     * @param newPassword The new password to set.
     * @return The updated UserProfile object if successful, otherwise null.
     */
    public UserProfile changePassword(String username, String oldPassword, String newPassword) {
        // First, verify that the old password is correct
        UserProfile player = getUserByUsername(username);
        if (player == null) {
            DebugPrint.println("User not found.", true);
            return null;
        }

        if (!player.getPass().equals(oldPassword)) {
            DebugPrint.println("Old password is incorrect.", true);
            return null;
        }

        // Update the password in the database
        String sql = "UPDATE users SET password = ? WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword); // In a real application, hash the new password
            pstmt.setString(2, username);
            pstmt.executeUpdate();
            DebugPrint.println("Password updated successfully!", true);

            // Update the UserProfile object with the new password
            player.setPass(newPassword);
            return player;
        } catch (SQLException e) {
            DebugPrint.println(e, true);
        }

        return null;
    }

    /**
     * Updates the balance for a given user in the database.
     *
     * @param player     The UserProfile object representing the user.
     * @param username   The username of the user.
     * @param newBalance The new balance to set.
     */
    public void updateBalance(UserProfile player, String username, double newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
            DebugPrint.println("Balance updated successfully!", true);
            player.setBalance(newBalance);
        } catch (SQLException e) {
            DebugPrint.println(e, true);
        }
    }

    /**
     * Updates the game mode for a given user in the database.
     *
     * @param player     The UserProfile object representing the user.
     * @param username   The username of the user.
     * @param gameMode   The new game mode to set.
     */
    public void updateGameMode(UserProfile player, String username, int gameMode) {
        String sql = "UPDATE users SET gameMode = ? WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, gameMode);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
            DebugPrint.println("Game mode updated successfully!", true);
        } catch (SQLException e) {
            DebugPrint.println(e, true);
        }
    }

    /**
     * Deletes a user from the database based on their username.
     *
     * @param username The username of the user to delete.
     */
    public void deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                DebugPrint.println("User deleted successfully!", true);
            } else {
                DebugPrint.println("User not found.", true);
            }
        } catch (SQLException e) {
            DebugPrint.println(e, true);
        }
    }
}

