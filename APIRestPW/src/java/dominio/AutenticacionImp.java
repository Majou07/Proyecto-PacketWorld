/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import dto.RSAutenticacionColaborador;
import java.util.HashMap;
import java.util.LinkedHashMap;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Colaborador;

/**
 *
 * @author Bruno
 */
public class AutenticacionImp {
    
     public static RSAutenticacionColaborador autenticarAdministracion(String numeroPersonal, String contrasena){
        //TODO LLamada al Mapper y procesamiento de la informacion del retorno
        
        RSAutenticacionColaborador respuesta = new RSAutenticacionColaborador();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD !=null){
            //Estoy en flujo principal todavia
            try{
                HashMap<String,String> parametros =new LinkedHashMap<>();
                parametros.put("numeroPersonal", numeroPersonal);
                parametros.put("contrasena", contrasena);
                Colaborador colaborador = conexionBD.selectOne("autenticacion.colaborador", parametros);
                if(colaborador !=null){
                    //Flujo principal credenciales correctas
                    respuesta.setError(false);
                    respuesta.setMensaje("Credenciales correctas del usuario"+colaborador.getNombre());
                    respuesta.setColaborador(colaborador);
                }else{
                    //Flujo alterno 3 Credenciales incorrectas
                    respuesta.setError(true);
                    respuesta.setMensaje("Credenciales incorrectas, "+"por favor verifique la informacion");
                    
                }
            }catch(Exception e){
                //Flujo alterno 2 error en la ejecucion del Query
                respuesta.setError(true);
                respuesta.setMensaje(e.getMessage());
            }
        }else{
            //Flujo alterno 1 sin conexion a base de datos
            respuesta.setMensaje("Lo sentimos por el momento no hay conexion a los datos...");
            
        }
        return respuesta;
    }
    
    
    
}
