/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clienteescritorio.dominio;

import clienteescritorio.conexion.ConexionAPI;
import clienteescritorio.pojo.CatalogoCP;
import clienteescritorio.pojo.RespuestaHTTP;
import clienteescritorio.utilidad.Constantes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bruno
 */
public class CatalogoCPImp {
    
    public static List<CatalogoCP> buscarPorCodigoPostal(String codigoPostal) { 
        List<CatalogoCP> resultados = new ArrayList<>(); 
        String url = Constantes.URL_WS + "catalogoCP/obtener-datos?codigo=" + codigoPostal; 
        
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(url); 
        if(respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) { 
            Gson gson = new Gson(); 
            Type tipoLista = new TypeToken<List<CatalogoCP>>(){}.getType(); 
            resultados = gson.fromJson(respuestaAPI.getContenido(), tipoLista); 
        } else { 
            System.out.println("Error al consultar catálogo CP: " + respuestaAPI.getContenido()); 
        } 
        return resultados; 
    }
    
}
