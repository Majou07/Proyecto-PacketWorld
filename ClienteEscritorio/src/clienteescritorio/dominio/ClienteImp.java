package clienteescritorio.dominio;

import clienteescritorio.conexion.ConexionAPI;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Cliente;
import clienteescritorio.pojo.RespuestaHTTP;
import clienteescritorio.utilidad.Constantes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ClienteImp {
    
    public static HashMap<String, Object> obtenerClientes() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        String URL = Constantes.URL_WS + "cliente/obtener-todos";
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            Type tipoLista = new TypeToken<List<Cliente>>(){}.getType();
            List<Cliente> clientes = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put("clientes", clientes);
        } else {
            respuesta.put(Constantes.KEY_ERROR, true);
            respuesta.put(Constantes.KEY_MENSAJE, "Error al cargar los clientes.");
        }
        return respuesta;
    }
    
    public static Respuesta registrar(Cliente cliente) {
        Respuesta respuesta = new Respuesta();
        String URL = Constantes.URL_WS + "cliente/registrar";
        Gson gson = new Gson();
        String json = gson.toJson(cliente);
        // Usamos POST para registrar
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(URL, Constantes.METODO_POST, json, "application/json");
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("Error al registrar cliente");
        }
        return respuesta;
    }

    // --- NUEVO MÉTODO EDITAR ---
    public static Respuesta editar(Cliente cliente) {
        Respuesta respuesta = new Respuesta();
        String URL = Constantes.URL_WS + "cliente/editar";
        Gson gson = new Gson();
        String json = gson.toJson(cliente);
        // Usamos PUT para editar según el estándar RESTful solicitado
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(URL, Constantes.METODO_PUT, json, "application/json");
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("Error al actualizar cliente");
        }
        return respuesta;
    }
    
    public static Respuesta eliminar(int idCliente) {
        String url = Constantes.URL_WS + "cliente/eliminar/" + idCliente;
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionSinBody(url, Constantes.METODO_DELETE);
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            return new Gson().fromJson(respuestaAPI.getContenido(), Respuesta.class);
        }
        return new Respuesta(true, "Error de comunicación con el servidor");
    }
}