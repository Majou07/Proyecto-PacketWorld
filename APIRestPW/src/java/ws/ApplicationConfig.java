package ws;

import java.util.Set;
import javax.ws.rs.core.Application;


@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(ws.AutenticacionWS.class);
        resources.add(ws.ColaboradorWS.class);
        resources.add(ws.EnvioWS.class);
        resources.add(ws.SucursalWS.class);
        resources.add(ws.UnidadWS.class);
        
    }
    
}
