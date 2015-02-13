/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.icesi.notas.control;

import java.sql.*;
import java.util.*;

/**
 *
 * @author 1130608864
 */
public class ControlUsuario {

    private String idUsuario;
    private List grupos;

    public ControlUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
        this.grupos = new ArrayList();
    }

    public void cargarRoles(Connection conexion) {
        String usuarioEstudiante = "";
        String usuarioProfesor = "";
        Statement stm = null;
        ResultSet rs = null;
        try {

            stm = conexion.createStatement();
            rs = stm.executeQuery("select fintbus_usuario_portal('" + this.idUsuario
                    + "','20') from dual");
            if (rs.next()) {
                usuarioEstudiante = rs.getString(1);
            }
            ControlRecursos.liberarRecursos(rs, stm);
            if (!usuarioEstudiante.trim().equals("") && !usuarioEstudiante.trim().equals("0")) {
                this.grupos.add("PREGRADO");
                
                
                //FFCEBALLOS se colocó esto aquí porque la secretaria de matemáticas es estudiante pero necesita el rol de coordinadora en matematicas
                
                  Statement stm2 = conexion.createStatement();
              ResultSet rs2 = stm2.executeQuery("SELECT DISTINCT *"
                      + " FROM tntp_roles_notpa rol"
                      + " WHERE rol.cedula='"
                      + this.idUsuario
                      + "' "
                      + "and rol.nombre_rol='COORDINMAT'"
                      + " and rol.activo='S'");
               
                if (rs2.next()) {  
                    this.grupos.add("COORDINMAT");
                }
                  ControlRecursos.liberarRecursos(rs2, stm2);
                
                //fin ffceballos
                
            } else {
                stm = conexion.createStatement();
                rs = stm.executeQuery("select fintbus_usuario_portal('" + this.idUsuario
                        + "','8') from dual");
                if (rs.next()) {
                    usuarioProfesor = rs.getString(1);
                }
                ControlRecursos.liberarRecursos(rs, stm);
                if (!usuarioProfesor.trim().equals("") && !usuarioProfesor.trim().equals("0") && usuarioProfesor.equalsIgnoreCase(this.idUsuario)) {
                    this.grupos.add("PROFESORES");
                }

                stm = conexion.createStatement();
                rs = stm.executeQuery("SELECT DISTINCT 's' "
                        + "  FROM rol_usuarios ru, empleado e "
                        + " WHERE e.cedula = '"
                        + this.idUsuario
                        + "'  AND ru.nombre_rol IN ('SECGEN', 'DIRPROGR', 'JEFDEPRE') "
                        + "   AND ru.nombre_usuario = e.usuario ");


                if (rs.next()) {
                    ControlRecursos.liberarRecursos(rs, stm);
                    stm = conexion.createStatement();
                    rs = stm.executeQuery("select count (dp.codigo) d, count (pr.codigo) p, sum (decode (ru.nombre_rol, 'SECGEN', 1, 0)) r "
                            + "  from (   tbas_deptos_acad dp "
                            + "        full join "
                            + "           tbas_programas pr "
                            + "        on dp.usuario = pr.usuario) "
                            + "       full join rol_usuarios ru "
                            + "          on ru.nombre_usuario = dp.usuario "
                            + "          or  ru.nombre_usuario = pr.usuario "
                            + "       join empleado e "
                            + "          on dp.usuario = e.usuario "
                            + "          or  pr.usuario = e.usuario "
                            + "          or  ru.nombre_usuario = e.usuario "
                            + " where e.cedula = '" + this.idUsuario + "' ");
                   
                    
                 
                    
                    rs.next();
                    /*Poner aqui el nuevo rol*/
                    int dpto = rs.getInt(1);
                    int prog = rs.getInt(2);
                    int secg = rs.getInt(3);
                    if (dpto > 0) {
                        this.grupos.add("JEFDEPRE");
                    } else if (prog > 0) {
                        this.grupos.add("DIRPROG");
                    } else if (secg > 0) {
                        this.grupos.add("SECGEN");
                    }
                }                
                ControlRecursos.liberarRecursos(rs, stm);
                
                //adicionado por ffceballos
              Statement  stm2 = conexion.createStatement();
              ResultSet rs2 = stm2.executeQuery("SELECT DISTINCT *"
                      + " FROM tntp_roles_notpa rol"
                      + " WHERE rol.cedula='"
                      + this.idUsuario
                      + "' "
                      + "and rol.nombre_rol='COORDINMAT'"
                      + " and rol.activo='S'"); 
              
           
                
                if (rs2.next()) {                    
                this.grupos.add("COORDINMAT");
                }
                
                  ControlRecursos.liberarRecursos(rs2, stm2);
                                        
                  //fin ffceballos
            }
            // ffceballos 21/04/2014
          
              Statement stm3 = conexion.createStatement();
              ResultSet rs3 = stm3.executeQuery("SELECT DISTINCT *"
                      + " FROM tntp_roles_notpa rol"
                      + " WHERE rol.cedula='"
                      + this.idUsuario
                      + "' "
                      + "and rol.nombre_rol='SEGUIMIENTO'"
                      + " and rol.activo='S'");
               
                if (rs3.next()) {  
                    this.grupos.add("SEGUIMIENTO");
                     //this.grupos.add("SECGEN");
                }
                  ControlRecursos.liberarRecursos(rs3, stm3);
            
            //fin ffceballos
        } catch (SQLException e) {
            System.out.println("Error: " + ControlUsuario.class.getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando los grupos del usuario "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
    }

    public List getGrupos() {
        return grupos;
    }

    public String getIdUsuario() {
        return idUsuario;
    }
    
    
     /**
     * Recupera las categorías que se deben bloquear 15 días despues de que se registren las notas
     * y las agrega a la cadena categorias
     *
     * @param conexion
     *            Conexión a la base de datos.
     */
    public String tiposCategorias(Connection conexion){
    String categorias="";
    
    String sql = "select c.valor_varchar2 from tbas_constantes  c where c.codigo=215 or c.codigo=216";
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            while (rs.next()) {                
                String categoria = rs.getString("VALOR_VARCHAR2");                
                categorias+=categoria+"-";
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error categorías a bloquear "
                    + e.getMessage());
            e.printStackTrace();
        }
        ControlRecursos.liberarRecursos(rs, stm);
    
    return categorias;
    
    }  
}
