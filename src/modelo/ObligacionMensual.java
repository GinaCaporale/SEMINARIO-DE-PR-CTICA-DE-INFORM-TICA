package modelo;

public class ObligacionMensual {

    private int idObligacion;
    private int idDj;
    private int idAfiliado;
    private int periodoMes;
    private int periodoAnio;
    private double importeDeterminado;
    private double importeAjustado;
    private String estado;
    private String fechaGeneracion;
    private int idUsuarioRegistro;

    // Constructor completo
    public ObligacionMensual(int idObligacion, int idDj, int idAfiliado,
                              int periodoMes, int periodoAnio,
                              double importeDeterminado, double importeAjustado,
                              String estado, String fechaGeneracion,
                              int idUsuarioRegistro) {
        this.idObligacion = idObligacion;
        this.idDj = idDj;
        this.idAfiliado = idAfiliado;
        this.periodoMes = periodoMes;
        this.periodoAnio = periodoAnio;
        this.importeDeterminado = importeDeterminado;
        this.importeAjustado = importeAjustado;
        this.estado = estado;
        this.fechaGeneracion = fechaGeneracion;
        this.idUsuarioRegistro = idUsuarioRegistro;
    }

    // Constructor para nueva obligación
    public ObligacionMensual(int idDj, int idAfiliado, int periodoMes,
                              int periodoAnio, double importeDeterminado,
                              int idUsuarioRegistro) {
        this.idDj = idDj;
        this.idAfiliado = idAfiliado;
        this.periodoMes = periodoMes;
        this.periodoAnio = periodoAnio;
        this.importeDeterminado = importeDeterminado;
        this.importeAjustado = 0.00;
        this.estado = "PENDIENTE";
        this.idUsuarioRegistro = idUsuarioRegistro;
    }

    // Getters y Setters
    public int getIdObligacion() { return idObligacion; }
    public void setIdObligacion(int idObligacion) { this.idObligacion = idObligacion; }

    public int getIdDj() { return idDj; }
    public void setIdDj(int idDj) { this.idDj = idDj; }

    public int getIdAfiliado() { return idAfiliado; }
    public void setIdAfiliado(int idAfiliado) { this.idAfiliado = idAfiliado; }

    public int getPeriodoMes() { return periodoMes; }
    public void setPeriodoMes(int periodoMes) { this.periodoMes = periodoMes; }

    public int getPeriodoAnio() { return periodoAnio; }
    public void setPeriodoAnio(int periodoAnio) { this.periodoAnio = periodoAnio; }

    public double getImporteDeterminado() { return importeDeterminado; }
    public void setImporteDeterminado(double importeDeterminado) { this.importeDeterminado = importeDeterminado; }

    public double getImporteAjustado() { return importeAjustado; }
    public void setImporteAjustado(double importeAjustado) { this.importeAjustado = importeAjustado; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(String fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

    public int getIdUsuarioRegistro() { return idUsuarioRegistro; }
    public void setIdUsuarioRegistro(int idUsuarioRegistro) { this.idUsuarioRegistro = idUsuarioRegistro; }

    @Override
    public String toString() {
        return "[OBL-" + idObligacion + "] Afiliado: " + idAfiliado +
               " | Período: " + periodoMes + "/" + periodoAnio +
               " | Importe: $" + importeDeterminado +
               " | Ajuste: $" + importeAjustado +
               " | Estado: " + estado;
    }
}