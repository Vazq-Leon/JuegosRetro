package juegosretro.games.buscaminas;

import juegosretro.games.GameOverHandler;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class MinesweeperPanel extends JPanel {
    private static final int SIZE = 9;
    private static final int BOMBS = 10;

    private final JButton[][] buttons = new JButton[SIZE][SIZE];
    private final boolean[][] bombs = new boolean[SIZE][SIZE];
    private final boolean[][] revealed = new boolean[SIZE][SIZE];
    private final boolean[][] flagged = new boolean[SIZE][SIZE];
    private final GameOverHandler gameOverHandler;
    private final JLabel scoreLabel = new JLabel("Puntaje: 0", SwingConstants.CENTER);
    private int revealedCount = 0;
    private boolean gameOver;

    public MinesweeperPanel(GameOverHandler gameOverHandler) {
        this.gameOverHandler = gameOverHandler;
        setLayout(new BorderLayout());
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(scoreLabel, BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridLayout(SIZE, SIZE));
        grid.setPreferredSize(new Dimension(360, 360));

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JButton button = new JButton();
                button.setFocusable(false);
                button.setFont(new Font("Arial", Font.BOLD, 14));
                int r = row;
                int c = col;
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (gameOver) {
                            return;
                        }
                        if (SwingUtilities.isRightMouseButton(e)) {
                            toggleFlag(r, c);
                        } else {
                            revealCell(r, c);
                        }
                    }
                });
                buttons[row][col] = button;
                grid.add(button);
            }
        }

        add(grid, BorderLayout.CENTER);
        placeBombs();
    }

    private void placeBombs() {
        Random random = new Random();
        int placed = 0;
        while (placed < BOMBS) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            if (!bombs[row][col]) {
                bombs[row][col] = true;
                placed++;
            }
        }
    }

    private void toggleFlag(int row, int col) {
        if (revealed[row][col]) {
            return;
        }
        flagged[row][col] = !flagged[row][col];
        buttons[row][col].setText(flagged[row][col] ? "F" : "");
        buttons[row][col].setForeground(Color.BLUE);
    }

    private void revealCell(int row, int col) {
        if (flagged[row][col] || revealed[row][col]) {
            return;
        }
        revealed[row][col] = true;
        if (bombs[row][col]) {
            buttons[row][col].setText("💣");
            buttons[row][col].setBackground(Color.RED);
            endGame(false);
            return;
        }

        int count = countAdjacentBombs(row, col);
        buttons[row][col].setEnabled(false);
        buttons[row][col].setText(count > 0 ? String.valueOf(count) : "");
        revealedCount++;
        updateScore();

        if (count == 0) {
            floodFill(row, col);
        }

        if (revealedCount == SIZE * SIZE - BOMBS) {
            endGame(true);
        }
    }

    private void floodFill(int row, int col) {
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (r < 0 || r >= SIZE || c < 0 || c >= SIZE) {
                    continue;
                }
                if (!revealed[r][c] && !bombs[r][c]) {
                    revealCell(r, c);
                }
            }
        }
    }

    private int countAdjacentBombs(int row, int col) {
        int count = 0;
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (r < 0 || r >= SIZE || c < 0 || c >= SIZE) {
                    continue;
                }
                if (bombs[r][c]) {
                    count++;
                }
            }
        }
        return count;
    }

    private void endGame(boolean win) {
        gameOver = true;
        revealAllBombs();
        int score = revealedCount * 5 + (win ? 50 : 0);
        gameOverHandler.onGameOver(score);
    }

    private void revealAllBombs() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (bombs[row][col]) {
                    buttons[row][col].setText("💣");
                }
                buttons[row][col].setEnabled(false);
            }
        }
    }

    private void updateScore() {
        scoreLabel.setText("Puntaje: " + (revealedCount * 5));
    }
}
