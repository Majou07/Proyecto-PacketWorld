/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import dto.Respuesta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Unidad;
import utilidades.Constantes;

/**
 *
 * @author Bruno
 */
public class UnidadImp {
     public static List<Unidad>obtenerUnidades(){
        List<Unidad>unidades = null;
       SqlSession conexionBD = MyBatisUtil.getSession();
       if(conexionBD !=null){
           try{
           unidades=conexionBD.selectList("unidad.obtener-todas");
           conexionBD.close();
           
       }catch(Exception e){
           e.printStackTrace();
       }
       
        }
       return unidades;
    }
    
    public static Respuesta registrarUnidad(Unidad unidad){
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();
        
        if(conexionBD !=null){
            try{
                //Gnerar NII automáticamente
                String nii= unidad.getAnio() + "-" + unidad.getVin().substring(0,4);
                unidad.setNii(nii);
                
                //Estatus inicial: Activo(1)
                unidad.setIdEstatusUnidad(1);
                //si no existe registrar
                int filasAfectadas = conexionBD.insert("unidad.registrar",unidad);
                conexionBD.commit();
                if(filasAfectadas >0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Unidad resgistrada correctamente...");
                }else{
                    respuesta.setMensaje("Lo sentimos la unidad no pudo ser registrada verifique la información");
                }
        }catch(Exception e){
            conexionBD.rollback();
            respuesta.setMensaje(e.getMessage());
            
        }finally{
          conexionBD.close();
          }
        }else{
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }
        return respuesta;
    }
    
    public static Respuesta editarUnidad(Unidad unidad){
    Respuesta respuesta = new Respuesta();
    SqlSession conexionBD =MyBatisUtil.getSession();
    
    if(conexionBD !=null){
        try{
            if(unidad.getIdEstatusUnidad()== 2){
                if(unidad.getMotivoBaja() == null || unidad.getMotivoBaja().trim().isEmpty()){
                     respuesta.setError(true);
                    respuesta.setMensaje("Debe especificar un motivo para dar de baja la unidad");
                    return respuesta; 
                }
            }else{
                //si no está inactiva, el motivo no debe existir
                unidad.setMotivoBaja(null);
            }
            int filasAfectadas = conexionBD.update("unidad.editar", unidad);
            conexionBD.commit();
            if(filasAfectadas>0){
                respuesta.setError(false);
                respuesta.setMensaje("La unidad fue actualizada correctamente");
            }else{
                respuesta.setMensaje("Lo sentimos la información de la unidad no pudo ser actualizada");
            }
        }catch(Exception e){
            conexionBD.rollback();
            respuesta.setError(true);
            respuesta.setMensaje(e.getMessage());
        }finally{
            conexionBD.close();
        }
    }else{
        respuesta.setError(true);
        respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
    }
    
    return respuesta;
}
    
    
    public static Respuesta darBajaUnidad(int idUnidad, String motivoBaja){
    Respuesta respuesta = new Respuesta();
    respuesta.setError(true);
    SqlSession conexionBD = MyBatisUtil.getSession();
    if(conexionBD !=null){
        try{
            
            if(motivoBaja == null || motivoBaja.trim().isEmpty()){
                respuesta.setError(true);
                respuesta.setMensaje("Debe especificar un motivo para dar de baja la unidad");
                return respuesta;
            }
            
            Map<String, Object> params = new HashMap<>();
            params.put("idUnidad",idUnidad);
            params.put("motivoBaja",motivoBaja);
            
            int filasAfectadas = conexionBD.update("unidad.dar-baja", params);
            conexionBD.commit();
            
            if(filasAfectadas>0){
                respuesta.setError(false);
                respuesta.setMensaje("La unidad fue dada de baja correctamente");
            }else{
                respuesta.setError(true);
                respuesta.setMensaje("Lo sentimos no se encontró la unidad");
     
            }
        }catch(Exception e){
            conexionBD.rollback();
            respuesta.setError(true);
            respuesta.setMensaje(e.getMessage());
        }finally{
            conexionBD.close();
        }
    }else{
        respuesta.setError(true);
        respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
    }
    return respuesta;
}
    public static List<Unidad> buscarUnidad(String vin, String marca, String nii) {
    SqlSession conexionBD = MyBatisUtil.getSession();
    List<Unidad> unidades = new ArrayList<>();

    if (conexionBD != null) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("vin", vin);
            params.put("marca", marca);
            params.put("nii", nii);

            unidades = conexionBD.selectList("unidad.buscar-unidad", params);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexionBD.close();
        }
    }

    return unidades;
}

    
}
