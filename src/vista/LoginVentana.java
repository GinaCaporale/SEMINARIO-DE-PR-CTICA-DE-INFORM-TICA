package vista;

import dao.UsuarioDAO;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class LoginVentana extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private JLabel lblMensaje;
    private Usuario usuarioAutenticado;

    public LoginVentana() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema de Gestión Sindical - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 280);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        // Título
        JLabel lblTitulo = new JLabel("SISTEMA DE GESTIÓN SINDICAL", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        JLabel lblSubtitulo = new JLabel("Afiliados Jubilados - Cuota Sindical", JLabel.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 11));
        gbc.gridy = 1;
        panel.add(lblSubtitulo, gbc);

        // Email
        gbc.gridwidth = 1; gbc.gridy = 2; gbc.gridx = 0;
        panel.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        // Contraseña
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Contraseña:"), gbc);
        txtContrasena = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(txtContrasena, gbc);

        // Botón
        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBackground(new Color(0, 102, 153));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(btnIngresar, gbc);

        // Mensaje de error
        lblMensaje = new JLabel("", JLabel.CENTER);
        lblMensaje.setForeground(Color.RED);
        gbc.gridy = 5;
        panel.add(lblMensaje, gbc);

        add(panel);

        // Acción del botón
        btnIngresar.addActionListener(e -> autenticar());
        txtContrasena.addActionListener(e -> autenticar());
    }

    private void autenticar() {
        String email = txtEmail.getText().trim();
        String contrasena = new String(txtContrasena.getPassword()).trim();

        if (email.isEmpty() || contrasena.isEmpty()) {
            lblMensaje.setText("Ingresá email y contraseña.");
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarioAutenticado = usuarioDAO.autenticar(email, contrasena);

        if (usuarioAutenticado != null) {
            dispose();
            new MenuPrincipalVentana(usuarioAutenticado).setVisible(true);
        } else {
            lblMensaje.setText("Credenciales incorrectas. Intentá nuevamente.");
            txtContrasena.setText("");
        }
    }

    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }
}