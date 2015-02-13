//Created by MyEclipse Struts
//XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl
package co.edu.icesi.notas.action;

import java.sql.*;
import javax.servlet.http.*;
import co.edu.icesi.notas.*;
import co.edu.icesi.notas.basica.Subject;
import co.edu.icesi.notas.control.*;
import java.util.ArrayList;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * MyEclipse Struts Creation date: 06-09-2005 Acción que termina de cargar el
 * curso escogido por el profesor y genera su correspondiente Subject, el cual
 * será la vista para el usuario. La salida que tome, depende de lo que el
 * usuario haya decidido. XDoclet definition:
 * 
 * @struts:action path="/listaCursos" name="listaCursosForm"
 *                input="/profesores/listaCurso.jsp" scope="request"
 *                validate="true"
 */
public class ListaCursosAction extends Action {

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
                DynaActionForm listaCursosForm = (DynaActionForm) form;
                ControlProfesores profesores = (ControlProfesores) sesion.getAttribute("profesores");
                String cursoId = request.getParameter("curso");
                // validamos que la sesión esté activa y que además tanto ésta
                // como
                // el
                // Request tengan
                // los parámetros requeridos.
                String opcion = (String) listaCursosForm.get("opcion");
                if (cursoId == null || cursoId.equals("") || opcion == null
                        || opcion.equals("")) {
                    request.setAttribute("mensajeError",
                            "La aplicación ha recibido parámetros inválidos.");
                    return mapping.findForward("errorAplicacion");
                }
                Curso curso = profesores.getCurso(cursoId);
             
                conexion = ControlRecursos.obtenerConexion();
                String dptoAcad = curso.buscarDeptoCurso(conexion);
                
                profesores.cargarTiposCategoria(conexion, dptoAcad);
                // Cargamos el Subject asociado al curso.
                
               
                Subject subject = new Subject(curso);
                String cerrado = curso.getEstado().equals("A") ? "N" : "S";

                
                //ffceballos  obetener todos los grupos que tiene la materia y cargarlos a la sesion
                ArrayList grupos=curso.cargarGrupos(conexion);
                sesion.setAttribute("grupos", grupos);
                //fin ffceballos
                
                
                sesion.setAttribute("cerrado", cerrado);
                sesion.setAttribute("curso", subject.getCurso());
                
                // sesion.setAttribute("curso", subje);
                sesion.setAttribute("subject", subject);

                if (curso.getCategorias().size() == 0) {
                    sesion.setAttribute("nuevo", "S");
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("config_ini");
                } else {
                    // Esto es necesario, p.e, cuando se escoge otro curso
                    sesion.removeAttribute("nuevo");
                    
                    
                }

                if (opcion.equals("1")) {
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("configuracion");
                }

                if (opcion.equals("2")) {
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("registrar");
                }

                if (opcion.equals("3")) {
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("definitivas");
                }
            }
            System.out.println("Sesión inactiva: " + this.getClass().getName());
            return mapping.findForward("sesionInactiva");
        } catch (Exception e) {
            
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Usuario: " + request.getRemoteUser());
            System.out.println("Descripción:"+e.getStackTrace());
            e.printStackTrace();
        } finally {
            ControlRecursos.liberarRecursos(conexion);
        }
        return mapping.findForward("errorAplicacion");

    }
}
