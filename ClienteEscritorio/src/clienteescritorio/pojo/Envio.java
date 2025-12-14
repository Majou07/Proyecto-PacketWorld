package clienteescritorio.pojo;

public class Envio {
    private int idEnvio;
    private String numeroGuia;
    private String nombreCliente;
    private String sucursalOrigen;
    private String estatusEnvio;
    private String nombreConductor;

    public Envio() {
    }

    public Envio(int idEnvio, String numeroGuia, String nombreCliente, String sucursalOrigen, String estatusEnvio, String nombreConductor) {
        this.idEnvio = idEnvio;
        this.numeroGuia = numeroGuia;
        this.nombreCliente = nombreCliente;
        this.sucursalOrigen = sucursalOrigen;
        this.estatusEnvio = estatusEnvio;
        this.nombreConductor = nombreConductor;
    }
    
    
    public int getIdEnvio() { 
        return idEnvio; 
    }
    
    public void setIdEnvio(int idEnvio) { 
        this.idEnvio = idEnvio; 
    }
    
    public String getNumeroGuia() { 
        return numeroGuia; 
    }
    
    public void setNumeroGuia(String numeroGuia) { 
        this.numeroGuia = numeroGuia; 
    }
    
    public String getNombreCliente() { 
        return nombreCliente; 
    }
    
    public void setNombreCliente(String nombreCliente) { 
        this.nombreCliente = nombreCliente; 
    }
    
    public String getSucursalOrigen() { 
        return sucursalOrigen; 
    }
    
    public void setSucursalOrigen(String sucursalOrigen) { 
        this.sucursalOrigen = sucursalOrigen; 
    }
    
    public String getEstatusEnvio() { 
        return estatusEnvio; 
    }
    
    public void setEstatusEnvio(String estatusEnvio) { 
        this.estatusEnvio = estatusEnvio; 
    }
    
    public String getNombreConductor() { 
        return nombreConductor; 
    }
    
    public void setNombreConductor(String nombreConductor) { 
        this.nombreConductor = nombreConductor; 
    }
}

