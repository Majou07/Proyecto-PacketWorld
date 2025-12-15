package clienteescritorio.dominio;

import clienteescritorio.conexion.ConexionAPI;
import clienteescritorio.dto.RSAutenticacionColaborador;
import clienteescritorio.pojo.RespuestaHTTP;
import clienteescritorio.utilidad.Constantes;
import com.google.gson.Gson;
import java.net.HttpURLConnection;

public class InicioSesionImp {
    
    public static RSAutenticacionColaborador verificarCredenciales(
        String noPersonal, String password) {

    RSAutenticacionColaborador respuesta = new RSAutenticacionColaborador();

    try {
        String parametros =
            "numeroPersonal=" + noPersonal + "&contrasena=" + password;

        RespuestaHTTP respuestaAPI =
            ConexionAPI.peticionPOST(
                Constantes.URL_WS + "autenticacion/administracion",
                parametros
            );

        switch (respuestaAPI.getCodigo()) {
            case HttpURLConnection.HTTP_OK:
                Gson gson = new Gson();
                respuesta = gson.fromJson(
                    respuestaAPI.getContenido(),
                    RSAutenticacionColaborador.class
                );
                break;

            case HttpURLConnection.HTTP_BAD_REQUEST:
                respuesta.setError(true);
                respuesta.setMensaje("Credenciales incorrectas.");
                break;

            default:
                respuesta.setError(true);
                respuesta.setMensaje(
                    "Error de conexión. Código: " + respuestaAPI.getCodigo()
                );
        }
    } catch (Exception e) {
        respuesta.setError(true);
        respuesta.setMensaje("Error fatal: " + e.getMessage());
    }

    return respuesta;
}

}