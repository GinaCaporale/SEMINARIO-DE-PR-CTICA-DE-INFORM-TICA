package vista;

import controlador.AfiliadoControlador;
import controlador.DeclaracionJuradaControlador;
import controlador.ObligacionMensualControlador;
import controlador.PagoControlador;
import controlador.CuentaCorrienteControlador;
import modelo.Usuario;
import javax.swing.*;
import java.awt.*;

public class MenuPrincipalVentana extends JFrame {

    private Usuario usuarioActivo;

    public MenuPrincipalVentana(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema de Gestión Sindical");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Panel superior con info del usuario
        JPanel panelUsuario = new JPanel(new GridLayout(2, 1));
        JLabel lblBienvenida = new JLabel("Bienvenido/a: " + usuarioActivo.getNombre(), JLabel.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 13));
        JLabel lblRol = new JLabel("Rol: " + usuarioActivo.getRol(), JLabel.CENTER);
        lblRol.setFont(new Font("Arial", Font.PLAIN, 11));
        lblRol.setForeground(new Color(0, 102, 153));
        panelUsuario.add(lblBienvenida);
        panelUsuario.add(lblRol);
        panel.add(panelUsuario, BorderLayout.NORTH);

        // Panel central con botones
        JPanel panelBotones = new JPanel(new GridLayout(6, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton btnAfiliados = crearBoton("1. Gestión de Afiliados");
        JButton btnDJ = crearBoton("2. Declaraciones Juradas");
        JButton btnObligaciones = crearBoton("3. Obligaciones Mensuales");
        JButton btnPagos = crearBoton("4. Gestión de Pagos");
        JButton btnCuenta = crearBoton("5. Cuenta Corriente");
        JButton btnSalir = crearBoton("0. Cerrar Sesión");
        btnSalir.setBackground(new Color(180, 0, 0));

        panelBotones.add(btnAfiliados);
        panelBotones.add(btnDJ);
        panelBotones.add(btnObligaciones);
        panelBotones.add(btnPagos);
        panelBotones.add(btnCuenta);
        panelBotones.add(btnSalir);
        panel.add(panelBotones, BorderLayout.CENTER);

        add(panel);

        // Acciones
        btnAfiliados.addActionListener(e ->
            new AfiliadoVentana(usuarioActivo).setVisible(true));
        btnDJ.addActionListener(e ->
            new DeclaracionJuradaVentana(usuarioActivo).setVisible(true));
        btnObligaciones.addActionListener(e ->
            new ObligacionMensualVentana(usuarioActivo).setVisible(true));
        btnPagos.addActionListener(e ->
            new PagoVentana(usuarioActivo).setVisible(true));
        btnCuenta.addActionListener(e ->
            new CuentaCorrienteVentana(usuarioActivo).setVisible(true));
        btnSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Cerrar sesión?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginVentana().setVisible(true);
            }
        });
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(0, 102, 153));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        return btn;
    }
}