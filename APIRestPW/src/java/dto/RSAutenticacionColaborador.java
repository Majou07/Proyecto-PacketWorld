/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import pojo.Colaborador;

/**
 *
 * @author Bruno
 */
public class RSAutenticacionColaborador {
    
    private boolean error;
    private String mensaje;
    private Colaborador colaborador;
    
    public boolean isError(){
        return error;
    }
    
    public void setError(boolean error){
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

   
    
    
    
}
