/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.icesi.notas.control;

/**
 *
 * @author 1130608864
 */
import javax.servlet.http.*;

public class ControlSesion {

    public static String getUsuario(HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);
        return ((ControlUsuario) sesion.getAttribute("ctrlUsuario")).getIdUsuario();
    }

    public static HttpSession iniciarSesion(HttpServletRequest request, String idUsuario) {
        HttpSession sesion = request.getSession(true);
        sesion.setAttribute("activa", "activa");
        //Establecer el usuario actual
        return sesion;
    }

    public static HttpSession obtenerSesion(HttpServletRequest request) {
        // TODO Auto-generated method stub
        HttpSession sesion = request.getSession(false);
        if (verificarSesion(request)) {
            return sesion;
        }
        return null;
    }

    private static boolean verificarSesion(HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);
        if (sesion == null) {
            // No retornó sesión
            return false;
        }
        Object activa = sesion.getAttribute("activa");
        if (activa == null) {
            // La sesión no ha sido iniciada, o se ha inactivado
            return false;
        }
        return true;
    }
}
