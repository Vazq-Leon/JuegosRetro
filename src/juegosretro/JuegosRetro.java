/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package juegosretro;

import juegosretro.db.DatabaseInitializer;
import juegosretro.ui.LoginFrame;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class JuegosRetro {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DatabaseInitializer.initialize();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "No se pudo conectar a la base de datos.\\n" +
                                "Verifica MySQL/XAMPP y el conector JDBC.",
                        "Error de conexión",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            new LoginFrame().setVisible(true);
        });
    }
    
}
