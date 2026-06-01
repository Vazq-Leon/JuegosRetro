package juegosretro.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseManager {
    private DatabaseManager() {
    }

    public static Connection getServerConnection() throws SQLException {
        return DriverManager.getConnection(
                DatabaseConfig.getServerUrl(),
                DatabaseConfig.getUser(),
                DatabaseConfig.getPassword()
        );
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DatabaseConfig.getDatabaseUrl(),
                DatabaseConfig.getUser(),
                DatabaseConfig.getPassword()
        );
    }
}
