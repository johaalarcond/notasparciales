/*
 * Created on 12/06/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.tags;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import co.edu.icesi.notas.Clasificacion;
import co.edu.icesi.notas.Curso;
import co.edu.icesi.notas.Categoria;
import co.edu.icesi.notas.TipoCategoria;
import co.edu.icesi.notas.control.*;

/**
 * @author mzapata
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TagRegistroNota extends TagSupport {

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
                    ArrayList estructuras = (ArrayList) curso.getActividad(tipo);
                    if (estructuras.size() == 0) {
                        texto += "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td class=\"titulo2\" colspan=\"2\">º&nbsp;&nbsp;"
                                + tipo.getNombre()
                                + " "
                                + pg.getPorcentaje()
                                + "% (No ha<br>configurado ninguna evaluación)</td></tr>\n";
                    } else {
                        if (!estudiante) {
                            texto += "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td class=\"titulo2\" colspan=\"2\">º&nbsp;&nbsp;<a class=\"titulo2\" href=\"./"
                                    + estructura
                                    + ".icesi?tipo="
                                    + tipo.getConsecutivo()
                                    + "\">"
                                    + tipo.getNombre()
                                    + "</a> "
                                    + pg.getPorcentaje() + "%</td></tr>\n";
                        } else {
                            texto += "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td class=\"titulo2\" colspan=\"2\">º&nbsp;&nbsp;<a class=\"titulo2\" href=\"./"
                                    + estructura
                                    + ".icesi?tipo="
                                    + tipo.getConsecutivo()
                                    + "&estud=true\">"
                                    + tipo.getNombre()
                                    + "</a> "
                                    + pg.getPorcentaje() + "%</td></tr>\n";
                        }
                    }
                }
            }

            texto += "<tr><td class=\"titulo2\" colspan=\"3\">"
                    + grupales.getNombre() + " - " + grupales.getPorcentaje()
                    + "%</td></tr>";

            for (int i = 0; i < grupales.getCategorias().size(); i++) {
                Categoria pg = (Categoria) grupales.getCategorias().get(i);
                TipoCategoria tipo = pg.getTipoCategoria();
                if (!pg.isCancelado()) {
                    ArrayList estructuras = (ArrayList) curso.getActividad(tipo);
                    if (estructuras.size() == 0) {
                        texto += "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td class=\"titulo2\" colspan=\"2\">º&nbsp;&nbsp;"
                                + tipo.getNombre()
                                + " "
                                + pg.getPorcentaje()
                                + "% (No ha<br>configurado ninguna evaluación)</td></tr>\n";
                    } else {
                        if (!estudiante) {
                            texto += "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td class=\"titulo2\" colspan=\"2\">º&nbsp;&nbsp;<a class=\"titulo2\" href=\"./"
                                    + estructura
                                    + ".icesi?tipo="
                                    + tipo.getConsecutivo()
                                    + "\">"
                                    + tipo.getNombre()
                                    + "</a> "
                                    + pg.getPorcentaje() + "%</td></tr>\n";
                        } else {
                            texto += "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td class=\"titulo2\" colspan=\"2\">º&nbsp;&nbsp;<a class=\"titulo2\" href=\"./"
                                    + estructura
                                    + ".icesi?tipo="
                                    + tipo.getConsecutivo()
                                    + "&estud=true\">"
                                    + tipo.getNombre()
                                    + "</a> "
                                    + pg.getPorcentaje() + "%</td></tr>\n";
                        }
                    }
                }
            }

            texto += "</table>";
            out.print(texto);
            out.flush();
        } catch (Exception e) {
            System.out.println("Error en el tag esquema: " + e.getMessage());
        }
        return (SKIP_BODY);
    }
    private boolean estudiante;

    public boolean isEstudiante() {
        return estudiante;
    }

    public void setEstudiante(boolean estudiante) {
        this.estudiante = estudiante;
    }
    private String padre;

    public String getPadre() {
        return padre;
    }

    public void setPadre(String padre) {
        this.padre = padre;
    }
    private String estructura;

    public String getEstructura() {
        return estructura;
    }

    public void setEstructura(String estructura) {
        this.estructura = estructura;
    }
}
