package juegosretro.db;

import juegosretro.auth.PasswordHasher;
import juegosretro.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRepository {
    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public User createUser(String username, char[] password) throws SQLException {
        PasswordHasher.HashData hashData = PasswordHasher.hash(password);
        String sql = "INSERT INTO users (username, password_hash, password_salt) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, username);
            statement.setString(2, hashData.getHash());
            statement.setString(3, hashData.getSalt());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return new User(keys.getInt(1), username);
                }
            }
        }
        throw new SQLException("No se pudo crear el usuario.");
    }

    public User authenticate(String username, char[] password) throws SQLException {
        String sql = "SELECT id, username, password_hash, password_salt FROM users WHERE username = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                String hash = resultSet.getString("password_hash");
                String salt = resultSet.getString("password_salt");
                if (!PasswordHasher.verify(password, salt, hash)) {
                    return null;
                }
                return new User(resultSet.getInt("id"), resultSet.getString("username"));
            }
        }
    }
}
