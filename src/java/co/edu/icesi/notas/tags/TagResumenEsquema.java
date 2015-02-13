/*
 * Created on 13-dic-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.tags;

import java.util.*;

import co.edu.icesi.notas.basica.*;
import co.edu.icesi.notas.control.*;

import javax.servlet.jsp.*;
import javax.servlet.http.*;
import javax.servlet.jsp.tagext.*;

import co.edu.icesi.notas.form.*;
import co.edu.icesi.notas.utilidades.*;

/**
 * @author lmdiaz , mzapata
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TagResumenEsquema extends TagSupport {

    private boolean estudiante;
    private boolean jefe;

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() {
        String idUsuario = "";
        try {
            HttpSession session = ControlSesion.obtenerSesion((HttpServletRequest) pageContext.getRequest());
            idUsuario = ControlSesion.getUsuario((HttpServletRequest) pageContext.getRequest());
            JspWriter out = pageContext.getOut();
            Subject sub = (Subject) session.getAttribute("subject");
            String tipoConf = sub.getCurso().getTipoConfiguracion();
            ArrayList listaCategorias = UtilidadCategories.organizarActividades(sub.getActivities());
            String texto = "" + "<div align=\"center\">"
                    + "<table class=\"tabla\">";

            //catorres
            
            if (tipoConf.equals("I")) {
                texto += "" + "<tr>"
                        + "<td height=\"21\" colspan=\"2\" align=\"center\" valign=\"middle\">"
                        + "<br />"
                        + "<b>"
                        + "Esquema Intermedio"
                        + "</b>"
                        + "<br />"
                        +"<td/>"
                        + " <div/>";
                
            }else{
                texto += "" + "<tr>"
                        +  "<td height=\"21\" colspan=\"2\" align=\"center\" valign=\"middle\">"
                         + "<br />"
                        + "<b>"
                        + "Esquema Básico"
                        + " </b>"
                        + "<br />"
                        + "<br />"
                        +"<td/>"
                        + " <div/>";
            }

            
              //catorres
            
            
            if (tipoConf.equals("I")) {
                texto += "<tr><td colspan=\"3\" class=\"celda2\">"
                        + sub.getCurso().getIndividuales().getNombre()
                        + "</td>";
                texto += "<td class=\"celda2\" align=\"right\">"
                        + sub.getCurso().getIndividuales().getPorcentaje()
                        + "%</td></tr>";
            }

            boolean imprimeGrupal = true;
            String contextPath = ((HttpServletRequest) pageContext.getRequest()).getContextPath();

            for (int i = 0; i < listaCategorias.size(); i++) {
                Category cat = (Category) listaCategorias.get(i);

                if (cat.getActivitiesType().equals("G") && imprimeGrupal
                        && tipoConf.equals("I")) {
                    texto += "<tr><td colspan=\"3\" class=\"celda2\">"
                            + sub.getCurso().getGrupales().getNombre()
                            + "</td>";
                    texto += "<td class=\"celda2\" align=\"right\">"
                            + sub.getCurso().getGrupales().getPorcentaje()
                            + "%</td></tr>";
                    imprimeGrupal = false;
                }

                texto += "<tr>";

                if (tipoConf.equals("I")) {
                    texto += "<td>&nbsp;</td>";
                }

                texto += "<td colspan=\"3\" class=\"titulo2\"><div align=\"left\">"
                        + cat.getName() + "</div></td>" + "</tr>\n";
                for (int j = 0; j < cat.getListActivities().size(); j++) {
                    Activity act = (Activity) cat.getActivity(j);
                    texto += "<tr><td>&nbsp;</td>";

                    if (tipoConf.equals("I")) {
                        texto += "<td>&nbsp;</td>";
                    }

                    if (estudiante) {
                        texto += "<td class=\"celda\">- <a href=\""
                                + contextPath
                                + "/mostrarActividadEstud.icesi?index1=" + i
                                + "&index2=" + j + "\">" + act.getName()
                                + " - " + act.getType() + "</a></td>";
                    } else {
                        if (jefe) {
                            texto += "<td class=\"celda\">- <a href=\""
                                    + contextPath
                                    + "/jefes/mostrarActividadJefe.icesi?index1="
                                    + i + "&index2=" + j + "\">"
                                    + act.getName() + " - " + act.getType()
                                    + "</a></td>";
                        } else {
                            texto += "<td class=\"celda\">- <a href=\""
                                    + contextPath
                                    + "/modificarActividad.icesi?index1=" + i
                                    + "&index2=" + j + "\">" + act.getName()
                                    + " - " + act.getType() + "</a></td>";
                        }
                    }

                    texto += "<td class=\"celda\" align=\"right\">"
                            + act.getPercentage() + "%</td></tr>";
                }
            }
            texto += "</table>" + "</div>";

            if (jefe) {
                double numNotasReg = sub.getCurso().obtenerNumeroNotasRegsitradas();
                double numNotas = sub.getCurso().obtenerNumeroTotalNotas();
                double porcentaje = 0;
                if (numNotas != 0) {
                    porcentaje = (OperacionesMatematicas.redondear(numNotasReg
                            / numNotas, 4)) * 100;

                }
                texto += "<div><br></div>";
                texto += "<div align=\"center\">";
                texto += "	<table class=\"tabla\" >"
                        + "<tr>"
                        + "<td height=\"41\" colspan=\"2\" class=\"curso\" align=\"center\">Estad&iacute;sticas</td>"
                        + "</tr>"
                        + "<tr>"
                        + "<td width=\"200\" class=\"celda\">Total de Notas registradas </td>"
                        + "<td width=\"110\" class=\"celda\" align=\"center\">"
                        + (int) numNotasReg
                        + " de "
                        + (int) numNotas
                        + "</td>"
                        + "</tr>"
                        + "<tr>"
                        + "<td class=\"celda\">Porcentaje de Notas registradas </td>"
                        + "<td class=\"celda\" align=\"center\">" + porcentaje
                        + "%</td>" + "</tr>" + "</table>";
                texto += "</div>";

            }

            session.setAttribute("listaCategorias", listaCategorias);

            out.print(texto);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (SKIP_BODY);
    }

    /**
     * @return Returns the estudiante.
     */
    public boolean isEstudiante() {
        return estudiante;
    }

    /**
     * @param estudiante
     *            The estudiante to set.
     */
    public void setEstudiante(boolean estudiante) {
        this.estudiante = estudiante;
    }

    public boolean isJefe() {
        return jefe;
    }

    public void setJefe(boolean jefe) {
        this.jefe = jefe;
    }
}
