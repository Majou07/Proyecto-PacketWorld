package clienteescritorio.pojo;

public class Colaborador {
    private int idColaborador;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String numeroPersonal;
    private String rol;
    private String sucursal;
    private String correo;
    
    public Colaborador() {
    }

    public Colaborador(int idColaborador, String nombre, String apellidoPaterno, String apellidoMaterno, 
            String numeroPersonal, String rol, String sucursal, String correo) {
        this.idColaborador = idColaborador;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.numeroPersonal = numeroPersonal;
        this.rol = rol;
        this.sucursal = sucursal;
        this.correo = correo;
    }
    

    public int getIdColaborador() { 
        return idColaborador; 
    }
    
    public void setIdColaborador(int idColaborador) { 
        this.idColaborador = idColaborador; 
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
    
    public String getNumeroPersonal() { 
        return numeroPersonal; 
    }
    
    public void setNumeroPersonal(String numeroPersonal) { 
        this.numeroPersonal = numeroPersonal; 
    }
    
    public String getRol() { 
        return rol; 
    }
    
    public void setRol(String rol) { 
        this.rol = rol; 
    }
    
    public String getSucursal() { 
        return sucursal; 
    }
    
    public void setSucursal(String sucursal) { 
        this.sucursal = sucursal; 
    }
    
    public String getCorreo() { 
        return correo; 
    }
    
    public void setCorreo(String correo) { 
        this.correo = correo; 
    }
    
    @Override
    public String toString() { 
        return nombre + " " + apellidoPaterno; 
    }
}
