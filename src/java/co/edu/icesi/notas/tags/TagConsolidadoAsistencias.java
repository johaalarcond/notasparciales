/*
 * Created on Dec 19, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.tags;

import javax.servlet.http.*;
import javax.servlet.jsp.*;

import java.sql.Connection;

import javax.servlet.jsp.tagext.*;
import java.util.*;
import co.edu.icesi.notas.*;
import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.utilidades.OperacionesMatematicas;

/**
 * 
 * Tag empleado para imprimir el total de asistencias a un curso.
 * 
 * @author rescobar
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TagConsolidadoAsistencias extends TagSupport {

    public int doStartTag() {
        String idUsuario = "";
        Connection conexion = null;
        try {
            HttpSession session = ControlSesion.obtenerSesion((HttpServletRequest) pageContext.getRequest());
            idUsuario = ControlSesion.getUsuario((HttpServletRequest) pageContext.getRequest());
            conexion = ControlRecursos.obtenerConexion();
            JspWriter out = pageContext.getOut();
            String texto = "<form method=\"post\" action=\"notificarEstudiante.icesi\" name=\"notificacionForm\"><table border=\"1\" cellspacing=\"0\" align=\"center\">";
            texto += "<tr><td class=\"celda2\" align=\"center\">C&oacute;digo</td><td class=\"celda2\" align=\"center\">Nombre</td>";
            ArrayList meses = (ArrayList) session.getAttribute("meses");
            for (int i = 0; i < meses.size(); i++) {
                Mes mes = (Mes) meses.get(i);
                texto += "<td class=\"celda2\" align=\"center\">"
                        + mes.getNombre() + "</td>";
            }
            // 20 Nov 2009
            // Se remueve la columna de porcentaje por acta del 19 deNov de 2009
            // <td class=\"celda2\" align=\"center\">Porcentaje<br/>de
            // asistencia<br/>hasta la fecha</td>
            texto += "<td class=\"celda2\" align=\"center\">Horas<br/>de inasistencia<br>hasta la fecha</td><td class=\"celda2\" align=\"center\">Notificación<br/>de inasistencia</td></tr>";
            texto += "<tr><td colspan=\"2\" class=\"celda2\">&nbsp;</td>";
            for (int i = 0; i < meses.size(); i++) {
                texto += "<td class=\"celda2\" align=\"center\">&nbsp;</td>";
            }
            texto += "<td class=\"celda2\">&nbsp;</td><td class=\"celda2\">&nbsp;</td></tr>";
            Curso curso = (Curso) session.getAttribute("curso");
            ArrayList alumnos = (curso).getAlumnos();
            for (int j = 0; j < alumnos.size(); j++) {
                Alumno alumno = (Alumno) alumnos.get(j);
                texto += "<tr><td class=\"celda2\" align=\"left\">"
                        + alumno.getCodigo()
                        + "</td><td class=\"celda2\" align=\"left\">"
                        + alumno.getNombre() + "</td>";
                for (int i = 0; i < meses.size(); i++) {
                    texto += "<td class=\"celda3\" align=\"center\">"
                            + alumno.calcularAsistenciasMes(conexion,
                            ((Mes) meses.get(i)).getId(), curso)
                            + "%</td>";
                }
                int asistenciaTotalAlumno = alumno.calcularAsistenciaTotal(
                        conexion, curso);
           double inasistenciaTotalAlumno = (double)alumno.calcularInasistenciaHoras(conexion, curso);
             // double inasistenciaTotalAlumno =(double)(Math.rint((alumno.calcularInasistenciaHoras(conexion, curso))*10)/10); 
              
              //  if (asistenciaTotalAlumno < ((Integer) session.getAttribute("porcentajeMinimoAsistencia")).intValue()) {
               //Permite identificar cuand se pierde por inasistencia, modificacion Mayo 15-2012 
               if(curso.perdidaPorInasistencia(alumno, conexion) ==true)
                {
                    /*
                     * texto += "<td class=\"celda4\" align=\"center\">" +
                     * asistenciaTotalAlumno + "%</td>";
                     */
                    texto += "<td class=\"celda4\" align=\"center\">"
                            + inasistenciaTotalAlumno +"</td>";
                } else {
                    /*
                     * texto += "<td class=\"celda2\" align=\"center\">" +
                     * asistenciaTotalAlumno + "%</td>";
                     */
                    texto += "<td class=\"celda2\" align=\"center\">"
                            + inasistenciaTotalAlumno+ "</td>";
                }
                /**
                 * ************Completar el añadido del botón de
                 * notificación***************
                 */
                texto += "<td><input type=\"button\" name=\""
                        + alumno.getCodigo()
                        + "\" value=\"Notificar inasistencia\" onclick=\"document.notificacionForm.datos.value = this.name+';"
                        + inasistenciaTotalAlumno
                        + "'; document.notificacionForm.submit(); \"/></td></tr>";
            }

            texto += "</table><input type=\"hidden\" name=\"datos\" /></form>";
            out.print(texto);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ControlRecursos.liberarRecursos(conexion);
        }
        return (SKIP_BODY);
    }
}
