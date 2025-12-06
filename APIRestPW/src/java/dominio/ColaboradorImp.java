/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import dto.Respuesta;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Colaborador;
import utilidades.Constantes;

/**
 *
 * @author Bruno
 */
public class ColaboradorImp {
    
    public static List<Colaborador>obtenerColaboradores(){
        List<Colaborador>colaboradores = null;
       SqlSession conexionBD = MyBatisUtil.getSession();
       if(conexionBD !=null){
           try{
           colaboradores=conexionBD.selectList("colaborador.obtener-todos");
           conexionBD.close();
           
       }catch(Exception e){
           e.printStackTrace();
       }
       
        }
       return colaboradores;
    }
    
    public static Respuesta registrarColaborador(Colaborador colaborador){
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();
        
        if(conexionBD !=null){
            try{
                
                //si no existe registrar
                int filasAfectadas = conexionBD.insert("colaborador.registrar",colaborador);
                conexionBD.commit();
                if(filasAfectadas >0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Registro del colaborador " + colaborador.getNombre() +  ", agregado correctamente...");
                }else{
                    respuesta.setError(false);
                    respuesta.setMensaje("Lo sentimos la informacion no pudo ser guardada, por favor verifique los datos");
                }
        }catch(Exception e){
            respuesta.setError(true);
            respuesta.setMensaje(e.getMessage());
            
        }
        }else{
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }
        return respuesta;
    }
    
    public static Respuesta editarColaborador(Colaborador colaborador){
    Respuesta respuesta = new Respuesta();
    SqlSession conexionBD =MyBatisUtil.getSession();
    if(conexionBD !=null){
        try{
            int filasAfectadas = conexionBD.update("colaborador.editar", colaborador);
            conexionBD.commit();
            if(filasAfectadas>0){
                respuesta.setError(false);
                respuesta.setMensaje(" Informacion del colaborador "
                + colaborador.getNombre() + ", actualizada correctamente ");
            }else{
                respuesta.setMensaje("Lo sentimos:( la información no pudo ser actualizada");
            }
            conexionBD.close();
        }catch(Exception e){
            respuesta.setMensaje(e.getMessage());
        }
    }else{
        respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
    }
    
    return respuesta;
}
    
    
    public static Respuesta eliminarColaborador(int idColaborador){
    Respuesta respuesta = new Respuesta();
    respuesta.setError(true);
    SqlSession conexionBD = MyBatisUtil.getSession();
    if(conexionBD !=null){
        try{
            int filasAfectadas = conexionBD.delete("colaborador.eliminar", idColaborador);
            conexionBD.commit();
            if(filasAfectadas>0){
                respuesta.setError(false);
                respuesta.setMensaje("La información del colaborador fue eliminada correctamente");
            }else{
                respuesta.setMensaje("Lo sentimos :( la información del colaborador no se pudo eliminar");
                conexionBD.close();
            }
        }catch(Exception e){
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }
    }
    return respuesta;
}
    public static List<Colaborador> obtenerColaboradoresPorNombre(String nombre){
    List<Colaborador> colaboradores = null;
    SqlSession conexionBD = MyBatisUtil.getSession();
    if(conexionBD != null){
        try {
            colaboradores = conexionBD.selectList("colaborador.obtener-por-nombre", nombre);
            conexionBD.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    return colaboradores;
}
    
    public static List<Colaborador> buscarPorNumeroPersonal(String numeroPersonal) {
        SqlSession conexionBD = MyBatisUtil.getSession();
        List<Colaborador> lista = null;

        if (conexionBD != null) {
            try {
                lista = conexionBD.selectList("colaborador.obtener-por-noPersonal", numeroPersonal);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return lista;
    }
    
    public static List<Colaborador> buscarPorRol(int idRol) {
        SqlSession conexionBD = MyBatisUtil.getSession();
        List<Colaborador> lista = null;

        if (conexionBD != null) {
            try {
                lista = conexionBD.selectList("colaborador.obtener-por-rol",idRol);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return lista;
    }
    
    
}
