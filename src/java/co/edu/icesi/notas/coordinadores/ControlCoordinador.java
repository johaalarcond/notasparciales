/*
 * Created on 12-feb-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.coordinadores;

import co.edu.icesi.notas.jefes.control.*;
import java.sql.*;
import java.util.*;

import co.edu.icesi.notas.*;
import co.edu.icesi.notas.control.ControlRecursos;
import co.edu.icesi.notas.jefes.DeptoJefes;

/**
 * Similar a la clase de ControlProfesores. Se usa en el módulo de coordinadores
 * únicamente.
 * 
 * @author ffceballos
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class ControlCoordinador {

   


    // en esta colección se guardan los cursos que tiene matriculados un alumno.
    public ControlCoordinador() {
        super();
        
    }

    public ArrayList cargarCursos(Connection conexion, String criterioBusqueda){
        
        String sql="SELECT distinct\n" +
"c.descripmat_codigo matcod, c.descripmat_consecutivo matcon,\n" +
" fprebus_descrip_mat (c.descripmat_consecutivo, c.descripmat_codigo, '03', '') descripcion\n" +
" FROM tntp_cursos c   \n" +
"WHERE c.periodo_periodo_acad || c.periodo_consecutivo =fprebus_per_notp('0102','') " +//fprebus_constantes ('002', '0304', '')     \n" +
" AND (c.descripmat_codigo LIKE '%"+criterioBusqueda+"%' || '%'        OR lower(fprebus_descrip_mat (c.descripmat_consecutivo, c.descripmat_codigo, '03', '')) LIKE   '%'||lower('%"+criterioBusqueda+"%') || '%')      \n" +
" AND fprebus_materias (c.descripmat_codigo, '04', '')='08'                  \n" +
"  ORDER BY fprebus_descrip_mat (c.descripmat_consecutivo, c.descripmat_codigo, '03', '')";
    
     Statement stm = null;
     ResultSet rs = null;  
     
     
     
     ArrayList cursos=new ArrayList();
     
     try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            
            while(rs.next()){
      
                 Statement stm2 = null;
                 ResultSet rs2 = null;
                                
            String codigoMateria = rs.getString("matcod");
            int materiaConsecutivo = rs.getInt("matcon");
            String descripcionMateria = rs.getString("descripcion");
            
            String consultaGrupo="select ''||min(C.CURSO_ACTU_GRUPO) grupo from  tntp_cursos c WHERE c.periodo_periodo_acad || c.periodo_consecutivo = fprebus_per_notp('0102','')   AND descripmat_codigo="+codigoMateria; //fprebus_constantes ('002', '0304', '') 
           
                System.out.println(consultaGrupo+"\n");
            
            stm2 = conexion.createStatement();
            rs2 = stm2.executeQuery(consultaGrupo);
            String grupoMinimo="";
            
             while(rs2.next()){
                // System.out.println("Entrooooooo");
             grupoMinimo=rs2.getString("GRUPO");
             }
             
           ControlRecursos.liberarRecursos(rs2, stm2);
                  
            Curso curso=new Curso(materiaConsecutivo, codigoMateria, descripcionMateria,grupoMinimo);
            
               cursos.add(curso);
                
            }
            
     }catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando cursos " + e.getMessage());
            e.printStackTrace();
        }
     
     
        ControlRecursos.liberarRecursos(rs, stm);
        
   
           return cursos;
    
    }
    
    
    public boolean verificarEsquema(Connection conexion, String criterioBusqueda){
    
        
         String [] arreglo=criterioBusqueda.split("-");
        
         String sql="select count(*) CUENTA from TNTP_CURSOS c, TNTP_CLASIF_CATEG cate where C.DESCRIPMAT_CODIGO ='"+arreglo[0]+"' and C.CURSO_ACTU_GRUPO='"+arreglo[1]+"' and C.CONSECUTIVO=CATE.CONSEC_CURSO AND c.periodo_periodo_acad || c.periodo_consecutivo = fprebus_per_notp('0102','')";// fprebus_constantes ('002', '0304', '')";
         System.out.println("consulta "+sql);
         Statement stm = null;
         ResultSet rs = null;
         
        /* String sql2="select distinct  fprebus_descrip_mat (c.descripmat_consecutivo, c.descripmat_codigo, '03', '') descripcion from TNTP_CURSOS c, TNTP_CLASIF_CATEG cate where C.DESCRIPMAT_CODIGO ='"+arreglo[0]+"' and C.CURSO_ACTU_GRUPO='"+arreglo[1]+"' and C.CONSECUTIVO=CATE.CONSEC_CURSO AND c.periodo_periodo_acad || c.periodo_consecutivo = fprebus_constantes ('002', '0304', '')";
         Statement stm2 = null;
         ResultSet rs2 = null;
         */
            
         int cuenta=0;
        
      try {
            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            
            while(rs.next()){
                
              cuenta = rs.getInt("CUENTA");
               
            }
            
            
             
            
            }catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando cursos " + e.getMessage());
            e.printStackTrace();
        }
     
     
        ControlRecursos.liberarRecursos(rs, stm);
     
      if(cuenta>0){
                return true;
                }else {return false;}
        
    }
    
   
}
