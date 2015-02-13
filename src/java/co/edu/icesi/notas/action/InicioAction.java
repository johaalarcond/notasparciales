//Created by MyEclipse Struts
//XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl
package co.edu.icesi.notas.action;

import java.util.*;

import co.edu.icesi.notas.Mes;
import co.edu.icesi.notas.control.*;
import java.net.Inet4Address;
import java.net.InetAddress;

import javax.servlet.http.*;
import java.sql.*;

import org.apache.struts.action.*;

/**
 * MyEclipse Struts Creation date: 06-09-2005 Acción de inicio a toda la
 * apliación XDoclet definition:
 * 
 * @struts:action
 */
public class InicioAction extends Action {

    // --------------------------------------------------------- Instance
    // Variables
    // --------------------------------------------------------- Methods
    /**
     * Method execute
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        String idUsuario = "";
        Connection conexion = null;
        try {
            System.out.println(request.getRemoteUser());
            ControlRecursos.iniciarRecursos("jdbc/ds_websiaepre");
            idUsuario = request.getRemoteUser();
            ControlProfesores cp = new ControlProfesores();
            if (idUsuario == null || idUsuario.equals("")) {
                idUsuario = request.getParameter("ced");
            }
            idUsuario = getCedula(idUsuario);
            // por ahora en comentario:

            HttpSession sesion = ControlSesion.iniciarSesion(request, idUsuario);            
            conexion = ControlRecursos.obtenerConexion();
            ControlUsuario ctrlUsuario = new ControlUsuario(idUsuario);



            ctrlUsuario.cargarRoles(conexion);
            sesion.setAttribute("ctrlUsuario", ctrlUsuario);
            sesion.setAttribute("RPROFESORES", Boolean.valueOf(ctrlUsuario.getGrupos().contains("PROFESORES")));
            sesion.setAttribute("RPREGRADO", Boolean.valueOf(ctrlUsuario.getGrupos().contains("PREGRADO")));
            sesion.setAttribute("RJEFDEPRE", Boolean.valueOf(ctrlUsuario.getGrupos().contains("JEFDEPRE")));
            sesion.setAttribute("RDIRPROG", Boolean.valueOf(ctrlUsuario.getGrupos().contains("DIRPROG")));
            sesion.setAttribute("RSECGEN", Boolean.valueOf(ctrlUsuario.getGrupos().contains("SECGEN")));
            
            //adicionado por ffceballos
            sesion.setAttribute("RCOORDINMAT", Boolean.valueOf(ctrlUsuario.getGrupos().contains("COORDINMAT")));
            sesion.setAttribute("RSECREMAT", Boolean.valueOf(ctrlUsuario.getGrupos().contains("SECREMAT")));
          
            //ffceballos 21/04/2014
            sesion.setAttribute("RSEGUIMIENTO", Boolean.valueOf(ctrlUsuario.getGrupos().contains("SEGUIMIENTO")));
            
            
            
            String tiposCategorias=ctrlUsuario.tiposCategorias(conexion);
            String []categorias=tiposCategorias.split("-");
            
            if(categorias.length>=2){
            sesion.setAttribute("codigoParciales",categorias[0]);
            sesion.setAttribute("codigoFinales",categorias[1]);
            }
           //sesion.setAttribute("cedulaUsuario",idUsuario);
            InetAddress ip;
              try { 
		ip = InetAddress.getLocalHost();
		String ipServer=ip.getHostAddress();
                sesion.setAttribute("ipServer", ipServer); 
	  } catch (Exception e) { 
		e.printStackTrace(); 
	  }
            
             //fin ffceballos
            idUsuario = cp.obtenerUsuarioUnico(idUsuario, conexion).toUpperCase();
           
            
            
            //ffceballos
            if(ctrlUsuario.getGrupos().contains("COORDINMAT")||ctrlUsuario.getGrupos().contains("SECREMAT")){
           // System.out.println("Si es cooordinnnnnn");
             cp.cargarTodo(conexion, idUsuario,true);
            }else{
            
             cp.cargarTodo(conexion, idUsuario,false);
            }          
            
            //fin ffceballos
            
            sesion.setAttribute("active", "active");
            java.util.Date fechaInicioCierre = cp.obtenerFechaInicioCierre(conexion);
            sesion.setAttribute("fechaLimite", cp.obtenerFechaLimiteCierre(conexion));
            sesion.setAttribute("fechaInicio", fechaInicioCierre);
            sesion.setAttribute("fechaActual", cp.obtenerFechaActual(conexion));
            // Para registro de asistencia
            java.util.Date fechaInicioClases = cp.obtenerFechaInicioClases(conexion);
            sesion.setAttribute("fechaInicioClases", fechaInicioClases);
            sesion.setAttribute("profesores", cp);
            sesion.setAttribute("curso", null);
            sesion.setAttribute("padre", null);
            sesion.setAttribute("estructura", null);
            sesion.setAttribute("porcentajeMinimoAsistencia", new Integer(
                    obtenerPorcentajeMinimoAsistencia(conexion)));
            GregorianCalendar calInicioClases = new GregorianCalendar();
            calInicioClases.setTime(fechaInicioClases);

            GregorianCalendar calInicioCierre = new GregorianCalendar();
            calInicioCierre.setTime(fechaInicioCierre);

            sesion.setAttribute("meses", cargarMesesSemestre(conexion,
                    calInicioClases.get(Calendar.MONTH) + 1, calInicioCierre.get(Calendar.MONTH) + 1));
            System.out.println("Inicio de sesión notas parciales: " + idUsuario);
            ControlRecursos.liberarRecursos(conexion);
            return mapping.findForward("exito");
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Usuario: " + request.getRemoteUser());
            System.out.println("Descripción:");
            e.printStackTrace();
        } finally {
            ControlRecursos.liberarRecursos(conexion);
        }
        return mapping.findForward("errorAplicacion");
    }

    /**
     * Obtiene la cédula o identificación del profesor a partir de lo que se
     * pasa por pàrámetro
     *
     * @param cadena
     * @return
     */
    public String getCedula(String cadena) {
        // asumiendo que el login del profesor es solo la cedula y que por ahora
        // solo es para profesores
        // capturo excepcion?
        try {
            StringTokenizer token = new StringTokenizer(cadena, "/");
            token.nextToken();
            String cad = token.nextToken();
            if (esCedula(cad) && (cad.charAt(0) == 'z' || cad.charAt(0) == 'Z')) {
                return cad.substring(1);
            }
            return cad;
            // return token.nextToken();
        } catch (Exception e) {
            System.out.println("El formato de cedula no corresponde");
            return cadena;
        }
    }

