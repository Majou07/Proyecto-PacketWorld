package dominio;

import dto.Respuesta;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Envio;
import utilidades.Constantes;

public class EnvioImp {

    public static List<Envio> obtenerEnvios() {
        List<Envio> envios = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                envios = conexionBD.selectList("envio.obtener-todos");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return envios;
    }

    public static Respuesta registrarEnvio(Envio envio) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                // Genera número de guía único 
                String guia = UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
                envio.setNumeroGuia(guia);

                // El estatus inicial suele ser 1 (recibido/pendiente)                
                int filasAfectadas = conexionBD.insert("envio.registrar", envio);
                conexionBD.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Envío registrado correctamente. Guía: " + guia);
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se pudo registrar el envío.");
                }
            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje(e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }
        return respuesta;
    }

    public static Respuesta actualizarEstatus(int idEnvio, int idEstatus, String comentario, int idColaborador) {
    Respuesta respuesta = new Respuesta();
    SqlSession conexionBD = MyBatisUtil.getSession();
    if (conexionBD != null) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idEnvio", idEnvio);
            params.put("idEstatus", idEstatus);
            params.put("idColaborador", idColaborador);
            params.put("comentario", comentario);

            // 1. Actualiza el estatus principal del envío
            int filasEnvio = conexionBD.update("envio.actualizar-estatus", params);
            
            // 2. Insertamos en el historial 
            int filasHistorial = conexionBD.insert("envio.registrar-historial", params);

            if (filasEnvio > 0 && filasHistorial > 0) {
                conexionBD.commit();
                respuesta.setError(false);
                respuesta.setMensaje("Estatus e historial actualizados correctamente.");
            } else {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("No se pudo completar la operación.");
            }
            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje(e.getMessage());
            } finally { conexionBD.close(); }
        }
        return respuesta;
    }
    
    public static Respuesta asignarConductor(int idEnvio, int idConductor) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                Map<String, Object> params = new HashMap<>();
                params.put("idEnvio", idEnvio);
                params.put("idConductor", idConductor);

                int filasAfectadas = conexionBD.update("envio.asignar-conductor", params);
                conexionBD.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Conductor asignado correctamente.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se pudo asignar el conductor.");
                }
            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje(e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }
        return respuesta;
    }
    
    
}