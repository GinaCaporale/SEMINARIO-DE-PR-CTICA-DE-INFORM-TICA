package vista;

import dao.AfiliadoJubiladoDAO;
import dao.ObligacionMensualDAO;
import modelo.AfiliadoJubilado;
import modelo.ObligacionMensual;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ObligacionMensualVentana extends JFrame {

    private Usuario usuarioActivo;
    private ObligacionMensualDAO dao = new ObligacionMensualDAO();
    private AfiliadoJubiladoDAO afiliadoDAO = new AfiliadoJubiladoDAO();
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public ObligacionMensualVentana(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setTitle("Obligaciones Mensuales");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 450);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = {"ID", "ID DJ", "ID Afiliado", "Período",
                             "Importe", "Ajuste", "Estado", "Fecha"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setBackground(new Color(0, 102, 153));
        tabla.getTableHeader().setForeground(Color.WHITE);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnNueva = crearBoton("Nueva Obligación", new Color(0, 153, 76));
        JButton btnBuscar = crearBoton("Buscar por DNI", new Color(0, 102, 153));
        JButton btnPendientes = crearBoton("Ver Pendientes", new Color(204, 102, 0));
        JButton btnEstado = crearBoton("Actualizar Estado", new Color(100, 100, 200));
        JButton btnRefrescar = crearBoton("Refrescar", new Color(100, 100, 100));

        panelBotones.add(btnNueva);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnPendientes);
        panelBotones.add(btnEstado);
        panelBotones.add(btnRefrescar);
        panel.add(panelBotones, BorderLayout.SOUTH);
        add(panel);

        btnNueva.addActionListener(e -> registrarObligacion());
        btnBuscar.addActionListener(e -> buscarPorDni());
        btnPendientes.addActionListener(e -> listarPendientes());
        btnEstado.addActionListener(e -> actualizarEstado());
        btnRefrescar.addActionListener(e -> cargarTabla());
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<ObligacionMensual> lista = dao.listarTodas();
        for (ObligacionMensual om : lista) {
            modeloTabla.addRow(new Object[]{
                om.getIdObligacion(), om.getIdDj(), om.getIdAfiliado(),
                om.getPeriodoMes() + "/" + om.getPeriodoAnio(),
                "$" + om.getImporteDeterminado(),
                "$" + om.getImporteAjustado(),
                om.getEstado(), om.getFechaGeneracion()
            });
        }
    }

    private void registrarObligacion() {
        JTextField dni = new JTextField();
        JTextField idDj = new JTextField();
        JTextField mes = new JTextField();
        JTextField anio = new JTextField();
        JTextField importe = new JTextField();

        Object[] campos = {
            "DNI del afiliado:", dni,
            "ID de la DJ:", idDj,
            "Mes (1-12):", mes,
            "Año:", anio,
            "Importe:", importe
        };

        int result = JOptionPane.showConfirmDialog(this, campos,
            "Nueva Obligación", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            AfiliadoJubilado afiliado = afiliadoDAO.buscarPorDni(dni.getText().trim());
            if (afiliado == null) {
                JOptionPane.showMessageDialog(this, "Afiliado no encontrado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ObligacionMensual om = new ObligacionMensual(
                Integer.parseInt(idDj.getText().trim()),
                afiliado.getIdAfiliado(),
                Integer.parseInt(mes.getText().trim()),
                Integer.parseInt(anio.getText().trim()),
                Double.parseDouble(importe.getText().trim()),
                usuarioActivo.getIdUsuario()
            );
            if (dao.insertar(om)) {
                JOptionPane.showMessageDialog(this, "Obligación registrada correctamente.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar.",
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
            List<ObligacionMensual> lista = dao.listarPorAfiliado(afiliado.getIdAfiliado());
            for (ObligacionMensual om : lista) {
                modeloTabla.addRow(new Object[]{
                    om.getIdObligacion(), om.getIdDj(), om.getIdAfiliado(),
                    om.getPeriodoMes() + "/" + om.getPeriodoAnio(),
                    "$" + om.getImporteDeterminado(),
                    "$" + om.getImporteAjustado(),
                    om.getEstado(), om.getFechaGeneracion()
                });
            }
        }
    }

    private void listarPendientes() {
        modeloTabla.setRowCount(0);
        List<ObligacionMensual> lista = dao.listarPendientes();
        for (ObligacionMensual om : lista) {
            modeloTabla.addRow(new Object[]{
                om.getIdObligacion(), om.getIdDj(), om.getIdAfiliado(),
                om.getPeriodoMes() + "/" + om.getPeriodoAnio(),
                "$" + om.getImporteDeterminado(),
                "$" + om.getImporteAjustado(),
                om.getEstado(), om.getFechaGeneracion()
            });
        }
    }

    private void actualizarEstado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccioná una obligación de la tabla.");
            return;
        }
        int idOm = (int) modeloTabla.getValueAt(fila, 0);
        String[] estados = {"PENDIENTE", "PAGADA", "PARCIAL"};
        String nuevoEstado = (String) JOptionPane.showInputDialog(this,
            "Seleccioná el nuevo estado:", "Actualizar Estado",
            JOptionPane.QUESTION_MESSAGE, null, estados, estados[0]);
        if (nuevoEstado != null) {
            if (dao.actualizarEstado(idOm, nuevoEstado)) {
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