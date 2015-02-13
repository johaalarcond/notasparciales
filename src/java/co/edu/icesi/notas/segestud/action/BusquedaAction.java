package co.edu.icesi.notas.segestud.action;

import java.sql.*;
import java.util.*;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.segestud.*;
import co.edu.icesi.notas.segestud.control.ControlSegestud;

public class BusquedaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        String idUsuario = "";
        Connection conexion = null;
        try {
            HttpSession session = ControlSesion.obtenerSesion(request);
            if (session != null) {
                idUsuario = ControlSesion.getUsuario(request);
                UsuarioSegestud usuario = (UsuarioSegestud) session.getAttribute("usuarioSegestud");
                DynaActionForm busquedaForm = (DynaActionForm) form;
                request.setAttribute("codigo", busquedaForm.get("estudiante"));
                // Cargar los datos del estudiante consultado
                conexion = ControlRecursos.obtenerConexion(); 
                // Cargar las definitivas de los alumnos que tengan un
                // código como
                // el ingresado (like) y que pertenezcan al contexto del
                // usuario
              
              /*   COMENTADO PARA REEMPLAZAR EL SEGUIMIENTO!!
                
                CallableStatement sta = conexion.prepareCall("{call siaepre.ppregen_tntp_definitivas(?, ?, ?)}");
                sta.setString(1, busquedaForm.get("estudiante").toString());
                sta.setString(2, usuario.getTipo());
                sta.setString(3, usuario.getNombreUsuario());
                sta.execute();
                sta.close();
                
                * FIN COMENTARIO
                */
                
            //    String uno=busquedaForm.get("estudiante").toString();
            //    String dos=usuario.getTipo();
             //   String tres=usuario.getNombreUsuario();
             
                // Almacenar los resultados en una colección
                //ffceballos
                String filtro=busquedaForm.get("estudiante").toString();
                String identificacion=usuario.getIdentificacion();
                Map alumnosDefinitivas = ControlSegestud.obtenerSeguimiento(conexion,filtro, identificacion );//obtenerDefinitivas(conexion, usuario);
                //fin ffceballos
                // Guardar la colección en el request
                request.setAttribute("definitivas", alumnosDefinitivas);
                ControlRecursos.liberarRecursos(conexion);
                return mapping.findForward("continuar");
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
        request.setAttribute(
                "mensajeError",
                "Su usuario no pertenece a ninguno de los grupos autorizados para usar esta aplicación");
        return mapping.findForward("aErrorSegestud");
    }
}
