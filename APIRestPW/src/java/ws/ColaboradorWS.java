/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ws;

import com.google.gson.Gson;
import dominio.ColaboradorImp;
import dto.Respuesta;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import static javax.ws.rs.client.Entity.json;
import javax.ws.rs.core.MediaType;
import pojo.Colaborador;

/**
 *
 * @author Bruno
 */

@Path("colaborador")
public class ColaboradorWS {
    
    @Path("obtener-todos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List <Colaborador> obtenerColaboradores(){
       return ColaboradorImp.obtenerColaboradores(); 
    }
    
    @Path("registrar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Respuesta registrar(String json){
        Gson gson = new Gson();
        
    try{
    Colaborador colaborador = gson.fromJson(json,Colaborador.class);
    return ColaboradorImp.registrarColaborador(colaborador);
}catch (Exception e){
    throw new BadRequestException(e.getMessage());
}
    }
    
    
    
    
}
