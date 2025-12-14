package clienteescritorio.dominio;

import clienteescritorio.conexion.ConexionAPI;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Colaborador;
import clienteescritorio.pojo.RespuestaHTTP;
import clienteescritorio.utilidad.Constantes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ColaboradorImp {
    
    public static HashMap<String, Object> obtenerColaboradores() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        String URL = Constantes.URL_WS + "colaborador/obtener-todos";
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            Type tipoLista = new TypeToken<List<Colaborador>>(){}.getType();
            List<Colaborador> lista = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put("colaboradores", lista);
        } else {
            respuesta.put(Constantes.KEY_ERROR, true);
            respuesta.put(Constantes.KEY_MENSAJE, "Error al cargar la información de colaboradores.");
        }
        return respuesta;
    }

    public static Respuesta registrar(Colaborador colaborador) {
        Respuesta respuesta = new Respuesta();
        String URL = Constantes.URL_WS + "colaborador/registrar";
        Gson gson = new Gson();
        String json = gson.toJson(colaborador);
        
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(URL, Constantes.METODO_POST, json, "application/json");
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("Error al registrar el colaborador.");
        }
        return respuesta;
    }

    public static Respuesta editar(Colaborador colaborador) {
        Respuesta respuesta = new Respuesta();
        String URL = Constantes.URL_WS + "colaborador/editar";
        Gson gson = new Gson();
        String json = gson.toJson(colaborador);
        
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(URL, Constantes.METODO_PUT, json, "application/json");
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("Error al editar el colaborador.");
        }
        return respuesta;
    }
}