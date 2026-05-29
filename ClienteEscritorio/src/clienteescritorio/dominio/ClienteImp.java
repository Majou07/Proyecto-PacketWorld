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
    
    public static HashMap<String, Object> buscarPorNombre(String valor) {
    HashMap<String, Object> respuesta = new LinkedHashMap<>();
    
    // Codificar el valor para evitar problemas con espacios en la URL
    String valorCodificado;
   try {
    valorCodificado = java.net.URLEncoder.encode(valor.trim(), "UTF-8");
    // Reemplazar '+' por '%20' para que sea igual que Postman
    valorCodificado = valorCodificado.replace("+", "%20");
    } catch (Exception e) {
    valorCodificado = valor.trim(); // fallback
    }

    String url = Constantes.URL_WS + "cliente/buscar/nombre/" + valorCodificado;

    // Depuración: imprime lo que se envía desde el cliente
    System.out.println("Cliente Escritorio - Valor enviado en búsqueda: [" + valor.trim() + "]");
    System.out.println("Cliente Escritorio - Valor codificado: [" + valorCodificado + "]");
    System.out.println("Cliente Escritorio - URL construida: " + url);

    RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(url);

    // Depuración: imprime lo que se recibe del servidor
    System.out.println("Cliente Escritorio - Código HTTP: " + respuestaAPI.getCodigo());
    System.out.println("Cliente Escritorio - Contenido recibido: " + respuestaAPI.getContenido());

    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        Gson gson = new Gson();
        Type tipoLista = new TypeToken<List<Cliente>>(){}.getType();
        List<Cliente> clientes = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
        respuesta.put(Constantes.KEY_ERROR, false);
        respuesta.put("clientes", clientes);
    } else {
        respuesta.put(Constantes.KEY_ERROR, true);
        respuesta.put(Constantes.KEY_MENSAJE, "Error al buscar clientes por nombre.");
    }
    return respuesta;
}


public static HashMap<String, Object> buscarPorTelefono(String valor) {
    HashMap<String, Object> respuesta = new LinkedHashMap<>();
    String url = Constantes.URL_WS + "cliente/buscar/telefono/" + valor.trim();
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(url);

    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        Gson gson = new Gson();
        Type tipoLista = new TypeToken<List<Cliente>>(){}.getType();
        List<Cliente> clientes = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
        respuesta.put(Constantes.KEY_ERROR, false);
        respuesta.put("clientes", clientes);
    } else {
        respuesta.put(Constantes.KEY_ERROR, true);
        respuesta.put(Constantes.KEY_MENSAJE, "Error al buscar clientes por teléfono.");
    }
    return respuesta;
}

    public static HashMap<String, Object> buscarPorCorreo(String valor) {
    HashMap<String, Object> respuesta = new LinkedHashMap<>();
    String url = Constantes.URL_WS + "cliente/buscar/correo/" + valor.trim();
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(url);

    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        Gson gson = new Gson();
        Type tipoLista = new TypeToken<List<Cliente>>(){}.getType();
        List<Cliente> clientes = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
        respuesta.put(Constantes.KEY_ERROR, false);
        respuesta.put("clientes", clientes);
    } else {
        respuesta.put(Constantes.KEY_ERROR, true);
        respuesta.put(Constantes.KEY_MENSAJE, "Error al buscar clientes por correo.");
    }
    return respuesta;
}

}