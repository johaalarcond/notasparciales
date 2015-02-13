/*
 * Created on 10-jun-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.tags;

import java.util.*;
import co.edu.icesi.notas.*;
import co.edu.icesi.notas.control.*;

import javax.servlet.jsp.*;
import javax.servlet.http.*;
import javax.servlet.jsp.tagext.*;

/**
 * @author drojas
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TagEsquema extends TagSupport {

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
            Curso curso = (Curso) session.getAttribute("curso");
            String texto = "<table cellspacing=\"1\" cellpadding=\"10\" align=\"center\" class=\"tabla\">";
            Clasificacion individuales = curso.getIndividuales();
            Clasificacion grupales = curso.getGrupales();
            texto += "<tr><td class=\"titulo2\" colspan=\"3\">"
                    + individuales.getNombre() + " - "
                    + individuales.getPorcentaje() + "%</td></tr>";

            for (int i = 0; i < individuales.getCategorias().size(); i++) {
                Categoria pg = (Categoria) individuales.getCategorias().get(i);
                TipoCategoria tipo = pg.getTipoCategoria();
                if (!pg.isCancelado()) {
                    if (!estudiante) {
                        texto += "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td class=\"titulo2\" colspan=\"2\">º&nbsp;&nbsp;<a class=\"titulo2\" href=\"./"
                                + padre
                                + ".icesi?tipo="
                                + tipo.getConsecutivo()
                                + "\">"
                                + tipo.getNombre()
                                + "</a> "
                                + pg.getPorcentaje() + "%</td></tr>\n";
                    } else {
                        texto += "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td class=\"titulo2\" colspan=\"2\">º&nbsp;&nbsp;"
                                + tipo.getNombre()
                                + " "
                                + pg.getPorcentaje()
                                + "%</td></tr>\n";
                    }
                    ArrayList estructuras = (ArrayList) curso.getActividad(tipo);
                    for (int j = 0; j < estructuras.size(); j++) {
                        Actividad est = (Actividad) estructuras.get(j);
                        // este parámetro se emplea para distinguir cuando se
                        // esta invocando el tag desde una pagina
                        // del modulo de estudiantes.
                        String codEstud = estudiante ? "&estud=true" : "";
                        texto += "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td>-&nbsp;&nbsp;<a  class=\"info\" href=\"./"
                                + estructura
                                + ".icesi?codEst="
                                + est.getConsecutivo()
                                + "&tipo="
                                + tipo.getConsecutivo()
                                + codEstud
                                + "\">"
                                + est.getNombre()
                                + " "
                                + est.getFechaRealizacion()
                                + " "
                                + est.getPorcentaje() + "%</a></td></tr>\n";
                    }
                    /***
                     * if(estructuras.size()==0) texto+="</tr>";
                     ***/
                }
            }

            texto += "<tr><td class=\"titulo2\" colspan=\"3\">"
                    + grupales.getNombre() + " - " + grupales.getPorcentaje()
                    + "%</td></tr>";

            for (int i = 0; i < grupales.getCategorias().size(); i++) {
                Categoria pg = (Categoria) grupales.getCategorias().get(i);
                TipoCategoria tipo = pg.getTipoCategoria();
                if (!pg.isCancelado()) {
                    if (!estudiante) {
                        texto += "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td class=\"titulo2\" colspan=\"2\">º&nbsp;&nbsp;<a class=\"titulo2\" href=\"./"
                                + padre
                                + ".icesi?tipo="
                                + tipo.getConsecutivo()
                                + "\">"
                                + tipo.getNombre()
                                + "</a> "
                                + pg.getPorcentaje() + "%</td></tr>\n";
                    } else {
                        texto += "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td class=\"titulo2\" colspan=\"2\">º&nbsp;&nbsp;"
                                + tipo.getNombre()
                                + " "
                                + pg.getPorcentaje()
                                + "%</td></tr>\n";
                    }
                    ArrayList estructuras = (ArrayList) curso.getActividad(tipo);
                    for (int j = 0; j < estructuras.size(); j++) {
                        Actividad est = (Actividad) estructuras.get(j);
                        String codEstud = estudiante ? "&estud=true" : "";
                        texto += "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td>-&nbsp;&nbsp;<a  class=\"info\" href=\"./"
                                + estructura
                                + ".icesi?codEst="
                                + est.getConsecutivo()
                                + "&tipo="
                                + tipo.getConsecutivo()
                                + codEstud
                                + "\">"
                                + est.getNombre()
                                + " "
                                + est.getFechaRealizacion()
                                + " "
                                + est.getPorcentaje() + "%</a></td></tr>\n";
                    }
                    /**
                     * if(estructuras.size()==0) texto+="</tr>";
                     ****/
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
    private boolean estudiante;
    private String padre;

    /**
     * @return Returns the pagina.
     */
    public String getPadre() {
        return padre;
    }

    /**
     * @param pagina
     *            The pagina to set.
     */
    public void setPadre(String padre) {
        this.padre = padre;
    }
    private String estructura;

    /**
     * @return Returns the estructura.
     */
    public String getEstructura() {
        return estructura;
    }

    /**
     * @param estructura
     *            The estructura to set.
     */
    public void setEstructura(String estructura) {
        this.estructura = estructura;
    }

    public boolean isEstudiante() {
        return estudiante;
    }

    public void setEstudiante(boolean estudiante) {
        this.estudiante = estudiante;
    }
}
