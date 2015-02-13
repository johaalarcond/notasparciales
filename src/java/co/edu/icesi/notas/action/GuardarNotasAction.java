//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl
package co.edu.icesi.notas.action;

import java.sql.Connection;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import co.edu.icesi.notas.*;
import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.form.*;

/**
 * MyEclipse Struts Creation date: 06-17-2005 Acción que guarda las notas en la
 * base de datos a partir del formulario XDoclet definition:
 * 
 * @struts:action path="/guardarNotas" name="guardarNotasForm"
 *                attribute="calificacionForm"
 *                input="/profesores/calificacion.jsp" scope="request"
 *                validate="true"
 * @struts:action-forward name="continuar" path="/profesores/registro.jsp"
 */
public class GuardarNotasAction extends Action {

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
        Connection conexion = null;
        try {
            HttpSession session = ControlSesion.obtenerSesion(request);
            if (session != null) {
                idUsuario = ControlSesion.getUsuario(request);
                String category = null;
                if (session.getAttribute("curso") == null) {
                    request.setAttribute(
                            "mensajeError",
                            "Ha vuelto a la página de listado de cursos pero no ha escogido un curso, por favor escoja un curso del listado inicial.");
                    return mapping.findForward("errorAplicacion");
                }

                ControlProfesores cp = (ControlProfesores) session.getAttribute("profesores");
                Categoria pg = (Categoria) session.getAttribute("padreActual");
                CalificacionForm copia = (CalificacionForm) session.getAttribute("copia");
                Curso curso = (Curso) session.getAttribute("curso");

                if (curso == null || copia == null || pg == null || cp == null) {
                    request.setAttribute("mensajeError",
                            "La aplicación ha recibido parámetros inválidos.");
                    return mapping.findForward("errorAplicacion");
                }
                conexion = ControlRecursos.obtenerConexion();
                CalificacionForm guardarNotasForm = (CalificacionForm) form;
                guardarNotasForm.setEscala(curso.getEscala());
                category = guardarNotasForm.getCategory();
                if (this.isCancelled(request)) {
                    // copiar(copia, pg, conexion, cp, curso, false);
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("cancelaBasico");
                } else {
                    guardarNotasForm.setCopia(copia);
                    guardarNotasForm.setEscala(curso.getEscala());
                    ActionErrors errores = guardarNotasForm.validate(mapping,
                            request);

                    if (errores.size() > 0) {
                        saveErrors(request, errores);
                        guardarNotasForm.setTipoSubmit("guardar");
                        /**
                         * La siguiente condición es para mostrar el fieldset en
                         * la página de configuración en caso de que existan
                         * errores asociados con la propiedad "fallas".
                         */
                        if (errores.get("boton").hasNext()) {
                            request.setAttribute("error", "S");
                        }
                        ControlRecursos.liberarRecursos(conexion);
                        return category == null ? mapping.findForward("fracaso") : mapping.findForward("fracasoBasico");
                    }

                    if (guardarNotasForm.getTipoSubmit().equals("guardar")) {
                        int[] cambios = copiar(guardarNotasForm, pg, conexion,
                                cp, curso, true);
                        if (cambios[1] != 0) {
                            request.setAttribute(
                                    "mensajeError",
                                    "Ha ocurrido un error al guardar las notas. Algunas notas no fueron guardadas o actualizadas correctamente. Por favor verifique las notas de "
                                    + category + ".");
                            return mapping.findForward("errorAplicacion");
                        }
                    } else {
                        if (guardarNotasForm.getTipoSubmit().equals(
                                "recalcular")) {
                            copiar(guardarNotasForm, pg, conexion, cp, curso,
                                    false);
                            pg.setActualizado(false);
                            guardarNotasForm.setTipoSubmit("guardar");
                            // En realidad no es un fracaso, es simplemente para
                            // indicar que retorne a la página
                            // calificaciones.jsp
                            ControlRecursos.liberarRecursos(conexion);
                            return category == null ? mapping.findForward("fracaso") : mapping.findForward("fracasoBasico");
                        }
                    }
                }
                               
                request.setAttribute("notasGuardadas", "S");
                ControlRecursos.liberarRecursos(conexion);
                return category == null ? mapping.findForward("continuar")
                        : mapping.findForward("continuarBasico");
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

    public int[] copiar(CalificacionForm guardarNotasForm, Categoria pg,
            Connection conexion, ControlProfesores cp, Curso curso,
            boolean guardarBD) {
        int[] cambios = new int[2];
        /*
         * if (pg.isActualizado()) return; pg.setActualizado(true);
         */
        for (int i = 0; i < guardarNotasForm.getMatriculas().size(); i++) {
            // Saca las calificaciones de un estudiante
            Calificacion calificacion = (Calificacion) guardarNotasForm.getMatriculas().get(i);
            for (int cont = 0; cont < calificacion.getNotas().size(); cont++) {
                // Saca una nota del estudiante seleccionado
                Nota note = (Nota) calificacion.getNotas().get(cont);
                note.setEscala(curso.getEscala());
                note.setNota(note.getNota().replace(',', '.'));
                // if(!note.isGuardado()){
                Actividad est = pg.getActividad(note.getConsecutivo());
                Matricula matricula = est.getMatricula(calificacion.getCodigo());
                matricula.setActividad(est);
                if (matricula.getAlumno().getEstado().equals("A")) {
                    if (!note.getNota().equals("0")) {
                        matricula.setCalificacion(note.getNotaDouble());
                        // matricula.setCopiaCalificacion(matricula.getCalificacion());
                        // Aqui guarda la nota en la base de datos
                        if (guardarBD) {
                            // est.guardarNotas(conexion, cp.getProfesor());
                            if (matricula.getCopiaCalificacion() == -1) {
                                int guardado = matricula.guardarBd(conexion);
                                if (guardado > 0) {
                                    matricula.setExisteBd(true);
                                    // Envio de correo de insercion de nota
                                    // matricula.getAlumno().cargarMails(conexion);
                                    //ffceballos 
                                    est.fechaIngreso(conexion);
                                    //ffceballos
                                    enviarNuevaNota(matricula, curso, est, pg);
                                } else {
                                    cambios[1] -= 1;
                                }
                                cambios[0] += guardado;
                            } else {
                                if (!matricula.cambioCalificacion()) {
                                    int actualizado = matricula.actualizarBd(conexion);
                                    if (actualizado > 0) {
                                        matricula.setExisteBd(true);
                                        // Envio de correo de actualizacion de
                                        // nota
                                        // matricula.getAlumno().cargarMails(conexion);
                                        enviarActualizacionNota(matricula,
                                                curso, est, pg);
                                    } else {
                                        cambios[1] -= 1;
                                    }
                                    cambios[0] += actualizado;
                                }
                            }
                        }
                    } else {
                        if (matricula.getCopiaCalificacion() != -1) {
                            if (guardarBD) {
                                matricula.eliminarBD(conexion);
                                matricula.setExisteBd(false);
                            } else {
                                matricula.setCalificacion(0);
                                // matricula.setCopiaCalificacion(-1);
                            }
                        } else {
                            matricula.setCalificacion(0);
                        }
                    }
                    // }
                    calificacion.setNombre(matricula.getAlumno().getNombre());
                    calificacion.setEstado(matricula.getAlumno().getEstado());
                }
            }
        }
        guardarNotasForm.setNombre(pg.getNombre());
        return cambios;
    }

    /**
     * Envia las nuevas notas a los alumnos del curso matriculado
     *
     * @param mat
     *            matricula
     * @param curso
     *            curso actual
     * @param est
     *            actividad sobre la que hizo la nueva nota
     * @param pg
     *            categoria a la cual pertenece
     */
    public void enviarNuevaNota(Matricula mat, Curso curso, Actividad est,
            Categoria pg) {
        String asunto = "Nueva nota en " + curso.getNombre();
        String mensaje = "Nueva nota en " + curso.getNombre();
        mensaje += "\n\n";
        mensaje += "El profesor del curso ha ingresado una nueva nota\n\n";
        // mensaje+="Categoría: "+pg.getNombre()+" ("+pg.getPorcentaje()+"% de
        // la nota definitiva).\n";
        // mensaje+="Nombre de actividad: "+est.getNombre()+" - Porcentaje
        // dentro de la categoría: "+est.getPorcentaje()+"%.\n";
        mensaje += "Nombre de actividad: " + est.getNombre() + ".\n";
        mensaje += "Calificación: " + mat.getCalificacion() + ".\n\n";
        mensaje += "Para obtener más información acerca de este curso, visite el portal de estudiantes en http://www.icesi.edu.co/estudiantes.";
        mat.enviarCorreoEstudiante(asunto, mensaje);
    }

    /**
     * Envía la actualización de la nota al estudiante del curso matriculado.
     *
     * @param mat
     * @param curso
     * @param est
     * @param pg
     */
    public void enviarActualizacionNota(Matricula mat, Curso curso,
            Actividad est, Categoria pg) {
        String asunto = "Actualización de nota en " + curso.getNombre();
        String mensaje = "Actualización de nota en " + curso.getNombre();
        mensaje += "\n\n";
        mensaje += "El profesor del curso ha actualizado una nota\n\n";
        // mensaje+="Categoría: "+pg.getNombre()+" ("+pg.getPorcentaje()+"% de
        // la nota definitiva).\n";
        // mensaje+="Nombre de actividad: "+est.getNombre()+" - Porcentaje
        // dentro de la categoría: "+est.getPorcentaje()+"%.\n";
        mensaje += "Nombre de actividad: " + est.getNombre() + ".\n";
        mensaje += "Calificación: " + mat.getCalificacion() + ".\n\n";
        mensaje += "Para obtener más información acerca de este curso, visite el portal de estudiantes en http://www.icesi.edu.co/estudiantes.";
        mat.enviarCorreoEstudiante(asunto, mensaje);
    }
}
