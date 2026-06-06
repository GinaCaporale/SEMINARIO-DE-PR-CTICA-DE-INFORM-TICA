package modelo;

public class DetalleCuentaCorriente {

    private long idDetalle;
    private int idCuenta;
    private String tipoMovimiento;
    private String concepto;
    private double importe;
    private String fechaMovimiento;
    private int idReferencia;

    // Constructor completo
    public DetalleCuentaCorriente(long idDetalle, int idCuenta, String tipoMovimiento,
                                   String concepto, double importe,
                                   String fechaMovimiento, int idReferencia) {
        this.idDetalle = idDetalle;
        this.idCuenta = idCuenta;
        this.tipoMovimiento = tipoMovimiento;
        this.concepto = concepto;
        this.importe = importe;
        this.fechaMovimiento = fechaMovimiento;
        this.idReferencia = idReferencia;
    }

    // Constructor para nuevo movimiento
    public DetalleCuentaCorriente(int idCuenta, String tipoMovimiento,
                                   String concepto, double importe, int idReferencia) {
        this.idCuenta = idCuenta;
        this.tipoMovimiento = tipoMovimiento;
        this.concepto = concepto;
        this.importe = importe;
        this.idReferencia = idReferencia;
    }

    // Getters y Setters
    public long getIdDetalle() { return idDetalle; }
    public void setIdDetalle(long idDetalle) { this.idDetalle = idDetalle; }

    public int getIdCuenta() { return idCuenta; }
    public void setIdCuenta(int idCuenta) { this.idCuenta = idCuenta; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }

    public double getImporte() { return importe; }
    public void setImporte(double importe) { this.importe = importe; }

    public String getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(String fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }

    public int getIdReferencia() { return idReferencia; }
    public void setIdReferencia(int idReferencia) { this.idReferencia = idReferencia; }

    @Override
    public String toString() {
        return "[" + tipoMovimiento + "] " + concepto +
               " | Importe: $" + importe +
               " | Fecha: " + fechaMovimiento;
    }
}
