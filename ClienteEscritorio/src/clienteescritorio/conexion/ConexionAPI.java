package clienteescritorio.conexion;

import clienteescritorio.pojo.RespuestaHTTP;
import clienteescritorio.utilidad.Constantes;
import clienteescritorio.utilidad.Utilidades;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConexionAPI {

    public static RespuestaHTTP peticionGET(String urlString) {
        RespuestaHTTP respuesta = new RespuestaHTTP();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");

            int codigo = conexion.getResponseCode();
            respuesta.setCodigo(codigo);

            if (codigo == HttpURLConnection.HTTP_OK) {
                respuesta.setContenido(Utilidades.streamToString(conexion.getInputStream()));
            } else {
                respuesta.setContenido(Utilidades.streamToString(conexion.getErrorStream()));
            }
        } catch (MalformedURLException e) {
            respuesta.setCodigo(Constantes.ERROR_MALFORMED_URL);
            respuesta.setContenido(e.getMessage());
        } catch (IOException e) {
            respuesta.setCodigo(Constantes.ERROR_PETICION);
            respuesta.setContenido(e.getMessage());
        }
        return respuesta;
    }

    public static RespuestaHTTP peticionSinBody(String urlString, String metodoHTTP) {
        RespuestaHTTP respuesta = new RespuestaHTTP();
        try {
            URL urlWS = new URL(urlString);
            HttpURLConnection conexionHTTP = (HttpURLConnection) urlWS.openConnection();
            conexionHTTP.setRequestMethod(metodoHTTP);

            int codigo = conexionHTTP.getResponseCode();
            respuesta.setCodigo(codigo);

            if (codigo == HttpURLConnection.HTTP_OK) {
                respuesta.setContenido(
                    Utilidades.streamToString(conexionHTTP.getInputStream())
                );
            } else {
                respuesta.setContenido(
                    Utilidades.streamToString(conexionHTTP.getErrorStream())
                );
            }
        } catch (MalformedURLException e) {
            respuesta.setCodigo(Constantes.ERROR_MALFORMED_URL);
            respuesta.setContenido(e.getMessage());
        } catch (IOException e) {
            respuesta.setCodigo(Constantes.ERROR_PETICION);
            respuesta.setContenido(e.getMessage());
        }
        return respuesta;
    }

    public static RespuestaHTTP peticionBody(
            String urlString,
            String metodo,
            String parametros,
            String contentType
    ) {
        RespuestaHTTP respuesta = new RespuestaHTTP();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod(metodo);
            conexion.setRequestProperty("Content-Type", contentType);
            conexion.setDoOutput(true);

            OutputStream os = conexion.getOutputStream();
            os.write(parametros.getBytes());
            os.flush();
            os.close();

            int codigo = conexion.getResponseCode();
            respuesta.setCodigo(codigo);

            if (codigo == HttpURLConnection.HTTP_OK) {
                respuesta.setContenido(Utilidades.streamToString(conexion.getInputStream()));
            } else {
                respuesta.setContenido(Utilidades.streamToString(conexion.getErrorStream()));
            }
        } catch (Exception e) {
            respuesta.setCodigo(Constantes.ERROR_PETICION);
            respuesta.setContenido(e.getMessage());
        }
        return respuesta;
    }

    public static RespuestaHTTP peticionPOST(String urlString, String parametros) {
        RespuestaHTTP respuesta = new RespuestaHTTP();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod(Constantes.METODO_POST);
            conexion.setDoOutput(true);
            conexion.setRequestProperty(
                "Content-Type",
                "application/x-www-form-urlencoded"
            );

            OutputStream os = conexion.getOutputStream();
            os.write(parametros.getBytes());
            os.flush();
            os.close();

            int codigo = conexion.getResponseCode();
            respuesta.setCodigo(codigo);

            if (codigo == HttpURLConnection.HTTP_OK) {
                respuesta.setContenido(Utilidades.streamToString(conexion.getInputStream()));
            } else {
                respuesta.setContenido(Utilidades.streamToString(conexion.getErrorStream()));
            }
        } catch (Exception e) {
            respuesta.setCodigo(Constantes.ERROR_PETICION);
            respuesta.setContenido(e.getMessage());
        }
        return respuesta;
    }

    public static RespuestaHTTP peticionBodyBytes(
            String urlString,
            String metodo,
            byte[] datos,
            String contentType
    ) {
        RespuestaHTTP respuesta = new RespuestaHTTP();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod(metodo);
            conexion.setRequestProperty("Content-Type", contentType);
            conexion.setDoOutput(true);

            OutputStream os = conexion.getOutputStream();
            os.write(datos);
            os.flush();
            os.close();

            int codigo = conexion.getResponseCode();
            respuesta.setCodigo(codigo);

            if (codigo == HttpURLConnection.HTTP_OK) {
                respuesta.setContenido(Utilidades.streamToString(conexion.getInputStream()));
            } else {
                respuesta.setContenido(Utilidades.streamToString(conexion.getErrorStream()));
            }
        } catch (Exception e) {
            respuesta.setCodigo(Constantes.ERROR_PETICION);
            respuesta.setContenido(e.getMessage());
        }
        return respuesta;
    }
}
