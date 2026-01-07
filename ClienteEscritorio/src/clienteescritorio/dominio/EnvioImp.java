package clienteescritorio.dominio;

import clienteescritorio.conexion.ConexionAPI;
import clienteescritorio.pojo.Envio;
import clienteescritorio.pojo.RespuestaHTTP;
import clienteescritorio.utilidad.Constantes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import clienteescritorio.dto.Respuesta; 

public class EnvioImp {
    public static HashMap<String, Object> obtenerEnvios() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        String URL = Constantes.URL_WS + "envio/obtener-todos";
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            Type tipoLista = new TypeToken<List<Envio>>(){}.getType();
            List<Envio> envios = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put("envios", envios);
            
        } else {
            
            respuesta.put(Constantes.KEY_ERROR, true);
            respuesta.put(Constantes.KEY_MENSAJE, "Error al cargar envíos");
        }
        return respuesta;
    }

    public static Respuesta actualizarEstatus(int idEnvio, int idEstatus, String comentario) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(false);
        respuesta.setMensaje("Estatus actualizado correctamente");
        return respuesta;
    }
    
    
    public static Respuesta asignarConductor(int idEnvio, int idConductor) {
    Respuesta respuesta = new Respuesta();
    // Definir la URL hacia el nuevo endpoint de la API
    String url = Constantes.URL_WS + "envio/asignar-conductor";
    String parametros = "idEnvio=" + idEnvio + "&idConductor=" + idConductor;
    
    // Realiza la petición POST 
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionPOST(url, parametros);
    
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("No se pudo conectar con el servidor para asignar el conductor.");
        }
        return respuesta;
    }
    
    public static Respuesta registrar(Envio envio) {
    Respuesta respuesta = new Respuesta();
    String url = Constantes.URL_WS + "envio/registrar";
    String json = new Gson().toJson(envio);
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(url, Constantes.METODO_POST, json, "application/json");
    
    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        respuesta = new Gson().fromJson(respuestaAPI.getContenido(), Respuesta.class);
    } else {
        respuesta.setError(true);
        respuesta.setMensaje("Error al conectar con el servidor.");
        }
        return respuesta;
    }
    
}