    /**
     * Valida si lo que se envía por parámetro es ya una cédula o no
     *
     * @param usuario
     * @return
     */
    private boolean esCedula(String usuario) {
        for (int i = 0; i < usuario.length(); i++) {
            if (Character.isDigit(usuario.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Carga los meses en los que cuales se va a desarrollar el semestre actual
     *
     * @param conexion
     * @param inicioClases
     * @param inicioCierre
     * @return
     */
    public ArrayList cargarMesesSemestre(Connection conexion, int inicioClases,
            int inicioCierre) {
        ArrayList meses = new ArrayList();
        Statement stm = null;
        ResultSet rs = null;
        try {
            stm = conexion.createStatement();
            for (int i = inicioClases; i <= inicioCierre; i++) {
                String cadena = String.valueOf(i);
                if (i < 10) {
                    cadena = "0" + cadena;
                }
                String sql = "select initcap(fprecal_nombremes('" + cadena
                        + "','E')) from dual";
                rs = stm.executeQuery(sql);
                rs.next();
                meses.add(new Mes(cadena, rs.getString(1)));
            }
            meses.trimToSize();
            ControlRecursos.liberarRecursos(rs, stm);
            return meses;
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando meses del semestre "
                    + e.getMessage());
            e.printStackTrace();
            ControlRecursos.liberarRecursos(rs, stm);
            return meses;
        }
    }

    /**
     * Obtiene el mínimo porcentaje de asistencia para que un estudiante pueda
     * pasar un curso.
     *
     * @param conexion
     * @return
     */
    public int obtenerPorcentajeMinimoAsistencia(Connection conexion) {
        Statement stm = null;
        ResultSet rs = null;
        int porcentajeMinimoAsistencia = 100;
        try {
            stm = conexion.createStatement();
            String sql = "select fprebus_constantes('136','04','') from dual";
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                porcentajeMinimoAsistencia = 100 - rs.getInt(1);
                ControlRecursos.liberarRecursos(rs, stm);
                return porcentajeMinimoAsistencia;
            } else {
                ControlRecursos.liberarRecursos(rs, stm);
                return porcentajeMinimoAsistencia;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error obteniendo el porcentaje mínimo de asistencia "
                    + e.getMessage());
            e.printStackTrace();
            ControlRecursos.liberarRecursos(rs, stm);
            return 100;
        }
    }
}
