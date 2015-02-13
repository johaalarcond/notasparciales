/*
 * Created on 12/06/2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.tags;

import java.util.ArrayList;

import co.edu.icesi.notas.basica.*;
import co.edu.icesi.notas.control.*;

import javax.servlet.jsp.*;
import javax.servlet.http.*;
import javax.servlet.jsp.tagext.*;

import co.edu.icesi.notas.form.*;
import co.edu.icesi.notas.utilidades.UtilidadCategories;

/**
 * Tag empleado para desplegar el esquema de un curso del semestre
 * inmediatamente anterior.
 * 
 * @author lmdiaz
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TagCopiaEsquemaAnterior extends TagSupport {

    public int doStartTag() {
        String idUsuario = "";
        try {
            HttpSession session = ControlSesion.obtenerSesion((HttpServletRequest) pageContext.getRequest());
            idUsuario = ControlSesion.getUsuario((HttpServletRequest) pageContext.getRequest());
            JspWriter out = pageContext.getOut();
            Subject sub = (Subject) session.getAttribute("SubjectCopia");
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

                    texto += "<td class=\"celda\">- " + act.getName() + " - "
                            + act.getType() + "</td>";
                    texto += "<td class=\"celda\" align=\"right\">"
                            + act.getPercentage() + "%</td></tr>";
                }
            }
            texto += "</table>" + "</div>";

            session.setAttribute("listaCategorias", listaCategorias);

            out.print(texto);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (SKIP_BODY);

    }
}
