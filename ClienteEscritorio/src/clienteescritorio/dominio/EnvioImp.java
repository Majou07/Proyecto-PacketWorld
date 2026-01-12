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
import clienteescritorio.pojo.HistorialEstatus;

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

    public static Respuesta actualizarEstatus(int idEnvio, int idEstatus, String comentario, int idColaborador) {
        Respuesta respuesta = new Respuesta();
        try {
            String comentarioCodificado = java.net.URLEncoder.encode(comentario, "UTF-8");

            String url = Constantes.URL_WS + "envio/actualizar-estatus";
            String parametros = "idEnvio=" + idEnvio + "&idEstatus=" + idEstatus + 
                                "&comentario=" + comentarioCodificado + "&idColaborador=" + idColaborador;

            System.out.println("Enviando actualización: " + parametros); 
            RespuestaHTTP respuestaAPI = ConexionAPI.peticionPUT(url, parametros);

            if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
                return new Gson().fromJson(respuestaAPI.getContenido(), Respuesta.class);
            } else {
                return new Respuesta(true, "Servidor respondió con código: " + respuestaAPI.getCodigo());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Respuesta(true, "Error local: " + e.getMessage());
        }
    }

    
    public static Respuesta asignarConductor(int idEnvio, int idConductor) {
    Respuesta respuesta = new Respuesta();
    String url = Constantes.URL_WS + "envio/asignar-conductor";
    String parametros = "idEnvio=" + idEnvio + "&idConductor=" + idConductor;
    
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionPUT(url, parametros);
    
    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        respuesta = new Gson().fromJson(respuestaAPI.getContenido(), Respuesta.class);
    } else {
        respuesta.setError(true);
        respuesta.setMensaje("Error al asignar el conductor.");
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
            respuesta.setMensaje("Error al conectar con el servidor para registrar el envío.");
        }
        return respuesta;
    }
    
    public static Respuesta editar(Envio envio) {
    Respuesta respuesta = new Respuesta();
    String url = Constantes.URL_WS + "envio/editar"; 
    String json = new Gson().toJson(envio);
    
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(url, Constantes.METODO_PUT, json, "application/json");
    
    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        respuesta = new Gson().fromJson(respuestaAPI.getContenido(), Respuesta.class);
    } else {
        respuesta.setError(true);
        respuesta.setMensaje("No se pudo actualizar la información del envío.");
    }
    return respuesta;
    }
    
    
    public static Envio buscarEnvioPorGuia(String guia) {
        Envio envio = null;
        String url = Constantes.URL_WS + "envio/" + guia;
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(url);
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            envio = new Gson().fromJson(respuestaAPI.getContenido(), Envio.class);
        }
        return envio;
    }
    
    
    public static Respuesta eliminarEnvio(int idEnvio) {
        return actualizarEstatus(idEnvio, 6, "Eliminado desde sistema", 1);
    }
    
    
    public static HashMap<String, Object> obtenerHistorial(int idEnvio) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        String url = Constantes.URL_WS + "envio/historial/" + idEnvio;
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(url);
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            Type tipoLista = new TypeToken<List<HistorialEstatus>>(){}.getType();
            List<HistorialEstatus> historial = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put("historial", historial);
        } else {
            respuesta.put(Constantes.KEY_ERROR, true);
            respuesta.put(Constantes.KEY_MENSAJE, "Error al cargar el historial.");
        }
        return respuesta;
    }
}