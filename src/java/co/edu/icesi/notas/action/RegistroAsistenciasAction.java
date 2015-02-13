/*
 * Created on Dec 12, 2006
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
 * @author rescobar Acción para el registro de asistencias
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class RegistroAsistenciasAction extends Action {

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

                conexion = ControlRecursos.obtenerConexion();
                RegistroAsistenciasForm nuevoForm = (RegistroAsistenciasForm) form;
                String mes = nuevoForm.getMesAsistencias();
                if (mes == null || mes.equals("")) {
                    ArrayList meses = (ArrayList) sesion.getAttribute("meses");
                    mes = ((Mes) meses.get(0)).getId();
                }
                
                curso.cargarProgramaciones(conexion, mes);
                nuevoForm.resetAlumnos();
                for (int i = 0; i < curso.getAlumnos().size(); i++) {
                    co.edu.icesi.notas.Alumno al = (co.edu.icesi.notas.Alumno) curso.getAlumnos().get(i);
                    al.cargarAsistencias(conexion, curso, mes);
                    co.edu.icesi.notas.form.Alumno copiaAlumno = new co.edu.icesi.notas.form.Alumno();
                    copiarAlumno(al, copiaAlumno);
                    nuevoForm.setAlumno(i, copiaAlumno);
                }
                sesion.setAttribute("festivosMes", cargarFestivosMes(conexion,
                        mes));
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

    public ArrayList cargarFestivosMes(Connection conexion, String mes) {
        ArrayList festivosMes = new ArrayList();
        Statement stm = null;
        ResultSet rs = null;
        try {
            Calendar fechaActual = Calendar.getInstance();
            stm = conexion.createStatement();
            String sql = "select to_char(festivo) from tbas_dias_festivos where to_char(festivo,'DD/MM/YYYY') like '%/"
                    + mes + "/" + fechaActual.get(Calendar.YEAR) + "'";
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                festivosMes.add(rs.getString(1));
            }
            ControlRecursos.liberarRecursos(rs, stm);
            return festivosMes;
        } catch (SQLException e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Descripción:");
            System.out.println("Error cargando festivos del mes "
                    + e.getMessage());
            e.printStackTrace();
            ControlRecursos.liberarRecursos(rs, stm);
            return festivosMes;
        }
    }

    public void copiarAlumno(co.edu.icesi.notas.Alumno al,
            co.edu.icesi.notas.form.Alumno copiaAlumno) {
        copiaAlumno.setNombre(al.getNombre());
        copiaAlumno.setApellidos(al.getApellidos());
        copiaAlumno.setCodigo(al.getCodigo());
        copiaAlumno.setPromedioAsistenciaMes(al.getPromedioAsistenciaMes());
        ArrayList copiasAsistencias = new ArrayList();
        for (int i = 0; i < al.getAsistencias().size(); i++) {
            ArrayList asistencias = al.getAsistencias();
            co.edu.icesi.notas.Asistencia asist = (co.edu.icesi.notas.Asistencia) asistencias.get(i);
            co.edu.icesi.notas.form.Asistencia copiaAsistencia = new co.edu.icesi.notas.form.Asistencia();
            if (asist.getPorcentajeAsistencia() == -1) {
                copiaAsistencia.setPorcentajeAsistencia("");
            } else {
                copiaAsistencia.setPorcentajeAsistencia(String.valueOf(asist.getPorcentajeAsistencia()));
            }
            copiasAsistencias.add(copiaAsistencia);
        }
        copiaAlumno.setAsistencias(copiasAsistencias);
    }
}
