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
    
    
}
