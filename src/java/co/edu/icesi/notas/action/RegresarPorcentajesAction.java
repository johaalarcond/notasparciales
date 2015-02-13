//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl
package co.edu.icesi.notas.action;

import java.util.*;

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
import co.edu.icesi.notas.form.ModificarPorcentajesForm;
import co.edu.icesi.notas.utilidades.UtilidadCategories;

/**
 * MyEclipse Struts Creation date: 12-13-2006 Acción que se regresa a la página
 * de modificación de porcentajes XDoclet definition:
 * 
 * @struts:action validate="true"
 */
public class RegresarPorcentajesAction extends Action {

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

                String tipoConf = request.getParameter("tipo");
                // En caso de que el parámetro 'tipo' no se encuentre en el
                // request
                if (tipoConf == null) {
                    tipoConf = ((Curso) session.getAttribute("curso")).getTipoConfiguracion();
                }

                Subject sub = (Subject) session.getAttribute("subject");

                ModificarPorcentajesForm modificarPorcentajesForm = new ModificarPorcentajesForm();

                if (sub.getCurso().getTipoConfiguracion().equals("I")) {
                    modificarPorcentajesForm.setPorcentajeIndividual(sub.getPorcentajeIndividual());
                    modificarPorcentajesForm.setPorcentajeGrupal(sub.getPorcentajeGrupal());
                }

                ArrayList listaCategories = UtilidadCategories.organizarActividades(sub.getActivities());
                modificarPorcentajesForm.setListaCategorias(listaCategories);
                modificarPorcentajesForm.setOpcion("4");
                request.setAttribute("modificarPorcentajesForm",
                        modificarPorcentajesForm);
                return mapping.findForward(ControlSecuencia.getNextRegresarPorcentaje(tipoConf));
            }
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Usuario: " + request.getRemoteUser());
            System.out.println("Descripción:");
            e.printStackTrace();
        }
        return mapping.findForward("errorAplicacion");
    }
}
