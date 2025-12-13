/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojo;

/**
 *
 * @author Bruno
 */
public class Unidad {
    
     private int idUnidad;
    private String vin;
    private String nii;
    private String marca;
    private String modelo;
    private int anio;
    private String motivoBaja;

    private int idTipoUnidad;
    private int idEstatusUnidad;

    // Campos opcionales para JOIN (lectura)
    private String tipoUnidad;
    private String estatusUnidad;

    public Unidad() {
    }

    public Unidad(int idUnidad, String vin, String nii, String marca, String modelo, int anio, String motivoBaja, int idTipoUnidad, int idEstatusUnidad, String tipoUnidad, String estatusUnidad) {
        this.idUnidad = idUnidad;
        this.vin = vin;
        this.nii = nii;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.motivoBaja = motivoBaja;
        this.idTipoUnidad = idTipoUnidad;
        this.idEstatusUnidad = idEstatusUnidad;
        this.tipoUnidad = tipoUnidad;
        this.estatusUnidad = estatusUnidad;
    }

    public int getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(int idUnidad) {
        this.idUnidad = idUnidad;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getNii() {
        return nii;
    }

    public void setNii(String nii) {
        this.nii = nii;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getMotivoBaja() {
        return motivoBaja;
    }

    public void setMotivoBaja(String motivoBaja) {
        this.motivoBaja = motivoBaja;
    }

    public int getIdTipoUnidad() {
        return idTipoUnidad;
    }

    public void setIdTipoUnidad(int idTipoUnidad) {
        this.idTipoUnidad = idTipoUnidad;
    }

    public int getIdEstatusUnidad() {
        return idEstatusUnidad;
    }

    public void setIdEstatusUnidad(int idEstatusUnidad) {
        this.idEstatusUnidad = idEstatusUnidad;
    }

    public String getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(String tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }

    public String getEstatusUnidad() {
        return estatusUnidad;
    }

    public void setEstatusUnidad(String estatusUnidad) {
        this.estatusUnidad = estatusUnidad;
    }
    
    
    
    
}
