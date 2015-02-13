/*
 * Created on 12-feb-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.jefes.action;

import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.jefes.control.*;
import java.sql.*;
import java.util.*;

import javax.servlet.http.*;

import org.apache.struts.action.*;

/**
 * Acción para el inicio de aplicación del jefe
 * 
 * @author lmdiaz TODO To change the template for this generated type comment go
 *         to Window - Preferences - Java - Code Style - Code Templates
 */
public class InicioJefeAction extends Action {
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
            ControlRecursos.iniciarRecursos("jdbc/ds_websiaepre");
            idUsuario = request.getRemoteUser();
            if (idUsuario == null || idUsuario.equals("")) {
                idUsuario = request.getParameter("ced");
            }
            idUsuario = getCedula(idUsuario);
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
            ControlJefe cj = new ControlJefe();
            
            //ffceballos 07/10/2013
            if(ctrlUsuario.getGrupos().contains("SECGEN")){
            idUsuario = cj.obtenerUsuarioUnico2(idUsuario, conexion);
            }else{
            idUsuario = cj.obtenerUsuarioUnico(idUsuario, conexion);
            }
            //fin ffceballos 07/10/2013
            cj.validarJefeDepto(conexion, idUsuario);
            if (cj.getDeptosUsuario().size() == 0) {
                request.setAttribute("noJefe", "noJefe");
                ControlRecursos.liberarRecursos(conexion);
                return mapping.findForward("error");
            }

            cj.cargarInformacion(conexion, idUsuario);
            sesion.setAttribute("jefes", cj);
            sesion.setAttribute("fechaLimite", cj.obtenerFechaLimiteCierre(conexion));
            System.out.println("Inicio de sesión seguimiento a cursos: " + idUsuario);
            ControlRecursos.liberarRecursos(conexion);
            return mapping.findForward("continuar");
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Usuario: " + request.getRemoteUser());
            System.out.println("Descripción:");
            e.printStackTrace();
        } finally {
            ControlRecursos.liberarRecursos(conexion);
        }
        request.setAttribute("mensajeError",
                "<strong>Error: </strong>La aplicación ha recibido parámetros inválidos.");
        return mapping.findForward("aErrorJefe");
    }

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

    private boolean esCedula(String usuario) {
        for (int i = 0; i < usuario.length(); i++) {
            if (Character.isDigit(usuario.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
