package dominio;

import dto.Respuesta;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Paquete;
import utilidades.Constantes;

public class PaqueteImp {
    public static List<Paquete> obtenerPaquetesPorEnvio(int idEnvio) {
        List<Paquete> lista = null;
        SqlSession conexion = MyBatisUtil.getSession();
        
        if(conexion != null){
            try {
                lista = conexion.selectList("paquete.obtener-por-envio", idEnvio);
            } finally { conexion.close(); }
        }
        
        return lista;
    }

    public static Respuesta registrar(Paquete paquete){
        Respuesta resp = new Respuesta();
        SqlSession conexion = MyBatisUtil.getSession();
        if(conexion != null){
            
            try {
                conexion.insert("paquete.registrar", paquete);
                conexion.commit();
                resp.setError(false);
                resp.setMensaje("Paquete agregado");
                
            } catch(Exception e) {
                resp.setError(true);
                resp.setMensaje(e.getMessage());
                
            } finally { conexion.close(); }
            
        } else { resp.setError(true); resp.setMensaje(Constantes.MSJ_ERROR_BD); }
        
        return resp;
    }
}