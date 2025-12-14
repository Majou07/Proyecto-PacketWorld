package clienteescritorio.dominio;


import java.lang.ProcessBuilder.Redirect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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
        // Implementar lógica POST/PUT similar a login para enviar params form-urlencoded
        return new Respuesta(false, "Simulación: Estatus actualizado");
    }
}
