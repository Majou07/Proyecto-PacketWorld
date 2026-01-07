/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;


import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.CatalogoCP;

public class CatalogoCPImp {

    public static List<CatalogoCP> buscarPorCodigoPostal(String codigoPostal) {
        SqlSession conexionBD = MyBatisUtil.getSession();
        List<CatalogoCP> resultados = null;

        if (conexionBD != null) {
            try {
                resultados = conexionBD.selectList("catalogoCP.buscar-por-cp", codigoPostal);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return resultados;
    }
}
