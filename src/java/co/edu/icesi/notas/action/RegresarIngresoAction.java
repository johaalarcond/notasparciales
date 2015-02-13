//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl
package co.edu.icesi.notas.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import co.edu.icesi.notas.basica.Activity;
import co.edu.icesi.notas.basica.Subject;
import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.form.ModificarEsquemaForm;

import java.util.*;

/**
 * MyEclipse Struts Creation date: 12-13-2006 Acción que se regresa a la pagina
 * de ingreso y eliminación de actividades. XDoclet definition:
 * 
 * @struts:action validate="true"
 */
public class RegresarIngresoAction extends Action {

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
        try {
            HttpSession session = ControlSesion.obtenerSesion(request);
            if (session != null) {
                idUsuario = ControlSesion.getUsuario(request);
                if (session.getAttribute("curso") == null) {
                    request.setAttribute(
                            "mensajeError",
                            "Ha vuelto a la página de listado de cursos pero no ha escogido un curso, por favor escoja un curso del listado inicial.");
                    return mapping.findForward("errorAplicacion");
                }
                Subject sub = (Subject) session.getAttribute("subject");
                List activities = sub.getActivities();

                for (int i = 0; i < activities.size(); i++) {
                    Activity act = (Activity) activities.get(i);
                    if (act.getOther() != null && !act.getOther().equals("")) {
                        act.setCategory("Otra");
                    }
                }
                ModificarEsquemaForm modificarEsquemaForm = new ModificarEsquemaForm();
                modificarEsquemaForm.setActivities(activities);
                modificarEsquemaForm.setOpcion("2");
                request.setAttribute("modificarEsquemaForm",
                        modificarEsquemaForm);
                ControlProfesores profesores = (ControlProfesores) session.getAttribute("profesores");
                session.setAttribute("tiposCategoria", profesores.getTiposCategoria());
                session.setAttribute("origen", "esquema");
                return mapping.findForward("exito");
            }
            System.out.println("Sesión inactiva: " + this.getClass().getName());
            return mapping.findForward("sesionInactiva");
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Usuario: " + request.getRemoteUser());
            System.out.println("Descripción:");
            e.printStackTrace();
        }
        return mapping.findForward("errorAplicacion");
    }
}
