package clienteescritorio.dominio;

import clienteescritorio.conexion.ConexionAPI;
import clienteescritorio.dto.BajaUnidadDTO;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.RespuestaHTTP;
import clienteescritorio.pojo.TipoUnidad;
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
    
    public static HashMap<String, Object> obtenerTipos() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        String URL = Constantes.URL_WS + "unidad/obtener-tipos"; 
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            Type tipoLista = new TypeToken<List<TipoUnidad>>(){}.getType();
            List<TipoUnidad> tipos = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put("tipos", tipos);
        } else {
            respuesta.put(Constantes.KEY_ERROR, true);
            respuesta.put(Constantes.KEY_MENSAJE, "Error al cargar los tipos de unidad.");
        }
        return respuesta;
    }
    
    public static Respuesta darBaja(int idUnidad, String motivo) {
    Respuesta respuesta = new Respuesta();
    String URL = Constantes.URL_WS + "unidad/dar-baja/" + idUnidad;

    // enviar directamente el motivo como texto plano
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(
        URL, Constantes.METODO_PUT, motivo, "text/plain");

    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        Gson gson = new Gson();
        respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
    } else {
        respuesta.setError(true);
        respuesta.setMensaje("Error al dar de baja");
    }
    return respuesta;
}
    
   public static HashMap<String, Object> buscarUnidades(String vin, String marca, String nii) {
    HashMap<String, Object> respuesta = new HashMap<>();
    String url = Constantes.URL_WS + "unidad/buscar?vin=" + (vin != null ? vin : "")
                                        + "&marca=" + (marca != null ? marca : "")
                                        + "&nii=" + (nii != null ? nii : "");

    RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(url);

    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Unidad>>(){}.getType();
        List<Unidad> unidades = gson.fromJson(respuestaAPI.getContenido(), listType);
        respuesta.put("error", false);
        respuesta.put("unidades", unidades);
    } else {
        respuesta.put("error", true);
        respuesta.put("mensaje", "Error al buscar unidades");
    }
    return respuesta;
}


}