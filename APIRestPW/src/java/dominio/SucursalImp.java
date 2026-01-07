
package dominio;

import dto.Respuesta;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Sucursal;
import utilidades.Constantes;


public class SucursalImp {
    
    public static List<Sucursal>obtenerSucursales(){
        List<Sucursal>sucursales = null;
       SqlSession conexionBD = MyBatisUtil.getSession();
       if(conexionBD !=null){
           try{
           sucursales=conexionBD.selectList("sucursal.obtener-todas");
           conexionBD.close();
           
       }catch(Exception e){
           e.printStackTrace();
       }
       
        }
       return sucursales;
    }
    
   public static Respuesta registrarSucursal(Sucursal sucursal){
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();
        
        if(conexionBD !=null){
            try{
                
                //si no existe registrar
                int filasAfectadas = conexionBD.insert("sucursal.registrar",sucursal);
                conexionBD.commit();
                if(filasAfectadas >0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Registro de la sucursal " + sucursal.getNombreCorto()  +  " , realizado correctamente...");
                }else{
                    respuesta.setError(false);
                    respuesta.setMensaje("Lo sentimos la informacion no pudo ser guardada, por favor verifique los datos");
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
   
   // 3. Editar sucursal (NO se modifica código ni estatus)
    public static Respuesta editarSucursal(Sucursal sucursal) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                int filasAfectadas = conexionBD.update("sucursal.editar", sucursal);
                conexionBD.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Sucursal " + sucursal.getNombreCorto() + " actualizada correctamente");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se encontró la sucursal");
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
    // 4. Dar de baja sucursal (estatus → inactiva)
    public static Respuesta darBajaSucursal(String codigoSucursal) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                
                int filasAfectadas = conexionBD.update("sucursal.dar-baja", codigoSucursal);
                conexionBD.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Sucursal dada de baja correctamente");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se encontró la sucursal");
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
    
    public static Respuesta reactivarSucursal(String codigoSucursal) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                
                int filasAfectadas = conexionBD.update("sucursal.reactivar", codigoSucursal);
                conexionBD.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Sucursal reactivada correctamente");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se encontró la sucursal");
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
