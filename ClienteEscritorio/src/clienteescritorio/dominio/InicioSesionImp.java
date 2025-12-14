package clienteescritorio.dominio;

import clienteescritorio.conexion.ConexionAPI;
import clienteescritorio.dto.RSAutenticacionColaborador;
import clienteescritorio.pojo.RespuestaHTTP;
import clienteescritorio.utilidad.Constantes;
import com.google.gson.Gson;
import java.net.HttpURLConnection;

public class InicioSesionImp {
    
    public static RSAutenticacionColaborador verificarCredenciales(String noPersonal, String password) {
        
        RSAutenticacionColaborador respuesta = new RSAutenticacionColaborador();
        
        String parametros = "numeroPersonal=" + noPersonal + "contrasena=" + password;
        
        String URL = Constantes.URL_WS + "autenticacion/administracion";
        
        try {
            RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(URL, "POST", parametros, "application/x-www-form-urlencoded");
            
            if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
                Gson gson = new Gson();
                try {
                    respuesta = gson.fromJson(respuestaAPI.getContenido(), RSAutenticacionColaborador.class);
                } catch (Exception e) {
                    respuesta.setError(true);
                    respuesta.setMensaje("Error al procesar la respuesta del servidor");
                }
            } else {
                respuesta.setError(true);
                switch (respuestaAPI.getCodigo()) {
                    case Constantes.ERROR_MALFORMED_URL:
                        respuesta.setMensaje(Constantes.MSJ_ERROR_URL);
                        break;
                    case Constantes.ERROR_PETICION:
                        respuesta.setMensaje(Constantes.MSJ_ERROR_PETICION);
                        break;
                    case HttpURLConnection.HTTP_BAD_REQUEST:
                        respuesta.setMensaje("Credenciales incorrectas o datos faltantes.");
                        break;
                    default:
                        respuesta.setMensaje("Error de conexión. Código: " + respuestaAPI.getCodigo());
                }
            }
        } catch (Exception e) {
            respuesta.setError(true);
            respuesta.setMensaje("Error fatal: " + e.getMessage());
        }
        
        return respuesta; 
    }
}