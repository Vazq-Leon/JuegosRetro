package juegosretro.db;

import juegosretro.games.GameType;
import juegosretro.model.ScoreEntry;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class ScoreRepository {
    public void recordScore(int userId, GameType gameType, int score) throws SQLException {
        String sql = "INSERT INTO scores (user_id, game_key, score) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, gameType.getKey());
            statement.setInt(3, score);
            statement.executeUpdate();
        }
    }

    public List<ScoreEntry> getTopScores(GameType gameType, int limit) throws SQLException {
        String sql = "SELECT u.username, s.score, s.created_at " +
                "FROM scores s JOIN users u ON u.id = s.user_id " +
                "WHERE s.game_key = ? " +
                "ORDER BY s.score DESC, s.created_at ASC " +
                "LIMIT ?";
        List<ScoreEntry> entries = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, gameType.getKey());
            statement.setInt(2, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Timestamp timestamp = resultSet.getTimestamp("created_at");
                    LocalDateTime createdAt = timestamp != null ? timestamp.toLocalDateTime() : null;
                    entries.add(new ScoreEntry(
                            resultSet.getString("username"),
                            resultSet.getInt("score"),
                            createdAt
                    ));
                }
            }
        }
        return entries;
    }

    public OptionalInt getUserBestScore(int userId, GameType gameType) throws SQLException {
        String sql = "SELECT MAX(score) AS best_score FROM scores WHERE user_id = ? AND game_key = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, gameType.getKey());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int best = resultSet.getInt("best_score");
                    if (!resultSet.wasNull()) {
                        return OptionalInt.of(best);
                    }
                }
            }
        }
        return OptionalInt.empty();
    }
}
