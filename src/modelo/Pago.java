package modelo;

public class Pago {

    private int idPago;
    private int idAfiliado;
    private int idObligacion;
    private double importe;
    private String fechaPago;
    private String medioPago;
    private String estado;
    private String motivoRechazo;
    private Integer idUsuarioValida;
    private String fechaValidacion;

    // Constructor completo
    public Pago(int idPago, int idAfiliado, int idObligacion, double importe,
                String fechaPago, String medioPago, String estado,
                String motivoRechazo, Integer idUsuarioValida,
                String fechaValidacion) {
        this.idPago = idPago;
        this.idAfiliado = idAfiliado;
        this.idObligacion = idObligacion;
        this.importe = importe;
        this.fechaPago = fechaPago;
        this.medioPago = medioPago;
        this.estado = estado;
        this.motivoRechazo = motivoRechazo;
        this.idUsuarioValida = idUsuarioValida;
        this.fechaValidacion = fechaValidacion;
    }

    // Constructor para nuevo pago
    public Pago(int idAfiliado, int idObligacion, double importe,
                String fechaPago, String medioPago) {
        this.idAfiliado = idAfiliado;
        this.idObligacion = idObligacion;
        this.importe = importe;
        this.fechaPago = fechaPago;
        this.medioPago = medioPago;
        this.estado = "PENDIENTE_VALIDACION";
    }

    // Getters y Setters
    public int getIdPago() { return idPago; }
    public void setIdPago(int idPago) { this.idPago = idPago; }

    public int getIdAfiliado() { return idAfiliado; }
    public void setIdAfiliado(int idAfiliado) { this.idAfiliado = idAfiliado; }

    public int getIdObligacion() { return idObligacion; }
    public void setIdObligacion(int idObligacion) { this.idObligacion = idObligacion; }

    public double getImporte() { return importe; }
    public void setImporte(double importe) { this.importe = importe; }

    public String getFechaPago() { return fechaPago; }
    public void setFechaPago(String fechaPago) { this.fechaPago = fechaPago; }

    public String getMedioPago() { return medioPago; }
    public void setMedioPago(String medioPago) { this.medioPago = medioPago; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMotivoRechazo() { return motivoRechazo; }
    public void setMotivoRechazo(String motivoRechazo) { this.motivoRechazo = motivoRechazo; }

    public Integer getIdUsuarioValida() { return idUsuarioValida; }
    public void setIdUsuarioValida(Integer idUsuarioValida) { this.idUsuarioValida = idUsuarioValida; }

    public String getFechaValidacion() { return fechaValidacion; }
    public void setFechaValidacion(String fechaValidacion) { this.fechaValidacion = fechaValidacion; }

    @Override
    public String toString() {
        return "[PAGO-" + idPago + "] Afiliado: " + idAfiliado +
               " | Obligación: " + idObligacion +
               " | Importe: $" + importe +
               " | Fecha: " + fechaPago +
               " | Medio: " + medioPago +
               " | Estado: " + estado;
    }
}