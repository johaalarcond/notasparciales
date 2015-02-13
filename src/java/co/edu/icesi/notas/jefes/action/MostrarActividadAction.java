/*
 * Created on 22-feb-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.jefes.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import co.edu.icesi.notas.basica.Activity;
import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.form.Category;

/**
 * Acción que permite mostrar información un poco más detallada de una
 * actividad.
 * 
 * @author lmdiaz
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
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
        return mapping.findForward("aErrorJefe");
    }
}
