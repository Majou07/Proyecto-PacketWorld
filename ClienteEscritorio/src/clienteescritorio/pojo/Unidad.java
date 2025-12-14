package clienteescritorio.pojo;

public class Unidad {
    private int idUnidad;
    private String marca;
    private String modelo;
    private int anio;
    private String vin;
    private String nii;
    private String tipo;

    public Unidad() {
    }

    public Unidad(int idUnidad, String marca, String modelo, int anio, String vin, String nii, String tipo) {
        this.idUnidad = idUnidad;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.vin = vin;
        this.nii = nii;
        this.tipo = tipo;
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
    
    public String getTipo() { 
        return tipo; 
    }
    
    public void setTipo(String tipo) { 
        this.tipo = tipo; 
    }
}
