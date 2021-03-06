/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package co.edu.icesi.notas.jefes.action;

import java.sql.*;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.jefes.control.*;

/**
 * MyEclipse Struts Creation date: 10-14-2009
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/busquedaCursos" name="cursosDeptoForm"
 *                input="/jefes/cursosDepto.jsp" scope="request" validate="true"
 */
public class BusquedaCursosAction extends Action {
    /*
     * Generated Methods
     */

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
                DynaActionForm cursosDeptoForm = (DynaActionForm) form;
                conexion = ControlRecursos.obtenerConexion();
                String criterioBusqueda = cursosDeptoForm.get("criterio").toString();
                ControlJefe cj = (ControlJefe) session.getAttribute("jefes");
                cj.cargarCursos(conexion, criterioBusqueda);
                if (!criterioBusqueda.trim().equals("")) {
                    request.setAttribute("resultadoBusqueda", criterioBusqueda);
                }
                request.setAttribute("deptos", cj.getDeptosUsuario());
                ControlRecursos.liberarRecursos(conexion);
                return mapping.findForward("continuar");
            }
            System.out.println("Sesi�n inactiva: " + this.getClass().getName());
            return mapping.findForward("sesionInactiva");
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Usuario: " + request.getRemoteUser());
            System.out.println("Descripci�n:");
            e.printStackTrace();
        } finally {
            ControlRecursos.liberarRecursos(conexion);
        }
        return mapping.findForward("aErrorJefe");
    }
}
