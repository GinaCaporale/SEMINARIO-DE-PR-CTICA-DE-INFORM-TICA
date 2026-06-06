package modelo;

public class Administrativo extends Usuario {

    public Administrativo(int idUsuario, String nombre, String email,
                          String contrasena, String estado) {
        super(idUsuario, nombre, email, contrasena, estado);
    }

    @Override
    public String getRol() {
        return "ADMINISTRATIVO";
    }
}