package ws;

import dominio.AutenticacionImp;
import dto.RSAutenticacionColaborador;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

 @Path ("autenticacion")
public class AutenticacionWS {
     @Path("administracion")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public RSAutenticacionColaborador autenticarColaborador(
            @FormParam("numeroPersonal")String numeroPersonal,
            @FormParam("contrasena")String contrasena){
        //TODO validación de campos y llamada a la capa de implementación
        if(numeroPersonal != null && !numeroPersonal.isEmpty()&& 
                (contrasena !=null && !contrasena.isEmpty())){
            RSAutenticacionColaborador respuesta = AutenticacionImp.autenticarAdministracion(numeroPersonal,contrasena);
            return AutenticacionImp.autenticarAdministracion(numeroPersonal,contrasena);
        }else{
            throw new BadRequestException();
        }
        //return null;
    }
     
   
    
    
}
