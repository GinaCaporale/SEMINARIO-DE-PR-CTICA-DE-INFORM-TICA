package vista;

import dao.AfiliadoJubiladoDAO;
import modelo.AfiliadoJubilado;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AfiliadoVentana extends JFrame {

    private Usuario usuarioActivo;
    private AfiliadoJubiladoDAO dao = new AfiliadoJubiladoDAO();
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public AfiliadoVentana(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
        initComponents();
        cargarTabla();
    }

    private void initComponents() {
        setTitle("Gestión de Afiliados");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabla
        String[] columnas = {"ID", "Nro Afiliado", "DNI", "Apellido",
                             "Nombre", "Email", "Teléfono", "Estado"};
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

        JButton btnNuevo = crearBoton("Nuevo Afiliado", new Color(0, 153, 76));
        JButton btnBuscar = crearBoton("Buscar por DNI", new Color(0, 102, 153));
        JButton btnActualizar = crearBoton("Actualizar Domicilio", new Color(204, 102, 0));
        JButton btnBaja = crearBoton("Dar de Baja", new Color(180, 0, 0));
        JButton btnRefrescar = crearBoton("Refrescar", new Color(100, 100, 100));

        panelBotones.add(btnNuevo);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnBaja);
        panelBotones.add(btnRefrescar);
        panel.add(panelBotones, BorderLayout.SOUTH);

        add(panel);

        // Acciones
        btnNuevo.addActionListener(e -> registrarAfiliado());
        btnBuscar.addActionListener(e -> buscarPorDni());
        btnActualizar.addActionListener(e -> actualizarDomicilio());
        btnBaja.addActionListener(e -> darDeBaja());
        btnRefrescar.addActionListener(e -> cargarTabla());
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<AfiliadoJubilado> lista = dao.listarTodos();
        for (AfiliadoJubilado a : lista) {
            modeloTabla.addRow(new Object[]{
                a.getIdAfiliado(), a.getNroAfiliado(), a.getDni(),
                a.getApellido(), a.getNombre(), a.getEmail(),
                a.getTelefono(), a.getEstado()
            });
        }
    }

    private void registrarAfiliado() {
        JTextField nro = new JTextField();
        JTextField dni = new JTextField();
        JTextField apellido = new JTextField();
        JTextField nombre = new JTextField();
        JTextField email = new JTextField();
        JTextField telefono = new JTextField();
        JTextField domicilio = new JTextField();
        JTextField fechaAlta = new JTextField("2024-01-01");

        Object[] campos = {
            "Nro Afiliado:", nro, "DNI:", dni,
            "Apellido:", apellido, "Nombre:", nombre,
            "Email:", email, "Teléfono:", telefono,
            "Domicilio:", domicilio, "Fecha Alta (YYYY-MM-DD):", fechaAlta
        };

        int result = JOptionPane.showConfirmDialog(this, campos,
            "Nuevo Afiliado", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            AfiliadoJubilado afiliado = new AfiliadoJubilado(
                nro.getText(), dni.getText(), apellido.getText(),
                nombre.getText(), email.getText(), telefono.getText(),
                domicilio.getText(), fechaAlta.getText()
            );
            if (dao.insertar(afiliado)) {
                JOptionPane.showMessageDialog(this, "Afiliado registrado correctamente.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar afiliado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarPorDni() {
        String dni = JOptionPane.showInputDialog(this, "Ingresá el DNI:");
        if (dni != null && !dni.trim().isEmpty()) {
            AfiliadoJubilado a = dao.buscarPorDni(dni.trim());
            if (a != null) {
                JOptionPane.showMessageDialog(this,
                    "Afiliado encontrado:\n" + a.toString());
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún afiliado con ese DNI.");
            }
        }
    }

    private void actualizarDomicilio() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccioná un afiliado de la tabla.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nuevoDomicilio = JOptionPane.showInputDialog(this, "Nuevo domicilio:");
        if (nuevoDomicilio != null && !nuevoDomicilio.trim().isEmpty()) {
            if (dao.actualizarDomicilio(id, nuevoDomicilio.trim())) {
                JOptionPane.showMessageDialog(this, "Domicilio actualizado correctamente.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void darDeBaja() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccioná un afiliado de la tabla.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String estado = (String) modeloTabla.getValueAt(fila, 7);
        if (estado.equals("INACTIVO")) {
            JOptionPane.showMessageDialog(this, "El afiliado ya está dado de baja.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Dar de baja al afiliado seleccionado?", "Confirmar",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.darDeBaja(id)) {
                JOptionPane.showMessageDialog(this, "Afiliado dado de baja correctamente.");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al dar de baja.",
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