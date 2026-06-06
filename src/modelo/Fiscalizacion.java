package modelo;

public class Fiscalizacion extends Usuario {

    public Fiscalizacion(int idUsuario, String nombre, String email,
                         String contrasena, String estado) {
        super(idUsuario, nombre, email, contrasena, estado);
    }

    @Override
    public String getRol() {
        return "FISCALIZACION";
    }
}