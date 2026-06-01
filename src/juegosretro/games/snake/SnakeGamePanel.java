package juegosretro.games.snake;

import juegosretro.games.GameOverHandler;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGamePanel extends JPanel {
    private static final int TILE_SIZE = 20;
    private static final int GRID_SIZE = 20;
    private static final int PANEL_SIZE = TILE_SIZE * GRID_SIZE;
    private static final int TIMER_DELAY = 120;

    private final Deque<Point> snake = new ArrayDeque<>();
    private final Random random = new Random();
    private final GameOverHandler gameOverHandler;
    private Timer timer;
    private Direction direction = Direction.RIGHT;
    private boolean running = true;
    private int score = 0;
    private Point food;

    public SnakeGamePanel(GameOverHandler gameOverHandler) {
        this.gameOverHandler = gameOverHandler;
        setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyHandler());
        startGame();
    }

    private void startGame() {
        snake.clear();
        snake.add(new Point(5, 10));
        snake.add(new Point(4, 10));
        snake.add(new Point(3, 10));
        direction = Direction.RIGHT;
        score = 0;
        running = true;
        spawnFood();
        timer = new Timer(TIMER_DELAY, event -> gameStep());
        timer.start();
    }

    private void gameStep() {
        if (!running) {
            return;
        }
        moveSnake();
        checkCollisions();
        repaint();
    }

    private void moveSnake() {
        Point head = snake.peekFirst();
        Point newHead = new Point(head.x + direction.dx, head.y + direction.dy);
        snake.addFirst(newHead);

        if (newHead.equals(food)) {
            score += 10;
            spawnFood();
        } else {
            snake.removeLast();
        }
    }

    private void checkCollisions() {
        Point head = snake.peekFirst();
        if (head.x < 0 || head.x >= GRID_SIZE || head.y < 0 || head.y >= GRID_SIZE) {
            endGame();
            return;
        }
        int index = 0;
        for (Point segment : snake) {
            if (index++ == 0) {
                continue;
            }
            if (segment.equals(head)) {
                endGame();
                return;
            }
        }
    }

    private void spawnFood() {
        while (true) {
            Point candidate = new Point(random.nextInt(GRID_SIZE), random.nextInt(GRID_SIZE));
            boolean occupied = false;
            for (Point segment : snake) {
                if (segment.equals(candidate)) {
                    occupied = true;
                    break;
                }
            }
            if (!occupied) {
                food = candidate;
                return;
            }
        }
    }

    private void endGame() {
        running = false;
        if (timer != null) {
            timer.stop();
        }
        gameOverHandler.onGameOver(score);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        for (Point segment : snake) {
            g.fillRect(segment.x * TILE_SIZE, segment.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
        if (food != null) {
            g.setColor(Color.RED);
            g.fillOval(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Puntaje: " + score, 10, 18);
    }

    private enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        final int dx;
        final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    private class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (direction != Direction.DOWN) {
                        direction = Direction.UP;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != Direction.UP) {
                        direction = Direction.DOWN;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != Direction.RIGHT) {
                        direction = Direction.LEFT;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != Direction.LEFT) {
                        direction = Direction.RIGHT;
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
