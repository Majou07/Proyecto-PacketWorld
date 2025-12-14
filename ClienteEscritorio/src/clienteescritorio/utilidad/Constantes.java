package clienteescritorio.utilidad;

public class Constantes {
    public static final String URL_WS = "http://localhost:8080/APIRestPW/api/";
    public static final int ERROR_MALFORMED_URL = 1001;
    public static final int ERROR_PETICION = 1002;
    public static final String MSJ_ERROR_URL = "Error en la URL del servicio.";
    public static final String MSJ_ERROR_PETICION = "Error de conexión con el servidor.";
    
    public static final String KEY_ERROR = "error";
    public static final String KEY_MENSAJE = "mensaje";
    public static final String KEY_OBJETO = "objeto";
    
    // Métodos HTTP
    public static final String METODO_POST = "POST";
    public static final String METODO_GET = "GET";
    public static final String METODO_PUT = "PUT";
    public static final String METODO_DELETE = "DELETE";
}
