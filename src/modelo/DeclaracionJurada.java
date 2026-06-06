package modelo;

public class DeclaracionJurada {

    private int idDj;
    private int idAfiliado;
    private Integer idDjOriginal;
    private String tipo;
    private int periodoMes;
    private int periodoAnio;
    private double baseImponible;
    private String estado;
    private String fechaRegistro;
    private int idUsuarioRegistro;

    // Constructor completo
    public DeclaracionJurada(int idDj, int idAfiliado, Integer idDjOriginal,
                              String tipo, int periodoMes, int periodoAnio,
                              double baseImponible, String estado,
                              String fechaRegistro, int idUsuarioRegistro) {
        this.idDj = idDj;
        this.idAfiliado = idAfiliado;
        this.idDjOriginal = idDjOriginal;
        this.tipo = tipo;
        this.periodoMes = periodoMes;
        this.periodoAnio = periodoAnio;
        this.baseImponible = baseImponible;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
        this.idUsuarioRegistro = idUsuarioRegistro;
    }

    // Constructor para nueva DJ
    public DeclaracionJurada(int idAfiliado, Integer idDjOriginal, String tipo,
                              int periodoMes, int periodoAnio,
                              double baseImponible, int idUsuarioRegistro) {
        this.idAfiliado = idAfiliado;
        this.idDjOriginal = idDjOriginal;
        this.tipo = tipo;
        this.periodoMes = periodoMes;
        this.periodoAnio = periodoAnio;
        this.baseImponible = baseImponible;
        this.estado = "PENDIENTE";
        this.idUsuarioRegistro = idUsuarioRegistro;
    }

    // Getters y Setters
    public int getIdDj() { return idDj; }
    public void setIdDj(int idDj) { this.idDj = idDj; }

    public int getIdAfiliado() { return idAfiliado; }
    public void setIdAfiliado(int idAfiliado) { this.idAfiliado = idAfiliado; }

    public Integer getIdDjOriginal() { return idDjOriginal; }
    public void setIdDjOriginal(Integer idDjOriginal) { this.idDjOriginal = idDjOriginal; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getPeriodoMes() { return periodoMes; }
    public void setPeriodoMes(int periodoMes) { this.periodoMes = periodoMes; }

    public int getPeriodoAnio() { return periodoAnio; }
    public void setPeriodoAnio(int periodoAnio) { this.periodoAnio = periodoAnio; }

    public double getBaseImponible() { return baseImponible; }
    public void setBaseImponible(double baseImponible) { this.baseImponible = baseImponible; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public int getIdUsuarioRegistro() { return idUsuarioRegistro; }
    public void setIdUsuarioRegistro(int idUsuarioRegistro) { this.idUsuarioRegistro = idUsuarioRegistro; }

    @Override
    public String toString() {
        return "[DJ-" + idDj + "] Afiliado: " + idAfiliado +
               " | Período: " + periodoMes + "/" + periodoAnio +
               " | Tipo: " + tipo +
               " | Base: $" + baseImponible +
               " | Estado: " + estado;
    }
}