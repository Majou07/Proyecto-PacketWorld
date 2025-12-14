package dominio;
import dto.Respuesta;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Envio;
import utilidades.Constantes;

public class EnvioImp {
    // ... método obtenerEnvios existente ...

    public static Respuesta registrarEnvio(Envio envio) {
        Respuesta resp = new Respuesta();
        SqlSession conexion = MyBatisUtil.getSession();
        if (conexion != null) {
            try {
                // Generar No. Guía automático
                envio.setNumeroGuia(UUID.randomUUID().toString().substring(0, 10).toUpperCase());
                conexion.insert("envio.registrar", envio);
                conexion.commit();
                resp.setError(false);
                resp.setMensaje("Envío registrado. Guía: " + envio.getNumeroGuia());
            } catch (Exception e) {
                resp.setError(true);
                resp.setMensaje(e.getMessage());
            } finally { conexion.close(); }
        } else { resp.setError(true); resp.setMensaje(Constantes.MSJ_ERROR_BD); }
        return resp;
    }

    public static Respuesta actualizarEstatus(int idEnvio, int idEstatus, String comentario, int idColab) {
        // Implementar actualización e inserción en historial_estatus
        // ... (Logica resumida)
        Respuesta resp = new Respuesta(false, "Estatus actualizado");
        SqlSession conexion = MyBatisUtil.getSession();
        if(conexion != null){
            try{
                Map<String, Object> params = new HashMap<>();
                params.put("idEnvio", idEnvio);
                params.put("idEstatus", idEstatus);
                conexion.update("envio.actualizar-estatus", params);
                // Aquí insertarías en tabla historial...
                conexion.commit();
            } finally { conexion.close(); }
        }
        return resp;
    }
}