
package ws;

import dominio.DireccionImp;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pojo.Direccion;

@Path("direccion")
public class DireccionWS {
    @Path("consulta-cp/{cp}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Direccion> obtenerPorCP(@PathParam("cp") String cp) {
        return DireccionImp.obtenerPorCP(cp);
        
    }
}
