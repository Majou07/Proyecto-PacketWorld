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
    
    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_FORM = "application/x-www-form-urlencoded";
    
    public static final String REGEX_NUMERO_PERSONAL = "^EMP\\d{3}$";
    public static final String REGEX_NUMERO_LICENCIA = "^[A-Z0-9-]{6,20}$";
    public static final String REGEX_CORREO = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String REGEX_CURP = "^[A-Z]{4}\\d{6}[HM][A-Z]{5}[A-Z0-9]\\d$";
    
    public static final String ESTILO_ERROR =
        "-fx-border-color: red;";
    public static final String ESTILO_NORMAL =
        "";
    
    public static final String ROL_CONDUCTOR = "Conductor";
    public static final String ROL_ADMIN = "Administrador";
    public static final String ROL_EJECUTIVO = "Ejecutivo de tienda";

}
