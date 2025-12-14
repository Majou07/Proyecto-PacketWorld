package clienteescritorio.dominio;

import clienteescritorio.conexion.ConexionAPI;
import clienteescritorio.dto.BajaUnidadDTO;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.RespuestaHTTP;
import clienteescritorio.pojo.Unidad;
import clienteescritorio.utilidad.Constantes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class UnidadImp {
    
    public static HashMap<String, Object> obtenerUnidades() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        String URL = Constantes.URL_WS + "unidad/obtener-todas";
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            Type tipoLista = new TypeToken<List<Unidad>>(){}.getType();
            List<Unidad> unidades = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put("unidades", unidades);
        } else {
            respuesta.put(Constantes.KEY_ERROR, true);
            respuesta.put(Constantes.KEY_MENSAJE, "Error al cargar las unidades.");
        }
        return respuesta;
    }

    public static Respuesta registrar(Unidad unidad) {
        Respuesta respuesta = new Respuesta();
        String URL = Constantes.URL_WS + "unidad/registrar";
        Gson gson = new Gson();
        String json = gson.toJson(unidad);
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(URL, Constantes.METODO_POST, json, "application/json");
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("Error al registrar unidad");
        }
        return respuesta;
    }
    
    public static Respuesta editar(Unidad unidad) {
        Respuesta respuesta = new Respuesta();
        String URL = Constantes.URL_WS + "unidad/editar";
        Gson gson = new Gson();
        String json = gson.toJson(unidad);
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(URL, Constantes.METODO_PUT, json, "application/json");
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("Error al editar unidad");
        }
        return respuesta;
    }
    
    public static Respuesta darBaja(int idUnidad, String motivo) {
        Respuesta respuesta = new Respuesta();
        String URL = Constantes.URL_WS + "unidad/dar-baja/" + idUnidad;
        Gson gson = new Gson();
        BajaUnidadDTO dto = new BajaUnidadDTO();
        dto.setMotivoBaja(motivo);
        String json = gson.toJson(dto);
        
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(URL, Constantes.METODO_PUT, json, "application/json");
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("Error al dar de baja");
        }
        return respuesta;
    }
}