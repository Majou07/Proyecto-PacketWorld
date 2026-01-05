package ws;
import com.google.gson.Gson;
import dominio.EnvioImp;
import dto.Respuesta;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import pojo.Envio;

@Path("envio")
public class EnvioWS {

    @Path("obtener-todos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Envio> obtenerEnvios() {
        return EnvioImp.obtenerEnvios();
    }

    @Path("registrar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Respuesta registrar(String json) {
        Gson gson = new Gson();
        try {
            Envio envio = gson.fromJson(json, Envio.class);
            return EnvioImp.registrarEnvio(envio);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    
    @Path("actualizar-estatus")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Respuesta actualizarEstatus(@FormParam("idEnvio") int idEnvio, @FormParam("idEstatus") int idEstatus, @FormParam("comentario") String comentario, @FormParam("idColaborador") int idColaborador) {
        return EnvioImp.actualizarEstatus(idEnvio, idEstatus, comentario, idColaborador);
    }
    
    
    @Path("conductor/{idConductor}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Envio> obtenerEnviosPorConductor(
            @PathParam("idConductor") int idConductor) {

        EntityManager em = Persistence.createEntityManagerFactory("APIRestPWPU")
                                      .createEntityManager();

        try {
            return em.createQuery("SELECT e FROM Envio e WHERE e.conductor.idColaborador = :id", Envio.class).setParameter("id", idConductor)
                .getResultList();
        } finally {
            em.close();
        }
    }

}