package controlador;

import dao.AfiliadoJubiladoDAO;
import dao.CuentaCorrienteDAO;
import modelo.AfiliadoJubilado;
import modelo.DetalleCuentaCorriente;
import modelo.Usuario;
import vista.Menu;
import java.util.List;

public class CuentaCorrienteControlador {

    private CuentaCorrienteDAO dao = new CuentaCorrienteDAO();
    private AfiliadoJubiladoDAO afiliadoDAO = new AfiliadoJubiladoDAO();
    private Usuario usuarioActivo;

    public CuentaCorrienteControlador(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
    }

    public void gestionar() {
        int opcion;
        do {
            System.out.println("\n--- CUENTA CORRIENTE ---");
            System.out.println("1. Ver estado de cuenta de un afiliado");
            System.out.println("2. Ver saldo de un afiliado");
            System.out.println("3. Registrar movimiento manual");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccioná una opción: ");
            opcion = Menu.leerEntero();

            switch (opcion) {
                case 1 -> verEstadoCuenta();
                case 2 -> verSaldo();
                case 3 -> registrarMovimiento();
                case 0 -> Menu.mostrarMensaje("Volviendo al menú principal...");
                default -> Menu.mostrarError("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private void verEstadoCuenta() {
        String dni = Menu.leerTexto("\nDNI del afiliado: ");
        AfiliadoJubilado afiliado = afiliadoDAO.buscarPorDni(dni);
        if (afiliado == null) {
            Menu.mostrarError("Afiliado no encontrado.");
            return;
        }

        int idCuenta = dao.obtenerIdCuenta(afiliado.getIdAfiliado());
        if (idCuenta == -1) {
            Menu.mostrarError("El afiliado no tiene cuenta corriente.");
            return;
        }

        List<DetalleCuentaCorriente> movimientos = dao.listarMovimientos(idCuenta);
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║   ESTADO DE CUENTA                               ║");
        System.out.println("║   " + afiliado.getApellido() + ", " + afiliado.getNombre());
        System.out.println("╚══════════════════════════════════════════════════╝");

        if (movimientos.isEmpty()) {
            Menu.mostrarMensaje("No hay movimientos registrados.");
        } else {
            movimientos.forEach(m -> System.out.println(m));
        }

        double saldo = dao.calcularSaldo(idCuenta);
        System.out.println("─────────────────────────────────────────────────");
        System.out.printf("  SALDO ACTUAL: $%.2f%n", saldo);
        System.out.println("─────────────────────────────────────────────────");
    }

    private void verSaldo() {
        String dni = Menu.leerTexto("\nDNI del afiliado: ");
        AfiliadoJubilado afiliado = afiliadoDAO.buscarPorDni(dni);
        if (afiliado == null) {
            Menu.mostrarError("Afiliado no encontrado.");
            return;
        }

        int idCuenta = dao.obtenerIdCuenta(afiliado.getIdAfiliado());
        if (idCuenta == -1) {
            Menu.mostrarError("El afiliado no tiene cuenta corriente.");
            return;
        }

        double saldo = dao.calcularSaldo(idCuenta);
        System.out.println("\nAfiliado: " + afiliado.getApellido() + ", " + afiliado.getNombre());
        System.out.printf("Saldo actual: $%.2f%n", saldo);

        if (saldo < 0) {
            Menu.mostrarError("El afiliado tiene deuda pendiente.");
        } else if (saldo == 0) {
            Menu.mostrarMensaje("El afiliado está al día.");
        } else {
            Menu.mostrarExito("El afiliado tiene saldo a favor.");
        }
    }

    private void registrarMovimiento() {
        String dni = Menu.leerTexto("\nDNI del afiliado: ");
        AfiliadoJubilado afiliado = afiliadoDAO.buscarPorDni(dni);
        if (afiliado == null) {
            Menu.mostrarError("Afiliado no encontrado.");
            return;
        }

        int idCuenta = dao.obtenerIdCuenta(afiliado.getIdAfiliado());
        if (idCuenta == -1) {
            Menu.mostrarError("El afiliado no tiene cuenta corriente.");
            return;
        }

        System.out.println("Tipo de movimiento:");
        System.out.println("1. OBLIGACION");
        System.out.println("2. PAGO");
        System.out.println("3. AJUSTE");
        int tipoOp = Menu.leerEntero();
        String[] tipos = {"OBLIGACION", "PAGO", "AJUSTE"};
        if (tipoOp < 1 || tipoOp > 3) {
            Menu.mostrarError("Opción inválida.");
            return;
        }

        String concepto = Menu.leerTexto("Concepto: ");
        double importe = Double.parseDouble(Menu.leerTexto("Importe (negativo para débito): $"));
        int idReferencia = Integer.parseInt(Menu.leerTexto("ID de referencia: "));

        DetalleCuentaCorriente detalle = new DetalleCuentaCorriente(
            idCuenta, tipos[tipoOp - 1], concepto, importe, idReferencia
        );

        if (dao.insertarMovimiento(detalle)) {
            Menu.mostrarExito("Movimiento registrado correctamente.");
        } else {
            Menu.mostrarError("No se pudo registrar el movimiento.");
        }
    }
}