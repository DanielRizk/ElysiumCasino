package org.daniel.elysium.user.database;

import org.daniel.elysium.debugUtils.DebugPrint;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles database connections and initialization for the application.
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:data/user_db.db";

    /**
     * Establishes a connection to the SQLite database.
     *
     * @return A connection object to the database.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /**
     * Initializes the database by creating the "users" table if it does not exist.
     * Also ensures the necessary "data" directory is created.
     */
    public static void initializeDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                balance REAL DEFAULT 0.00,
                gameMode INTEGER DEFAULT 0
            )
        """;

        try {
            // Get the current working directory
            String workingDir = System.getProperty("user.dir");
            Path baseDir = Paths.get(workingDir);

            // Define the directory to check/create (e.g., "data" directory)
            Path targetDir = baseDir.resolve("data");

            // Check if the directory exists
            if (!Files.exists(targetDir)) {
                // Create the directory (and parent directories if needed)
                Files.createDirectories(targetDir);
                DebugPrint.println("Directory created: " + targetDir, true);
            } else {
                DebugPrint.println("Directory already exists: " + targetDir, true);
            }
        } catch (Exception e) {
            DebugPrint.println(e, true);
        }

        try (Connection conn = getConnection(); var stmt = conn.createStatement()) {
            stmt.execute(sql);
            DebugPrint.println("Database initialized successfully", true);
        } catch (SQLException e) {
            DebugPrint.println(e, true);
        }
    }

    /**
     * Resets the database by dropping the "users" table and reinitializing it.
     */
    public static void resetDatabase() {
        String dropTableSql = "DROP TABLE IF EXISTS users";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            // Drop the existing table
            stmt.execute(dropTableSql);
            DebugPrint.println("Database reset: Table dropped successfully!", true);

            // Reinitialize the database
            initializeDatabase();
        } catch (SQLException e) {
            DebugPrint.println(e, true);
        }
    }
}
