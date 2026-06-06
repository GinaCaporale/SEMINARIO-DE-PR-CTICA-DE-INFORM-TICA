package modelo;

public class AfiliadoJubilado {

    private int idAfiliado;
    private String nroAfiliado;
    private String dni;
    private String apellido;
    private String nombre;
    private String email;
    private String telefono;
    private String domicilio;
    private String estado;
    private String fechaAlta;

    // Constructor completo
    public AfiliadoJubilado(int idAfiliado, String nroAfiliado, String dni,
                             String apellido, String nombre, String email,
                             String telefono, String domicilio, String estado,
                             String fechaAlta) {
        this.idAfiliado = idAfiliado;
        this.nroAfiliado = nroAfiliado;
        this.dni = dni;
        this.apellido = apellido;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.domicilio = domicilio;
        this.estado = estado;
        this.fechaAlta = fechaAlta;
    }

    // Constructor sin id (para altas nuevas)
    public AfiliadoJubilado(String nroAfiliado, String dni, String apellido,
                             String nombre, String email, String telefono,
                             String domicilio, String fechaAlta) {
        this.nroAfiliado = nroAfiliado;
        this.dni = dni;
        this.apellido = apellido;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.domicilio = domicilio;
        this.estado = "ACTIVO";
        this.fechaAlta = fechaAlta;
    }

    // Getters y Setters
    public int getIdAfiliado() { return idAfiliado; }
    public void setIdAfiliado(int idAfiliado) { this.idAfiliado = idAfiliado; }

    public String getNroAfiliado() { return nroAfiliado; }
    public void setNroAfiliado(String nroAfiliado) { this.nroAfiliado = nroAfiliado; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDomicilio() { return domicilio; }
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(String fechaAlta) { this.fechaAlta = fechaAlta; }

    @Override
    public String toString() {
        return "[" + nroAfiliado + "] " + apellido + ", " + nombre +
               " - DNI: " + dni + " - Estado: " + estado;
    }
}