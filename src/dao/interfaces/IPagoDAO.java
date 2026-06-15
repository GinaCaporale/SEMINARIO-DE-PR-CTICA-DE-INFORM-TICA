package dao.interfaces;

import modelo.Pago;
import java.util.List;

public interface IPagoDAO {
    boolean insertar(Pago pago);
    List<Pago> listarTodos();
    List<Pago> listarPorAfiliado(int idAfiliado);
    List<Pago> listarPendientesValidacion();
    boolean validarPago(int idPago, int idUsuarioValida);
    boolean rechazarPago(int idPago, int idUsuarioValida, String motivo);
}