package org.daniel.elysium.user.database;

import org.daniel.elysium.debugUtils.DebugPrint;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:data/user_db.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase(){
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                balance REAL DEFAULT 0.00
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
            e.printStackTrace();
        }

        try(Connection conn = getConnection(); var stmt = conn.createStatement()){
            stmt.execute(sql);
            DebugPrint.println("Database initialized successfully", true);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
    }
}
