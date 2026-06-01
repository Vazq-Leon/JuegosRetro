package juegosretro.games;

@FunctionalInterface
public interface GameOverHandler {
    void onGameOver(int score);
}
