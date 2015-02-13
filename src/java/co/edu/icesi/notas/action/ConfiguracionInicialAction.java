//Created by MyEclipse Struts
//XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl
package co.edu.icesi.notas.action;

import java.sql.*;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import co.edu.icesi.notas.basica.Subject;
import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.*;

/**
 * MyEclipse Struts Creation date: 06-27-2006 Esta acción pertenece a la versión
 * Beta 1. No se usa en estos momentos (Deprecated) XDoclet definition:
 * 
 * @struts:action validate="true"
 */
public class ConfiguracionInicialAction extends Action {

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
                String fwd = "continuacion";

                if (sesion.getAttribute("curso") == null) {
                    request.setAttribute(
                            "mensajeError",
                            "Ha vuelto a la página de listado de cursos pero no ha escogido un curso, por favor escoja un curso del listado inicial.");
                    return mapping.findForward("errorAplicacion");
                }

                if (this.isCancelled(request)) {
                    return mapping.findForward("cancelar");
                }

                ControlProfesores profesores = (ControlProfesores) sesion.getAttribute("profesores");
                Curso curso = (Curso) sesion.getAttribute("curso");
                String opcion = request.getParameter("opcion");
                Subject subject = (Subject) sesion.getAttribute("subject");
                if (curso == null || profesores == null || opcion == null
                        || opcion.equals("") || subject == null) {
                    request.setAttribute("mensajeError",
                            "La aplicación ha recibido parámetros inválidos.");
                    return mapping.findForward("errorAplicacion");
                }
                conexion = ControlRecursos.obtenerConexion();

                if (opcion.equals("1")) {
                    // Desea crear el esquema desde cero
                    fwd = "confEsquema";
                } else {
                    int error = obtenerCursoAnterior(profesores, conexion,
                            curso);
                    System.out.println("errores en obtener curso"+error);
                    if (error == 0) {
                        ControlRecursos.liberarRecursos(conexion);
                        return mapping.findForward("copiar");
                    } else if (error == 1) {
                        ActionErrors errores = new ActionErrors();
                        errores.add("esquemaAnterior", new ActionError(
                                "esquemaAnterior.error"));
                        request.setAttribute("error", "S");
                        saveErrors(request, errores);
                        ControlRecursos.liberarRecursos(conexion);
                        return mapping.findForward("error");
                    } else {
                        ControlRecursos.liberarRecursos(conexion);
                        request.setAttribute(
                                "mensajeError",
                                "En el momento no se pudo verificar la existencia de un esquema en un periodo anterior al actual. Por favor intente copiar el esquema anterior de nuevo.");
                        return mapping.findForward("errorAplicacion");
                    }
                }
                sesion.setAttribute("origen", "inicial");
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

    public int obtenerCursoAnterior(ControlProfesores profesores,
            Connection conexion, Curso actual) {
        Statement stm = null;
        ResultSet rs = null;
        try {
            String periodoAnterior = profesores.getPeriodoAnterior();
            int consecutivoAnterior = profesores.getConsecutivoAnterior();
            String sql = "select count(cur.consecutivo) cantidad "
                    + "from tntp_cursos cur,tntp_clasif_categ cla, tntp_categorias cat, tntp_actividades act, tbas_cursos_profesor pfe_cur "
                    + "where cur.periodo_periodo_acad='" + periodoAnterior
                    + "' " + "and cur.periodo_consecutivo="
                    + consecutivoAnterior + " " + "and cur.descripmat_codigo='"
                    + actual.getCodigoMateria() + "' "
                    + "and cur.descripmat_consecutivo="
                    + actual.getConsecutivoMateria() + " "
                    + "and cla.consec_curso=cur.consecutivo "
                    + "and cat.consec_clasif_categ=cla.consecutivo "
                    + "and act.consec_categoria=cat.consecutivo "                        
                     //adicionado ffceballos (todo lo de pfe_cur)
                    + " and pfe_cur.profesor_cedula='"
                    + profesores.getProfesor().getCedula()              
                   + "'"
                    + " and pfe_cur.descripmat_codigo = cur.descripmat_codigo"
                    + " and pfe_cur.descripmat_consecutivo = cur.descripmat_consecutivo"
                    + " and pfe_cur.curso_actu_grupo = cur.curso_actu_grupo"
                    + " and pfe_cur.periodo_periodo_acad = cur.periodo_periodo_acad"
                    + " and pfe_cur.periodo_consecutivo = cur.periodo_consecutivo";
            
            //System.out.println(sql);
           

            stm = conexion.createStatement();
            rs = stm.executeQuery(sql);
            if (rs.next()) {
                int cantidad = rs.getInt("cantidad");
                ControlRecursos.liberarRecursos(rs, stm);
                if (cantidad > 0) {
                    return 0;
                }
            }
            ControlRecursos.liberarRecursos(rs, stm);
            return 1;
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error obteniendo el curso anterior "
                    + e.getMessage());
            e.printStackTrace();
            ControlRecursos.liberarRecursos(rs, stm);
            return 2;
        }

    }
}
