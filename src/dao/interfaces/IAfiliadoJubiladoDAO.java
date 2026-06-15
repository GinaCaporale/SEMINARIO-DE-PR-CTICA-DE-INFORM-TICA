package dao.interfaces;

import modelo.AfiliadoJubilado;
import java.util.List;

public interface IAfiliadoJubiladoDAO {
    boolean insertar(AfiliadoJubilado afiliado);
    List<AfiliadoJubilado> listarTodos();
    AfiliadoJubilado buscarPorDni(String dni);
    boolean actualizarDomicilio(int idAfiliado, String nuevoDomicilio);
    boolean darDeBaja(int idAfiliado);
}
