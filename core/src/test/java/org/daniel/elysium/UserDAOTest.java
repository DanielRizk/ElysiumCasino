package org.daniel.elysium;

import org.daniel.elysium.user.database.DatabaseConnection;
import org.daniel.elysium.user.database.UserDAO;
import org.daniel.elysium.user.profile.UserProfile;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDAOTest {

    private UserDAO userDAO;

    /**
     * Runs once before all tests. Ensures the 'users' table exists in the database
     * (in case it's a fresh or in-memory setup). Uses the same DatabaseConnection
     * as the application.
     */
    @BeforeAll
    static void setupDatabase() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Create the 'users' table if it doesn't exist
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    balance REAL DEFAULT 0.00,
                    gameMode INTEGER DEFAULT 0
                )
                """
            );
        }
    }

    /**
     * Runs before each test method. Resets/cleans the database to ensure each test
     * runs in isolation and starts with a known empty state.
     * Also initializes a new UserDAO instance for each test.
     */
    @BeforeEach
    void setUp() throws SQLException {
        // This method truncates or re-creates the table, ensuring it is empty
        DatabaseConnection.resetDatabase();
        userDAO = new UserDAO();
    }

    /**
     * Test adding a new user that does not yet exist.
     * The result should be a non-null UserProfile matching the inserted data.
     */
    @Test
    @Order(1)
    void testAddUserSuccess() {
        UserProfile newUser = userDAO.addUser("john_doe", "secret123", 500.0);

        // Ensure the returned UserProfile is not null and that all fields match
        assertNotNull(newUser, "UserProfile should not be null if user is added successfully");
        assertEquals("john_doe", newUser.getName(), "Username should match what was inserted");
        assertEquals("secret123", newUser.getPass(), "Password should match what was inserted");
        assertEquals(500.0, newUser.getBalance(), 1e-9, "Balance should match what was inserted");
    }

    /**
     * Test attempting to add a user with a duplicate username.
     * The DAO should return null, indicating that the user already exists.
     */
    @Test
    @Order(2)
    void testAddUserDuplicate() {
        // Insert a user first
        userDAO.addUser("jane_doe", "pwd", 300.0);

        // Insert again with the same username
        UserProfile duplicate = userDAO.addUser("jane_doe", "differentPwd", 500.0);

        // Expect null because the username already exists
        assertNull(duplicate, "Should return null because username already exists");
    }

    /**
     * Test retrieving a user that does exist in the database.
     * The returned UserProfile should not be null and should match the stored data.
     */
    @Test
    @Order(3)
    void testGetUserByUsernameExists() {
        // Add a user to the DB
        userDAO.addUser("alice", "password", 1000.0);

        // Attempt to retrieve the same user
        UserProfile retrieved = userDAO.getUserByUsername("alice");
        assertNotNull(retrieved, "Should find 'alice' since it was just added");
        assertEquals("alice", retrieved.getName());
        assertEquals("password", retrieved.getPass());
        assertEquals(1000.0, retrieved.getBalance(), 1e-9);
    }

    /**
     * Test retrieving a user that does not exist in the database.
     * The method should return null.
     */
    @Test
    @Order(4)
    void testGetUserByUsernameNotFound() {
        // No user "does_not_exist" in DB, so getUserByUsername should return null
        UserProfile missing = userDAO.getUserByUsername("does_not_exist");
        assertNull(missing, "Should return null if user does not exist");
    }

    /**
     * Test successfully changing a user's password, given the correct old password.
     * The method should return the updated UserProfile with the new password.
     */
    @Test
    @Order(5)
    void testChangePasswordSuccess() {
        // Add a user, then change the password
        userDAO.addUser("bob", "oldpass", 400.0);
        UserProfile updated = userDAO.changePassword("bob", "oldpass", "newpass");

        // Should return a non-null updated profile with the new password
        assertNotNull(updated, "Should return updated user profile");
        assertEquals("newpass", updated.getPass(), "Password should be updated to newpass");
    }

    /**
     * Test changing a user's password with an incorrect old password.
     * The method should return null, indicating a failure.
     */
    @Test
    @Order(6)
    void testChangePasswordWrongOldPassword() {
        // Add a user with password "abc123"
        userDAO.addUser("charlie", "abc123", 500.0);

        // Try to change using the wrong old password
        UserProfile updated = userDAO.changePassword("charlie", "wrongOldPass", "newPass");
        assertNull(updated, "Should return null because old password is incorrect");
    }

    /**
     * Test changing the password for a user that doesn't exist in the database.
     * The method should return null, as there is no such user.
     */
    @Test
    @Order(7)
    void testChangePasswordUserNotFound() {
        // No user named "non_existent", so this should fail
        UserProfile updated = userDAO.changePassword("non_existent", "whatever", "newPass");
        assertNull(updated, "Should return null if user is not found");
    }

    /**
     * Test updating the balance of an existing user, both in the local
     * UserProfile object and in the database. Then confirm the new balance
     * by re-querying the user from the DB.
     */
    @Test
    @Order(8)
    void testUpdateBalance() {
        // Add a user with initial balance of 1000.0
        UserProfile user = userDAO.addUser("david", "pwd", 1000.0);
        assertNotNull(user);

        // Update balance to 2000.0
        userDAO.updateBalance(user, "david", 2000.0);
        assertEquals(2000.0, user.getBalance(), 1e-9, "Local UserProfile should be updated too");

        // Retrieve from DB to confirm
        UserProfile fromDb = userDAO.getUserByUsername("david");
        assertNotNull(fromDb, "Should still find 'david' in DB");
        assertEquals(2000.0, fromDb.getBalance(), 1e-9, "DB balance should match updated value");
    }

    /**
     * Test updating the game mode of an existing user, both in the local
     * UserProfile object and in the database. Then confirm the new game mode
     * by re-querying the user from the DB.
     */
    @Test
    @Order(8)
    void testUpdateGameMode() {
        // Add a user with initial balance of 1000.0
        UserProfile user = userDAO.addUser("david", "pwd", 1000.0);
        assertNotNull(user);

        // Update game mode to 4
        user.setGameMode(4);
        assertEquals(4, user.getGameMode(), "Local UserProfile should be updated too");

        // Retrieve from DB to confirm
        UserProfile fromDb = userDAO.getUserByUsername("david");
        assertNotNull(fromDb, "Should still find 'david' in DB");
        assertEquals(4, fromDb.getGameMode(), "DB game mode should match updated value");
    }

    /**
     * Test deleting an existing user from the database.
     * After deletion, the user profile should be null when retrieved by username.
     */
    @Test
    @Order(9)
    void testDeleteUser() {
        // Add a user
        userDAO.addUser("eve", "pw", 750.0);
        UserProfile existing = userDAO.getUserByUsername("eve");
        assertNotNull(existing, "Should find 'eve' before deletion");

        // Now delete the user
        userDAO.deleteUser("eve");

        // Confirm user no longer exists
        UserProfile removed = userDAO.getUserByUsername("eve");
        assertNull(removed, "Should be null because the user was deleted");
    }
}
