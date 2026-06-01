package juegosretro.ui;

import juegosretro.games.GameType;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GameOverDialog extends JDialog {
    public GameOverDialog(GameFrame owner, GameType gameType, int score) {
        super(owner, "Fin de juego", true);
        JLabel message = new JLabel(
                "Puntaje en " + gameType.getDisplayName() + ": " + score,
                SwingConstants.CENTER
        );
        JButton closeButton = new JButton("Volver al menú");
        closeButton.addActionListener(event -> dispose());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actions.add(closeButton);

        add(message, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }
}
