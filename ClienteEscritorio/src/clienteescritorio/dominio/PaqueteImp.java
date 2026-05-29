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

            // Si se registró correctamente, recalcular costo del envío
            if (!respuesta.isError() && paquete.getIdEnvio() != null) {
                Respuesta costoResp = recalcularCostoEnvio(paquete.getIdEnvio());
                System.out.println("Recálculo de costo: " + costoResp.getMensaje());
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("Error al registrar paquete. Código: " + respuestaAPI.getCodigo());
        }
        return respuesta;
    }

    public static Respuesta recalcularCostoEnvio(int idEnvio) {
        String url = Constantes.URL_WS + "envio/recalcular-costo/" + idEnvio;
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(url);

        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            return new Gson().fromJson(respuestaAPI.getContenido(), Respuesta.class);
        }
        return new Respuesta(true, "Error al recalcular costo. Código: " + respuestaAPI.getCodigo());
    }
    
    public static Respuesta editar(Paquete paquete) {
    Respuesta respuesta = new Respuesta();
    String url = Constantes.URL_WS + "paquete/editar";
    String json = new Gson().toJson(paquete);
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(url, "PUT", json, "application/json");
    
    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        respuesta = new Gson().fromJson(respuestaAPI.getContenido(), Respuesta.class);
        
        if (!respuesta.isError() && paquete.getIdEnvio() != null) {
            recalcularCostoEnvio(paquete.getIdEnvio());
        }
    } else {
        respuesta.setError(true);
        respuesta.setMensaje("Error al intentar actualizar el paquete.");
    }
    return respuesta;
}
    
    
    public static Respuesta eliminarPaquete(int idPaquete) {
        Respuesta respuesta = new Respuesta();

        // Primero obtener el idEnvio antes de eliminar
        String urlGet = Constantes.URL_WS + "paquete/obtener-por-id/" + idPaquete;
        RespuestaHTTP getResp = ConexionAPI.peticionGET(urlGet);
        Integer idEnvio = null;

        if (getResp.getCodigo() == HttpURLConnection.HTTP_OK) {
            Paquete p = new Gson().fromJson(getResp.getContenido(), Paquete.class);
            if (p != null) idEnvio = p.getIdEnvio();
        }

        String url = Constantes.URL_WS + "paquete/eliminar/" + idPaquete;
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionDELETE(url);

        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            respuesta = new Gson().fromJson(respuestaAPI.getContenido(), Respuesta.class);

            // Recalcular costo después de eliminar
            if (!respuesta.isError() && idEnvio != null) {
                recalcularCostoEnvio(idEnvio);
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("Error al conectar con el servidor para eliminar el paquete.");
        }
        return respuesta;
    }
    
}
