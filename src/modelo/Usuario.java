package modelo;

public abstract class Usuario {

    private int idUsuario;
    private String nombre;
    private String email;
    private String contrasena;
    private String estado;

    public Usuario(int idUsuario, String nombre, String email, 
                   String contrasena, String estado) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    // Método abstracto — cada rol lo implementa distinto (POLIMORFISMO)
    public abstract String getRol();

    @Override
    public String toString() {
        return "Usuario [" + getRol() + "] " + nombre + " - " + email;
    }
}