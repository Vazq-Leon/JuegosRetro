package juegosretro.ui;

import juegosretro.games.GameType;
import juegosretro.model.User;
import juegosretro.session.Session;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainMenuFrame extends JFrame {
    public MainMenuFrame() {
        User user = Session.getCurrentUser();
        if (user == null) {
            JOptionPane.showMessageDialog(this, "No hay sesión activa.");
            new LoginFrame().setVisible(true);
            dispose();
            return;
        }
        setTitle("Juegos Retro - Menú");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildLayout(user);
        pack();
        setLocationRelativeTo(null);
    }

    private void buildLayout(User user) {
        JLabel welcome = new JLabel("Bienvenido, " + user.getUsername(), SwingConstants.CENTER);
        add(welcome, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(0, 1, 10, 10));
        JButton snakeButton = new JButton("Snake");
        snakeButton.addActionListener(event -> openGame(GameType.SNAKE));
        JButton tetrisButton = new JButton("Tetris");
        tetrisButton.addActionListener(event -> openGame(GameType.TETRIS));
        JButton minesButton = new JButton("Buscaminas");
        minesButton.addActionListener(event -> openGame(GameType.BUSCAMINAS));
        JButton rankingButton = new JButton("Ranking");
        rankingButton.addActionListener(event -> openRanking());
        JButton logoutButton = new JButton("Cerrar sesión");
        logoutButton.addActionListener(event -> logout());

        buttons.add(snakeButton);
        buttons.add(tetrisButton);
        buttons.add(minesButton);
        buttons.add(rankingButton);
        buttons.add(logoutButton);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.add(buttons);
        add(wrapper, BorderLayout.CENTER);
    }

    private void openGame(GameType gameType) {
        GameFrame gameFrame = new GameFrame(gameType, this);
        gameFrame.setVisible(true);
    }

    private void openRanking() {
        RankingDialog dialog = new RankingDialog(this);
        dialog.setVisible(true);
    }

    private void logout() {
        Session.clear();
        new LoginFrame().setVisible(true);
        dispose();
    }
}
