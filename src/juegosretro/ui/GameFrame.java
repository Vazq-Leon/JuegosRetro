package juegosretro.ui;

import juegosretro.db.ScoreRepository;
import juegosretro.games.GameOverHandler;
import juegosretro.games.GameType;
import juegosretro.games.buscaminas.MinesweeperPanel;
import juegosretro.games.snake.SnakeGamePanel;
import juegosretro.games.tetris.TetrisGamePanel;
import juegosretro.model.User;
import juegosretro.session.Session;
import java.awt.BorderLayout;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GameFrame extends JFrame {
    private final GameType gameType;
    private final ScoreRepository scoreRepository = new ScoreRepository();
    private final JFrame parent;

    public GameFrame(GameType gameType, JFrame parent) {
        this.gameType = gameType;
        this.parent = parent;
        setTitle("Juegos Retro - " + gameType.getDisplayName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(buildGamePanel(), BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(parent);
    }

    private JPanel buildGamePanel() {
        GameOverHandler handler = this::handleGameOver;
        switch (gameType) {
            case SNAKE:
                return new SnakeGamePanel(handler);
            case TETRIS:
                return new TetrisGamePanel(handler);
            case BUSCAMINAS:
                return new MinesweeperPanel(handler);
            default:
                throw new IllegalStateException("Juego no soportado.");
        }
    }

    private void handleGameOver(int score) {
        User user = Session.getCurrentUser();
        if (user != null) {
            try {
                scoreRepository.recordScore(user.getId(), gameType, score);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo guardar el puntaje. Revisa la conexión a la base de datos.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        GameOverDialog dialog = new GameOverDialog(this, gameType, score);
        dialog.setVisible(true);
        dispose();
        if (parent != null) {
            parent.toFront();
        }
    }
}
