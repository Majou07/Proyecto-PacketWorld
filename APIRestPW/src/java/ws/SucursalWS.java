/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ws;

import com.google.gson.Gson;
import dominio.SucursalImp;
import dto.Respuesta;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pojo.Sucursal;

/**
 *
 * @author Bruno
 */
@Path("sucursal")
public class SucursalWS {
    
     @Path("obtener-todas")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List <Sucursal> obtenerSucursales(){
       return SucursalImp.obtenerSucursales(); 
    }
    
    
    @Path("registrar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
  public Respuesta registrar(String json){
        Gson gson = new Gson();
        
    try{
        Sucursal sucursal = gson.fromJson(json,Sucursal.class);
        return SucursalImp.registrarSucursal(sucursal);
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
            Sucursal sucursal = gson.fromJson(json,Sucursal.class);
            return SucursalImp.editarSucursal(sucursal);
        }catch (Exception e){
            throw new BadRequestException(e.getMessage());
            
        }
        
    }
    
    @Path("dar-baja/{codigoSucursal}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta darBajaUnidad(
        @PathParam("codigoSucursal") String codigoSucursal ) {

    try {
        return SucursalImp.darBajaSucursal(codigoSucursal);
    } catch (Exception e) {
        throw new BadRequestException(e.getMessage());
    }
}
  
  
}
