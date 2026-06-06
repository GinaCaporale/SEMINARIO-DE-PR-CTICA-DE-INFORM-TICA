package controlador;

import dao.AfiliadoJubiladoDAO;
import dao.DeclaracionJuradaDAO;
import modelo.AfiliadoJubilado;
import modelo.DeclaracionJurada;
import modelo.Usuario;
import vista.Menu;
import java.util.List;

public class DeclaracionJuradaControlador {

    private DeclaracionJuradaDAO dao = new DeclaracionJuradaDAO();
    private AfiliadoJubiladoDAO afiliadoDAO = new AfiliadoJubiladoDAO();
    private Usuario usuarioActivo;

    public DeclaracionJuradaControlador(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
    }

    public void gestionar() {
        int opcion;
        do {
            System.out.println("\n--- DECLARACIONES JURADAS ---");
            System.out.println("1. Registrar nueva DJ");
            System.out.println("2. Listar DJ por afiliado");
            System.out.println("3. Listar todas las DJ");
            System.out.println("4. Actualizar estado de DJ");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccioná una opción: ");
            opcion = Menu.leerEntero();

            switch (opcion) {
                case 1 -> registrarDJ();
                case 2 -> listarPorAfiliado();
                case 3 -> listarTodas();
                case 4 -> actualizarEstado();
                case 0 -> Menu.mostrarMensaje("Volviendo al menú principal...");
                default -> Menu.mostrarError("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private void registrarDJ() {
        System.out.println("\n--- REGISTRAR NUEVA DJ ---");

        String dni = Menu.leerTexto("DNI del afiliado: ");
        AfiliadoJubilado afiliado = afiliadoDAO.buscarPorDni(dni);
        if (afiliado == null) {
            Menu.mostrarError("Afiliado no encontrado.");
            return;
        }
        System.out.println("Afiliado: " + afiliado.getApellido() + ", " + afiliado.getNombre());

        System.out.println("Tipo de DJ:");
        System.out.println("1. ORIGINAL");
        System.out.println("2. RECTIFICATIVA");
        int tipoOpcion = Menu.leerEntero();
        String tipo = tipoOpcion == 2 ? "RECTIFICATIVA" : "ORIGINAL";

        Integer idDjOriginal = null;
        if (tipo.equals("RECTIFICATIVA")) {
            idDjOriginal = Integer.parseInt(Menu.leerTexto("ID de la DJ original que rectifica: "));
        }

        int mes = Integer.parseInt(Menu.leerTexto("Período - Mes (1-12): "));
        int anio = Integer.parseInt(Menu.leerTexto("Período - Año (ej: 2024): "));
        double base = Double.parseDouble(Menu.leerTexto("Base imponible: $"));

        DeclaracionJurada dj = new DeclaracionJurada(
            afiliado.getIdAfiliado(), idDjOriginal, tipo,
            mes, anio, base, usuarioActivo.getIdUsuario()
        );

        if (dao.insertar(dj)) {
            Menu.mostrarExito("Declaración Jurada registrada correctamente.");
        } else {
            Menu.mostrarError("No se pudo registrar la DJ.");
        }
    }

    private void listarPorAfiliado() {
        String dni = Menu.leerTexto("\nDNI del afiliado: ");
        AfiliadoJubilado afiliado = afiliadoDAO.buscarPorDni(dni);
        if (afiliado == null) {
            Menu.mostrarError("Afiliado no encontrado.");
            return;
        }
        List<DeclaracionJurada> lista = dao.listarPorAfiliado(afiliado.getIdAfiliado());
        if (lista.isEmpty()) {
            Menu.mostrarMensaje("El afiliado no tiene DJ registradas.");
        } else {
            System.out.println("\n--- DJ de " + afiliado.getApellido() + " ---");
            lista.forEach(dj -> System.out.println(dj));
        }
    }

    private void listarTodas() {
        List<DeclaracionJurada> lista = dao.listarTodas();
        if (lista.isEmpty()) {
            Menu.mostrarMensaje("No hay DJ registradas.");
        } else {
            System.out.println("\n--- TODAS LAS DJ ---");
            lista.forEach(dj -> System.out.println(dj));
        }
    }

    private void actualizarEstado() {
        int idDj = Integer.parseInt(Menu.leerTexto("\nID de la DJ: "));
        System.out.println("Nuevo estado:");
        System.out.println("1. PENDIENTE");
        System.out.println("2. CORRECTA");
        System.out.println("3. OBSERVADA");
        System.out.println("4. AJUSTADA");
        System.out.println("5. RECTIFICADA");
        int op = Menu.leerEntero();
        String[] estados = {"PENDIENTE", "CORRECTA", "OBSERVADA", "AJUSTADA", "RECTIFICADA"};
        if (op < 1 || op > 5) {
            Menu.mostrarError("Opción inválida.");
            return;
        }
        if (dao.actualizarEstado(idDj, estados[op - 1])) {
            Menu.mostrarExito("Estado actualizado correctamente.");
        } else {
            Menu.mostrarError("No se pudo actualizar el estado.");
        }
    }
}