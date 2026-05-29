package ws;
import com.google.gson.Gson;
import dominio.ClienteImp;
import dto.Respuesta;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import pojo.Cliente;

@Path("cliente")
public class ClienteWS {
    @Path("obtener-todos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cliente> obtenerTodos() { return ClienteImp.obtenerClientes(); }

    @Path("registrar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Respuesta registrar(String json) {
        return ClienteImp.registrar(new Gson().fromJson(json, Cliente.class));
    }
    
    @PUT
    @Path("editar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Respuesta editar(String json) {
        return ClienteImp.editar(new Gson().fromJson(json, Cliente.class));
    }

    @DELETE
    @Path("eliminar/{idCliente}")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta eliminar(@PathParam("idCliente") int idCliente) {
        return ClienteImp.eliminar(idCliente);
    }
    
    @Path("buscar/nombre/{valor}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cliente> buscarPorNombre(@PathParam("valor") String valor) {
        return ClienteImp.buscarPorNombre(valor);
    }

    // =========================
    // BUSCAR POR TELEFONO
    // =========================
    @Path("buscar/telefono/{valor}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cliente> buscarPorTelefono(@PathParam("valor") String valor) {
        return ClienteImp.buscarPorTelefono(valor);
    }

    // =========================
    // BUSCAR POR CORREO
    // =========================
    @Path("buscar/correo/{valor}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cliente> buscarPorCorreo(@PathParam("valor") String valor) {
        return ClienteImp.buscarPorCorreo(valor);
    }
  
}