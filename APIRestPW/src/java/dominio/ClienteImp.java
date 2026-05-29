package dominio;
import dto.Respuesta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Cliente;
import utilidades.Constantes;

public class ClienteImp {
    public static List<Cliente> obtenerClientes() {
        List<Cliente> lista = null;
        SqlSession conexion = MyBatisUtil.getSession();
        if(conexion != null){
            try {
                lista = conexion.selectList("cliente.obtener-todos");
            } finally { conexion.close(); }
        }
        return lista;
    }

    public static Respuesta registrar(Cliente cliente){
        Respuesta resp = new Respuesta();
        SqlSession conexion = MyBatisUtil.getSession();
        if(conexion != null){
            try {
                conexion.insert("cliente.registrar", cliente);
                conexion.commit();
                resp.setError(false);
                resp.setMensaje("Cliente registrado");
            } catch(Exception e) {
                resp.setError(true);
                resp.setMensaje(e.getMessage());
            } finally { conexion.close(); }
        } else { resp.setError(true); resp.setMensaje(Constantes.MSJ_ERROR_BD); }
        return resp;
    }
    
    public static Respuesta editar(Cliente cliente) {
    Respuesta resp = new Respuesta();
    SqlSession conexion = MyBatisUtil.getSession();
    if (conexion != null) {
        try {
            int filas = conexion.update("cliente.editar", cliente);
            conexion.commit();
            if (filas > 0) {
                resp.setError(false);
                resp.setMensaje("Cliente actualizado correctamente");
            }
            } catch (Exception e) {
                resp.setError(true);
                resp.setMensaje(e.getMessage());
            } finally { conexion.close(); }
        }
        return resp;
    }

    public static Respuesta eliminar(int idCliente) {
        Respuesta resp = new Respuesta();
        SqlSession conexion = MyBatisUtil.getSession();
        if (conexion != null) {
            try {
                int filas = conexion.delete("cliente.eliminar", idCliente);
                conexion.commit();
                resp.setError(false);
                resp.setMensaje("Cliente eliminado");
            } catch (Exception e) {
                resp.setError(true);
                resp.setMensaje(e.getMessage());
            } finally { conexion.close(); }
        }
        return resp;
    }
    public static List<Cliente> buscarPorNombre(String valor) {
    List<Cliente> lista = new ArrayList<>();
    SqlSession conexion = MyBatisUtil.getSession();
    if (conexion != null) {
        try {
            // Depuración: imprime lo que recibe el servidor
            System.out.println("API - Valor recibido en búsqueda: [" + valor + "]");
            lista = conexion.selectList("cliente.buscarPorNombre", valor);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexion.close();
        }
    }
    return lista != null ? lista : new ArrayList<>();
}

public static List<Cliente> buscarPorTelefono(String valor) {
    List<Cliente> lista = new ArrayList<>();
    SqlSession conexion = MyBatisUtil.getSession();
    if (conexion != null) {
        try {
            lista = conexion.selectList("cliente.buscarPorTelefono", valor);
            System.out.println("Valor recibido en búsqueda: [" + valor + "]");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexion.close();
        }
    }
    return lista != null ? lista : new ArrayList<>();
}

public static List<Cliente> buscarPorCorreo(String valor) {
    List<Cliente> lista = new ArrayList<>();
    SqlSession conexion = MyBatisUtil.getSession();
    if (conexion != null) {
        try {
            lista = conexion.selectList("cliente.buscarPorCorreo", valor);
            System.out.println("Valor recibido en búsqueda: [" + valor + "]");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexion.close();
        }
    }
    return lista != null ? lista : new ArrayList<>();
}
}
