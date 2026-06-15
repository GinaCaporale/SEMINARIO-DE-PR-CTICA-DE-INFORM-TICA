package vista;

import dao.AfiliadoJubiladoDAO;
import dao.DeclaracionJuradaDAO;
import modelo.AfiliadoJubilado;
import modelo.DeclaracionJurada;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DeclaracionJuradaVentana extends JFrame {

    private Usuario usuarioActivo;
    private DeclaracionJuradaDAO dao = new DeclaracionJuradaDAO();
    private AfiliadoJubiladoDAO afiliadoDAO = new AfiliadoJubiladoDAO();
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public DeclaracionJuradaVentana(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setTitle("Declaraciones Juradas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 450);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = {"ID", "ID Afiliado", "Tipo", "Período",
                             "Base Imponible", "Estado", "Fecha Registro"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setBackground(new Color(0, 102, 153));
        tabla.getTableHeader().setForeground(Color.WHITE);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnNueva = crearBoton("Nueva DJ", new Color(0, 153, 76));
        JButton btnBuscar = crearBoton("Buscar por DNI", new Color(0, 102, 153));
        JButton btnEstado = crearBoton("Actualizar Estado", new Color(204, 102, 0));
        JButton btnRefrescar = crearBoton("Refrescar", new Color(100, 100, 100));

        panelBotones.add(btnNueva);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnEstado);
        panelBotones.add(btnRefrescar);
        panel.add(panelBotones, BorderLayout.SOUTH);
        add(panel);

        btnNueva.addActionListener(e -> registrarDJ());
        btnBuscar.addActionListener(e -> buscarPorDni());
        btnEstado.addActionListener(e -> actualizarEstado());
        btnRefrescar.addActionListener(e -> cargarTabla());
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<DeclaracionJurada> lista = dao.listarTodas();
        for (DeclaracionJurada dj : lista) {
            modeloTabla.addRow(new Object[]{
                dj.getIdDj(), dj.getIdAfiliado(), dj.getTipo(),
                dj.getPeriodoMes() + "/" + dj.getPeriodoAnio(),
                "$" + dj.getBaseImponible(), dj.getEstado(),
                dj.getFechaRegistro()
            });
        }
    }

    private void registrarDJ() {
        JTextField dni = new JTextField();
        JComboBox<String> tipo = new JComboBox<>(new String[]{"ORIGINAL", "RECTIFICATIVA"});
        JTextField mes = new JTextField();
        JTextField anio = new JTextField();
        JTextField base = new JTextField();

        Object[] campos = {
            "DNI del afiliado:", dni,
            "Tipo:", tipo,
            "Mes (1-12):", mes,
            "Año:", anio,
            "Base Imponible:", base
        };

        int result = JOptionPane.showConfirmDialog(this, campos,
            "Nueva Declaración Jurada", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            AfiliadoJubilado afiliado = afiliadoDAO.buscarPorDni(dni.getText().trim());
            if (afiliado == null) {
                JOptionPane.showMessageDialog(this, "Afiliado no encontrado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            DeclaracionJurada dj = new DeclaracionJurada(
                afiliado.getIdAfiliado(), null,
                (String) tipo.getSelectedItem(),
                Integer.parseInt(mes.getText().trim()),
                Integer.parseInt(anio.getText().trim()),
                Double.parseDouble(base.getText().trim()),
                usuarioActivo.getIdUsuario()
            );
            if (dao.insertar(dj)) {
                JOptionPane.showMessageDialog(this, "DJ registrada correctamente.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar DJ.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarPorDni() {
        String dni = JOptionPane.showInputDialog(this, "Ingresá el DNI:");
        if (dni != null && !dni.trim().isEmpty()) {
            AfiliadoJubilado afiliado = afiliadoDAO.buscarPorDni(dni.trim());
            if (afiliado == null) {
                JOptionPane.showMessageDialog(this, "Afiliado no encontrado.");
                return;
            }
            modeloTabla.setRowCount(0);
            List<DeclaracionJurada> lista = dao.listarPorAfiliado(afiliado.getIdAfiliado());
            for (DeclaracionJurada dj : lista) {
                modeloTabla.addRow(new Object[]{
                    dj.getIdDj(), dj.getIdAfiliado(), dj.getTipo(),
                    dj.getPeriodoMes() + "/" + dj.getPeriodoAnio(),
                    "$" + dj.getBaseImponible(), dj.getEstado(),
                    dj.getFechaRegistro()
                });
            }
        }
    }

    private void actualizarEstado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccioná una DJ de la tabla.");
            return;
        }
        int idDj = (int) modeloTabla.getValueAt(fila, 0);
        String[] estados = {"PENDIENTE", "CORRECTA", "OBSERVADA", "AJUSTADA", "RECTIFICADA"};
        String nuevoEstado = (String) JOptionPane.showInputDialog(this,
            "Seleccioná el nuevo estado:", "Actualizar Estado",
            JOptionPane.QUESTION_MESSAGE, null, estados, estados[0]);
        if (nuevoEstado != null) {
            if (dao.actualizarEstado(idDj, nuevoEstado)) {
                JOptionPane.showMessageDialog(this, "Estado actualizado correctamente.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar.",
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
