package dao.interfaces;

import modelo.DeclaracionJurada;
import java.util.List;

public interface IDeclaracionJuradaDAO {
    boolean insertar(DeclaracionJurada dj);
    List<DeclaracionJurada> listarTodas();
    List<DeclaracionJurada> listarPorAfiliado(int idAfiliado);
    boolean actualizarEstado(int idDj, String nuevoEstado);
}