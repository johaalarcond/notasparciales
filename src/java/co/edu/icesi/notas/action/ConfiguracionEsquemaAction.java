//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl
package co.edu.icesi.notas.action;

import java.sql.Connection;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import co.edu.icesi.notas.Curso;
import co.edu.icesi.notas.basica.Subject;
import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.form.ModificarEsquemaForm;

/**
 * MyEclipse Struts Creation date: 06-07-2007
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/configuracionEsquema" name="configurarEsquemaForm"
 *                scope="request" validate="true"
 */
public class ConfiguracionEsquemaAction extends Action {

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
            String fwd = "continuar";
            if (sesion != null) {
                idUsuario = ControlSesion.getUsuario(request);
                if (sesion.getAttribute("curso") == null) {
                    request.setAttribute(
                            "mensajeError",
                            "Ha vuelto a la página de listado de cursos pero no ha escogido un curso, por favor escoja un curso del listado inicial.");
                    return mapping.findForward("errorAplicacion");
                }

                // En caso de que se haya presionado el botón cancelar
                if (this.isCancelled(request)) {
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("cancelar");
                }

                DynaActionForm configurarEsquemaForm = (DynaActionForm) form;
                String opcionEsquema = (String) configurarEsquemaForm.get("opcionEsquema");
                Curso curso = (Curso) sesion.getAttribute("curso");

                conexion = ControlRecursos.obtenerConexion();

                // Modificamos el tipo de configuración del curso, de ser
                // necesario.
                if (!curso.getTipoConfiguracion().equals(opcionEsquema)) {
                    curso.setTipoConfiguracion(opcionEsquema);
                    curso.actualizarTipoConfiguracion(conexion);
                }
                Subject subject = (Subject) sesion.getAttribute("subject");
                ControlProfesores profesores = (ControlProfesores) sesion.getAttribute("profesores");

                ModificarEsquemaForm forma = new ModificarEsquemaForm();
                if (subject.getActivities().isEmpty()) {
                    subject.agregarActivities(1);
                }
                forma.getActivities().addAll(subject.getActivities());
                sesion.setAttribute("tiposCategoria", profesores.getTiposCategoria());
                request.setAttribute("modificarEsquemaForm", forma);
                ControlRecursos.liberarRecursos(conexion);
                return mapping.findForward(fwd);
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
