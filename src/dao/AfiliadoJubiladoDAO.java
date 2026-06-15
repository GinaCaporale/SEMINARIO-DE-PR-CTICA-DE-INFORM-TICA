package dao;

import modelo.AfiliadoJubilado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dao.interfaces.IAfiliadoJubiladoDAO;

public class AfiliadoJubiladoDAO implements IAfiliadoJubiladoDAO {

    // INSERTAR afiliado
    public boolean insertar(AfiliadoJubilado afiliado) {
        String sql = "INSERT INTO afiliado_jubilado (nro_afiliado, dni, apellido, " +
                     "nombre, email, telefono, domicilio, estado, fecha_alta) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, afiliado.getNroAfiliado());
            ps.setString(2, afiliado.getDni());
            ps.setString(3, afiliado.getApellido());
            ps.setString(4, afiliado.getNombre());
            ps.setString(5, afiliado.getEmail());
            ps.setString(6, afiliado.getTelefono());
            ps.setString(7, afiliado.getDomicilio());
            ps.setString(8, afiliado.getEstado());
            ps.setString(9, afiliado.getFechaAlta());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar afiliado: " + e.getMessage());
            return false;
        }
    }

    // LISTAR todos los afiliados
    public List<AfiliadoJubilado> listarTodos() {
        List<AfiliadoJubilado> lista = new ArrayList<>();
        String sql = "SELECT * FROM afiliado_jubilado";
        try (Connection con = ConexionBD.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new AfiliadoJubilado(
                    rs.getInt("id_afiliado"),
                    rs.getString("nro_afiliado"),
                    rs.getString("dni"),
                    rs.getString("apellido"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("telefono"),
                    rs.getString("domicilio"),
                    rs.getString("estado"),
                    rs.getString("fecha_alta")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar afiliados: " + e.getMessage());
        }
        return lista;
    }

    // BUSCAR por DNI
    public AfiliadoJubilado buscarPorDni(String dni) {
        String sql = "SELECT * FROM afiliado_jubilado WHERE dni = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new AfiliadoJubilado(
                    rs.getInt("id_afiliado"),
                    rs.getString("nro_afiliado"),
                    rs.getString("dni"),
                    rs.getString("apellido"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("telefono"),
                    rs.getString("domicilio"),
                    rs.getString("estado"),
                    rs.getString("fecha_alta")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar afiliado: " + e.getMessage());
        }
        return null;
    }

    // ACTUALIZAR domicilio
    public boolean actualizarDomicilio(int idAfiliado, String nuevoDomicilio) {
        String sql = "UPDATE afiliado_jubilado SET domicilio = ? WHERE id_afiliado = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoDomicilio);
            ps.setInt(2, idAfiliado);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar domicilio: " + e.getMessage());
            return false;
        }
    }

    // DAR DE BAJA (cambio de estado)
    public boolean darDeBaja(int idAfiliado) {
        String sql = "UPDATE afiliado_jubilado SET estado = 'INACTIVO' WHERE id_afiliado = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idAfiliado);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al dar de baja: " + e.getMessage());
            return false;
        }
    }
}   