package co.edu.icesi.notas.action;

import java.sql.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.*;

import co.edu.icesi.email.Correo;
import co.edu.icesi.notas.Curso;
import co.edu.icesi.notas.basica.Subject;
import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.utilidades.xls.FormatoExcelBasico;

/**
 * Esta acción de la vista básica de la aplicación, es la encargada de generar
 * un archivo en formato Excel que se enviará por correo electrónico al profesor
 * del curso.
 * 
 * @author mzapata, lmdiaz
 */
public class ArchivoXLSBasicoAction extends Action {

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

                conexion = ControlRecursos.obtenerConexion();
                ControlProfesores profesores = (ControlProfesores) sesion.getAttribute("profesores");
                Curso curso = (Curso) sesion.getAttribute("curso");
                Subject sub = (Subject) sesion.getAttribute("subject");
                String continuar = request.getParameter("fw");

                if (profesores == null || sub == null || curso == null
                        || (continuar == null || continuar.equals(""))) {
                    request.setAttribute("mensajeError",
                            "La aplicación ha recibido parámetros inválidos.");
                    ControlRecursos.liberarRecursos(conexion);
                    return mapping.findForward("errorAplicacion");
                }

                // En caso de que se ingrese un parámetro no válido.
                continuar = (!continuar.equals("esquemaBasico")
                        && !continuar.equals("registroBasico") && !continuar.equals("definitivasBasico")) ? "esquemaBasico"
                        : continuar;

                String ruta = this.getServlet().getServletContext().getRealPath(
                        "/xls/Notas-"
                        + profesores.getProfesor().getCedula()
                        + "-" + curso.getCodigoMateria() + "-"
                        + curso.getGrupo() + ".xls");
                FormatoExcelBasico formato = new FormatoExcelBasico(curso,
                        profesores.getProfesor(), ruta);
                formato.setSubject(sub);
                formato.crearArchivo(conexion);
                enviarCorreo(ruta, profesores, curso);
                ActionErrors errores = new ActionErrors();
                errores.add("mensaje", new ActionError("mensaje.excel"));
                saveErrors(request, errores);
                request.setAttribute("error", "S");
                ControlRecursos.liberarRecursos(conexion);
                return mapping.findForward(continuar);
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
     * Este método se encargará de enviar un correo electrónico al profesor, con
     * una copia en formato Excel de las notas registradas hasta el momento.
     *
     * @param path
     *            Ruta donde se almacenará el archivo en Excel.
     * @param cp
     *            Objeto ControlProfesores
     * @param curso
     *            Curso actual
     */
    public void enviarCorreo(String path, ControlProfesores cp, Curso curso) {
        Correo correo = new Correo();
        String[] para = {cp.getProfesor().getCorreo()};
      //  String []para={"ffceballos@icesi.edu.co"};
        correo.setDe("admin-ele@listas.icesi.edu.co");
       System.out.println("envio correo");
        /*correo.setDe("webmaster@icesi.edu.co");*/
        correo.setPara(para);
        correo.setAsunto("Notas en formato Excel");
        String mensaje = "Cordial saludo,\n\nAdjunto a este correo se envía las notas registradas hasta el momento en el curso ";
        mensaje += curso.getDescripcionMateria() + ", grupo: "
                + curso.getGrupo() + ".";
        correo.setMensaje(mensaje);
        correo.setFilename(path);
        correo.enviarMSGFile();
    }
}
