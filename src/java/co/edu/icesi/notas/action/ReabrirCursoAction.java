package co.edu.icesi.notas.action;

import java.sql.Connection;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import co.edu.icesi.notas.*;
import co.edu.icesi.notas.control.*;

/**
 * MyEclipse Struts Creation date: 06-12-2006 Acción que envía al cierre del
 * curso en cuestión. XDoclet definition:
 *
 * @struts:action validate="true"
 */
public class ReabrirCursoAction extends Action
{

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
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
  {
    String forward = "IrDefinitivas";
    String idUsuario = "";
    Connection conexion = null;
    try
    {
      HttpSession sesion = ControlSesion.obtenerSesion(request);
      if (sesion != null)
      {
        idUsuario = ControlSesion.getUsuario(request);
        if (sesion.getAttribute("profesores") == null || sesion.getAttribute("fechaLimite") == null)
        {
          request.setAttribute("mensajeError", "La aplicación ha recibido parámetros inválidos.");
          return mapping.findForward("errorAplicacion");
        }

        if (sesion.getAttribute("curso") == null)
        {
          request.setAttribute("mensajeError", "Ha vuelto a la página de listado de cursos pero no ha escogido un curso, por favor escoja un curso del listado inicial.");
          return mapping.findForward("errorAplicacion");
        }
        conexion = ControlRecursos.obtenerConexion();
        ActionErrors errores = new ActionErrors();
        ActionMessages mensajes = new ActionMessages();
        ControlProfesores profesores = (ControlProfesores) sesion.getAttribute("profesores");
        Profesor profesor = profesores.getProfesor();

        Curso curso;

        // En caso de que se invoque el cierre desde
        // listaCursos.jsp.
        //if (forward == null) {
//        if (sesion.getParameter("curso") == null || request.getParameter("curso").equals(""))
//        {
//          request.setAttribute("mensajeError", "La aplicación ha recibido parámetros inválidos.");
//          ControlRecursos.liberarRecursos(conexion);
//          return mapping.findForward("errorAplicacion");
//        }
//        String cursoId = request.getParameter("curso");
//        curso = profesores.getCurso(cursoId);
//        sesion.setAttribute("curso", curso);
        //}

//        if (!forward.equals("IrCierre"))
//        {
//          ControlRecursos.liberarRecursos(conexion);
//          return mapping.findForward("IrCierre");
//        }

        curso = (Curso) sesion.getAttribute("curso");
        if (curso.reabrir(conexion, profesor.getCedula()) == 0)
        {
          curso.setEstado("A");
          sesion.setAttribute("cerrado", "N");
          request.setAttribute("mensaje", "S");
          mensajes.add("reabrirCurso", new ActionMessage("curso.reabrir"));
          saveMessages(request, mensajes);
        } else
        {
          request.setAttribute("error", "S");
          errores.add("errorReabrirCurso", new ActionError("curso.reabrir.error"));
          saveErrors(request, errores);
        }
        // Bloque para realizar el cierre del curso.
//        if (cerrar.equals("cerrar"))
//        {
//          int resultadoOperacionCierre = cerrarCurso(curso, conexion,
//                  sesion, profesor.getDeptoAcademico());
//          if (resultadoOperacionCierre < 0)
//          {
//            request.setAttribute("error", "S");
//            switch (resultadoOperacionCierre)
//            {
//              case -1:
//                errores.add("noCerrarCurso", new ActionError(
//                        "noCerrarCurso-1.error"));
//                break;
//              case -2:
//                errores.add("noCerrarCurso", new ActionError(
//                        "noCerrarCurso-2.error"));
//                break;
//              case -3:
//                errores.add("noCerrarCurso", new ActionError(
//                        "noCerrarCurso-3.error"));
//                break;
//            }
//          }
//          saveErrors(request, errores);
//          forward = "IrCierre";
//        }
        ControlRecursos.liberarRecursos(conexion);
        return mapping.findForward(forward);
      }
      System.out.println("Sesión inactiva: " + this.getClass().getName());
      return mapping.findForward("sesionInactiva");
    } catch (Exception e)
    {
      System.out.println("Error: " + this.getClass().getName());
      System.out.println("Usuario: " + request.getRemoteUser());
      System.out.println("Descripción:");
      e.printStackTrace();
    } finally
    {
      ControlRecursos.liberarRecursos(conexion);
    }
    return mapping.findForward("errorAplicacion");
  }
//  public int cerrarCurso(Curso curso, Connection conexion,
//          HttpSession sesion, String deptoAcadProfesor)
//  {
//    int resultadoOperacionCierre = curso.cerrarCurso(conexion,
//            deptoAcadProfesor);
//    if (resultadoOperacionCierre >= 0)
//    {
//
//      sesion.setAttribute("cerrado", "S");
//      // return "IrCierre";
//    }
//    // return "NoCierre";
//    return resultadoOperacionCierre;
//  }
}
