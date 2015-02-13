//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl
package co.edu.icesi.notas.estudiante.action;

import java.sql.Connection;
import java.util.StringTokenizer;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.estudiante.control.ControlEstudiante;

/**
 * Este action es cuando inicia sesión un estudiante..
 */
public class InicioEstudianteAction extends Action {

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
                idUsuario = request.getParameter("cod");

            }
            idUsuario = getCodigo(idUsuario);

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
            ControlEstudiante ce = new ControlEstudiante();
            idUsuario = ce.obtenerUsuarioUnico(idUsuario, conexion);
            ce.cargarInformacion(conexion, idUsuario);
            sesion.setAttribute("estudiante", ce);
            sesion.setAttribute("fechaLimite", ce.obtenerFechaLimiteCierre(conexion));
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
        return mapping.findForward("aErrorEstud");
    }

    public String getCodigo(String codigo) {
        try {
            StringTokenizer token = new StringTokenizer(codigo, "/");
            token.nextToken();
            codigo = token.nextToken();
            char primerLetra = codigo.charAt(0);
            String ultimos = codigo.substring(1);
            boolean esEntero = true;
            try {
                Integer.parseInt(ultimos);
            } catch (NumberFormatException nfe) {
                esEntero = false;
            }
            if (esEntero && (primerLetra == 'z' || primerLetra == 'Z')) {
                return ultimos;
            }
            return codigo;
        } catch (Exception e) {
            System.out.println("Formato de código de estudiante distinto al esperado - Inicio Action");
            return codigo;
        }
    }
}
