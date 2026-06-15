package dao.interfaces;

import modelo.DetalleCuentaCorriente;
import java.util.List;

public interface ICuentaCorrienteDAO {
    int obtenerIdCuenta(int idAfiliado);
    List<DetalleCuentaCorriente> listarMovimientos(int idCuenta);
    double calcularSaldo(int idCuenta);
    boolean insertarMovimiento(DetalleCuentaCorriente detalle);
}
