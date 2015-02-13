package co.edu.icesi.notas.tags;

import java.sql.Connection;
import java.util.*;

import javax.servlet.http.*;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import co.edu.icesi.notas.*;
import co.edu.icesi.notas.basica.*;
import co.edu.icesi.notas.control.*;

/**
 * @author mzapata
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TagDefinitivasBasico extends TagSupport {

    private boolean estudiante;

    public int doStartTag() {
        String idUsuario = "";
        Connection conexion = null;
        try {
            HttpSession session = ControlSesion.obtenerSesion((HttpServletRequest) pageContext.getRequest());
            idUsuario = ControlSesion.getUsuario((HttpServletRequest) pageContext.getRequest());
            conexion = ControlRecursos.obtenerConexion();
            JspWriter out = pageContext.getOut();
            Curso curso = (Curso) session.getAttribute("curso");
            Subject sub = (Subject) session.getAttribute("subject");
            Clasificacion individuales = curso.getIndividuales();
            Clasificacion grupales = curso.getGrupales();
            String texto = "";
            String ultimaLine = "<tr><td colspan=\"2\" class=\"resaltado\" align=\"right\">Promedio</td>";

            texto += "<table border=\"1\" cellspacing=\"0\" width=\"90%\" align=\"center\">";

            String tipoConf = curso.getTipoConfiguracion();

            Iterator iterator = sub.getActivities().iterator();
            Activity actual;

            int numAct = 0;
            if (tipoConf.equals("I")) {
                numAct = individuales.getNumActividades();
                texto += "<tr><td>&nbsp;</td><td>&nbsp;</td>";
                if (numAct > 0) {
                    texto += "<td class=\"celda2\" align=\"center\" colspan=\""
                            + numAct + "\">" + individuales.getNombre()
                            + "</td>";
                }
                numAct = grupales.getNumActividades();
                if (numAct > 0) {
                    texto += "<td class=\"celda2\" align=\"center\" colspan=\""
                            + numAct + "\">" + grupales.getNombre() + "</td>";
                }

                if (curso.getControlAsistencia().equals("S")) {
                    texto += "<td>&nbsp;</td>";
                }
                texto += "<td>&nbsp;</td>";
                texto += "</tr>";

            }

            texto += "<tr><td class=\"celda2\">Código</td><td class=\"celda2\">Nombre</td>";
            numAct = 0;
            // La primera fila con las evaluaciones que se realizaron.
            while (iterator.hasNext()) {
                actual = (Activity) iterator.next();

                texto += "<td class=\"celda2\" align=\"center\">"
                        + actual.getName();
                numAct--;
                if (!estudiante) {
                    texto += "<br>" + actual.getPercentage() + "%";
                }
                texto += "</td>";
            }

            // Completa la primera fila con los campos de porcentaje de
            // asistencia y de definitiva
            if (curso.getControlAsistencia().equals("S")) {
                texto += "<td class=\"celda4\" align=\"center\" >Horas<br/>de inasistencia<br>hasta la fecha</td>";
            }
            texto += "<td class=\"resaltado\" align=\"center\">Definitiva (Acumulada)</td></tr>";

            List alumnos = curso.getAlumnos();
            String celda;
            String resaltado;
            Actividad actividad;
            for (int i = 0; i < alumnos.size(); i++) {
                Alumno alumno = (Alumno) alumnos.get(i);
                if ((alumno.getEstado() != null && alumno.getEstado().equals(
                        "A"))
                        || estudiante) {
                    celda = "celda";
                    resaltado = "resaltado";

                    iterator = sub.getActivities().iterator();

                    // Fila con el nombre del alumno
                    texto += "<tr><td class=\"" + celda + "\">"
                            + alumno.getCodigo() + "</td><td class=\"" + celda
                            + "\">" + alumno.getNombre().toLowerCase()
                            + "</td>";

                    while (iterator.hasNext()) {
                        actual = (Activity) iterator.next();
                        actividad = curso.getActividad(actual.getConsecutive());
                        texto += "<td class=\""
                                + celda
                                + "\" align=\"center\">"
                                + actividad.getMatricula(alumno.getCodigo()).getCalificacion() + "</td>";
                    }

                    if (curso.getControlAsistencia().equals("S")) {
                        // Calculamos el porcentaje de asistencia
						/*
                         * texto += "<td class=\"celda4\" align=\"center\">" +
                         * alumno.calcularAsistenciaTotal(conexion, curso) +
                         * "%</td>";
                         */
                        texto += "<td class=\"celda4\" align=\"center\">"
                                + alumno.calcularInasistenciaHoras(conexion,
                                curso) + "</td>";
                    }

                    // Obtenemos la definitiva del curso
                 
                            if(curso.perdidaPorInasistencia(alumno, conexion) ==true)
                    {
                         texto += "<td class=\"" + resaltado
                            + "\" align=\"center\">"
                            +curso.perdidAsistencia(alumno, conexion)+ "</td>";
                    texto += "</tr>";
                    }
                  else
                    {
                         
                    texto += "<td class=\"" + resaltado
                            + "\" align=\"center\">"
                            + curso.getDefinitiva(alumno, conexion) + "</td>";
                    texto += "</tr>";
                    }
                 
                    /*
                    if((curso.getDefinitiva(alumno, conexion)==0.5))
                    {
                         texto += "<td class=\"" + resaltado
                            + "\" align=\"center\">"
                            +   curso.perdidAsistencia(alumno, conexion) + "</td>";
                    texto += "</tr>";
                    }
                  else
                    {
                    texto += "<td class=\"" + resaltado
                            + "\" align=\"center\">"
                            + curso.getDefinitiva(alumno, conexion) + "</td>";
                    texto += "</tr>";
                    }
                    */
                    /*
                     if((curso.perdidaPorInasistencia(alumno, conexion)==true)||(curso.pierdePrInasistencia(alumno, conexion)==true))
                    {
                         texto += "<td class=\"" + resaltado
                            + "\" align=\"center\">"
                            +   curso.perdidAsistencia(alumno, conexion) + "</td>";
                    texto += "</tr>";
                    }
                  else
                    {
                    texto += "<td class=\"" + resaltado
                            + "\" align=\"center\">"
                            + curso.getDefinitiva(alumno, conexion) + "</td>";
                    texto += "</tr>";
                    }**/
                    
                }
            }
            if (!estudiante) {
                texto += ultimaLine;
                iterator = sub.getActivities().iterator();
                while (iterator.hasNext()) {
                    actual = (Activity) iterator.next();
                    actividad = curso.getActividad(actual.getConsecutive());
                    texto += "<td class=\"resaltado\" align=\"center\">"
                            + actividad.getPromedio() + "</td>";
                }
                if (curso.getControlAsistencia().equals("S")) {
                    // promedio total de asistencias
                    texto += "<td class=\"celda4\" align=\"center\">"
                            + curso.getPromedioAsistenciaTotal(conexion)
                            + "%</td>";
                }
                texto += "<td class=\"resaltado\" align=\"center\">"
                        + curso.getPromedio(conexion) + "</td></tr>";
                
               
                //ffceballos 19/09/2013
               
             //   if(curso.getDepartamento().equalsIgnoreCase("08")){
                
                texto+="<tr><td colspan=\"2\" class=\"resaltado\" align=\"right\">Desviación estándar</td>";
                iterator = sub.getActivities().iterator();
                while (iterator.hasNext()) {
                    actual = (Activity) iterator.next();
                    actividad = curso.getActividad(actual.getConsecutive());
                    texto += "<td class=\"resaltado\" align=\"center\">"
                            + actividad.getDesviacion() + "</td>";
                }
                if (curso.getControlAsistencia().equals("S")) {
                    // desviación total de asistencias
                    texto += "<td class=\"celda4\" align=\"center\">"
                           
                            + "</td>";
                }
                texto += "<td class=\"resaltado\" align=\"center\">"
                        +curso.getDesviacion(conexion)+  "</td></tr>";
                
              //   }
                //fin ffceballos
                
               
            }
            texto += "</table>";
            out.print(texto);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {            
            ControlRecursos.liberarRecursos(conexion);
        }
        return SKIP_BODY;
    }

    public boolean isEstudiante() {
        return estudiante;
    }

    public void setEstudiante(boolean estudiante) {
        this.estudiante = estudiante;
    }
}
