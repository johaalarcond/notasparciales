//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl
package co.edu.icesi.notas.action;

import java.sql.Connection;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import co.edu.icesi.notas.Curso;
import co.edu.icesi.notas.basica.Subject;
import co.edu.icesi.notas.control.*;

/**
 * MyEclipse Struts Creation date: 06-12-2007
 * 
 * XDoclet definition:
 * 
 * @struts:action validate="true"
 */
public class IrCopiaEsquemaAnteriorAction extends Action {

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
            HttpSession sesion = ControlSesion.obtenerSesion(request);
            if (sesion != null) {
                idUsuario = ControlSesion.getUsuario(request);
                if (sesion.getAttribute("curso") == null) {
                    request.setAttribute(
                            "mensajeError",
                            "Ha vuelto a la página de listado de cursos pero no ha escogido un curso, por favor escoja un curso del listado inicial.");
                    return mapping.findForward("errorAplicacion");
                }

                ControlProfesores cp = (ControlProfesores) sesion.getAttribute("profesores");
                Curso curso = (Curso) sesion.getAttribute("curso");

                if (cp == null || curso == null) {
                    request.setAttribute("mensajeError",
                            "La aplicación ha recibido parámetros inválidos.");
                    return mapping.findForward("errorAplicacion");
                }

                conexion = ControlRecursos.obtenerConexion();
                ControlCopiaEsquemas controlCopia = new ControlCopiaEsquemas();
                controlCopia.obtenerCursoAnterior(cp, conexion, curso);

                if (controlCopia.getCursoAnterior() == null) {
                    ActionErrors errores = new ActionErrors();
                    errores.add("esquemaAnterior", new ActionError(
                            "esquemaAnterior.error"));
                    saveErrors(request, errores);
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("regresar");
                }

                Subject sub = new Subject(controlCopia.getCursoAnterior());
                sesion.setAttribute("controlCopia", controlCopia);
                sesion.setAttribute("SubjectCopia", sub);
                ControlRecursos.liberarRecursos(conexion);
                return mapping.findForward("exito");
            }
            System.out.println("Sesión inactiva: " + this.getClass().getName());
            return mapping.findForward("sesionInactiva");
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
}
