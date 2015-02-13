//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl
package co.edu.icesi.notas.estudiante.action;

import java.util.ArrayList;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import co.edu.icesi.notas.basica.Activity;
import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.form.Category;

/**
 * MyEclipse Struts Creation date: 01-19-2007
 * 
 * XDoclet definition:
 * 
 * @struts:action
 */
public class MostrarActividadAction extends Action {

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
            HttpSession sesion = ControlSesion.obtenerSesion(request);
            if (sesion != null) {
                idUsuario = ControlSesion.getUsuario(request);
                String indice1 = request.getParameter("index1");
                String indice2 = request.getParameter("index2");
                ArrayList listaCat = (ArrayList) sesion.getAttribute("listaCategorias");
                Category cat = (Category) listaCat.get(Integer.parseInt(indice1));
                Activity act = cat.getActivity(Integer.parseInt(indice2));
                request.setAttribute("actMostrar", act);
                return mapping.findForward("continuar");
            }
            System.out.println("Sesión inactiva: " + this.getClass().getName());
            return mapping.findForward("sesionInactiva");
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Usuario: " + request.getRemoteUser());
            System.out.println("Descripción:");
            e.printStackTrace();
        }
        request.setAttribute("mensajeError",
                "<strong>Error: </strong>La aplicación ha recibido parámetros inválidos.");
        return mapping.findForward("aErrorEstud");
    }
}
