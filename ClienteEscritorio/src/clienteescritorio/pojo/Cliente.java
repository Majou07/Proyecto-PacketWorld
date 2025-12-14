package clienteescritorio.pojo;


public class Cliente {
    private Integer idCliente;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String calle;
    private String numero;
    private String colonia;
    private String codigoPostal;
    private String telefono;
    private String correoElectronico;

    public Cliente() {
    
    }

    public Cliente(Integer idCliente, String nombre, String apellidoPaterno, String apellidoMaterno, String calle, String numero, String colonia, String codigoPostal, String telefono, String correoElectronico) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.calle = calle;
        this.numero = numero;
        this.colonia = colonia;
        this.codigoPostal = codigoPostal;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
    }
    

    public Integer getIdCliente() { 
        return idCliente; 
    }
    
    public void setIdCliente(Integer idCliente) { 
        this.idCliente = idCliente; 
    }
    
    public String getNombre() { 
        return nombre; 
    }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    
    public String getApellidoPaterno() { 
        return apellidoPaterno; 
    }
    
    public void setApellidoPaterno(String apellidoPaterno) { 
        this.apellidoPaterno = apellidoPaterno; 
    }
    
    public String getApellidoMaterno() { 
        return apellidoMaterno; 
    }
    
    public void setApellidoMaterno(String apellidoMaterno) { 
        this.apellidoMaterno = apellidoMaterno; 
    }
    
    public String getCalle() { 
        return calle; 
    }
    
    public void setCalle(String calle) { 
        this.calle = calle; 
    }
    
    public String getNumero() {
        return numero; 
    }
    
    public void setNumero(String numero) { 
        this.numero = numero; 
    }
    
    public String getColonia() { 
        return colonia; 
    }
    
    public void setColonia(String colonia) { 
        this.colonia = colonia; 
    }
    
    public String getCodigoPostal() { 
        return codigoPostal; 
    }
    
    public void setCodigoPostal(String codigoPostal) { 
        this.codigoPostal = codigoPostal; 
    }
    
    public String getTelefono() { 
        return telefono; 
    }
    
    public void setTelefono(String telefono) { 
        this.telefono = telefono; 
    }
    
    public String getCorreoElectronico() { 
        return correoElectronico; 
    }
    
    public void setCorreoElectronico(String correoElectronico) { 
        this.correoElectronico = correoElectronico; 
    }
}