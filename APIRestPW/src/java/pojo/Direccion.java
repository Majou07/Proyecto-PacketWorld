package pojo;


public class Direccion {
    private String colonia;
    private String ciudad;
    private String estado;
    private String municipio;
    private String codigoPostal;

    public Direccion() {
    }

    public Direccion(String colonia, String ciudad, String estado, String municipio, String codigoPostal) {
        this.colonia = colonia;
        this.ciudad = ciudad;
        this.estado = estado;
        this.municipio = municipio;
        this.codigoPostal = codigoPostal;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
    
    
}
