package clienteescritorio.pojo;

public class Sucursal {
    private String codigoSucursal;
    private String nombreCorto;

    public Sucursal() {
    }

    public Sucursal(String codigoSucursal, String nombreCorto) {
        this.codigoSucursal = codigoSucursal;
        this.nombreCorto = nombreCorto;
    }
    

    public String getCodigoSucursal() { 
        return codigoSucursal; 
    }
    
    public void setCodigoSucursal(String codigoSucursal) { 
        this.codigoSucursal = codigoSucursal; 
    }

    public String getNombreCorto() { 
        return nombreCorto; 
    }
    
    public void setNombreCorto(String nombreCorto) { 
        this.nombreCorto = nombreCorto; 
    }

    @Override
    public String toString() {
        return nombreCorto; 
    }
}
