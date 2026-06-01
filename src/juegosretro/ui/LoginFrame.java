package juegosretro.ui;

import juegosretro.db.UserRepository;
import juegosretro.model.User;
import juegosretro.session.Session;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LoginFrame extends JFrame {
    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final UserRepository userRepository = new UserRepository();

    public LoginFrame() {
        setTitle("Juegos Retro - Iniciar sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildLayout();
        pack();
        setLocationRelativeTo(null);
    }

    private void buildLayout() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("Ingresar");
        loginButton.addActionListener(event -> handleLogin());
        JButton registerButton = new JButton("Crear cuenta");
        registerButton.addActionListener(event -> openRegister());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actions.add(loginButton);
        actions.add(registerButton);

        add(formPanel, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();

        try {
            if (username.isBlank() || password.length == 0) {
                showError("Ingresa tu usuario y contraseña.");
                return;
            }
            User user = userRepository.authenticate(username, password);
            if (user == null) {
                showError("Usuario o contraseña inválidos.");
                return;
            }
            Session.setCurrentUser(user);
            SwingUtilities.invokeLater(() -> {
                new MainMenuFrame().setVisible(true);
                dispose();
            });
        } catch (SQLException ex) {
            showError("No se pudo iniciar sesión. Revisa la conexión a la base de datos.");
        } finally {
            Arrays.fill(password, '\0');
        }
    }

    private void openRegister() {
        RegisterDialog dialog = new RegisterDialog(this);
        dialog.setVisible(true);
        if (dialog.isRegistered()) {
            usernameField.setText(dialog.getRegisteredUsername());
            passwordField.setText("");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
