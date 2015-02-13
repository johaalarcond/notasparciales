package co.edu.icesi.notas.tags;

import java.util.*;
import java.lang.*;
import java.net.*;

import javax.servlet.http.*;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import co.edu.icesi.notas.basica.Subject;
import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.form.Category;
import co.edu.icesi.notas.utilidades.UtilidadCategories;

/**
 * Aunque el nombre de este tag parezca contradictorio, esta clase se emplea
 * tanto para la impresión de esquemas básicos como de esquemas intermedios.
 * 
 * @author mzapata
 * Cambio ffceballos, porque estaba sucediendo problemas con las tildes.
 * 
 */
public class TagNotaBasica extends TagSupport {

    private boolean estudiante;

    public int doStartTag() {
        String idUsuario = "";
        try {
            HttpSession session = ControlSesion.obtenerSesion((HttpServletRequest) pageContext.getRequest());
            idUsuario = ControlSesion.getUsuario((HttpServletRequest) pageContext.getRequest());
            JspWriter out = pageContext.getOut();

            String texto = "<table cellspacing=\"1\" cellpadding=\"10\" align=\"center\" class=\"tabla\">";

            Subject sub = (Subject) session.getAttribute("subject");

            List actiOrganizadas = UtilidadCategories.organizarActividades(sub.getActivities());

            Iterator itera = actiOrganizadas.iterator();

            boolean primeraInd = true, primeraGrup = true;
            String tipoConf = sub.getCurso().getTipoConfiguracion();

            Category cat;
            while (itera.hasNext()) {
                cat = (Category) itera.next();

                if (primeraInd && tipoConf.equals("I")
                        && cat.getActivitiesType().equals("I")) {
                    texto += "<tr><td colspan=\"2\" class=\"celda2\">"
                            + sub.getCurso().getIndividuales().getNombre()
                            + "</td></tr>";
                    primeraInd = false;
                }
                if (primeraGrup && tipoConf.equals("I")
                        && cat.getActivitiesType().equals("G")) {
                    texto += "<tr><td colspan=\"2\" class=\"celda2\">"
                            + sub.getCurso().getGrupales().getNombre()
                            + "</td></tr>";
                    primeraGrup = false;
                }
                /*ffceballos*/
                String nombreCodificado=java.net.URLEncoder.encode(cat.getName().toString(), "ISO-8859-1");
                
                if (!estudiante) {
                    texto += "<tr><td>&nbsp;</td><td class=\"titulo2\"><a class=\"titulo2\" href=\"./"
                            + "calificacion.icesi?categoria="
                            + nombreCodificado// cat.getName()
                            + "\">" + cat.getName() + "</a> </td></tr>\n";
                } else {
                    texto += "<tr><td>&nbsp;</td><td class=\"titulo2\"><a class=\"titulo2\" href=\"./"
                            + "calificacion.icesi?categoria="
                            + nombreCodificado//cat.getName()
                            + "&estud=true\">"
                            + cat.getName()
                            + "</a> </td></tr>\n";
                }
            }
            texto += "</table>";

            out.print(texto);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
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
