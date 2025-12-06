/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojo;

/**
 *
 * @author Bruno
 */
public class Colaborador {
    // Campos de la tabla 'colaborador'
    private int idColaborador;
    private String numeroPersonal;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String curp;
    private String correoElectronico;
    private String contrasena; 
    private byte[] foto;
    private String fotografia;
    private String numeroLicencia; // Solo para conductores
    private int idRol;
    private String rol;
    
   
    private String codigoSucursal;
    private Integer idUnidadAsignada; // Puede ser null
    
    
    

    public Colaborador(int idColaborador, String numeroPersonal, String nombre, String apellidoPaterno, String apellidoMaterno, String curp, String correoElectronico, String contrasena,  String numeroLicencia, int idRol, String rol) {
        this.idColaborador = idColaborador;
        this.numeroPersonal = numeroPersonal;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.curp = curp;
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
        this.numeroLicencia = numeroLicencia;
        this.idRol = idRol;
        this.rol = rol;
    }

    public Colaborador() {
    }

    public int getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(int idColaborador) {
        this.idColaborador = idColaborador;
    }

    public String getNumeroPersonal() {
        return numeroPersonal;
    }

    public void setNumeroPersonal(String numeroPersonal) {
        this.numeroPersonal = numeroPersonal;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
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

    public Integer getIdUnidadAsignada() {
        return idUnidadAsignada;
    }

    public void setIdUnidadAsignada(Integer idUnidadAsignada) {
        this.idUnidadAsignada = idUnidadAsignada;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
    
    
    
    
    
}
