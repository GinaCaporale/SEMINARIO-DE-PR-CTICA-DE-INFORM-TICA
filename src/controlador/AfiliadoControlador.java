package controlador;

import dao.AfiliadoJubiladoDAO;
import modelo.AfiliadoJubilado;
import vista.Menu;
import java.util.List;

public class AfiliadoControlador {

    private AfiliadoJubiladoDAO dao = new AfiliadoJubiladoDAO();

    public void gestionar() {
        int opcion;
        do {
            opcion = Menu.mostrarMenuAfiliados();
            switch (opcion) {
                case 1 -> registrarAfiliado();
                case 2 -> listarAfiliados();
                case 3 -> buscarPorDni();
                case 4 -> actualizarDomicilio();
                case 5 -> darDeBaja();
                case 0 -> Menu.mostrarMensaje("Volviendo al menú principal...");
                default -> Menu.mostrarError("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private void registrarAfiliado() {
        System.out.println("\n--- REGISTRAR NUEVO AFILIADO ---");
        String nro = Menu.leerTexto("Nro de afiliado: ");
        String dni = Menu.leerTexto("DNI: ");
        String apellido = Menu.leerTexto("Apellido: ");
        String nombre = Menu.leerTexto("Nombre: ");
        String email = Menu.leerTexto("Email: ");
        String telefono = Menu.leerTexto("Teléfono: ");
        String domicilio = Menu.leerTexto("Domicilio: ");
        String fechaAlta = Menu.leerTexto("Fecha de alta (YYYY-MM-DD): ");

        AfiliadoJubilado afiliado = new AfiliadoJubilado(
            nro, dni, apellido, nombre, email, telefono, domicilio, fechaAlta
        );

        if (dao.insertar(afiliado)) {
            Menu.mostrarExito("Afiliado registrado correctamente.");
        } else {
            Menu.mostrarError("No se pudo registrar el afiliado.");
        }
    }

    private void listarAfiliados() {
        System.out.println("\n--- LISTADO DE AFILIADOS ---");
        List<AfiliadoJubilado> lista = dao.listarTodos();
        if (lista.isEmpty()) {
            Menu.mostrarMensaje("No hay afiliados registrados.");
        } else {
            lista.forEach(a -> System.out.println(a));
        }
    }

    private void buscarPorDni() {
        String dni = Menu.leerTexto("\nIngresá el DNI a buscar: ");
        AfiliadoJubilado afiliado = dao.buscarPorDni(dni);
        if (afiliado != null) {
            System.out.println("\n--- AFILIADO ENCONTRADO ---");
            System.out.println(afiliado);
        } else {
            Menu.mostrarError("No se encontró ningún afiliado con ese DNI.");
        }
    }

    private void actualizarDomicilio() {
        String dni = Menu.leerTexto("\nIngresá el DNI del afiliado: ");
        AfiliadoJubilado afiliado = dao.buscarPorDni(dni);
        if (afiliado == null) {
            Menu.mostrarError("Afiliado no encontrado.");
            return;
        }
        String nuevoDomicilio = Menu.leerTexto("Nuevo domicilio: ");
        if (dao.actualizarDomicilio(afiliado.getIdAfiliado(), nuevoDomicilio)) {
            Menu.mostrarExito("Domicilio actualizado correctamente.");
        } else {
            Menu.mostrarError("No se pudo actualizar el domicilio.");
        }
    }

    private void darDeBaja() {
        String dni = Menu.leerTexto("\nIngresá el DNI del afiliado a dar de baja: ");
        AfiliadoJubilado afiliado = dao.buscarPorDni(dni);
        if (afiliado == null) {
            Menu.mostrarError("Afiliado no encontrado.");
            return;
        }
        if (afiliado.getEstado().equals("INACTIVO")) {
            Menu.mostrarError("El afiliado ya está dado de baja.");
            return;
        }
        if (dao.darDeBaja(afiliado.getIdAfiliado())) {
            Menu.mostrarExito("Afiliado dado de baja correctamente.");
        } else {
            Menu.mostrarError("No se pudo dar de baja al afiliado.");
        }
    }
}