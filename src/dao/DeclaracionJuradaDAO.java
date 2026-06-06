package dao;

import modelo.DeclaracionJurada;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeclaracionJuradaDAO {

    // INSERTAR DJ
    public boolean insertar(DeclaracionJurada dj) {
        String sql = "INSERT INTO declaracion_jurada (id_afiliado, id_dj_original, tipo, " +
                     "periodo_mes, periodo_anio, base_imponible, estado, id_usuario_registro) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, dj.getIdAfiliado());
            if (dj.getIdDjOriginal() == null)
                ps.setNull(2, Types.INTEGER);
            else
                ps.setInt(2, dj.getIdDjOriginal());
            ps.setString(3, dj.getTipo());
            ps.setInt(4, dj.getPeriodoMes());
            ps.setInt(5, dj.getPeriodoAnio());
            ps.setDouble(6, dj.getBaseImponible());
            ps.setString(7, dj.getEstado());
            ps.setInt(8, dj.getIdUsuarioRegistro());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar DJ: " + e.getMessage());
            return false;
        }
    }

    // LISTAR DJ por afiliado
    public List<DeclaracionJurada> listarPorAfiliado(int idAfiliado) {
        List<DeclaracionJurada> lista = new ArrayList<>();
        String sql = "SELECT * FROM declaracion_jurada WHERE id_afiliado = ? ORDER BY periodo_anio, periodo_mes";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idAfiliado);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar DJ: " + e.getMessage());
        }
        return lista;
    }

    // LISTAR todas las DJ
    public List<DeclaracionJurada> listarTodas() {
        List<DeclaracionJurada> lista = new ArrayList<>();
        String sql = "SELECT * FROM declaracion_jurada ORDER BY periodo_anio, periodo_mes";
        try (Connection con = ConexionBD.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar DJ: " + e.getMessage());
        }
        return lista;
    }

    // ACTUALIZAR estado
    public boolean actualizarEstado(int idDj, String nuevoEstado) {
        String sql = "UPDATE declaracion_jurada SET estado = ? WHERE id_dj = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idDj);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar estado DJ: " + e.getMessage());
            return false;
        }
    }

    // MAPEAR ResultSet a objeto
    private DeclaracionJurada mapear(ResultSet rs) throws SQLException {
        int idDjOriginal = rs.getInt("id_dj_original");
        return new DeclaracionJurada(
            rs.getInt("id_dj"),
            rs.getInt("id_afiliado"),
            rs.wasNull() ? null : idDjOriginal,
            rs.getString("tipo"),
            rs.getInt("periodo_mes"),
            rs.getInt("periodo_anio"),
            rs.getDouble("base_imponible"),
            rs.getString("estado"),
            rs.getString("fecha_registro"),
            rs.getInt("id_usuario_registro")
        );
    }
}