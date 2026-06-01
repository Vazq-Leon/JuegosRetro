package juegosretro.ui;

import juegosretro.db.ScoreRepository;
import juegosretro.games.GameType;
import juegosretro.model.ScoreEntry;
import juegosretro.model.User;
import juegosretro.session.Session;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.OptionalInt;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class RankingDialog extends JDialog {
    private final ScoreRepository scoreRepository = new ScoreRepository();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public RankingDialog(JFrame owner) {
        super(owner, "Ranking", true);
        JTabbedPane tabs = new JTabbedPane();
        for (GameType gameType : GameType.values()) {
            tabs.add(gameType.getDisplayName(), buildTab(gameType));
        }
        add(tabs, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(owner);
    }

    private JPanel buildTab(GameType gameType) {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"#", "Usuario", "Puntaje", "Fecha"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel(new GridLayout(0, 1));
        summaryPanel.add(new JLabel("Top 10 global", SwingConstants.CENTER));

        User currentUser = Session.getCurrentUser();
        if (currentUser != null) {
            JLabel bestLabel = new JLabel("", SwingConstants.CENTER);
            summaryPanel.add(bestLabel);
            populateUserBest(gameType, currentUser, bestLabel);
        }

        panel.add(summaryPanel, BorderLayout.NORTH);
        populateScores(gameType, model);
        return panel;
    }

    private void populateScores(GameType gameType, DefaultTableModel model) {
        try {
            List<ScoreEntry> scores = scoreRepository.getTopScores(gameType, 10);
            int index = 1;
            for (ScoreEntry entry : scores) {
                String date = entry.getCreatedAt() != null
                        ? entry.getCreatedAt().format(DATE_FORMAT)
                        : "";
                model.addRow(new Object[]{index++, entry.getUsername(), entry.getScore(), date});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los rankings. Revisa la conexión a la base de datos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateUserBest(GameType gameType, User user, JLabel label) {
        try {
            OptionalInt best = scoreRepository.getUserBestScore(user.getId(), gameType);
            if (best.isPresent()) {
                label.setText("Tu mejor puntaje: " + best.getAsInt());
            } else {
                label.setText("Aún no tienes puntajes en este juego.");
            }
        } catch (SQLException ex) {
            label.setText("No se pudo cargar tu mejor puntaje.");
        }
    }
}
