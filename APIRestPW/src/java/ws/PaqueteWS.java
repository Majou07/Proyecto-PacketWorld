package ws;
import com.google.gson.Gson;
import dominio.PaqueteImp;
import dto.Respuesta;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import pojo.Paquete;

@Path("paquete")
public class PaqueteWS {
    @Path("obtener-por-envio/{idEnvio}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Paquete> obtenerPorEnvio(@PathParam("idEnvio") int idEnvio) {
        return PaqueteImp.obtenerPaquetesPorEnvio(idEnvio);
    }

    @Path("registrar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Respuesta registrar(String json) {
        return PaqueteImp.registrar(new Gson().fromJson(json, Paquete.class));
    }
    
    @Path("eliminar/{idPaquete}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta eliminarPaquete(@PathParam("idPaquete") Integer idPaquete) {
        if (idPaquete != null && idPaquete > 0) {
            return dominio.PaqueteImp.eliminar(idPaquete);
        }
        return new Respuesta(true, "ID de paquete no válido.");
    }
    
}