
package dominio;

import dto.Respuesta;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Colaborador;
import utilidades.Constantes;

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
            // Validar formato número de personal
            if (!colaborador.getNumeroPersonal().matches(Constantes.REGEX_NUMERO_PERSONAL)) {
                respuesta.setError(true);
                respuesta.setMensaje("El número de personal debe tener el formato EMP001");
            return respuesta;
        }
            // Validar contraseña mínima
            if (colaborador.getContrasena() == null 
            || colaborador.getContrasena().length() < 8) {
                respuesta.setError(true);
                respuesta.setMensaje("La contraseña debe tener mínimo 8 caracteres");
            return respuesta;
            }
            
                
            // 1. Validar CURP
            int curpExiste = conexionBD.selectOne("colaborador.existe-curp", colaborador.getCurp());
            if (curpExiste > 0) {
                respuesta.setError(true);
                respuesta.setMensaje("La CURP ya se encuentra registrada");
                return respuesta;
            }

            // 2. Validar correo
            int correoExiste = conexionBD.selectOne("colaborador.existe-correo", colaborador.getCorreoElectronico());
            if (correoExiste > 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El correo electrónico ya está registrado");
                return respuesta;
            }

            // 3. Validar número de personal
            int numeroExiste = conexionBD.selectOne(
                "colaborador.existe-numero-personal",
                colaborador.getNumeroPersonal()
            );
            if (numeroExiste > 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El número de personal ya está registrado");
                return respuesta;
            }

            // 4. Validar licencia solo si es conductor
           if (colaborador.getIdRol() == 2) {

            if (colaborador.getNumeroLicencia() == null
            || colaborador.getNumeroLicencia().trim().isEmpty()) {
            respuesta.setError(true);
            respuesta.setMensaje("El número de licencia es obligatorio para conductores");
            return respuesta;
          }

            // Validar formato
             if (!colaborador.getNumeroLicencia()
                .matches(Constantes.REGEX_NUMERO_LICENCIA)) {
            respuesta.setError(true);
            respuesta.setMensaje("El número de licencia tiene un formato inválido");
            return respuesta;
         }

            // Validar duplicado
             int licenciaExiste = conexionBD.selectOne(
                "colaborador.existe-numero-licencia",
                colaborador.getNumeroLicencia()
            );

            if (licenciaExiste > 0) {
            respuesta.setError(true);
            respuesta.setMensaje("El número de licencia ya está registrado");
            return respuesta;
        }
    }
            
                //si no existe registrar
                int filasAfectadas = conexionBD.insert("colaborador.registrar",colaborador);
                conexionBD.commit();
                if(filasAfectadas >0){
                    respuesta.setError(false);
                    respuesta.setMensaje("Registro del colaborador " + colaborador.getNombre() +  ", agregado correctamente...");
                }else{
                    respuesta.setError(true);
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
    
    public static Respuesta editarColaborador(Colaborador colaborador){
    Respuesta respuesta = new Respuesta();
    SqlSession conexionBD =MyBatisUtil.getSession();
    if(conexionBD !=null){
        try{
            
                // Validar contraseña mínima
            if (colaborador.getContrasena() == null
                    || colaborador.getContrasena().length() < 8) {

                respuesta.setError(true);
                respuesta.setMensaje("La contraseña debe tener mínimo 8 caracteres");
                return respuesta;
            }

            // Validaciones para conductores
            if (colaborador.getIdRol() == 2) {

                // Licencia obligatoria
                if (colaborador.getNumeroLicencia() == null
                        || colaborador.getNumeroLicencia().trim().isEmpty()) {

                    respuesta.setError(true);
                    respuesta.setMensaje("El número de licencia es obligatorio para conductores");
                    return respuesta;
                }

                // Validar formato licencia
                if (!colaborador.getNumeroLicencia()
                        .matches(Constantes.REGEX_NUMERO_LICENCIA)) {

                    respuesta.setError(true);
                    respuesta.setMensaje("El número de licencia tiene un formato inválido");
                    return respuesta;
                }
            }

            
            int filasAfectadas = conexionBD.update("colaborador.editar", colaborador);
            conexionBD.commit();
            if(filasAfectadas>0){
                respuesta.setError(false);
                respuesta.setMensaje(" Informacion del colaborador "
                + colaborador.getNombre() + ", actualizada correctamente ");
            }else{
                respuesta.setMensaje("Lo sentimos:( la información no pudo ser actualizada");
            }
        }catch(Exception e){
            conexionBD.rollback();
            respuesta.setError(true);
           
            // Detectar restricciones UNIQUE
            if(e.getMessage().contains("uq_curp")){
                respuesta.setMensaje("La CURP ya está registrada");

            }else if(e.getMessage().contains("uq_correo_colaborador")){
                respuesta.setMensaje("El correo electrónico ya está registrado");

            }else if(e.getMessage().contains("uq_numero_licencia")){
                respuesta.setMensaje("El número de licencia ya está registrado");

            }else{
                respuesta.setMensaje(e.getMessage());
            }
        }finally{
            conexionBD.close();
        }
    }else{
        respuesta.setError(true);
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
                respuesta.setError(true);
                respuesta.setMensaje("Lo sentimos :( la información del colaborador no se pudo eliminar");
     
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
   public static List<Colaborador> obtenerColaboradoresPorNombre(String nombre) {
    List<Colaborador> colaboradores = null;
    SqlSession conexionBD = MyBatisUtil.getSession();

    if (conexionBD != null) {
        try {

            if (nombre == null) {
                return Collections.emptyList();
            }

            String filtroLimpio = nombre.trim().replaceAll("\\s+", " ");

            if (filtroLimpio.isEmpty()) {
                return Collections.emptyList();
            }

            Map<String, Object> params = new HashMap<>();
            params.put("filtro", filtroLimpio);

            colaboradores = conexionBD.selectList(
                "colaborador.obtener-por-nombre",
                params
            );

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexionBD.close();
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
    public static Colaborador obtenerPorId(int idColaborador) {
    SqlSession session = MyBatisUtil.getSession();
    Colaborador colaborador = null;
    try {
        colaborador = session.selectOne("colaborador.obtener-por-id", idColaborador);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        session.close();
    }
    return colaborador;
}

    
    
    
    public static List<pojo.Rol> obtenerRoles() {
        List<pojo.Rol> roles = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                roles = conexionBD.selectList("colaborador.obtener-roles");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return roles;
    }
    
    
   public static Respuesta asignarUnidad(int idColaborador, Integer idUnidad) {
    Respuesta resp = new Respuesta();
    SqlSession conexionBD = MyBatisUtil.getSession();

    if (conexionBD == null) {
        resp.setError(true);
        resp.setMensaje("No se pudo establecer conexión con la base de datos");
        return resp;
    }

    try {
        Map<String, Object> params = new HashMap<>();
        params.put("idColaborador", idColaborador);
        params.put("idUnidad", idUnidad);

        int filas = conexionBD.update("colaborador.asignar-unidad", params);
        conexionBD.commit();

        if (filas > 0) {
            resp.setError(false);
            resp.setMensaje("Unidad asignada correctamente");
        } else {
            resp.setError(true);
            resp.setMensaje("No se encontró el colaborador o no se pudo asignar la unidad");
        }
    } catch (Exception e) {
        resp.setError(true);

        // Detectar violación de restricción única
        if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
            resp.setMensaje("La unidad seleccionada ya está asignada a otro colaborador.");
        } else {
            resp.setMensaje("Error al asignar unidad: " + e.getMessage());
        }
    } finally {
        conexionBD.close();
    }

    return resp;
}


    
    
    public static Respuesta guardarFoto(int idColaborador, byte[] foto){
    Respuesta respuesta = new Respuesta();
    respuesta.setError(true);
    SqlSession conexionBD = MyBatisUtil.getSession();
    if(conexionBD != null){
        try{
            Colaborador colaborador= new Colaborador();
            colaborador.setIdColaborador(idColaborador);
            colaborador.setFoto(foto);
            int filasAfectadas = conexionBD.update("colaborador.guardar-foto", colaborador);
            conexionBD.commit();
            if(filasAfectadas>0){
                respuesta.setError(false);
                respuesta.setMensaje("La fotografia del colaborador (a) ha sido guardada correctamente");
            }else{
                respuesta.setMensaje("Lo sentimos :( la fotografia no ha sido guardada");
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
    
  
    
    public static Colaborador obtenerFoto(int idColaborador){
        Colaborador colaborador = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                colaborador = conexionBD.selectOne("colaborador.obtener-foto",idColaborador);
                conexionBD.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return colaborador;
    }
    
    
}
