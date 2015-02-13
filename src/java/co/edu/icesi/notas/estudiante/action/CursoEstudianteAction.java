//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl
package co.edu.icesi.notas.estudiante.action;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import co.edu.icesi.notas.*;
import co.edu.icesi.notas.basica.Subject;
import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.estudiante.control.ControlEstudiante;

/**
 * Acción análoga a listaCursos. Carga los datos disponibles para el estudiante
 * acerca del curso que seleccionó. XDoclet definition:
 * 
 * @struts:action path="/cursosEstud" name="cursosEstudianteForm"
 *                scope="request"
 */
public class CursoEstudianteAction extends Action {

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
                DynaActionForm cursosEstudianteForm = (DynaActionForm) form;

                String curso = request.getParameter("curso");
                String opcion = (String) cursosEstudianteForm.get("opcion");
                ControlEstudiante control = (ControlEstudiante) sesion.getAttribute("estudiante");

                if (control == null) {
                    request.setAttribute("mensajeError",
                            "<strong>Error: </strong>La aplicación ha recibido parámetros inválidos.");
                    return mapping.findForward("aErrorEstud");
                }

                Curso seleccionado = control.getCurso(curso);
                // Eliminamos las referencias
                seleccionado.setCategorias(new ArrayList());
                seleccionado.setIndividuales(new Clasificacion());
                seleccionado.setGrupales(new Clasificacion());
                //
                ControlProfesores controlProf = new ControlProfesores();
                String forward = "";
                conexion = ControlRecursos.obtenerConexion();
                controlProf.cargarTiposCategoria(conexion);
                controlProf.setConsecutivo(control.getConsecutivo());
                controlProf.setPeriodoAcademico(control.getPeriodoAcademico());
                seleccionado.cargarClasificacionesBd(conexion, controlProf);
                seleccionado.cargarCategoriasEstudiante(conexion, controlProf,
                        control.getAlumno());
                if (seleccionado.getIndividuales().getCategorias().isEmpty()
                        && seleccionado.getGrupales().getCategorias().isEmpty()) {
                    request.setAttribute("errorCurso", "El curso '"
                            + seleccionado.getNombre() + "' (Grupo "
                            + seleccionado.getGrupo()
                            + ") no posee evaluaciones asignadas.");
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("fracaso");
                }
                Subject subject = new Subject(seleccionado);
                sesion.setAttribute("curso", subject.getCurso());
                sesion.setAttribute("subject", subject);
                sesion.setAttribute("profesores", controlProf);
                // Opción 1: Ver esquema del curso
                // Opción 2: Ver notas
                if (opcion.equals("1")) {
                    forward = "esquema";
                } else {
                    if (opcion.equals("2")) {
                        forward = "notas";
                    } else {
                        if (opcion.equals("3")) {
                            forward = "definitivas";
                        }
                    }
                }
                ControlRecursos.liberarRecursos(conexion);
                return mapping.findForward(forward);
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
        return mapping.findForward("aErrorEstud");
    }
}
