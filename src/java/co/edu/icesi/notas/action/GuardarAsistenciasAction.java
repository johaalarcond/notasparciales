/*
 * Created on Dec 15, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.action;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import co.edu.icesi.notas.*;
import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.form.*;

import java.sql.*;
import java.util.*;

/**
 * @author rescobar
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class GuardarAsistenciasAction extends Action {

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

                Curso curso = (Curso) sesion.getAttribute("curso");
                if (curso == null) {
                    request.setAttribute("mensajeError",
                            "La aplicación ha recibido parámetros inválidos.");
                    return mapping.findForward("errorAplicacion");
                }

                ArrayList alumnos = curso.getAlumnos();
                RegistroAsistenciasForm nuevoForm = (RegistroAsistenciasForm) form;
                if (nuevoForm.getBoton().equalsIgnoreCase("guardar")) {
                    conexion = ControlRecursos.obtenerConexion();
                    ArrayList copiaAlumnos = nuevoForm.getAlumnos();
                    for (int i = 0; i < copiaAlumnos.size(); i++) {
                        co.edu.icesi.notas.Alumno alumno = (co.edu.icesi.notas.Alumno) alumnos.get(i);
                        co.edu.icesi.notas.form.Alumno copiaAlumno = (co.edu.icesi.notas.form.Alumno) copiaAlumnos.get(i);
                        for (int j = 0; j < copiaAlumno.getAsistencias().size(); j++) {
                            co.edu.icesi.notas.form.Asistencia copiaAsistencia = (co.edu.icesi.notas.form.Asistencia) copiaAlumno.getAsistencias().get(j);
                            int copiaPorcentajeAsistencia;
                            try {
                                copiaPorcentajeAsistencia = Integer.parseInt(copiaAsistencia.getPorcentajeAsistencia());
                            } catch (NumberFormatException exc) {
                                copiaPorcentajeAsistencia = -1;
                            }
                            ((co.edu.icesi.notas.Asistencia) (alumno.getAsistencias().get(j))).setPorcentajeAsistencia(copiaPorcentajeAsistencia);
                        }
                        alumno.guardarAsistencias(
                                conexion,
                                curso,
                                ((Integer) sesion.getAttribute("porcentajeMinimoAsistencia")).intValue());
                        copiaAlumno.setPromedioAsistenciaMes(alumno.getPromedioAsistenciaMes());
                    }
                    request.setAttribute("guardado", "true");
                } else if (nuevoForm.getBoton().equalsIgnoreCase("cancelar")) {
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("cancel");
                } else if (nuevoForm.getBoton().equalsIgnoreCase("recalcular")) {
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("continue");
                }
                ControlRecursos.liberarRecursos(conexion);
                return mapping.findForward("continue");
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
