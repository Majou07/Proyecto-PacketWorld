package clienteescritorio.dominio;

import clienteescritorio.conexion.ConexionAPI;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Paquete;
import clienteescritorio.pojo.RespuestaHTTP;
import clienteescritorio.utilidad.Constantes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class PaqueteImp {
    
    public static HashMap<String, Object> obtenerPaquetesPorEnvio(int idEnvio) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        String URL = Constantes.URL_WS + "paquete/obtener-por-envio/" + idEnvio;
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            Type tipoLista = new TypeToken<List<Paquete>>(){}.getType();
            List<Paquete> paquetes = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put("paquetes", paquetes);
        } else {
            respuesta.put(Constantes.KEY_ERROR, true);
            respuesta.put(Constantes.KEY_MENSAJE, "Error al cargar paquetes");
        }
        return respuesta;
    }

    public static Respuesta registrar(Paquete paquete) {
        Respuesta respuesta = new Respuesta();
        String URL = Constantes.URL_WS + "paquete/registrar";
        Gson gson = new Gson();
        String json = gson.toJson(paquete);
        
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(URL, Constantes.METODO_POST, json, "application/json");
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("Error al registrar paquete");
        }
        return respuesta;
    }
    
    public static Respuesta editar(Paquete paquete) {
    Respuesta respuesta = new Respuesta();
    String url = Constantes.URL_WS + "paquete/editar";
    String json = new Gson().toJson(paquete);
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(url, "PUT", json, "application/json");
    
    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        respuesta = new Gson().fromJson(respuestaAPI.getContenido(), Respuesta.class);
    } else {
        respuesta.setError(true);
        respuesta.setMensaje("Error al intentar actualizar el paquete.");
    }
    return respuesta;
    }
    
    public static Respuesta eliminarPaquete(int idPaquete) {
    String URL = Constantes.URL_WS + "paquete/eliminar/" + idPaquete; 
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionSinBody(URL, "DELETE");
    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        return new Gson().fromJson(respuestaAPI.getContenido(), Respuesta.class);
    }
    return new Respuesta(true, "Error al eliminar el paquete.");
    }
    
}
