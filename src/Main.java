import controlador.AfiliadoControlador;
import controlador.CuentaCorrienteControlador;
import controlador.DeclaracionJuradaControlador;
import controlador.ObligacionMensualControlador;
import controlador.PagoControlador;
import dao.UsuarioDAO;
import modelo.Usuario;
import vista.Menu;

public class Main {

    public static void main(String[] args) {

        // LOGIN
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE GESTIÓN SINDICAL            ║");
        System.out.println("║   Iniciar Sesión                         ║");
        System.out.println("╚══════════════════════════════════════════╝");

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuarioActivo = null;
        int intentos = 0;

        while (usuarioActivo == null && intentos < 3) {
            String email = Menu.leerTexto("Email: ");
            String contrasena = Menu.leerTexto("Contraseña: ");
            usuarioActivo = usuarioDAO.autenticar(email, contrasena);
            if (usuarioActivo == null) {
                intentos++;
                Menu.mostrarError("Credenciales incorrectas. Intentos restantes: " + (3 - intentos));
            }
        }

        if (usuarioActivo == null) {
            Menu.mostrarError("Acceso bloqueado. Demasiados intentos fallidos.");
            return;
        }

        Menu.mostrarExito("Bienvenido/a, " + usuarioActivo.getNombre() +
                          " [" + usuarioActivo.getRol() + "]");

        // CONTROLADORES
        AfiliadoControlador afiliadoControlador =
                new AfiliadoControlador();
        DeclaracionJuradaControlador djControlador =
                new DeclaracionJuradaControlador(usuarioActivo);
        ObligacionMensualControlador omControlador =
                new ObligacionMensualControlador(usuarioActivo);
        PagoControlador pagoControlador =
                new PagoControlador(usuarioActivo);
        CuentaCorrienteControlador ccControlador =
                new CuentaCorrienteControlador(usuarioActivo);

        int opcion;
        do {
            opcion = Menu.mostrarMenuPrincipal();
            switch (opcion) {
                case 1 -> afiliadoControlador.gestionar();
                case 2 -> djControlador.gestionar();
                case 3 -> omControlador.gestionar();
                case 4 -> pagoControlador.gestionar();
                case 5 -> ccControlador.gestionar();
                case 0 -> Menu.mostrarMensaje("Cerrando sesión. ¡Hasta luego!");
                default -> Menu.mostrarError("Opción no válida.");
            }
        } while (opcion != 0);
    }
}