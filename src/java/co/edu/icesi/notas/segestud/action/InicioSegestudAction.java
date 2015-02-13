package co.edu.icesi.notas.segestud.action;

import java.sql.*;
import java.util.*;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.segestud.*;
import co.edu.icesi.notas.segestud.control.*;

public class InicioSegestudAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        String idUsuario = "";
        Connection conexion = null;
        try {
            ControlRecursos.iniciarRecursos("jdbc/ds_websiaepre");
            idUsuario = request.getRemoteUser();
            if (idUsuario == null || idUsuario.equals("")) {
                idUsuario = request.getParameter("ced");
            }
            idUsuario = getidentificacion(idUsuario);
            HttpSession sesion = ControlSesion.iniciarSesion(request, idUsuario);
            conexion = ControlRecursos.obtenerConexion();

            ControlUsuario ctrlUsuario = new ControlUsuario(idUsuario);
            ctrlUsuario.cargarRoles(conexion);
            sesion.setAttribute("ctrlUsuario", ctrlUsuario);
            sesion.setAttribute("RPROFESORES", Boolean.valueOf(ctrlUsuario.getGrupos().contains("PROFESORES")));
            sesion.setAttribute("RPREGRADO", Boolean.valueOf(ctrlUsuario.getGrupos().contains("PREGRADO")));
            sesion.setAttribute("RJEFDEPRE", Boolean.valueOf(ctrlUsuario.getGrupos().contains("JEFDEPRE")));
            sesion.setAttribute("RDIRPROG", Boolean.valueOf(ctrlUsuario.getGrupos().contains("DIRPROG")));
            sesion.setAttribute("RSECGEN", Boolean.valueOf(ctrlUsuario.getGrupos().contains("SECGEN")));

            // Verificar que pertenece al rol SECGEN, JEFDEPRE, DIRPROGR
            boolean usuarioValido = ControlSegestud.usuarioValido(conexion,
                    idUsuario);
            if (usuarioValido) {
                UsuarioSegestud usuario = ControlSegestud.cargarUsuario(
                        conexion, idUsuario);
                sesion.setAttribute("usuarioSegestud", usuario);
                System.out.println("Inicio de sesión seguimiento a estudiantes: " + idUsuario);
                ControlRecursos.liberarRecursos(conexion);
                return mapping.findForward("exito");
            }
            request.setAttribute(
                    "mensajeError",
                    "Su usuario no pertenece a ninguno de los grupos autorizados para usar esta aplicación");
            return mapping.findForward("error");
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Usuario: " + request.getRemoteUser());
            System.out.println("Descripción:");
            e.printStackTrace();
        } finally {
            ControlRecursos.liberarRecursos(conexion);
        }
        return mapping.findForward("aErrorSegestud");
    }

    /**
     * Obtiene la cédula o identificación del usuario a partir de la cédula
     * enviada por parámetro
     *
     * @param cadena
     * @return
     */
    public String getidentificacion(String cadena) {
        try {
            StringTokenizer token = new StringTokenizer(cadena, "/");
            token.nextToken();
            String cad = token.nextToken();
            if (esCedula(cad) && (cad.charAt(0) == 'z' || cad.charAt(0) == 'Z')) {
                return cad.substring(1);
            }
            return cad;
        } catch (Exception e) {
            System.out.println("El formato de cédula no corresponde");
            return cadena;
        }
    }

    /**
     * Valida si lo que se envía por parámetro es una cédula o no
     *
     * @param usuario
     * @return
     */
    private boolean esCedula(String usuario) {
        for (int i = 0; i < usuario.length(); i++) {
            if (Character.isDigit(usuario.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
