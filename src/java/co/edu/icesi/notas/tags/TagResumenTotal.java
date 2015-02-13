package co.edu.icesi.notas.tags;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.*;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import co.edu.icesi.notas.*;
import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.utilidades.OperacionesMatematicas;

/**
 * @author lmdiaz, mzapata
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TagResumenTotal extends TagSupport {

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
            Clasificacion individuales = curso.getIndividuales();
            Clasificacion grupales = curso.getGrupales();
            String texto = "";
            String ultimaLine = "<tr><td colspan=\"2\" class=\"resaltado\" align=\"right\">Promedio</td>";
            // Aqui crea la tabla
            if (!estudiante) {
                texto += "<div class=\"curso\" align=\"center\">Notas definitivas</div><br>";
            }
            texto += "<table border=\"1\" cellspacing=\"0\" width=\"90%\" align=\"center\">";
            texto += "<tr><td colspan=\"2\" class=\"celda2\">&nbsp</td><td class=\"celda2\" align=\"center\" colspan=\""
                    + individuales.calcularCategoriasActivas()
                    + "\">Individuales</td><td  class=\"celda2\">&nbsp</td>";
            int act = grupales.calcularCategoriasActivas();
            if (act != 0) {
                texto += "<td class=\"celda2\"  align=\"center\" colspan=\""
                        + grupales.calcularCategoriasActivas()
                        + "\">Grupales</td>";
            }
            texto += "<td colspan=\"2\" class=\"celda2\">&nbsp</td></tr>";

            texto += "<tr><td class=\"celda2\">Código</td><td class=\"celda2\">Nombre</td>";

            // La primera fila con los tipos de examenes que se realizaron
            List pgIndi = individuales.getCategorias();
            List pgGrup = grupales.getCategorias();

            for (int i = 0; i < pgIndi.size(); i++) {
                Categoria pg = (Categoria) pgIndi.get(i);
                TipoCategoria tipo = (TipoCategoria) pg.getTipoCategoria();
                if (pg != null && !pg.isCancelado()) {
                    texto += "<td class=\"celda2\" align=\"center\">"
                            + tipo.getNombre();
                    if (!estudiante) {
                        texto += "<br>" + pg.getPorcentaje() + "%";
                    }
                    texto += "</td>";
                }
            }
            // Completa la primera fila con los campos de Definitiva individual,
            // grupal, definitiva
            texto += "<td class=\"resaltado\"  align=\"center\">Definitiva individual";
            if (!estudiante) {
                texto += "<br>" + individuales.getPorcentaje() + "%";
            }
            texto += "</td>";

            for (int i = 0; i < pgGrup.size(); i++) {
                Categoria pg = (Categoria) pgGrup.get(i);
                TipoCategoria tipo = (TipoCategoria) pg.getTipoCategoria();
                if (pg != null && !pg.isCancelado()) {
                    texto += "<td class=\"celda2\" align=\"center\">"
                            + tipo.getNombre();
                    if (!estudiante) {
                        texto += "<br>" + pg.getPorcentaje() + "%";
                    }
                    texto += "</td>";
                }
            }
            texto += "<td class=\"resaltado\" align=\"center\">Definitiva grupal";
            if (!estudiante) {
                texto += "<br>" + grupales.getPorcentaje() + "%";
            }
            texto += "</td><td class=\"resaltado\">Definitiva</td>";
            texto += "</tr>";
            List alumnos = curso.getAlumnos();
            String celda;
            String resaltado;
            for (int i = 0; i < alumnos.size(); i++) {
                Alumno alumno = (Alumno) alumnos.get(i);
                if ((alumno.getEstado() != null && alumno.getEstado().equals(
                        "A"))
                        || estudiante) {
                    celda = "celda";
                    resaltado = "resaltado";

                    // Fila con el nombre del alumno
                    texto += "<tr><td class=\"" + celda + "\">"
                            + alumno.getCodigo() + "</td><td class=\"" + celda
                            + "\">" + alumno.getNombre().toLowerCase()
                            + "</td>";
                    for (int z = 0; z < pgIndi.size(); z++) {
                        Categoria pg = (Categoria) pgIndi.get(z);
                        if (pg != null && !pg.isCancelado()) {
                            // Coloca cada una de las notas individuales del
                            // alumno para cada tipo de examen
                            texto += "<td class=\""
                                    + celda
                                    + "\" align=\"center\">"
                                    + OperacionesMatematicas.redondear(pg.calcularNotaSinPorcentaje(alumno),
                                    1) + "</td>";
                        }
                    }
                    // Coloca la definitiva individual
                    texto += "<td class=\"resaltado\" align=\"center\">"
                            + OperacionesMatematicas.redondear(curso.getDefinitivaIndividual(alumno,conexion), 1)
                            + "</td>";

                    for (int z = 0; z < pgGrup.size(); z++) {
                        Categoria pg = (Categoria) pgGrup.get(z);
                        if (pg != null && !pg.isCancelado()) {
                            // Coloca cada una de las notas individuales del
                            // alumno para cada tipo de examen
                            texto += "<td class=\""
                                    + celda
                                    + "\" align=\"center\">"
                                    + OperacionesMatematicas.redondear(pg.calcularNotaSinPorcentaje(alumno),
                                    1) + "</td>";
                        }
                    }
                    // Coloca la definitiva grupal y la definitiva total del
                    // alumno
                    texto += "<td class=\"resaltado\" align=\"center\">"
                            + OperacionesMatematicas.redondear(curso.getDefinitivaGrupal(alumno,conexion), 1) + "</td>";
                    texto += "<td class=\"" + resaltado
                            + "\" align=\"center\">"
                            + curso.getDefinitiva(alumno, conexion) + "</td>";
                    texto += "</tr>";
                }
            }
            if (!estudiante) {
                // Colocamos el promedio de cada Categoria individual
                texto += ultimaLine;
                for (int z = 0; z < pgIndi.size(); z++) {
                    Categoria pg = (Categoria) pgIndi.get(z);
                    if (pg != null && !pg.isCancelado()) {
                        texto += "<td class=\"resaltado\" align=\"center\">"
                                + pg.getPromedio(curso.getAlumnos()) + "</td>";
                    }
                }
                // Ahora colocamos el promedio de notas individuales
                texto += "<td class=\"resaltado\" align=\"center\">"
                        + curso.getDefinitivaIndividual(conexion) + "</td>";

                // Colocamos el promedio de cada Categoria grupal
                for (int z = 0; z < pgGrup.size(); z++) {
                    Categoria pg = (Categoria) pgGrup.get(z);
                    if (pg != null && !pg.isCancelado()) {
                        texto += "<td class=\"resaltado\" align=\"center\">"
                                + pg.getPromedio(curso.getAlumnos()) + "</td>";
                    }
                }
                // Ahora colocamos el promedio de notas individuales
                texto += "<td class=\"resaltado\" align=\"center\">"
                        + curso.getDefinitivaGrupal(conexion) + "</td>";
                texto += "<td class=\"resaltado\" align=\"center\">"
                        + curso.getPromedio(conexion) + "</td></tr>";
            }
            texto += "</table>";
            out.print(texto);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ControlRecursos.liberarRecursos(conexion);
        }
        return (SKIP_BODY);
    }

    public boolean isEstudiante() {
        return estudiante;
    }

    public void setEstudiante(boolean estudiante) {
        this.estudiante = estudiante;
    }
}
