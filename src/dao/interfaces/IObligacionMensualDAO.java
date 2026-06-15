package dao.interfaces;

import modelo.ObligacionMensual;
import java.util.List;

public interface IObligacionMensualDAO {
    boolean insertar(ObligacionMensual om);
    List<ObligacionMensual> listarTodas();
    List<ObligacionMensual> listarPorAfiliado(int idAfiliado);
    List<ObligacionMensual> listarPendientes();
    boolean actualizarEstado(int idObligacion, String nuevoEstado);
}
