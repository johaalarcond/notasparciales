//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl
package co.edu.icesi.notas.action;

import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import co.edu.icesi.notas.Curso;
import co.edu.icesi.notas.basica.Subject;
import co.edu.icesi.notas.control.*;
import java.util.Enumeration;
import org.apache.struts.action.DynaActionForm;

/**
 * MyEclipse Struts Creation date: 06-12-2007
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/clonarEsquema" name="clonarEsquemaForm"
 *                input="/basica/esquema.jsp" scope="request"
 */
public class ClonarEsquemaAction extends Action {

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
                if (this.isCancelled(request)) {
                    return mapping.findForward("regresar");
                }
                
                
                //ffceballos 30/09/2013
                
                DynaActionForm listaCursosForm = (DynaActionForm) form;
                //ControlProfesores profesores = (ControlProfesores) sesion.getAttribute("profesores");
                int opcion = Integer.parseInt(request.getParameter("opcion"));
              
                  
                        
               conexion = ControlRecursos.obtenerConexion();
                
                Curso cursoActual = (Curso) sesion.getAttribute("curso");  
                ControlCopiaEsquemas controlCopia=new ControlCopiaEsquemas();
               
                System.out.println("*****************Opción: "+opcion);
                
                switch(opcion){
                
                    case 1: 
                        controlCopia.eliminarEsquemas(cursoActual, conexion);
                        controlCopia.clonarEsquema(cursoActual, conexion);
                        break;
                        
                    case 2:
                         controlCopia.reiniciarEsquema(cursoActual, conexion);
                         
                        break;
                    
                    case 3:
                        
                        String codigoMateria=cursoActual.getCodigoMateria();
                        String grupoMateria=cursoActual.getGrupo();
                        
                         for(Enumeration e = request.getParameterNames(); e.hasMoreElements(); ){
                                                          
                String attName = (String)e.nextElement();
                System.out.println("*************************************************Parámetro: "+attName);
                
                //if(attName.contains("rupo")){
                 if(attName.indexOf("rupo")!= -1){
                
                String []arreglo=attName.split("rupo");
                if(arreglo.length>1){
                    String grupoActual=arreglo[1]; 
                
                if(!grupoMateria.equalsIgnoreCase(grupoActual)){                    
                    System.out.println("*************************************************Grupo: "+grupoActual);
                    controlCopia.eliminarEsquema(codigoMateria,grupoActual, conexion);
                    controlCopia.copiarEsquema(cursoActual, conexion, grupoActual);
                    }
                         }
                }
                         }
                        break;
                        
                    default:    
                
                }
                
               // ControlCopiaEsquemas controlCopia = (ControlCopiaEsquemas) sesion.getAttribute("controlCopia");
              //  controlCopia.realizarCopiaCurso(cursoActual, controlCopia.getCursoAnterior(), conexion);
                
                
                Subject sub = new Subject(cursoActual);
                sesion.setAttribute("subject", sub);
                ControlRecursos.liberarRecursos(conexion);
                
               /*   if(opcion==3){
                return mapping.findForward("seleccionar");
                }*/
                
                if(opcion!=2){                  
                return mapping.findForward("siguiente");
                }else{
                return mapping.findForward("inicio");
                }
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
}
