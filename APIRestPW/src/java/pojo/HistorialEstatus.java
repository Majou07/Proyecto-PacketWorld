package pojo;

import java.io.Serializable;

public class HistorialEstatus implements Serializable {

    private Integer idHistorial;
    private Integer idEnvio;
    private String estatus;
    private String fechaCambio;
    private String observaciones;

    public HistorialEstatus() {
    }

    public HistorialEstatus(Integer idHistorial, Integer idEnvio, String estatus, String fechaCambio, String observaciones) {
        this.idHistorial = idHistorial;
        this.idEnvio = idEnvio;
        this.estatus = estatus;
        this.fechaCambio = fechaCambio;
        this.observaciones = observaciones;
    }


    public Integer getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Integer getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(Integer idEnvio) {
        this.idEnvio = idEnvio;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(String fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}