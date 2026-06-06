package controlador;

import dao.AfiliadoJubiladoDAO;
import dao.ObligacionMensualDAO;
import modelo.AfiliadoJubilado;
import modelo.ObligacionMensual;
import modelo.Usuario;
import vista.Menu;
import java.util.List;

public class ObligacionMensualControlador {

    private ObligacionMensualDAO dao = new ObligacionMensualDAO();
    private AfiliadoJubiladoDAO afiliadoDAO = new AfiliadoJubiladoDAO();
    private Usuario usuarioActivo;

    public ObligacionMensualControlador(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
    }

    public void gestionar() {
        int opcion;
        do {
            System.out.println("\n--- OBLIGACIONES MENSUALES ---");
            System.out.println("1. Registrar nueva obligación");
            System.out.println("2. Listar todas las obligaciones");
            System.out.println("3. Listar obligaciones por afiliado");
            System.out.println("4. Listar obligaciones pendientes");
            System.out.println("5. Actualizar estado de obligación");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccioná una opción: ");
            opcion = Menu.leerEntero();

            switch (opcion) {
                case 1 -> registrarObligacion();
                case 2 -> listarTodas();
                case 3 -> listarPorAfiliado();
                case 4 -> listarPendientes();
                case 5 -> actualizarEstado();
                case 0 -> Menu.mostrarMensaje("Volviendo al menú principal...");
                default -> Menu.mostrarError("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private void registrarObligacion() {
        System.out.println("\n--- REGISTRAR NUEVA OBLIGACIÓN ---");

        String dni = Menu.leerTexto("DNI del afiliado: ");
        AfiliadoJubilado afiliado = afiliadoDAO.buscarPorDni(dni);
        if (afiliado == null) {
            Menu.mostrarError("Afiliado no encontrado.");
            return;
        }
        System.out.println("Afiliado: " + afiliado.getApellido() + ", " + afiliado.getNombre());

        int idDj = Integer.parseInt(Menu.leerTexto("ID de la DJ asociada: "));
        int mes = Integer.parseInt(Menu.leerTexto("Período - Mes (1-12): "));
        int anio = Integer.parseInt(Menu.leerTexto("Período - Año (ej: 2024): "));
        double importe = Double.parseDouble(Menu.leerTexto("Importe determinado: $"));

        ObligacionMensual om = new ObligacionMensual(
            idDj, afiliado.getIdAfiliado(), mes, anio,
            importe, usuarioActivo.getIdUsuario()
        );

        if (dao.insertar(om)) {
            Menu.mostrarExito("Obligación registrada correctamente.");
        } else {
            Menu.mostrarError("No se pudo registrar la obligación.");
        }
    }

    private void listarTodas() {
        List<ObligacionMensual> lista = dao.listarTodas();
        if (lista.isEmpty()) {
            Menu.mostrarMensaje("No hay obligaciones registradas.");
        } else {
            System.out.println("\n--- TODAS LAS OBLIGACIONES ---");
            lista.forEach(om -> System.out.println(om));
        }
    }

    private void listarPorAfiliado() {
        String dni = Menu.leerTexto("\nDNI del afiliado: ");
        AfiliadoJubilado afiliado = afiliadoDAO.buscarPorDni(dni);
        if (afiliado == null) {
            Menu.mostrarError("Afiliado no encontrado.");
            return;
        }
        List<ObligacionMensual> lista = dao.listarPorAfiliado(afiliado.getIdAfiliado());
        if (lista.isEmpty()) {
            Menu.mostrarMensaje("El afiliado no tiene obligaciones registradas.");
        } else {
            System.out.println("\n--- OBLIGACIONES de " + afiliado.getApellido() + " ---");
            lista.forEach(om -> System.out.println(om));
        }
    }

    private void listarPendientes() {
        List<ObligacionMensual> lista = dao.listarPendientes();
        if (lista.isEmpty()) {
            Menu.mostrarMensaje("No hay obligaciones pendientes.");
        } else {
            System.out.println("\n--- OBLIGACIONES PENDIENTES ---");
            lista.forEach(om -> System.out.println(om));
        }
    }

    private void actualizarEstado() {
        int idOm = Integer.parseInt(Menu.leerTexto("\nID de la obligación: "));
        System.out.println("Nuevo estado:");
        System.out.println("1. PENDIENTE");
        System.out.println("2. PAGADA");
        System.out.println("3. PARCIAL");
        int op = Menu.leerEntero();
        String[] estados = {"PENDIENTE", "PAGADA", "PARCIAL"};
        if (op < 1 || op > 3) {
            Menu.mostrarError("Opción inválida.");
            return;
        }
        if (dao.actualizarEstado(idOm, estados[op - 1])) {
            Menu.mostrarExito("Estado actualizado correctamente.");
        } else {
            Menu.mostrarError("No se pudo actualizar el estado.");
        }
    }
}
