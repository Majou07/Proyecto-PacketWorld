package ws;
import com.google.gson.Gson;
import dominio.EnvioImp;
import dto.EnvioStatus;
import dto.Respuesta;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    
    
    @Path("conductor/{idColaborador}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Object[]> obtenerEnviosPorConductor(
            @PathParam("idColaborador") int idColaborador) {

        EntityManager em = Persistence
                .createEntityManagerFactory("APIRestPWPU")
                .createEntityManager();

        try {
            return em.createQuery(
                "SELECT e.numeroGuia, " +
                "CONCAT(e.destinoCalle, ' ', e.destinoNumero, ', ', e.destinoColonia), " +
                "es.nombre " +
                "FROM Envio e " +
                "JOIN EstatusEnvio es ON e.idEstatusEnvio = es.idEstatusEnvio " +
                "WHERE e.idConductorAsignado.idColaborador = :id"
            )
            .setParameter("id", idColaborador)
            .getResultList();
        } finally {
            em.close();
        }
    }

    
    
    @Path("{guia}/estatus")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizarEstatusEnvio(@PathParam("guia") String guia, EnvioStatus dto) {

        EntityManager em = Persistence.createEntityManagerFactory("APIRestPWPU").createEntityManager();

        try { 
            em.getTransaction().begin();

            Envio envio = em.createQuery("SELECT e FROM Envio e WHERE e.numeroGuia = :guia", Envio.class) .setParameter("guia", guia)
                .getSingleResult();

            envio.setEstatus(dto.getEstatus());
            envio.setFechaEstatus(new Date());


            em.persist(h);

            em.merge(envio);
            em.getTransaction().commit();

            return Response.ok().build();

        } catch (Exception e) {
            em.getTransaction().rollback();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            em.close();
        }
    }


}