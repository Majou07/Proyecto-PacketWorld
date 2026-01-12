package clienteescritorio.dominio;

import clienteescritorio.conexion.ConexionAPI;
import clienteescritorio.pojo.Direccion;
import clienteescritorio.pojo.RespuestaHTTP;
import clienteescritorio.utilidad.Constantes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.List;



public class DireccionImp {
    public static List<Direccion> obtenerInformacionPorCP(String cp) {
    List<Direccion> direcciones = null;
    String url = Constantes.URL_WS + "direccion/consulta-cp/" + cp;
    System.out.println("Consultando URL: " + url); // Debug
    
    RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(url);
    System.out.println("Codigo respuesta: " + respuestaAPI.getCodigo()); // Debug

    if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
        Gson gson = new Gson();
        Type tipoLista = new TypeToken<List<Direccion>>(){}.getType();
        direcciones = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
        System.out.println("Direcciones encontradas: " + (direcciones != null ? direcciones.size() : 0));
    }
    return direcciones;
}
}