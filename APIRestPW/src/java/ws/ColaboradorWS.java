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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
  
    @Path("editar")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Respuesta editar(String json){
        Gson gson = new Gson();
        try{
            Colaborador colaborador = gson.fromJson(json, Colaborador.class);
            return ColaboradorImp.editarColaborador(colaborador);
        }catch (Exception e){
            throw new BadRequestException(e.getMessage());
            
        }
        
    }
    
    @Path("eliminar/{idColaborador}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta eliminar(@PathParam("idColaborador")int idColaborador){
       try{
           return ColaboradorImp.eliminarColaborador(idColaborador);
           
       }catch(Exception e){
           throw new BadRequestException(e.getMessage());
       } 
    }
    
   @Path("buscar/nombre/{filtro}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Colaborador> buscarPorNombre(@PathParam("filtro") String filtro) {
        if (filtro != null && !filtro.trim().isEmpty()) {
            return ColaboradorImp.obtenerColaboradoresPorNombre(filtro);
        }
        throw new BadRequestException("Filtro vacío");
    }

    @Path("buscar/numero/{numero}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Colaborador> buscarPorNumero(@PathParam("numero") String numero) {
        if (numero != null && !numero.trim().isEmpty()) {
            return ColaboradorImp.buscarPorNumeroPersonal(numero);
        }
        throw new BadRequestException("Número vacío");
    }

    @Path("buscar/rol/{idRol}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Colaborador> buscarPorRol(@PathParam("idRol") int idRol) {
        if (idRol > 0) {
            return ColaboradorImp.buscarPorRol(idRol);
        }
        throw new BadRequestException("Rol inválido");
    }
    
    @Path("subir-foto/{idColaborador}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta subirFoto(@PathParam("idColaborador")Integer idColaborador,byte[] foto){
        
        if(idColaborador !=null && idColaborador > 0 && foto.length > 0){
            return ColaboradorImp.guardarFoto(idColaborador, foto);
        }
        throw new BadRequestException();
    }
    
    @Path("obtener-foto/{idColaborador}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Colaborador obtenerFoto(@PathParam("idColaborador")Integer idColaborador){
        if(idColaborador != null && idColaborador>0){
        return ColaboradorImp.obtenerFoto(idColaborador);
        
    }
        throw new BadRequestException();
        
    }
}
    
    
    
    
   
