package dao;

import modelo.Usuario;
import modelo.Administrativo;
import modelo.Fiscalizacion;
import modelo.Hacienda;
import java.sql.*;

public class UsuarioDAO {

    // AUTENTICAR usuario (login)
    public Usuario autenticar(String email, String contrasena) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND contrasena = ? AND estado = 'ACTIVO'";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id_usuario");
                String nombre = rs.getString("nombre");
                String emailBD = rs.getString("email");
                String clave = rs.getString("contrasena");
                String rol = rs.getString("rol");
                String estado = rs.getString("estado");

                // POLIMORFISMO: devuelve el tipo correcto según el rol
                switch (rol) {
                    case "ADMINISTRATIVO":
                        return new Administrativo(id, nombre, emailBD, clave, estado);
                    case "FISCALIZACION":
                        return new Fiscalizacion(id, nombre, emailBD, clave, estado);
                    case "HACIENDA":
                        return new Hacienda(id, nombre, emailBD, clave, estado);
                    default:
                        return null;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al autenticar usuario: " + e.getMessage());
        }
        return null;
    }
}