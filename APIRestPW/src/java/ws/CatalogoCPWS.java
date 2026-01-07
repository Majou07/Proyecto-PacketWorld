/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ws;

import dominio.CatalogoCPImp;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pojo.CatalogoCP;



@Path("catalogoCP")
public class CatalogoCPWS {
     @Path("obtener-datos")
    @GET 
    @Produces(MediaType.APPLICATION_JSON) 
    public List<CatalogoCP> buscarPorCodigoPostal(@QueryParam("codigo") String codigoPostal){ 
    return CatalogoCPImp.buscarPorCodigoPostal(codigoPostal); 
    }
}
