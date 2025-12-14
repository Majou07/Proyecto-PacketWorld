package clienteescritorio.dominio;
import clienteescritorio.conexion.ConexionAPI;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Cliente;
import clienteescritorio.pojo.RespuestaHTTP;
import clienteescritorio.utilidad.Constantes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ClienteImp {
    public static HashMap<String, Object> obtenerClientes() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        String URL = Constantes.URL_WS + "cliente/obtener-todos";
        RespuestaHTTP respAPI = ConexionAPI.peticionGET(URL);
        if (respAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            List<Cliente> lista = new Gson().fromJson(respAPI.getContenido(), new TypeToken<List<Cliente>>(){}.getType());
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put("clientes", lista);
        } else {
            respuesta.put(Constantes.KEY_ERROR, true);
            respuesta.put(Constantes.KEY_MENSAJE, "Error al cargar clientes");
        }
        return respuesta;
    }
    // Implementar método registrar similar a ColaboradorImp...
}