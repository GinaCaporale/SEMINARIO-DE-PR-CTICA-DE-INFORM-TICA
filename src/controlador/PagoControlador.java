package controlador;

import dao.AfiliadoJubiladoDAO;
import dao.PagoDAO;
import modelo.AfiliadoJubilado;
import modelo.Pago;
import modelo.Usuario;
import vista.Menu;
import java.util.List;

public class PagoControlador {

    private PagoDAO dao = new PagoDAO();
    private AfiliadoJubiladoDAO afiliadoDAO = new AfiliadoJubiladoDAO();
    private Usuario usuarioActivo;
    
    // Arreglo estático de medios de pago disponibles
private static final String[] MEDIOS_PAGO = {"TRANSFERENCIA", "EFECTIVO", "CHEQUE"};

// Arreglo estático de estados de pago
private static final String[] ESTADOS_PAGO = {"PENDIENTE_VALIDACION", "VALIDADO", "RECHAZADO"};

    public PagoControlador(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
    }

    public void gestionar() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE PAGOS ---");
            System.out.println("1. Registrar nuevo pago");
            System.out.println("2. Listar todos los pagos");
            System.out.println("3. Listar pagos por afiliado");
            System.out.println("4. Listar pagos pendientes de validación");
            System.out.println("5. Validar pago");
            System.out.println("6. Rechazar pago");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccioná una opción: ");
            opcion = Menu.leerEntero();

            switch (opcion) {
                case 1 -> registrarPago();
                case 2 -> listarTodos();
                case 3 -> listarPorAfiliado();
                case 4 -> listarPendientes();
                case 5 -> validarPago();
                case 6 -> rechazarPago();
                case 0 -> Menu.mostrarMensaje("Volviendo al menú principal...");
                default -> Menu.mostrarError("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private void registrarPago() {
        System.out.println("\n--- REGISTRAR NUEVO PAGO ---");

        String dni = Menu.leerTexto("DNI del afiliado: ");
        AfiliadoJubilado afiliado = afiliadoDAO.buscarPorDni(dni);
        if (afiliado == null) {
            Menu.mostrarError("Afiliado no encontrado.");
            return;
        }
        System.out.println("Afiliado: " + afiliado.getApellido() + ", " + afiliado.getNombre());

        int idObligacion = Integer.parseInt(Menu.leerTexto("ID de la obligación: "));
        double importe = Double.parseDouble(Menu.leerTexto("Importe: $"));
        String fecha = Menu.leerTexto("Fecha de pago (YYYY-MM-DD): ");

        System.out.println("Medio de pago:");
        System.out.println("1. TRANSFERENCIA");
        System.out.println("2. EFECTIVO");
        System.out.println("3. CHEQUE");
        int medioOp = Menu.leerEntero();
        String[] medios = MEDIOS_PAGO;
        if (medioOp < 1 || medioOp > 3) {
            Menu.mostrarError("Opción inválida.");
            return;
        }

        Pago pago = new Pago(
            afiliado.getIdAfiliado(), idObligacion,
            importe, fecha, medios[medioOp - 1]
        );

        if (dao.insertar(pago)) {
            Menu.mostrarExito("Pago registrado. Pendiente de validación por Hacienda.");
        } else {
            Menu.mostrarError("No se pudo registrar el pago.");
        }
    }

    private void listarTodos() {
        List<Pago> lista = dao.listarTodos();
        if (lista.isEmpty()) {
            Menu.mostrarMensaje("No hay pagos registrados.");
        } else {
            System.out.println("\n--- TODOS LOS PAGOS ---");
            lista.forEach(p -> System.out.println(p));
        }
    }

    private void listarPorAfiliado() {
        String dni = Menu.leerTexto("\nDNI del afiliado: ");
        AfiliadoJubilado afiliado = afiliadoDAO.buscarPorDni(dni);
        if (afiliado == null) {
            Menu.mostrarError("Afiliado no encontrado.");
            return;
        }
        List<Pago> lista = dao.listarPorAfiliado(afiliado.getIdAfiliado());
        if (lista.isEmpty()) {
            Menu.mostrarMensaje("El afiliado no tiene pagos registrados.");
        } else {
            System.out.println("\n--- PAGOS de " + afiliado.getApellido() + " ---");
            lista.forEach(p -> System.out.println(p));
        }
    }

    private void listarPendientes() {
        List<Pago> lista = dao.listarPendientesValidacion();
        if (lista.isEmpty()) {
            Menu.mostrarMensaje("No hay pagos pendientes de validación.");
        } else {
            System.out.println("\n--- PAGOS PENDIENTES DE VALIDACIÓN ---");
            lista.forEach(p -> System.out.println(p));
        }
    }

    private void validarPago() {
        if (!usuarioActivo.getRol().equals("HACIENDA")) {
            Menu.mostrarError("Solo el área de Hacienda puede validar pagos.");
            return;
        }
        int idPago = Integer.parseInt(Menu.leerTexto("\nID del pago a validar: "));
        if (dao.validarPago(idPago, usuarioActivo.getIdUsuario())) {
            Menu.mostrarExito("Pago validado correctamente.");
        } else {
            Menu.mostrarError("No se pudo validar el pago.");
        }
    }

    private void rechazarPago() {
        if (!usuarioActivo.getRol().equals("HACIENDA")) {
            Menu.mostrarError("Solo el área de Hacienda puede rechazar pagos.");
            return;
        }
        int idPago = Integer.parseInt(Menu.leerTexto("\nID del pago a rechazar: "));
        String motivo = Menu.leerTexto("Motivo del rechazo: ");
        if (dao.rechazarPago(idPago, usuarioActivo.getIdUsuario(), motivo)) {
            Menu.mostrarExito("Pago rechazado correctamente.");
        } else {
            Menu.mostrarError("No se pudo rechazar el pago.");
        }
    }
}