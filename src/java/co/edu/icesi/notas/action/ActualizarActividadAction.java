//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl
package co.edu.icesi.notas.action;

import java.sql.Connection;

import javax.servlet.http.*;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.*;

import co.edu.icesi.notas.*;
import co.edu.icesi.notas.basica.*;
import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.estudiante.control.ControlEstudiante;
import co.edu.icesi.notas.form.ActualizarActividadForm;

/**
 * Esta Acción pertenece a la vista básica del módulo de profesores. Es la
 * encargada de registrar y validar las modificaciones que se hagan sobre un
 * objeto Activity y que se reflejarán sobre el objeto Actividad
 * correspondiente.
 * 
 * @struts:action path="/actualizarActividad" name="actualizarActividadForm"
 *                scope="request" validate="true"
 */
public class ActualizarActividadAction extends Action {

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
            HttpSession session = ControlSesion.obtenerSesion(request);
            if (session != null) {
                idUsuario = ControlSesion.getUsuario(request);
                ActualizarActividadForm actualizarActividadForm = (ActualizarActividadForm) form;

                if (session.getAttribute("curso") == null) {
                    request.setAttribute(
                            "mensajeError",
                            "Ha vuelto a la página de listado de cursos pero no ha escogido un curso, por favor escoja un curso del listado inicial.");
                    return mapping.findForward("errorAplicacion");
                }
                // Origen puede ser : esquema o porcentajes, dependiendo de
                // donde lo
                // han
                // llamado
                String origen = (String) session.getAttribute("origen");
                if (origen == null) {
                    origen = "esquema";
                }

                ControlEstudiante estudiante = (ControlEstudiante) session.getAttribute("estud");

                if (this.isCancelled(request)) {

                    session.removeAttribute("Actividad");
                    session.removeAttribute("origen");
                    if (estudiante != null) {
                        ControlRecursos.liberarRecursos(conexion);
                        return mapping.findForward("regresoEstud");
                    } else {
                        ControlRecursos.liberarRecursos(conexion);
                        return mapping.findForward(origen);
                    }

                }

                ActionErrors errores = actualizarActividadForm.validate(
                        mapping, request);

                if (!errores.isEmpty()) {
                    saveErrors(request, errores);
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("error");
                }

                Activity act = (Activity) session.getAttribute("Actividad");

                Subject sub = (Subject) session.getAttribute("subject");

                Actividad actividad = sub.getCurso().getActividad(
                        act.getConsecutive());
                // Creamos las copias
                if (actividad != null) {
                    actividad.crearCopia();
                }
                BeanUtils.copyProperties(act, actualizarActividadForm);
                if (origen.equals("porcentajeBasico")
                        || origen.equals("porcentajeIntermedio")) {
                    session.removeAttribute("Actividad");
                    return mapping.findForward(origen);
                } else {
                    if (origen.equals("esquema")) {
                        conexion = ControlRecursos.obtenerConexion();
                        // Copiamos los campos de 'act' a 'actividad'.
                        actividad.setDescripcion(act.getDescription());
                        actividad.setFechaRealizacion(act.getDate());
                        actividad.setTemas(act.getTopics());
                        actividad.actualizarBd(conexion);
                        // Registramos los cambios en auditorias
                        actividad.registrarCambios(conexion, actividad.getCategoria().getClasificacion().getCurso());
                        session.removeAttribute("Actividad");
                        // Se debe regresar a la pagina de esquema.jsp
                        ControlRecursos.liberarRecursos(conexion);
                        return mapping.findForward(origen);
                    }
                }
                ControlRecursos.liberarRecursos(conexion);
                return mapping.findForward("esquema");
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
