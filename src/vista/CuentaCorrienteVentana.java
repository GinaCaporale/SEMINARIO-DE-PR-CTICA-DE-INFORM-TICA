package vista;

import dao.AfiliadoJubiladoDAO;
import dao.CuentaCorrienteDAO;
import modelo.AfiliadoJubilado;
import modelo.DetalleCuentaCorriente;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CuentaCorrienteVentana extends JFrame {

    private Usuario usuarioActivo;
    private CuentaCorrienteDAO dao = new CuentaCorrienteDAO();
    private AfiliadoJubiladoDAO afiliadoDAO = new AfiliadoJubiladoDAO();
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JLabel lblSaldo;
    private JLabel lblAfiliado;

    public CuentaCorrienteVentana(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
        initComponents();
    }

    private void initComponents() {
        setTitle("Cuenta Corriente");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior con info del afiliado y saldo
        JPanel panelInfo = new JPanel(new GridLayout(2, 1));
        lblAfiliado = new JLabel("Seleccioná un afiliado para ver su cuenta", JLabel.CENTER);
        lblAfiliado.setFont(new Font("Arial", Font.BOLD, 13));
        lblSaldo = new JLabel("", JLabel.CENTER);
        lblSaldo.setFont(new Font("Arial", Font.BOLD, 14));
        panelInfo.add(lblAfiliado);
        panelInfo.add(lblSaldo);
        panel.add(panelInfo, BorderLayout.NORTH);

        // Tabla de movimientos
        String[] columnas = {"Tipo", "Concepto", "Importe", "Fecha"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setBackground(new Color(0, 102, 153));
        tabla.getTableHeader().setForeground(Color.WHITE);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnVerCuenta = crearBoton("Ver Cuenta por DNI", new Color(0, 102, 153));
        JButton btnMovimiento = crearBoton("Registrar Movimiento", new Color(204, 102, 0));

        panelBotones.add(btnVerCuenta);
        panelBotones.add(btnMovimiento);
        panel.add(panelBotones, BorderLayout.SOUTH);
        add(panel);

        btnVerCuenta.addActionListener(e -> verCuenta());
        btnMovimiento.addActionListener(e -> registrarMovimiento());
    }

    private void verCuenta() {
        String dni = JOptionPane.showInputDialog(this, "Ingresá el DNI del afiliado:");
        if (dni != null && !dni.trim().isEmpty()) {
            AfiliadoJubilado afiliado = afiliadoDAO.buscarPorDni(dni.trim());
            if (afiliado == null) {
                JOptionPane.showMessageDialog(this, "Afiliado no encontrado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int idCuenta = dao.obtenerIdCuenta(afiliado.getIdAfiliado());
            if (idCuenta == -1) {
                JOptionPane.showMessageDialog(this, "El afiliado no tiene cuenta corriente.");
                return;
            }

            lblAfiliado.setText("Afiliado: " + afiliado.getApellido() +
                                ", " + afiliado.getNombre());

            double saldo = dao.calcularSaldo(idCuenta);
            lblSaldo.setText("Saldo actual: $" + String.format("%.2f", saldo));
            if (saldo < 0) {
                lblSaldo.setForeground(Color.RED);
            } else if (saldo == 0) {
                lblSaldo.setForeground(Color.BLACK);
            } else {
                lblSaldo.setForeground(new Color(0, 153, 0));
            }

            modeloTabla.setRowCount(0);
            List<DetalleCuentaCorriente> movimientos = dao.listarMovimientos(idCuenta);
            for (DetalleCuentaCorriente m : movimientos) {
                modeloTabla.addRow(new Object[]{
                    m.getTipoMovimiento(), m.getConcepto(),
                    "$" + m.getImporte(), m.getFechaMovimiento()
                });
            }
        }
    }

    private void registrarMovimiento() {
        String dni = JOptionPane.showInputDialog(this, "Ingresá el DNI del afiliado:");
        if (dni == null || dni.trim().isEmpty()) return;

        AfiliadoJubilado afiliado = afiliadoDAO.buscarPorDni(dni.trim());
        if (afiliado == null) {
            JOptionPane.showMessageDialog(this, "Afiliado no encontrado.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int idCuenta = dao.obtenerIdCuenta(afiliado.getIdAfiliado());
        if (idCuenta == -1) {
            JOptionPane.showMessageDialog(this, "El afiliado no tiene cuenta corriente.");
            return;
        }

        String[] tipos = {"OBLIGACION", "PAGO", "AJUSTE"};
        JComboBox<String> tipoCombo = new JComboBox<>(tipos);
        JTextField concepto = new JTextField();
        JTextField importe = new JTextField();
        JTextField referencia = new JTextField();

        Object[] campos = {
            "Tipo de movimiento:", tipoCombo,
            "Concepto:", concepto,
            "Importe (negativo para débito):", importe,
            "ID Referencia:", referencia
        };

        int result = JOptionPane.showConfirmDialog(this, campos,
            "Registrar Movimiento", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            DetalleCuentaCorriente detalle = new DetalleCuentaCorriente(
                idCuenta,
                (String) tipoCombo.getSelectedItem(),
                concepto.getText().trim(),
                Double.parseDouble(importe.getText().trim()),
                Integer.parseInt(referencia.getText().trim())
            );
            if (dao.insertarMovimiento(detalle)) {
                JOptionPane.showMessageDialog(this, "Movimiento registrado correctamente.");
                verCuenta();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar movimiento.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setFocusPainted(false);
        return btn;
    }
}
