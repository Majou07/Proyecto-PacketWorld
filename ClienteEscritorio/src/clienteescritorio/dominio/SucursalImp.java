package clienteescritorio.dominio;

import clienteescritorio.conexion.ConexionAPI;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.RespuestaHTTP;
import clienteescritorio.pojo.Sucursal;
import clienteescritorio.utilidad.Constantes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class SucursalImp {
    
    public static HashMap<String, Object> obtenerSucursales() {
    HashMap<String, Object> respuesta = new LinkedHashMap<>();
    String URL = Constantes.URL_WS + "sucursal/obtener-todas";
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);

    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        Gson gson = new Gson();
        Type tipoLista = new TypeToken<List<Sucursal>>(){}.getType();

        try {
            List<Sucursal> sucursales = gson.fromJson(respuestaAPI.getContenido(), tipoLista);

            if (sucursales != null) {
                respuesta.put(Constantes.KEY_ERROR, false);
                respuesta.put("sucursales", sucursales);
            } else {
                respuesta.put(Constantes.KEY_ERROR, false);
                respuesta.put("sucursales", new ArrayList<Sucursal>()); // ✅ lista vacía en vez de null
            }
        } catch (Exception e) {
            respuesta.put(Constantes.KEY_ERROR, true);
            respuesta.put(Constantes.KEY_MENSAJE, "Error al procesar la respuesta de sucursales.");
        }
    } else {
        respuesta.put(Constantes.KEY_ERROR, true);
        respuesta.put(Constantes.KEY_MENSAJE, "Error al cargar las sucursales.");
    }
    return respuesta;
}

    
    public static Respuesta registrarSucursal(Sucursal nuevaSucursal) {
    // Convertir el objeto a JSON
    Gson gson = new Gson();
    String json = gson.toJson(nuevaSucursal);

    String URL = Constantes.URL_WS + "sucursal/registrar";
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(URL, Constantes.METODO_POST, json, "application/json");

    Respuesta respuesta = new Respuesta();
    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
    } else {
        respuesta.setError(true);
        respuesta.setMensaje("Error al registrar la sucursal");
    }
    return respuesta;
}

    
    public static Respuesta darBajaSucursal(String codigoSucursal) {
    Respuesta respuesta = new Respuesta();
    String URL = Constantes.URL_WS + "sucursal/dar-baja/" + codigoSucursal;

    // Usamos peticionSinBody porque no hay body, solo PUT
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionSinBody(URL, Constantes.METODO_PUT);

    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        Gson gson = new Gson();
        respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
    } else {
        respuesta.setError(true);
        respuesta.setMensaje("Error al dar de baja la sucursal");
    }
    return respuesta;
}

public static Respuesta reactivarSucursal(String codigoSucursal) {
    Respuesta respuesta = new Respuesta();
    String URL = Constantes.URL_WS + "sucursal/reactivar/" + codigoSucursal;

    // Igual que arriba, PUT sin body
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionSinBody(URL, Constantes.METODO_PUT);

    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        Gson gson = new Gson();
        respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
    } else {
        respuesta.setError(true);
        respuesta.setMensaje("Error al reactivar la sucursal");
    }
    return respuesta;
}

public static Respuesta editarSucursal(Sucursal sucursalEditada) {
    // Convertir el objeto a JSON
    Gson gson = new Gson();
    String json = gson.toJson(sucursalEditada);

    String URL = Constantes.URL_WS + "sucursal/editar";
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(
            URL, Constantes.METODO_PUT, json, "application/json");

    Respuesta respuesta = new Respuesta();
    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
    } else {
        respuesta.setError(true);
        respuesta.setMensaje("Error al editar la sucursal");
    }
    return respuesta;
}


}