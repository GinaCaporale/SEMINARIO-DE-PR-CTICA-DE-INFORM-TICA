package dao;

import modelo.DetalleCuentaCorriente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dao.interfaces.ICuentaCorrienteDAO;

public class CuentaCorrienteDAO implements ICuentaCorrienteDAO {

    // OBTENER id de cuenta por afiliado
    public int obtenerIdCuenta(int idAfiliado) {
        String sql = "SELECT id_cuenta FROM cuenta_corriente WHERE id_afiliado = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idAfiliado);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_cuenta");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener cuenta: " + e.getMessage());
        }
        return -1;
    }

    // LISTAR movimientos de una cuenta
    public List<DetalleCuentaCorriente> listarMovimientos(int idCuenta) {
        List<DetalleCuentaCorriente> lista = new ArrayList<>();
        String sql = "SELECT * FROM detalle_cuenta_corriente WHERE id_cuenta = ? " +
                     "ORDER BY fecha_movimiento";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCuenta);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new DetalleCuentaCorriente(
                    rs.getLong("id_detalle"),
                    rs.getInt("id_cuenta"),
                    rs.getString("tipo_movimiento"),
                    rs.getString("concepto"),
                    rs.getDouble("importe"),
                    rs.getString("fecha_movimiento"),
                    rs.getInt("id_referencia")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar movimientos: " + e.getMessage());
        }
        return lista;
    }

    // CALCULAR saldo de una cuenta
    public double calcularSaldo(int idCuenta) {
        String sql = "SELECT SUM(importe) AS saldo FROM detalle_cuenta_corriente " +
                     "WHERE id_cuenta = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCuenta);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("saldo");
            }
        } catch (SQLException e) {
            System.out.println("Error al calcular saldo: " + e.getMessage());
        }
        return 0.0;
    }

    // INSERTAR movimiento
    public boolean insertarMovimiento(DetalleCuentaCorriente detalle) {
        String sql = "INSERT INTO detalle_cuenta_corriente (id_cuenta, tipo_movimiento, " +
                     "concepto, importe, id_referencia) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detalle.getIdCuenta());
            ps.setString(2, detalle.getTipoMovimiento());
            ps.setString(3, detalle.getConcepto());
            ps.setDouble(4, detalle.getImporte());
            ps.setInt(5, detalle.getIdReferencia());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar movimiento: " + e.getMessage());
            return false;
        }
    }
}