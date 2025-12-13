/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ws;

import com.google.gson.Gson;
import dominio.UnidadImp;
import dto.BajaUnidadDTO;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pojo.Unidad;

/**
 *
 * @author Bruno
 */
@Path("unidad")
public class UnidadWS {
    @Path("obtener-todas")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List <Unidad> obtenerUnidades(){
       return UnidadImp.obtenerUnidades(); 
    }
    
    @Path("registrar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
  public Respuesta registrar(String json){
        Gson gson = new Gson();
        
    try{
        Unidad unidad = gson.fromJson(json,Unidad.class);
        return UnidadImp.registrarUnidad(unidad);
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
            Unidad unidad = gson.fromJson(json,Unidad.class);
            return UnidadImp.editarUnidad(unidad);
        }catch (Exception e){
            throw new BadRequestException(e.getMessage());
            
        }
        
    }
    
    @Path("dar-baja/{idUnidad}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta darBajaUnidad(
        @PathParam("idUnidad") int idUnidad,
        BajaUnidadDTO dto) {

    try {
        return UnidadImp.darBajaUnidad(idUnidad, dto.getMotivoBaja());
    } catch (Exception e) {
        throw new BadRequestException(e.getMessage());
    }
}
    
@GET
@Path("buscar")
@Produces(MediaType.APPLICATION_JSON)
public List<Unidad> buscarUnidad(
        @QueryParam("vin") String vin,
        @QueryParam("marca") String marca,
        @QueryParam("nii") String nii) {
    return UnidadImp.buscarUnidad(vin, marca, nii);
}
    
    
    
}
