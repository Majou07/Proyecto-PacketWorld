package clienteescritorio.dominio;

import clienteescritorio.conexion.ConexionAPI;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Colaborador;
import clienteescritorio.pojo.RespuestaHTTP;
import clienteescritorio.pojo.Rol;
import clienteescritorio.utilidad.Constantes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    
     public static Respuesta eliminar(int idColaborador){
        Respuesta respuesta = new Respuesta();
        String URL = Constantes.URL_WS +"colaborador/eliminar/"+idColaborador;
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionSinBody(URL, Constantes.METODO_DELETE);
        if(respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK){
            Gson gson = new Gson();
            respuesta = gson.fromJson(respuestaAPI.getContenido(),Respuesta.class);
        }else{
              respuesta.setError(true);
          switch(respuestaAPI.getCodigo()){
                case Constantes.ERROR_MALFORMED_URL:
                respuesta.setMensaje(Constantes.MSJ_ERROR_URL);
                break;
                
                case Constantes.ERROR_PETICION:
                    respuesta.setMensaje(Constantes.MSJ_ERROR_PETICION);
                    break;
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    respuesta.setMensaje("Campos en formato incorrecto,"+
                            "por favor verifica la informacion enviada");
                    break;
                default:
                    respuesta.setMensaje("Lo sentimos hay problemas para obtener la información"
                            + "en este momento por favor intentelo mas tarde");
                    
        }
                  
        }   
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerRoles() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        String URL = Constantes.URL_WS + "colaborador/obtener-roles"; 
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            Type tipoLista = new TypeToken<List<Rol>>(){}.getType();
            List<Rol> roles = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put("roles", roles);
        } else {
            respuesta.put(Constantes.KEY_ERROR, true);
            respuesta.put(Constantes.KEY_MENSAJE, "Error al cargar los roles.");
        }
        return respuesta;
    }
    
    public static Respuesta subirFoto(Integer idColaborador, byte[] foto) {
    Respuesta respuesta = new Respuesta();
    String URL = Constantes.URL_WS + "colaborador/subir-foto/" + idColaborador;

    RespuestaHTTP respuestaAPI = ConexionAPI.peticionBodyBytes(
            URL,
            Constantes.METODO_PUT,
            foto,
            "application/octet-stream"
    );

    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        Gson gson = new Gson();
        respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
    } else {
        respuesta.setError(true);
        respuesta.setMensaje("Error al subir la fotografía.");
    }
    return respuesta;
}
    public static Colaborador obtenerFoto(Integer idColaborador) {
    String URL = Constantes.URL_WS + "colaborador/obtener-foto/" + idColaborador;
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);

    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK 
        && respuestaAPI.getContenido() != null 
        && !respuestaAPI.getContenido().isEmpty()) {
        
        Gson gson = new Gson();
        return gson.fromJson(respuestaAPI.getContenido(), Colaborador.class);
    }
    return null; 
}

    
    public static HashMap<String, Object> buscarPorNumeroPersonal(String numeroPersonal) {
    HashMap<String, Object> respuesta = new LinkedHashMap<>();
    String URL = Constantes.URL_WS + "colaborador/buscar/numero/" + numeroPersonal;
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);

    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        Gson gson = new Gson();
        Type tipoLista = new TypeToken<List<Colaborador>>(){}.getType();
        List<Colaborador> lista = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
        respuesta.put(Constantes.KEY_ERROR, false);
        respuesta.put("colaboradores", lista);
    } else {
        respuesta.put(Constantes.KEY_ERROR, true);
        respuesta.put(Constantes.KEY_MENSAJE, "Error al buscar colaborador por número personal.");
    }
    return respuesta;
}

    public static HashMap<String, Object> buscarPorRol(int idRol) {
    HashMap<String, Object> respuesta = new LinkedHashMap<>();
    String URL = Constantes.URL_WS + "colaborador/buscar/rol/" + idRol;
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);

    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        Gson gson = new Gson();
        Type tipoLista = new TypeToken<List<Colaborador>>(){}.getType();
        List<Colaborador> lista = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
        respuesta.put(Constantes.KEY_ERROR, false);
        respuesta.put("colaboradores", lista);
    } else {
        respuesta.put(Constantes.KEY_ERROR, true);
        respuesta.put(Constantes.KEY_MENSAJE, "Error al buscar colaboradores por rol.");
    }
    return respuesta;
}
    
  public static HashMap<String, Object> buscarPorNombre(String filtro) {
    HashMap<String, Object> respuesta = new LinkedHashMap<>();

    try {
        // Codificar el filtro
        String filtroEncoded = URLEncoder.encode(filtro, "UTF-8");
        String URL = Constantes.URL_WS + "colaborador/buscar/nombre/" + filtroEncoded;

        // Log de la URL que se está llamando
        System.out.println("[buscarPorNombre] URL: " + URL);

        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);

        // Logs de depuración
        System.out.println("[buscarPorNombre] Código HTTP: " + respuestaAPI.getCodigo());
        System.out.println("[buscarPorNombre] Contenido recibido: " + respuestaAPI.getContenido());

        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            Type tipoLista = new TypeToken<List<Colaborador>>(){}.getType();
            List<Colaborador> lista = gson.fromJson(respuestaAPI.getContenido(), tipoLista);

            // Log de la lista parseada
            System.out.println("[buscarPorNombre] Lista parseada, tamaño: " + (lista != null ? lista.size() : 0));

            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put("colaboradores", lista);
        } else {
            respuesta.put(Constantes.KEY_ERROR, true);
            respuesta.put(Constantes.KEY_MENSAJE, "Error al buscar colaboradores por nombre.");
        }
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.put(Constantes.KEY_ERROR, true);
            respuesta.put(Constantes.KEY_MENSAJE, "Error al codificar o procesar la búsqueda por nombre.");
        }

        return respuesta;
    }
  
  
  
 public static Respuesta asignarVehiculo(int idColaborador, Integer idUnidad) {
    String url = Constantes.URL_WS + "colaborador/asignar-unidad";

    // Construir parámetros tipo form-urlencoded
    String params = "idColaborador=" + idColaborador + "&idUnidad=" + idUnidad;

    System.out.println("📤 Enviando solicitud PUT a: " + url);
    System.out.println("📦 Parámetros: " + params);

    // Usar peticionPUT (que envía application/x-www-form-urlencoded)
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionPUT(url, params);

    System.out.println("📥 Código de respuesta HTTP: " + respuestaAPI.getCodigo());
    System.out.println("📥 Contenido de respuesta: " + respuestaAPI.getContenido());

    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        Respuesta respuesta = new Gson().fromJson(respuestaAPI.getContenido(), Respuesta.class);
        System.out.println("✅ Respuesta procesada: " + respuesta.getMensaje());
        return respuesta;
    }

    System.out.println("❌ Error de comunicación con el servidor");
    return new Respuesta(true, "Error de comunicación");
}



}