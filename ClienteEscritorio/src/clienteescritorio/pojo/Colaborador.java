package clienteescritorio.pojo;

public class Colaborador {
    private int idColaborador;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String curp;
    private String correoElectronico; 
    private String numeroPersonal;
    private String contrasena;
    private int idRol;
    private String rol;
    private String codigoSucursal;
    private String sucursal;
    private byte[] foto;
    private String fotografia;
      private String numeroLicencia; 
    private Integer idUnidadAsignada; 

    public Colaborador() {
    }

    public Colaborador(int idColaborador, String nombre, String apellidoPaterno, String apellidoMaterno, String curp, String correoElectronico, String numeroPersonal, String contrasena, int idRol, String rol, String codigoSucursal, String sucursal, byte[] foto, String fotografia) {
        this.idColaborador = idColaborador;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.curp = curp;
        this.correoElectronico = correoElectronico;
        this.numeroPersonal = numeroPersonal;
        this.contrasena = contrasena;
        this.idRol = idRol;
        this.rol = rol;
        this.codigoSucursal = codigoSucursal;
        this.sucursal = sucursal;
        this.foto = foto;
        this.fotografia = fotografia;
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

    public String getCurp() { 
        return curp; 
    }
    
    public void setCurp(String curp) { 
        this.curp = curp; 
    }

    public String getCorreoElectronico() { 
        return correoElectronico; 
    }
    
    public void setCorreoElectronico(String correoElectronico) { 
        this.correoElectronico = correoElectronico; 
    }

    public String getNumeroPersonal() { 
        return numeroPersonal; 
    }
    
    public void setNumeroPersonal(String numeroPersonal) { 
        this.numeroPersonal = numeroPersonal; 
    }

    public String getContrasena() { 
        return contrasena; 
    }
    
    public void setContrasena(String contrasena) { 
        this.contrasena = contrasena; 
    }

    public int getIdRol() { 
        return idRol; 
    }
    
    public void setIdRol(int idRol) { 
        this.idRol = idRol; 
    }

    public String getRol() { 
        return rol; 
    }
    
    public void setRol(String rol) { 
        this.rol = rol; 
    }

    public String getCodigoSucursal() { 
        return codigoSucursal; 
    }
    
    public void setCodigoSucursal(String codigoSucursal) { 
        this.codigoSucursal = codigoSucursal; 
    }

    public String getSucursal() { 
        return sucursal; 
    }
    
    public void setSucursal(String sucursal) { 
        this.sucursal = sucursal; 
    }

    public byte[] getFoto() { 
        return foto; 
    }
    
    public void setFoto(byte[] foto) { 
        this.foto = foto; 
    }

    public String getFotografia() { 
        return fotografia; 
    }
    
    public void setFotografia(String fotografia) { 
        this.fotografia = fotografia; 
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }
    

    public Integer getIdUnidadAsignada() {
        return idUnidadAsignada;
    }

    public void setIdUnidadAsignada(Integer idUnidadAsignada) {
        this.idUnidadAsignada = idUnidadAsignada;
    }
    
    
    
    @Override
    public String toString() {
        return this.nombre + " " + this.apellidoPaterno + " (" + this.numeroPersonal + ")";
    }
}

