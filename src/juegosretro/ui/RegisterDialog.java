package juegosretro.ui;

import juegosretro.db.UserRepository;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegisterDialog extends JDialog {
    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JPasswordField confirmField = new JPasswordField(15);
    private final UserRepository userRepository = new UserRepository();
    private boolean registered;
    private String registeredUsername;

    public RegisterDialog(JDialog owner) {
        super(owner, "Crear cuenta", true);
        buildLayout();
        pack();
        setLocationRelativeTo(owner);
    }

    public RegisterDialog(JFrame owner) {
        super(owner, "Crear cuenta", true);
        buildLayout();
        pack();
        setLocationRelativeTo(owner);
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

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Confirmar:"), gbc);
        gbc.gridx = 1;
        formPanel.add(confirmField, gbc);

        JButton registerButton = new JButton("Registrar");
        registerButton.addActionListener(event -> handleRegister());
        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(event -> dispose());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actions.add(registerButton);
        actions.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();
        char[] confirm = confirmField.getPassword();

        try {
            if (username.isBlank() || password.length == 0 || confirm.length == 0) {
                showError("Completa todos los campos.");
                return;
            }
            if (!Arrays.equals(password, confirm)) {
                showError("Las contraseñas no coinciden.");
                return;
            }
            if (userRepository.usernameExists(username)) {
                showError("El usuario ya existe. Usa otro nombre.");
                return;
            }
            userRepository.createUser(username, password);
            registered = true;
            registeredUsername = username;
            JOptionPane.showMessageDialog(this, "Cuenta creada. Ya puedes iniciar sesión.");
            dispose();
        } catch (SQLException ex) {
            showError("No se pudo registrar. Revisa la conexión a la base de datos.");
        } finally {
            Arrays.fill(password, '\0');
            Arrays.fill(confirm, '\0');
        }
    }

    public boolean isRegistered() {
        return registered;
    }

    public String getRegisteredUsername() {
        return registeredUsername;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
