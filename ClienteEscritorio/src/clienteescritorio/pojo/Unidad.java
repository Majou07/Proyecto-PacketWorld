package clienteescritorio.pojo;

public class Unidad {
    private int idUnidad;
    private String marca;
    private String modelo;
    private int anio;
    private String vin;
    private String nii;
    private String tipoUnidad;
    private String motivoBaja;
    private String estatusUnidad;
    private int idTipoUnidad;
    private int idEstatusUnidad;


    public Unidad() {
    }

    public Unidad(int idUnidad, String marca, String modelo, int anio, String vin, String nii, String tipo) {
        this.idUnidad = idUnidad;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.vin = vin;
        this.nii = nii;
        this.tipoUnidad = tipo;
    }
    
    

    public int getIdUnidad() { 
        return idUnidad; 
    }
    
    public void setIdUnidad(int idUnidad) { 
        this.idUnidad = idUnidad; 
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
    
    public String getTipoUnidad() { 
        return tipoUnidad; 
    }
    
    public void setTipoUnidad(String tipoUnidad) { 
        this.tipoUnidad = tipoUnidad; 
    }

    public String getMotivoBaja() {
        return motivoBaja;
    }

    public void setMotivoBaja(String motivoBaja) {
        this.motivoBaja = motivoBaja;
    }

    public String getEstatusUnidad() {
        return estatusUnidad;
    }

    public void setEstatusUnidad(String estatusUnidad) {
        this.estatusUnidad = estatusUnidad;
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
    
    
}
