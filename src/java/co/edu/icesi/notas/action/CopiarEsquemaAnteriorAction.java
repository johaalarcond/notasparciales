//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl
package co.edu.icesi.notas.action;

import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import co.edu.icesi.notas.Curso;
import co.edu.icesi.notas.basica.Subject;
import co.edu.icesi.notas.control.*;

/**
 * MyEclipse Struts Creation date: 06-12-2007
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/copiarEsquemaAnterior" name="copiarEsquemaAnteriorForm"
 *                input="/basica/copiaEsquemaAnterior.jsp" scope="request"
 */
public class CopiarEsquemaAnteriorAction extends Action {

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
                if (this.isCancelled(request)) {
                    return mapping.findForward("regresar");
                }

                if (sesion.getAttribute("curso") == null) {
                    request.setAttribute(
                            "mensajeError",
                            "Ha vuelto a la página de listado de cursos pero no ha escogido un curso, por favor escoja un curso del listado inicial.");
                    return mapping.findForward("errorAplicacion");
                }

                conexion = ControlRecursos.obtenerConexion();
                Curso cursoActual = (Curso) sesion.getAttribute("curso");
                ControlCopiaEsquemas controlCopia = (ControlCopiaEsquemas) sesion.getAttribute("controlCopia");
              //  controlCopia.setCedulaProfesor(idUsuario);
                controlCopia.realizarCopiaCurso(cursoActual, controlCopia.getCursoAnterior(), conexion);

                Subject sub = new Subject(cursoActual);
                sesion.setAttribute("subject", sub);
                ControlRecursos.liberarRecursos(conexion);
                return mapping.findForward("siguiente");
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
