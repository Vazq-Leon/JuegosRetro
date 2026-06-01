package juegosretro.games;

public enum GameType {
    SNAKE("snake", "Snake"),
    TETRIS("tetris", "Tetris"),
    BUSCAMINAS("buscaminas", "Buscaminas");

    private final String key;
    private final String displayName;

    GameType(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    public String getKey() {
        return key;
    }

    public String getDisplayName() {
        return displayName;
    }
}
