package juegosretro.games.tetris;

import juegosretro.games.GameOverHandler;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TetrisGamePanel extends JPanel {
    private static final int COLS = 10;
    private static final int ROWS = 20;
    private static final int TILE = 24;
    private static final int PANEL_WIDTH = COLS * TILE;
    private static final int PANEL_HEIGHT = ROWS * TILE;

    private static final Point[][][] SHAPES = new Point[][][] {
        { { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
          { new Point(2, 0), new Point(2, 1), new Point(2, 2), new Point(2, 3) },
          { new Point(0, 2), new Point(1, 2), new Point(2, 2), new Point(3, 2) },
          { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) } },
        { { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
          { new Point(1, 0), new Point(2, 0), new Point(1, 1), new Point(1, 2) },
          { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
          { new Point(1, 0), new Point(1, 1), new Point(0, 2), new Point(1, 2) } },
        { { new Point(2, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
          { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
          { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
          { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(1, 2) } },
        { { new Point(1, 0), new Point(2, 0), new Point(1, 1), new Point(2, 1) },
          { new Point(1, 0), new Point(2, 0), new Point(1, 1), new Point(2, 1) },
          { new Point(1, 0), new Point(2, 0), new Point(1, 1), new Point(2, 1) },
          { new Point(1, 0), new Point(2, 0), new Point(1, 1), new Point(2, 1) } },
        { { new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
          { new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
          { new Point(1, 1), new Point(2, 1), new Point(0, 2), new Point(1, 2) },
          { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) } },
        { { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
          { new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
          { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
          { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) } },
        { { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
          { new Point(2, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
          { new Point(0, 1), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
          { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) } }
    };

    private static final Color[] COLORS = {
        Color.CYAN, new Color(0, 100, 255), new Color(255, 140, 0),
        Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.RED
    };

    private final int[][] board = new int[ROWS][COLS];
    private final Random random = new Random();
    private final GameOverHandler gameOverHandler;
    private Timer timer;
    private int currentShape;
    private int rotation;
    private int shapeRow;
    private int shapeCol;
    private int score;
    private boolean running = true;

    public TetrisGamePanel(GameOverHandler gameOverHandler) {
        this.gameOverHandler = gameOverHandler;
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyHandler());
        startGame();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

    private void startGame() {
        clearBoard();
        score = 0;
        running = true;
        spawnShape();
        timer = new Timer(500, event -> tick());
        timer.start();
    }

    private void tick() {
        if (!running) {
            return;
        }
        if (canMove(shapeRow + 1, shapeCol, rotation)) {
            shapeRow++;
        } else {
            lockShape();
        }
        repaint();
    }

    private void spawnShape() {
        currentShape = random.nextInt(SHAPES.length);
        rotation = 0;
        shapeRow = 0;
        shapeCol = 3;
        if (!canMove(shapeRow, shapeCol, rotation)) {
            endGame();
        }
    }

    private boolean canMove(int row, int col, int rotationIndex) {
        for (Point block : SHAPES[currentShape][rotationIndex]) {
            int newRow = row + block.y;
            int newCol = col + block.x;
            if (newRow < 0 || newRow >= ROWS || newCol < 0 || newCol >= COLS) {
                return false;
            }
            if (board[newRow][newCol] != 0) {
                return false;
            }
        }
        return true;
    }

    private void lockShape() {
        for (Point block : SHAPES[currentShape][rotation]) {
            int row = shapeRow + block.y;
            int col = shapeCol + block.x;
            if (row >= 0 && row < ROWS && col >= 0 && col < COLS) {
                board[row][col] = currentShape + 1;
            }
        }
        clearLines();
        spawnShape();
    }

    private void clearLines() {
        int linesCleared = 0;
        for (int row = ROWS - 1; row >= 0; row--) {
            boolean full = true;
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == 0) {
                    full = false;
                    break;
                }
            }
            if (full) {
                linesCleared++;
                for (int r = row; r > 0; r--) {
                    System.arraycopy(board[r - 1], 0, board[r], 0, COLS);
                }
                for (int col = 0; col < COLS; col++) {
                    board[0][col] = 0;
                }
                row++;
            }
        }
        if (linesCleared > 0) {
            score += switch (linesCleared) {
                case 1 -> 100;
                case 2 -> 300;
                case 3 -> 500;
                default -> 800;
            };
        }
    }

    private void moveLeft() {
        if (canMove(shapeRow, shapeCol - 1, rotation)) {
            shapeCol--;
        }
    }

    private void moveRight() {
        if (canMove(shapeRow, shapeCol + 1, rotation)) {
            shapeCol++;
        }
    }

    private void moveDown() {
        if (canMove(shapeRow + 1, shapeCol, rotation)) {
            shapeRow++;
            score += 1;
        }
    }

    private void hardDrop() {
        while (canMove(shapeRow + 1, shapeCol, rotation)) {
            shapeRow++;
            score += 2;
        }
        lockShape();
    }

    private void rotate() {
        int nextRotation = (rotation + 1) % 4;
        if (canMove(shapeRow, shapeCol, nextRotation)) {
            rotation = nextRotation;
        }
    }

    private void endGame() {
        running = false;
        if (timer != null) {
            timer.stop();
        }
        gameOverHandler.onGameOver(score);
    }

    private void clearBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                board[row][col] = 0;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int value = board[row][col];
                if (value != 0) {
                    g.setColor(COLORS[value - 1]);
                    g.fillRect(col * TILE, row * TILE, TILE, TILE);
                }
                g.setColor(Color.DARK_GRAY);
                g.drawRect(col * TILE, row * TILE, TILE, TILE);
            }
        }

        if (running) {
            g.setColor(COLORS[currentShape]);
            for (Point block : SHAPES[currentShape][rotation]) {
                int x = (shapeCol + block.x) * TILE;
                int y = (shapeRow + block.y) * TILE;
                g.fillRect(x, y, TILE, TILE);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, TILE, TILE);
                g.setColor(COLORS[currentShape]);
            }
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Puntaje: " + score, 10, 20);
    }

    private class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (!running) {
                return;
            }
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> moveLeft();
                case KeyEvent.VK_RIGHT -> moveRight();
                case KeyEvent.VK_DOWN -> moveDown();
                case KeyEvent.VK_UP -> rotate();
                case KeyEvent.VK_SPACE -> hardDrop();
                default -> {
                }
            }
            repaint();
        }
    }
}
