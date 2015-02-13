//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl
package co.edu.icesi.notas.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import co.edu.icesi.notas.*;
import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.form.*;

import java.util.*;

/**
 * MyEclipse Struts Creation date: 06-17- 2005 Acción que acomoda y prepara los
 * formularios de notas de estudiantes, antes de su modificación. XDoclet
 * definition:
 * 
 * @struts:action path="/calificacion" name="calificacionForm"
 *                input="/profesores/calificacion.jsp" scope="request"
 *                validate="true"
 * @struts:action-forward name="guardar" path="/profesores/calificacion.jsp"
 * @struts:action-forward name="aceptar" path="/profesores/registro.jsp"
 */
public class CalificacionAction extends Action {

    // --------------------------------------------------------- Instance
    // Variables
    // --------------------------------------------------------- Methods
    /**
     * Method execute
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        String idUsuario = "";
        try {
            HttpSession session = ControlSesion.obtenerSesion(request);
            if (session != null) {
                idUsuario = ControlSesion.getUsuario(request);
                if (this.isCancelled(request)) {
                    // se supone que no hay ninguna estructura en la session
                    // si la hay toca quitarla
                    return mapping.findForward("atras");
                }

                if (session.getAttribute("curso") == null) {
                    request.setAttribute(
                            "mensajeError",
                            "Ha vuelto a la página de listado de cursos pero no ha escogido un curso, por favor escoja un curso del listado inicial.");
                    return mapping.findForward("errorAplicacion");
                }

                Curso curso = (Curso) session.getAttribute("curso");

                CalificacionForm calificacionForm = (CalificacionForm) form;
                calificacionForm.setEscala(curso.getEscala());
                calificacionForm.getMatriculas().clear();

                String categoria = request.getParameter("categoria");
                Categoria pg;
                if (categoria == null) {
                    String tipo = request.getParameter("tipo");
                    if (tipo == null || tipo.equals("")) {
                        request.setAttribute("mensajeError",
                                "La aplicación ha recibido parámetros inválidos.");
                        return mapping.findForward("errorAplicacion");
                    }
                    pg = (Categoria) curso.getCategoriaByTipo(Integer.parseInt(tipo));
                } else {
                    pg = curso.getCategoria(categoria);
                }
                String estud = request.getParameter("estud");
                boolean esEstudiante = (estud != null && estud.equals("true")) ? true
                        : false;

                pg.setActualizado(false);
                session.setAttribute("padreActual", pg);
                ArrayList alumnos = curso.getAlumnos();
                /*******************************************************************
                 * una copia del objeto CalificacionForm actual, para emplearlo
                 * en caso de que el usuario no guarde los cambios recientemente
                 * realizados en el registro de notas.
                 ******************************************************************/
                CalificacionForm copia = new CalificacionForm();
                copia.setEscala(curso.getEscala());
                for (int cont2 = 0; cont2 < alumnos.size(); cont2++) {
                    co.edu.icesi.notas.Alumno alumno = (co.edu.icesi.notas.Alumno) alumnos.get(cont2);
                    Calificacion c = new Calificacion();
                    c.setEscala(curso.getEscala());
                    c.setCodigo(new String(alumno.getCodigo()));
                    c.setNombre(new String(alumno.getNombre()));

                    if (!esEstudiante) {
                        c.setEstado(new String(alumno.getEstado()));
                    }

                    Calificacion copiaCalif = new Calificacion();
                    if (!esEstudiante) {
                        copiaCalif.setEscala(curso.getEscala());
                        copiaCalif.setCodigo(new String(alumno.getCodigo()));
                        copiaCalif.setNombre(new String(alumno.getNombre()));
                        copiaCalif.setEstado(new String(alumno.getEstado()));
                    }
                    Nota nota, notaCopia;
                    String cod;
                    Matricula m;
                    Actividad estructura;
                    for (int cont3 = 0; cont3 < pg.getActividades().size(); cont3++) {
                        estructura = (Actividad) pg.getActividades().get(cont3);
                        for (int i = 0; i < estructura.getMatriculas().size(); i++) {
                            m = (Matricula) estructura.getMatriculas().get(i);
                            cod = m.getAlumno().getCodigo();
                            if (alumno.igualCodigo(cod)) {
                                nota = new Nota();
                                nota.setEscala(curso.getEscala());
                                // Si la copia de la calificación es -1, esto
                                // indica
                                // que la nota
                                // actualmente no existe en la BD.
                                if (m.getCopiaCalificacion() != -1) {
                                    nota.setNota(String.valueOf(m.getCalificacion()));
                                } else {
                                    //ffceballos 12/05/2014 se cambió el 0 por un guión
                                    nota.setNota("0");
                                }
                                if (!esEstudiante) {
                                    nota.setCopiaNota(String.valueOf(m.getCopiaCalificacion()));
                                }
                                nota.setNombreEstructura(new String(estructura.getNombre()));
                                nota.setConsecutivo(estructura.getConsecutivo());
                                 c.getNotas().add(nota);
                                if (!esEstudiante) {
                                    notaCopia = new Nota();
                                    notaCopia.setEscala(curso.getEscala());
                                    if (m.getCopiaCalificacion() != -1) {
                                        notaCopia.setNota(String.valueOf(m.getCalificacion()));
                                    } else {
                                        notaCopia.setNota("0");
                                    }
                                    notaCopia.setCopiaNota(String.valueOf(m.getCopiaCalificacion()));
                                    notaCopia.setNombreEstructura(new String(
                                            estructura.getNombre()));
                                    notaCopia.setConsecutivo(estructura.getConsecutivo());
                                    copiaCalif.getNotas().add(notaCopia);
                                }
                            }
                        }
                    }
                    calificacionForm.getMatriculas().add(c);
                    if (!esEstudiante) {
                        copia.getMatriculas().add(copiaCalif);
                    }
                }
                calificacionForm.setTipoSubmit("guardar");
                calificacionForm.setNombre(new String(pg.getNombre()));
                calificacionForm.setCategory(categoria);

                if (!esEstudiante) {
                    copia.setNombre(new String(pg.getNombre()));
                    copia.setCategory(categoria);
                    // }
                    // Guardamos la copia en la sesión.

                    /*
                     * estas lineas se adicionaron para indexar el contenido de
                     * las tablas de notas verticalmente
                     */
                    ArrayList tamannoM = calificacionForm.getMatriculas();
                    session.setAttribute("tamannoM", tamannoM);
                    /*-------------------------------------------------*/
                    session.setAttribute("copia", copia);
                }
                if (!esEstudiante) {
                    if (categoria == null) {
                        return mapping.findForward("continuar");
                    } else {
                        return mapping.findForward("continuarBasico");
                    }
                }
            }
            System.out.println("Sesión inactiva: " + this.getClass().getName());
            return mapping.findForward("sesionInactiva");
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Usuario: " + request.getRemoteUser());
            System.out.println("Descripción:");
            e.printStackTrace();
        }
        return mapping.findForward("continuarEstud");
    }
}
