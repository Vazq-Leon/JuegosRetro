package juegosretro.model;

import java.time.LocalDateTime;

public class ScoreEntry {
    private final String username;
    private final int score;
    private final LocalDateTime createdAt;

    public ScoreEntry(String username, int score, LocalDateTime createdAt) {
        this.username = username;
        this.score = score;
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
