package juegosretro.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {
    private DatabaseInitializer() {
    }

    public static void initialize() throws SQLException {
        loadDriver();
        createDatabase();
        createTables();
    }

    private static void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.err.println("No se encontró el driver MySQL. Agrega el conector JDBC al classpath.");
        }
    }

    private static void createDatabase() throws SQLException {
        try (Connection connection = DatabaseManager.getServerConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE DATABASE IF NOT EXISTS " + DatabaseConfig.getDatabaseName() +
                            " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci"
            );
        }
    }

    private static void createTables() throws SQLException {
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                            "username VARCHAR(50) NOT NULL UNIQUE," +
                            "password_hash VARCHAR(255) NOT NULL," +
                            "password_salt VARCHAR(255) NOT NULL," +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                            ") ENGINE=InnoDB"
            );
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS scores (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                            "user_id INT NOT NULL," +
                            "game_key VARCHAR(20) NOT NULL," +
                            "score INT NOT NULL," +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                            "INDEX idx_scores_game (game_key)," +
                            "INDEX idx_scores_user (user_id)," +
                            "CONSTRAINT fk_scores_user FOREIGN KEY (user_id) REFERENCES users(id)" +
                            ") ENGINE=InnoDB"
            );
        }
    }
}
