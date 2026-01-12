package dominio;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Respuesta;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Envio;
import pojo.HistorialEstatus;
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
    
    public static List<Envio> obtenerPorConductor(int idConductor) {
    List<Envio> envios = null;
    SqlSession conexionBD = MyBatisUtil.getSession();
    if (conexionBD != null) {
        try {
            envios = conexionBD.selectList("envio.obtener-por-conductor", idConductor);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexionBD.close();
        }
    }
    return envios;
}
    
    public static Envio obtenerDetalle(int idEnvio) {
    Envio envio = null;
    SqlSession conexionBD = MyBatisUtil.getSession();
    if (conexionBD != null) {
        try {
            envio = conexionBD.selectOne("envio.obtener-detalle", idEnvio);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexionBD.close();
        }
    }
    return envio;
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

            int filasEnvio = conexionBD.update("envio.actualizar-estatus", params);
            
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
    
    
    private static String llamarApiExterna(String urlString) throws Exception {
    StringBuilder resultado = new StringBuilder();
    java.net.URL url = new java.net.URL(urlString);
    java.net.HttpURLConnection conexion = (java.net.HttpURLConnection) url.openConnection();
    conexion.setRequestMethod("GET"); 

        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(conexion.getInputStream()))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                resultado.append(linea);
            }
        }
        return resultado.toString();
    }
    
    
    
    public static Double calcularCostoEnvio(String cpOrigen, String cpDestino, int numeroPaquetes) {
    try {
        String urlDistancia = "http://sublimas.com.mx:8080/calculadora/api/envios/distancia/" + cpOrigen + "," + cpDestino;
        String jsonRespuesta = llamarApiExterna(urlDistancia); 
        JsonObject resp = new Gson().fromJson(jsonRespuesta, JsonObject.class);
        double distancia = resp.get("distanciaKM").getAsDouble();

        // Determina el costo por kilómetro 
        double costoKM = 0.50; // Más de 2000 km
        if (distancia <= 200) costoKM = 4.00;
        else if (distancia <= 500) costoKM = 3.00;
        else if (distancia <= 1000) costoKM = 2.00;
        else if (distancia <= 2000) costoKM = 1.00;

        
        // Determina el costo adicional por paquetes
        double costoAdicional = 150.00; // 5 o más
        switch (numeroPaquetes) {
            case 1: costoAdicional = 0.00; break;
            case 2: costoAdicional = 50.00; break;
            case 3: costoAdicional = 80.00; break;
            case 4: costoAdicional = 110.00; break;
        }

        return (distancia * costoKM) + costoAdicional;

        } catch (Exception e) {
            return 0.0;
        }
    }
    
    
    
    public static List<HistorialEstatus> obtenerHistorial(int idEnvio) {
    List<HistorialEstatus> historial = null;
    SqlSession conexionBD = MyBatisUtil.getSession();
    if (conexionBD != null) {
        try {
            historial = conexionBD.selectList("envio.obtener-historial", idEnvio);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexionBD.close();
        }
    }
    return historial;
    }
    
    public static HashMap<String, Object> eliminarEnvio(Integer idEnvio) {
    HashMap<String, Object> respuesta = new HashMap<>();
    respuesta.put("error", false); 
    respuesta.put("mensaje", "Envío eliminado correctamente");
    return respuesta;
    }

        public static HashMap<String, Object> buscarEnvioPorGuia(String numeroGuia) {
        HashMap<String, Object> respuesta = new HashMap<>();
        return respuesta;
    }
        
        
    public static Respuesta editarEnvio(Envio envio) {
    Respuesta respuesta = new Respuesta();
    SqlSession conexionBD = MyBatisUtil.getSession();
    if (conexionBD != null) {
        try {
            int filasAfectadas = conexionBD.update("envio.editar", envio);
            conexionBD.commit();
            
            if (filasAfectadas > 0) {
                respuesta.setError(false);
                respuesta.setMensaje("Información del envío actualizada correctamente.");
            } else {
                respuesta.setError(true);
                respuesta.setMensaje("No se encontró el envío para actualizar.");
            }
            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error: " + e.getMessage());
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