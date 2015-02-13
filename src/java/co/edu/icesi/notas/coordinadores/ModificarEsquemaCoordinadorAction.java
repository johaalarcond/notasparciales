package co.edu.icesi.notas.coordinadores;

import co.edu.icesi.notas.action.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

import co.edu.icesi.notas.*;
import co.edu.icesi.notas.form.ModificarEsquemaForm;
import co.edu.icesi.notas.form.ModificarPorcentajesForm;
import co.edu.icesi.notas.utilidades.UtilidadCategories;
import co.edu.icesi.notas.basica.*;
import co.edu.icesi.notas.control.*;

import java.sql.Connection;
import java.util.*;

/**
 * MyEclipse Struts Creation date: 12-12-2006 Esta acción permite preparar la
 * página en la que se van a agregar o eliminar las actividades que van a formar
 * el curso, asignándoles categorias y clasificaciones. XDoclet definition:
 * 
 * @struts:action path="/modificarEsquema" name="modificarEsquemaForm"
 *                scope="request" validate="true"
 */
public class ModificarEsquemaCoordinadorAction extends Action {

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
        Connection conexion = null;
        try {
            HttpSession sesion = ControlSesion.obtenerSesion(request);
            if (sesion != null) {
                idUsuario = ControlSesion.getUsuario(request);
                if (sesion.getAttribute("curso") == null) {
                    request.setAttribute(
                            "mensajeError",
                            "Ha vuelto a la página de listado de cursos pero no ha escogido un curso, por favor escoja un curso del listado inicial.");
                    return mapping.findForward("errorAplicacion");
                }

                sesion.removeAttribute("errores");

                if (this.isCancelled(request)) {
                    return mapping.findForward("seleccion");
                }

                ModificarEsquemaForm modificarEsquemaForm = (ModificarEsquemaForm) form;
                Subject sub = (Subject) sesion.getAttribute("subject");
                ControlProfesores cp = (ControlProfesores) sesion.getAttribute("profesores");
                Curso curso = (Curso) sesion.getAttribute("curso");

                if (modificarEsquemaForm == null || sub == null || cp == null) {
                    request.setAttribute("mensajeError",
                            "La aplicación ha recibido parámetros inválidos.");
                    return mapping.findForward("errorAplicacion");
                }

                ActionErrors errores = modificarEsquemaForm.validate(mapping,
                        request);
                List lista = modificarEsquemaForm.getActivities();
                sub.setActivities(lista);
                String opcion = modificarEsquemaForm.getOpcion();

                // Si se desea eliminar una actividad
                if (opcion.equals("0")) {
                    // Debe existir por lo menos una actividad.
                    if (lista.size() > 1) {
                        String indice = modificarEsquemaForm.getIndice();
                        // Obtenemos y borramos el elemento que se desea
                        // eliminar.
                        Activity activity = (Activity) lista.remove(Integer.parseInt(indice));
                        modificarEsquemaForm.setActivities(lista);
                        // en caso de que el consecutivo sea cero, resulta
                        // necesario
                        // eliminar la actividad de la BD.
                        if (activity.getConsecutive() != 0) {
                            conexion = ControlRecursos.obtenerConexion();
                            boolean elimino = sub.eliminarActitivy(conexion,
                                    activity.getConsecutive());
                            if (elimino) {
                                /*
                                 * Este atributo se empleará para deshabilitar o
                                 * habilitar el botón cancelar de la página
                                 * /basica/ingresaActividad.jsp dependiendo de
                                 * si se ha eliminado o no alguna actividad.
                                 */
                                sesion.setAttribute("actividadEliminada", "S");
                                request.setAttribute("modificarEsquemaForm",
                                        modificarEsquemaForm);
                            } else {
                                sesion.setAttribute("errores", "S");

                                errores.add("errorEliminar", new ActionError(
                                        "borrar.error"));
                                saveErrors(request, errores);
                            }
                        }
                        ControlRecursos.liberarRecursos(conexion);
                        return mapping.findForward("regresa");
                    } else {
                        errores.add("actividadCero", new ActionError(
                                "errores.actividadCero"));
                        saveErrors(request, errores);
                        sesion.setAttribute("errores", "S");
                        ControlRecursos.liberarRecursos(conexion);
                        return mapping.findForward("regresa");
                    }
                }

                // Si se desea agregar una actividad
                if (opcion.equals("1")) {
                    Activity nueva = new Activity();
                    lista.add(nueva);
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("regresa");
                }

                // Si se desea continuar

                // Validar de que los nombres de las actividades no estén vacios
                if (!validarNombresVacios(lista)) {
                    errores.add("nombresVacios", new ActionError(
                            "errores.nombresVacios"));
                    saveErrors(request, errores);
                    sesion.setAttribute("errores", "S");
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("regresa");
                }

                // Validar que si hay una actividad con categoría otra, el campo
                // otra no
                // esté vacío
                if (!validarNombresOtraCategoriaVacios(lista)) {
                    errores.add("nombresVacios", new ActionError(
                            "errores.nombresOtrasVacios"));
                    saveErrors(request, errores);
                    sesion.setAttribute("errores", "S");
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("regresa");
                }

                // Validar que las categorias ingresadas en 'Otras' no existan
                // en la
                // lista de categorías.
                if (!validarCategorias(cp.getTiposCategoria(), lista)) {
                    errores.add("categoriasRepetidas", new ActionError(
                            "errores.categoriasRepetidas"));
                    saveErrors(request, errores);
                    sesion.setAttribute("errores", "S");
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("regresa");
                }

                // Validar los nombres unicos por categoria
                if (!validarNombres(lista)) {
                    errores.add("nombresUnicos", new ActionError(
                            "errores.nombresUnicos"));
                    saveErrors(request, errores);
                    sesion.setAttribute("errores", "S");
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("regresa");
                }
                // Validar que los nombres sean unicos para todo el curso
                if (!validarNombresRepetidos(lista)) {
                    errores.add("nombresRepetidos", new ActionError(
                            "errores.nombresRepetidos"));
                    saveErrors(request, errores);
                    sesion.setAttribute("errores", "S");
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("regresa");
                }

                // Validar que una misma categoria no tenga clasificaciones
                // distintas
                if (!validarClasificacionCategorias(lista)) {
                    errores.add("categoriaClasificacion", new ActionError(
                            "errores.categoriaClasificacion"));
                    saveErrors(request, errores);
                    sesion.setAttribute("errores", "S");
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("regresa");
                }

                /*
                 * Validar, en caso de que sea un esquema intermedio, que se
                 * hayan ingresado al menos una actividad individual y una
                 * grupal.
                 */
                if (!validarActividadIndividualGrupal(curso.getTipoConfiguracion(), lista)) {
                    errores.add("errorCantIndGrupal", new ActionError(
                            "cantidad.individual.grupal"));
                    saveErrors(request, errores);
                    sesion.setAttribute("errores", "S");
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("regresa");
                }

                // Organizar y agrupar actividades por categorias
                ArrayList organizacion = UtilidadCategories.organizarActividades(lista);
                ModificarPorcentajesForm modificarPorcentajeForm = new ModificarPorcentajesForm();
                modificarPorcentajeForm.setListaCategorias(organizacion);
                // Asignamos el porcentaje de la nota individual y la nota
                // grupal
                this.asignarPorcentajeIndvidualGrupal(modificarPorcentajeForm,
                        curso);

                request.setAttribute("modificarPorcentajesForm",
                        modificarPorcentajeForm);
                sesion.setAttribute("subject", sub);
                sesion.removeAttribute("actividadEliminada");
                ControlRecursos.liberarRecursos(conexion);
                return mapping.findForward(ControlSecuencia.getNextModificarEsquema(curso.getTipoConfiguracion()));
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

    /**
     * Este método se encarga de validar que en caso de que el curso posea un
     * esquema intermedio, se hayan ingresado al menos una actividad individual
     * y una actividad grupal.
     *
     * @return true si la validación fue exitosa.
     */
    public boolean validarActividadIndividualGrupal(String tipoConf,
            List actividades) {
        if (tipoConf.equals("I")) {
            boolean tieneInd = false, tieneGrupal = false;

            Iterator iterador = actividades.iterator();
            Activity activity;
            while (iterador.hasNext()) {
                activity = (Activity) iterador.next();
                if (activity.getType().equals("I")) {
                    tieneInd = true;
                } else {
                    if (activity.getType().equals("G")) {
                        tieneGrupal = true;
                    }
                }
                if (tieneInd && tieneGrupal) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Este método asigna el porcentaje de la nota individual y la nota grupal,
     * si el tipo de esquema es intermedio.
     */
    private void asignarPorcentajeIndvidualGrupal(
            ModificarPorcentajesForm form, Curso curso) {
        if (curso.getTipoConfiguracion().equals("I")) { // I: Intermedio
            // if(curso.getIndividuales()!=null)
            form.setPorcentajeIndividual(curso.getIndividuales().getPorcentaje());
            form.setPorcentajeGrupal(curso.getGrupales().getPorcentaje());
        }
    }

    /***************************************************************************
     *
     * @param tiposCategoria
     * @param actividades
     * @return
     */
    private boolean validarCategorias(List tiposCategoria, List actividades) {
        Iterator iterador = actividades.iterator(), iterador2;
        Activity activity;
        TipoCategoria tipoCateg;
        while (iterador.hasNext()) {
            activity = (Activity) iterador.next();
            iterador2 = tiposCategoria.iterator();
            while (iterador2.hasNext()) {
                tipoCateg = (TipoCategoria) iterador2.next();
                if (activity.getOther() != null
                        && activity.getOther().trim().equalsIgnoreCase(
                        tipoCateg.getNombre().trim())) {
                    return false;
                }
            }
        }
        return true;
    }

    /***************************************************************************
     * Valida que no haya nombres repetidos dentro de una categoria. Importante
     * para hacer búsquedas en la base de datos
     *
     * @param actividades
     * @return
     */
    private boolean validarNombres(List actividades) {
        boolean nam, cat;
        boolean comparaCat, comparaCatOther, comparaOtherCat, comparaOther;
        Activity act, compara;
        for (int i = 0; i < actividades.size(); i++) {
            act = (Activity) actividades.get(i);
            for (int j = 0; j < actividades.size(); j++) {
                compara = (Activity) actividades.get(j);
                nam = compara.getName().trim().equalsIgnoreCase(
                        act.getName().trim());

                comparaCat = compara.getCategory() != null
                        && compara.getCategory().trim().equalsIgnoreCase(
                        act.getCategory());
                comparaCatOther = compara.getCategory() != null
                        && compara.getCategory().equalsIgnoreCase(
                        act.getOther());
                comparaOtherCat = compara.getOther() != null
                        && compara.getOther().equalsIgnoreCase(
                        act.getCategory());
                comparaOther = compara.getOther() != null
                        && compara.getOther().equalsIgnoreCase(act.getOther());
                cat = comparaCat || comparaCatOther || comparaOtherCat
                        || comparaOther;
                if (i != j && nam && cat) {
                    return false;
                }
            }
        }

        return true;
    }

    /***************************************************************************
     * Valida que el nombre de alguna actividad este vacio
     *
     * @param actividades
     * @return true: si no hay vacios false: si hay vacios
     */
    private boolean validarNombresVacios(List actividades) {
        for (int i = 0; i < actividades.size(); i++) {
            String nom = ((Activity) actividades.get(i)).getName();
            if (nom == null || nom.trim().equals("")) {
                return false;
            }
        }
        return true;
    }

    private boolean validarNombresOtraCategoriaVacios(List actividades) {
        for (int i = 0; i < actividades.size(); i++) {
            if (((Activity) actividades.get(i)).getCategoryName().equalsIgnoreCase("Otra")) {
                if (((Activity) actividades.get(i)).getOther() == null
                        || ((Activity) actividades.get(i)).getOther().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    /***************************************************************************
     * Valida si una categoria no tiene más de dos clasificaciones distintas
     * dentro de sus actividades. Es decir parciales no puede tener actividades
     * individuales y grupales al mismo tiempo. Para esto se debe crear una
     * categoria nueva.
     *
     * @param actividades
     * @return true: si no hay incoherencia false: si hay incoherencia
     */
    private boolean validarClasificacionCategorias(List actividades) {
        boolean cat;
        boolean comparaCat, comparaCatOther, comparaOtherCat, comparaOther;
        for (int i = 0; i < actividades.size(); i++) {
            Activity act = (Activity) actividades.get(i);
            String categ = act.getCategory();
            String tipo = act.getType();
            for (int j = 0; j < actividades.size(); j++) {
                Activity compara = (Activity) actividades.get(j);

                if (categ.equals("Otra")
                        && compara.getCategory().equals("Otra")) {
                    if (act.getOther() != null
                            && act.getOther().equalsIgnoreCase(
                            compara.getOther())) {
                        if (!compara.getType().equals(tipo)) {
                            return false;
                        }
                    }
                } else {
                    comparaCat = compara.getCategory() != null
                            && compara.getCategory().trim().equalsIgnoreCase(
                            act.getCategory());
                    comparaCatOther = compara.getCategory() != null
                            && compara.getCategory().equalsIgnoreCase(
                            act.getOther());
                    comparaOtherCat = compara.getOther() != null
                            && !compara.getOther().equals("")
                            && compara.getOther().equalsIgnoreCase(
                            act.getCategory());
                    comparaOther = compara.getOther() != null
                            && !compara.getOther().equals("")
                            && compara.getOther().equalsIgnoreCase(
                            act.getOther());
                    cat = comparaCat || comparaCatOther || comparaOtherCat
                            || comparaOther;

                    if (cat && !compara.getType().equals(tipo)) {
                        return false;
                    }
                }
            }

        }
        return true;
    }

    /***************************************************************************
     * Este método dice si hay nombres repetidos dentro de las actividades. Es
     * importante para la validación y sicronización contra moodle.
     *
     * @param actividades
     * @return true: si hay nombres repetidos ; false: si no hay nombres
     *         repetidos
     */
    public boolean validarNombresRepetidos(List actividades) {
        for (int i = 0; i < actividades.size(); i++) {
            Activity act = (Activity) actividades.get(i);
            for (int j = 0; j < actividades.size(); j++) {
                Activity compara = (Activity) actividades.get(j);
                if (i != j && act.getName().equalsIgnoreCase(compara.getName())) {
                    return false;
                }
            }

        }
        return true;
    }
}
