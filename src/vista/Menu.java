package vista;

import java.util.Scanner;

public class Menu {

    private static Scanner scanner = new Scanner(System.in);

    public static int mostrarMenuPrincipal() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE GESTIÓN SINDICAL            ║");
        System.out.println("║   Afiliados Jubilados - Cuota Sindical   ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  1. Gestión de Afiliados                 ║");
        System.out.println("║  2. Declaraciones Juradas                ║");
        System.out.println("║  3. Obligaciones Mensuales               ║");
        System.out.println("║  4. Gestión de Pagos                     ║");
        System.out.println("║  5. Cuenta Corriente                     ║");
        System.out.println("║  0. Salir                                ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.print("  Seleccioná una opción: ");
        return leerEntero();
    }

    public static int mostrarMenuAfiliados() {
        System.out.println("\n--- GESTIÓN DE AFILIADOS ---");
        System.out.println("1. Registrar nuevo afiliado");
        System.out.println("2. Listar todos los afiliados");
        System.out.println("3. Buscar afiliado por DNI");
        System.out.println("4. Actualizar domicilio");
        System.out.println("5. Dar de baja afiliado");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccioná una opción: ");
        return leerEntero();
    }

    public static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    public static int leerEntero() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("⚠ Opción inválida. Ingresá un número.");
            return -1;
        }
    }

    public static void mostrarMensaje(String mensaje) {
        System.out.println("\n" + mensaje);
    }

    public static void mostrarError(String mensaje) {
        System.out.println("\n⚠ ERROR: " + mensaje);
    }

    public static void mostrarExito(String mensaje) {
        System.out.println("\n✔ " + mensaje);
    }
}