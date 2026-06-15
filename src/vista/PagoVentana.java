package vista;

import dao.AfiliadoJubiladoDAO;
import dao.PagoDAO;
import modelo.AfiliadoJubilado;
import modelo.Pago;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PagoVentana extends JFrame {

    private Usuario usuarioActivo;
    private PagoDAO dao = new PagoDAO();
    private AfiliadoJubiladoDAO afiliadoDAO = new AfiliadoJubiladoDAO();
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private static final String[] MEDIOS_PAGO = {"TRANSFERENCIA", "EFECTIVO", "CHEQUE"};

    public PagoVentana(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setTitle("Gestión de Pagos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 450);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = {"ID", "ID Afiliado", "ID Obligación", "Importe",
                             "Fecha", "Medio", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setBackground(new Color(0, 102, 153));
        tabla.getTableHeader().setForeground(Color.WHITE);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnNuevo = crearBoton("Registrar Pago", new Color(0, 153, 76));
        JButton btnPendientes = crearBoton("Ver Pendientes", new Color(0, 102, 153));
        JButton btnValidar = crearBoton("Validar Pago", new Color(0, 153, 76));
        JButton btnRechazar = crearBoton("Rechazar Pago", new Color(180, 0, 0));
        JButton btnRefrescar = crearBoton("Refrescar", new Color(100, 100, 100));

        panelBotones.add(btnNuevo);
        panelBotones.add(btnPendientes);
        panelBotones.add(btnValidar);
        panelBotones.add(btnRechazar);
        panelBotones.add(btnRefrescar);
        panel.add(panelBotones, BorderLayout.SOUTH);
        add(panel);

        btnNuevo.addActionListener(e -> registrarPago());
        btnPendientes.addActionListener(e -> listarPendientes());
        btnValidar.addActionListener(e -> validarPago());
        btnRechazar.addActionListener(e -> rechazarPago());
        btnRefrescar.addActionListener(e -> cargarTabla());
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Pago> lista = dao.listarTodos();
        for (Pago p : lista) {
            modeloTabla.addRow(new Object[]{
                p.getIdPago(), p.getIdAfiliado(), p.getIdObligacion(),
                "$" + p.getImporte(), p.getFechaPago(),
                p.getMedioPago(), p.getEstado()
            });
        }
    }

    private void registrarPago() {
        JTextField dni = new JTextField();
        JTextField idObligacion = new JTextField();
        JTextField importe = new JTextField();
        JTextField fecha = new JTextField("2024-01-01");
        JComboBox<String> medio = new JComboBox<>(MEDIOS_PAGO);

        Object[] campos = {
            "DNI del afiliado:", dni,
            "ID Obligación:", idObligacion,
            "Importe:", importe,
            "Fecha (YYYY-MM-DD):", fecha,
            "Medio de pago:", medio
        };

        int result = JOptionPane.showConfirmDialog(this, campos,
            "Registrar Pago", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            AfiliadoJubilado afiliado = afiliadoDAO.buscarPorDni(dni.getText().trim());
            if (afiliado == null) {
                JOptionPane.showMessageDialog(this, "Afiliado no encontrado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Pago pago = new Pago(
                afiliado.getIdAfiliado(),
                Integer.parseInt(idObligacion.getText().trim()),
                Double.parseDouble(importe.getText().trim()),
                fecha.getText().trim(),
                (String) medio.getSelectedItem()
            );
            if (dao.insertar(pago)) {
                JOptionPane.showMessageDialog(this, "Pago registrado. Pendiente de validación.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar pago.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void listarPendientes() {
        modeloTabla.setRowCount(0);
        List<Pago> lista = dao.listarPendientesValidacion();
        for (Pago p : lista) {
            modeloTabla.addRow(new Object[]{
                p.getIdPago(), p.getIdAfiliado(), p.getIdObligacion(),
                "$" + p.getImporte(), p.getFechaPago(),
                p.getMedioPago(), p.getEstado()
            });
        }
    }

    private void validarPago() {
        if (!usuarioActivo.getRol().equals("HACIENDA")) {
            JOptionPane.showMessageDialog(this,
                "Solo el área de Hacienda puede validar pagos.",
                "Acceso denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccioná un pago de la tabla.");
            return;
        }
        int idPago = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Validar el pago seleccionado?", "Confirmar",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.validarPago(idPago, usuarioActivo.getIdUsuario())) {
                JOptionPane.showMessageDialog(this, "Pago validado correctamente.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al validar.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void rechazarPago() {
        if (!usuarioActivo.getRol().equals("HACIENDA")) {
            JOptionPane.showMessageDialog(this,
                "Solo el área de Hacienda puede rechazar pagos.",
                "Acceso denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccioná un pago de la tabla.");
            return;
        }
        int idPago = (int) modeloTabla.getValueAt(fila, 0);
        String motivo = JOptionPane.showInputDialog(this, "Motivo del rechazo:");
        if (motivo != null && !motivo.trim().isEmpty()) {
            if (dao.rechazarPago(idPago, usuarioActivo.getIdUsuario(), motivo)) {
                JOptionPane.showMessageDialog(this, "Pago rechazado correctamente.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al rechazar.",
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
