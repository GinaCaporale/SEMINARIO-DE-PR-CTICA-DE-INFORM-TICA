package modelo;

public class Hacienda extends Usuario {

    public Hacienda(int idUsuario, String nombre, String email,
                    String contrasena, String estado) {
        super(idUsuario, nombre, email, contrasena, estado);
    }

    @Override
    public String getRol() {
        return "HACIENDA";
    }
}