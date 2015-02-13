//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl
package co.edu.icesi.notas.action;

import java.sql.Connection;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import co.edu.icesi.notas.Curso;
import co.edu.icesi.notas.control.*;

/**
 * Esta acción se empleará para modificar si se llevará o no la asistencia en el
 * curso, y para cambiar la escala que se usará.
 * 
 * @struts:action path="/controlAsistencia" name="controlAsistenciaForm"
 *                scope="request"
 */
public class ControlAsistenciaAction extends Action {

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

                DynaActionForm controlAsistenciaForm = (DynaActionForm) form;
                
                String controlAsistencia = (String) controlAsistenciaForm.get("controlAsistencia");
                
                
                //jalarcon
                String reglaMatematicas = (String) controlAsistenciaForm.get("reglaMatematicas");
                //jalarcon
                
                //ffceballos 30/01/2014
                String reglaQuiz = (String) controlAsistenciaForm.get("reglaQuiz");
                //fin ffceballos
                
                //ffceballos 11/02/2014
                if(!reglaMatematicas.equalsIgnoreCase("S")){reglaMatematicas="N";}
                if(!reglaQuiz.equalsIgnoreCase("S")){reglaQuiz="N";}
                // fin ffceballos 11/02/2014
                
                double escala = 0;
                ActionErrors errores = new ActionErrors();
                try {
                    // Convertimos la escala de cadena a doble.
                    escala = Double.parseDouble((String) controlAsistenciaForm.get("escala"));
                    if (escala < 5) {
                        request.setAttribute("error", "S");
                        errores.add("errorNota", new ActionError("error.nota"));
                        saveErrors(request, errores);
                        ControlRecursos.liberarRecursos(conexion);
                        return mapping.findForward("error");
                    }
                } catch (NumberFormatException nf) {
                    request.setAttribute("error", "S");
                    errores.add("errorNota", new ActionError("error.nota"));
                    saveErrors(request, errores);
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("error");
                }

                Curso curso = (Curso) sesion.getAttribute("curso");
                conexion = ControlRecursos.obtenerConexion();
                //jalarcon
                 if (!reglaMatematicas.equalsIgnoreCase(curso.getControlReglaMate()))
                {curso.setControlReglaMate(reglaMatematicas);
                curso.actualizarControlReglaMate(conexion);
                }
                 //jalarcon
                 
                 
                 //ffceballos 30/01/2014
                  if (!reglaQuiz.equalsIgnoreCase(curso.getControlReglaQuiz()))
                {curso.setControlReglaQuiz(reglaQuiz);
                curso.actualizarControlReglaQuiz(conexion);
                }
                 //fin ffceballos
                 
                // Actualización del atributo Control de Asistencia.
                if (!curso.getControlAsistencia().equals(controlAsistencia)) {
                    curso.setControlAsistencia(controlAsistencia);
                    curso.actualizarControlAsistencia(conexion);
                }
                // Actualizamos el atributo escala
                if (curso.getEscala() != escala) {
                    curso.actualizarEscala(conexion, escala);
                }
                ControlRecursos.liberarRecursos(conexion);
                return mapping.findForward("continuar");
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
