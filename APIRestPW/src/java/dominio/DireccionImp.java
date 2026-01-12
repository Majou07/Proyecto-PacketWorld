package dominio;

import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Direccion;


public class DireccionImp {
    public static List<Direccion> obtenerPorCP(String cp) {
        List<Direccion> resultado = null;
        SqlSession conexion = MyBatisUtil.getSession(); // Abre la conexión con MyBatis

        if (conexion != null) {
            try {
                resultado = conexion.selectList("DireccionMapper.obtenerInformacionPorCP", cp);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexion.close(); 
            }
        }
        return resultado;
    }
}
