package dao;

import modelo.Pago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {

    // INSERTAR pago
    public boolean insertar(Pago pago) {
        String sql = "INSERT INTO pago (id_afiliado, id_obligacion, importe, " +
                     "fecha_pago, medio_pago, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, pago.getIdAfiliado());
            ps.setInt(2, pago.getIdObligacion());
            ps.setDouble(3, pago.getImporte());
            ps.setString(4, pago.getFechaPago());
            ps.setString(5, pago.getMedioPago());
            ps.setString(6, pago.getEstado());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar pago: " + e.getMessage());
            return false;
        }
    }

    // LISTAR todos los pagos
    public List<Pago> listarTodos() {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM pago ORDER BY fecha_pago DESC";
        try (Connection con = ConexionBD.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar pagos: " + e.getMessage());
        }
        return lista;
    }

    // LISTAR pagos pendientes de validación
    public List<Pago> listarPendientesValidacion() {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM pago WHERE estado = 'PENDIENTE_VALIDACION'";
        try (Connection con = ConexionBD.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar pagos pendientes: " + e.getMessage());
        }
        return lista;
    }

    // LISTAR pagos por afiliado
    public List<Pago> listarPorAfiliado(int idAfiliado) {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM pago WHERE id_afiliado = ? ORDER BY fecha_pago DESC";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idAfiliado);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar pagos: " + e.getMessage());
        }
        return lista;
    }

    // VALIDAR pago
    public boolean validarPago(int idPago, int idUsuarioValida) {
        String sql = "UPDATE pago SET estado = 'VALIDADO', " +
                     "id_usuario_valida = ?, fecha_validacion = NOW() " +
                     "WHERE id_pago = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuarioValida);
            ps.setInt(2, idPago);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al validar pago: " + e.getMessage());
            return false;
        }
    }

    // RECHAZAR pago
    public boolean rechazarPago(int idPago, int idUsuarioValida, String motivo) {
        String sql = "UPDATE pago SET estado = 'RECHAZADO', " +
                     "id_usuario_valida = ?, fecha_validacion = NOW(), " +
                     "motivo_rechazo = ? WHERE id_pago = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuarioValida);
            ps.setString(2, motivo);
            ps.setInt(3, idPago);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al rechazar pago: " + e.getMessage());
            return false;
        }
    }

    // MAPEAR ResultSet a objeto
    private Pago mapear(ResultSet rs) throws SQLException {
        int idUsuarioValida = rs.getInt("id_usuario_valida");
        return new Pago(
            rs.getInt("id_pago"),
            rs.getInt("id_afiliado"),
            rs.getInt("id_obligacion"),
            rs.getDouble("importe"),
            rs.getString("fecha_pago"),
            rs.getString("medio_pago"),
            rs.getString("estado"),
            rs.getString("motivo_rechazo"),
            rs.wasNull() ? null : idUsuarioValida,
            rs.getString("fecha_validacion")
        );
    }
}