package dao;

import modelo.ObligacionMensual;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ObligacionMensualDAO {

    // INSERTAR obligación
    public boolean insertar(ObligacionMensual om) {
        String sql = "INSERT INTO obligacion_mensual (id_dj, id_afiliado, periodo_mes, " +
                     "periodo_anio, importe_determinado, importe_ajustado, estado, " +
                     "id_usuario_registro) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, om.getIdDj());
            ps.setInt(2, om.getIdAfiliado());
            ps.setInt(3, om.getPeriodoMes());
            ps.setInt(4, om.getPeriodoAnio());
            ps.setDouble(5, om.getImporteDeterminado());
            ps.setDouble(6, om.getImporteAjustado());
            ps.setString(7, om.getEstado());
            ps.setInt(8, om.getIdUsuarioRegistro());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar obligación: " + e.getMessage());
            return false;
        }
    }

    // LISTAR todas las obligaciones
    public List<ObligacionMensual> listarTodas() {
        List<ObligacionMensual> lista = new ArrayList<>();
        String sql = "SELECT * FROM obligacion_mensual ORDER BY periodo_anio, periodo_mes";
        try (Connection con = ConexionBD.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar obligaciones: " + e.getMessage());
        }
        return lista;
    }

    // LISTAR obligaciones por afiliado
    public List<ObligacionMensual> listarPorAfiliado(int idAfiliado) {
        List<ObligacionMensual> lista = new ArrayList<>();
        String sql = "SELECT * FROM obligacion_mensual WHERE id_afiliado = ? " +
                     "ORDER BY periodo_anio, periodo_mes";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idAfiliado);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar obligaciones: " + e.getMessage());
        }
        return lista;
    }

    // LISTAR obligaciones pendientes
    public List<ObligacionMensual> listarPendientes() {
        List<ObligacionMensual> lista = new ArrayList<>();
        String sql = "SELECT * FROM obligacion_mensual WHERE estado = 'PENDIENTE' " +
                     "ORDER BY periodo_anio, periodo_mes";
        try (Connection con = ConexionBD.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar pendientes: " + e.getMessage());
        }
        return lista;
    }

    // ACTUALIZAR estado
    public boolean actualizarEstado(int idObligacion, String nuevoEstado) {
        String sql = "UPDATE obligacion_mensual SET estado = ? WHERE id_obligacion = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idObligacion);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar estado: " + e.getMessage());
            return false;
        }
    }

    // MAPEAR ResultSet a objeto
    private ObligacionMensual mapear(ResultSet rs) throws SQLException {
        return new ObligacionMensual(
            rs.getInt("id_obligacion"),
            rs.getInt("id_dj"),
            rs.getInt("id_afiliado"),
            rs.getInt("periodo_mes"),
            rs.getInt("periodo_anio"),
            rs.getDouble("importe_determinado"),
            rs.getDouble("importe_ajustado"),
            rs.getString("estado"),
            rs.getString("fecha_generacion"),
            rs.getInt("id_usuario_registro")
        );
    }
}