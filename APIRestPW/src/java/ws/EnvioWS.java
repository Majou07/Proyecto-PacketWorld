package ws;
import com.google.gson.Gson;
import dominio.EnvioImp;
import dominio.PaqueteImp;
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
import pojo.HistorialEstatus;
import pojo.Paquete;

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
    
    @Path("editar")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Respuesta editar(String json) {
        Envio envio = new Gson().fromJson(json, Envio.class);
        return EnvioImp.editarEnvio(envio); 
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

        EntityManager em = Persistence.createEntityManagerFactory("APIRestPWPU").createEntityManager();

        try {
            return em.createQuery(
                "SELECT e.numeroGuia, " + "CONCAT(e.destinoCalle, ' ', e.destinoNumero, ', ', e.destinoColonia), " +
                "es.nombre " + "FROM Envio e " +
                "JOIN EstatusEnvio es ON e.idEstatusEnvio = es.idEstatusEnvio " +
                "WHERE e.idConductorAsignado.idColaborador = :id"
            )
            .setParameter("id", idColaborador).getResultList();
        } finally {
            em.close();
        }
    }

    @Path("asignar-conductor")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Respuesta asignarConductor(@FormParam("idEnvio") int idEnvio, 
                @FormParam("idConductor") int idConductor) {
        return EnvioImp.asignarConductor(idEnvio, idConductor);
    }

    @Path("{guia}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response obtenerEnvioPorGuia(@PathParam("guia") String guia) {
        EntityManager em = Persistence.createEntityManagerFactory("APIRestPWPU").createEntityManager();
        try {
            Envio envio = em.createQuery("SELECT e FROM Envio e WHERE e.numeroGuia = :guia", Envio.class)
                            .setParameter("guia", guia)
                            .getSingleResult();

            return javax.ws.rs.core.Response.ok(envio)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE")
                    .header("Access-Control-Allow-Headers", "Content-Type")
                    .build();
        } catch (javax.persistence.NoResultException e) {
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } finally {
            em.close();
        }
    }
    
    @GET
    @Path("historial/{idEnvio}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<HistorialEstatus> obtenerHistorial(@PathParam("idEnvio") int idEnvio) {
        return EnvioImp.obtenerHistorial(idEnvio);
    }
    
    @GET 
    @Path("asignados/{idConductor}") 
    @Produces(MediaType.APPLICATION_JSON) 
    public List<Envio> obtenerPorConductor(@PathParam("idConductor") int idConductor) { 
        return EnvioImp.obtenerPorConductor(idConductor); 
    }
    
    @GET 
    @Path("detalle/{idEnvio}") 
    @Produces(MediaType.APPLICATION_JSON) 
    public Envio obtenerDetalle(@PathParam("idEnvio") int idEnvio) { 
        return EnvioImp.obtenerDetalle(idEnvio); 
    }
    
    @Path("eliminar/{idEnvio}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta eliminarEnvio(@PathParam("idEnvio") Integer idEnvio) {
        if (idEnvio != null && idEnvio > 0) {
            return EnvioImp.eliminar(idEnvio); 
        }
        return new Respuesta(true, "ID de envío no válido.");
    }
    
    @GET
    @Path("detalle-web/{numeroGuia}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerDetalleWeb(
        @PathParam("numeroGuia") String numeroGuia) {

    Envio envio = EnvioImp.obtenerDetallePorGuia(numeroGuia);

    if (envio == null) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new Respuesta(true, "No existe el envío"))
                .build();
    }

    List<Paquete> paquetes =
            PaqueteImp.obtenerPaquetesPorEnvio(envio.getIdEnvio());

    List<HistorialEstatus> historial =
            EnvioImp.obtenerHistorial(envio.getIdEnvio());

    envio.setPaquetes(paquetes);
    envio.setHistorial(historial);

    return Response.ok(envio).build();
}
    
    
    
}