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
            // 1. Obtener CP de la sucursal origen
            String cpOrigen = conexionBD.selectOne("sucursal.obtener-cp", envio.getCodigoSucursalOrigen());
            String cpDestino = envio.getDestinoCodigoPostal();
            
            System.out.println("=== REGISTRANDO ENVÍO ===");
            System.out.println("CP Origen: " + cpOrigen);
            System.out.println("CP Destino: " + cpDestino);
            System.out.println("Sucursal: " + envio.getCodigoSucursalOrigen());
            
            // 2. Calcular costo (con fallback si la API falla)
            Double costo = 150.00; // costo base por defecto
            
            if (cpOrigen != null && cpDestino != null && !cpOrigen.isEmpty() && !cpDestino.isEmpty()) {
                costo = calcularCostoEnvio(cpOrigen, cpDestino, 1);
                if (costo == null || costo == 0.0) {
                    costo = calcularCostoPorDefecto(cpOrigen, cpDestino, 1);
                }
            } else {
                System.out.println("Advertencia: CP origen o destino faltante, usando costo por defecto");
            }
            
            envio.setCostoTotal(costo);
            
            String guia = UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
            envio.setNumeroGuia(guia);
            envio.setIdEstatusEnvio(1); // Estatus inicial: recibido
            
            int filasAfectadas = conexionBD.insert("envio.registrar", envio);
            conexionBD.commit();

            if (filasAfectadas > 0) {
                respuesta.setError(false);
                respuesta.setMensaje("Envío registrado correctamente. Guía: " + guia + ". Costo: $" + String.format("%.2f", costo));
            } else {
                respuesta.setError(true);
                respuesta.setMensaje("No se pudo registrar el envío.");
            }
        } catch (Exception e) {
            conexionBD.rollback();
            respuesta.setError(true);
            respuesta.setMensaje(e.getMessage());
            e.printStackTrace();
        } finally {
            conexionBD.close();
        }
    } else {
        respuesta.setError(true);
        respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
    }
    return respuesta;
}

private static Double calcularCostoPorDefecto(String cpOrigen, String cpDestino, int numeroPaquetes) {
    double costoBase = 150.00;
    
    if (cpOrigen != null && cpDestino != null && !cpOrigen.equals(cpDestino)) {
        costoBase += 100.00;
    }
    
    double costoAdicional = 0.00;
    switch (numeroPaquetes) {
        case 2: costoAdicional = 50.00; break;
        case 3: costoAdicional = 80.00; break;
        case 4: costoAdicional = 110.00; break;
        default: costoAdicional = 0.00; break;
    }
    
    return costoBase + costoAdicional;
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

    public static Envio obtenerDetallePorGuia(String numeroGuia) {
    Envio envio = null;
    SqlSession conexionBD = MyBatisUtil.getSession();
    
    if (conexionBD != null) {
        try {
            envio = conexionBD.selectOne(
                "envio.obtener-detalle-por-guia",
                numeroGuia
            );
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

            if (resp.has("distanciaKM")) {
                double distancia = resp.get("distanciaKM").getAsDouble();

                double costoKM = 0.50;
                if (distancia <= 200) costoKM = 4.00;
                else if (distancia <= 500) costoKM = 3.00;
                else if (distancia <= 1000) costoKM = 2.00;
                else if (distancia <= 2000) costoKM = 1.00;

                double costoAdicional = 150.00;
                switch (numeroPaquetes) {
                    case 1: costoAdicional = 0.00; break;
                    case 2: costoAdicional = 50.00; break;
                    case 3: costoAdicional = 80.00; break;
                    case 4: costoAdicional = 110.00; break;
                }

                return (distancia * costoKM) + costoAdicional;
            } else {
                return calcularCostoPorDefecto(cpOrigen, cpDestino, numeroPaquetes);
            }
        } catch (Exception e) {
            System.err.println("Error en API de distancias: " + e.getMessage());
            return calcularCostoPorDefecto(cpOrigen, cpDestino, numeroPaquetes);
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
    
    public static Respuesta eliminar(Integer idEnvio) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                conexionBD.delete("envio.eliminar-historial", idEnvio);


                int filasAfectadas = conexionBD.delete("envio.eliminar", idEnvio);

                if (filasAfectadas > 0) {
                    conexionBD.commit();
                    respuesta.setError(false);
                    respuesta.setMensaje("Envío eliminado físicamente de la base de datos.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se encontró el envío para eliminar.");
                }
            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al eliminar: " + e.getMessage());
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }
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
    
    
    public static Respuesta recalcularCostosDeTodosLosEnvios() {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD == null) {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }

        try {
            List<Envio> envios = conexionBD.selectList("envio.obtener-todos-sin-costo");
            int actualizados = 0;

            for (Envio envio : envios) {
                String cpOrigen = conexionBD.selectOne("sucursal.obtener-cp", envio.getCodigoSucursalOrigen());
                String cpDestino = envio.getDestinoCodigoPostal();

                if (cpOrigen != null && cpDestino != null && !cpOrigen.isEmpty() && !cpDestino.isEmpty()) {
                    Double costo = calcularCostoEnvio(cpOrigen, cpDestino, 1);
                    if (costo == null || costo == 0.0) {
                        costo = calcularCostoPorDefecto(cpOrigen, cpDestino, 1);
                    }

                    Map<String, Object> params = new HashMap<>();
                    params.put("idEnvio", envio.getIdEnvio());
                    params.put("costoTotal", costo);
                    conexionBD.update("envio.actualizar-costo", params);
                    actualizados++;
                }
            }

            conexionBD.commit();
            respuesta.setError(false);
            respuesta.setMensaje("Se recalcularon " + actualizados + " envíos correctamente");

        } catch (Exception e) {
            conexionBD.rollback();
            respuesta.setError(true);
            respuesta.setMensaje("Error: " + e.getMessage());
        } finally {
            conexionBD.close();
        }
        return respuesta;
    }
    
    
    public static Respuesta recalcularCostoEnvio(int idEnvio) {
    Respuesta respuesta = new Respuesta();
    SqlSession conexionBD = MyBatisUtil.getSession();
    
    if (conexionBD == null) {
        respuesta.setError(true);
        respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        return respuesta;
    }
    
    try {
        Envio envio = conexionBD.selectOne("envio.obtener-detalle", idEnvio);
        if (envio == null) {
            respuesta.setError(true);
            respuesta.setMensaje("Envío no encontrado");
            return respuesta;
        }
        
        String cpOrigen = conexionBD.selectOne("sucursal.obtener-cp", envio.getCodigoSucursalOrigen());
        String cpDestino = envio.getDestinoCodigoPostal();
        
        // Contar paquetes
        Integer numeroPaquetes = conexionBD.selectOne("paquete.contar-por-envio", idEnvio);
        if (numeroPaquetes == null || numeroPaquetes == 0) {
            numeroPaquetes = 1;
        }
        
        Double costo = calcularCostoEnvio(cpOrigen, cpDestino, numeroPaquetes);
        if (costo == null || costo == 0.0) {
            costo = calcularCostoPorDefecto(cpOrigen, cpDestino, numeroPaquetes);
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put("idEnvio", idEnvio);
        params.put("costoTotal", costo);
        conexionBD.update("envio.actualizar-costo", params);
        conexionBD.commit();
        
        respuesta.setError(false);
        respuesta.setMensaje("Costo recalculado: $" + String.format("%.2f", costo));
        
    } catch (Exception e) {
        conexionBD.rollback();
        respuesta.setError(true);
        respuesta.setMensaje("Error: " + e.getMessage());
    } finally {
        conexionBD.close();
    }
    return respuesta;
}
}