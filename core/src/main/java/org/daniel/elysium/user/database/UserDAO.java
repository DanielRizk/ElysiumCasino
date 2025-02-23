package org.daniel.elysium.user.database;

import org.daniel.elysium.debugUtils.DebugPrint;
import org.daniel.elysium.user.profile.UserProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public UserProfile addUser(String username, String password, double balance) {
        if (userExists(username)) {
            DebugPrint.println("User already exists with username: " + username, true);
            return null;
        }

        String sql = "INSERT INTO users (username, password, balance) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // In a real application, hash the password
            pstmt.setDouble(3, balance);
            pstmt.executeUpdate();
            DebugPrint.println("User added successfully!", true);
            return new UserProfile(username, password, balance);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean userExists(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if a user with the username exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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
                return new UserProfile(rs.getString("username"), rs.getString("password"), rs.getDouble("balance"));
            } else {
                DebugPrint.println("User not found.", true);
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

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

            // Update the PlayerProfile object with the new password
            player.setPass(newPassword);
            return player;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public UserProfile updateBalance(UserProfile player, String username, double newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
            DebugPrint.println("Balance updated successfully!", true);
            player.setBalance(newBalance);
            return player;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

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
            e.printStackTrace();
        }
    }
}
