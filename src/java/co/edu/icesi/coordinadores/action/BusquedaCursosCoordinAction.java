/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.icesi.coordinadores.action;

import co.edu.icesi.notas.Curso;
import java.sql.*;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import co.edu.icesi.notas.control.*;
import co.edu.icesi.notas.coordinadores.ControlCoordinador;
import co.edu.icesi.notas.jefes.control.*;
import java.util.ArrayList;

/**
 *
 * @author 1144030807 - ffceballos
 * @struts.action path="/coordinadores/busquedaCursos" name="cursosDeptoForm"
 *                input="/coordinadores/index.jsp" scope="request" validate="true"
 */
/**
 * MyEclipse Struts Creation date: 10-14-2009
 * 
 * XDoclet definition:
 * 
 * @struts.action path="/busquedaCursos" name="cursosDeptoForm"
 *                input="/coordinadores/index.jsp" scope="request" validate="true"
 */
public class BusquedaCursosCoordinAction extends Action{
    
      /*
     * Generated Methods
     */

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
       
        Connection conexion = null;
        try {
           
            conexion = ControlRecursos.obtenerConexion();
            DynaActionForm cursosDeptoForm = (DynaActionForm) form;
            
             String opcion = (String) cursosDeptoForm.get("opcion");
           // System.out.println("Opcion elegida: "+opcion+" ****************"); 
            ControlCoordinador cc = new ControlCoordinador();
            
            if(opcion.equals("1")){
            
            String criterioBusqueda = cursosDeptoForm.get("criterio").toString();
            
            //ArrayList cursos=cc.cargarCursos(conexion, criterioBusqueda);
            
            
            HttpSession session = ControlSesion.obtenerSesion(request);
            
            ControlProfesores cf=(ControlProfesores)session.getAttribute("profesores");
            ArrayList auxiliar=cf.getProfesor().getCursos();
            
            ArrayList cursos=new ArrayList();
            
            
            for(int cont=0;cont<auxiliar.size();cont++){
            
                Curso curso=(Curso)auxiliar.get(cont);
                
                String nombreMateria=curso.getDescripcionMateria().toLowerCase();
                
               // String criterioMinuscula=criterioBusqueda.toLowerCase();
                
                
                
                
                
                
                
                
                
                
                if((nombreMateria.toLowerCase().indexOf(criterioBusqueda.toLowerCase()))>=0){
                    //xq si tiene varios grupos va a salir repetido
                    boolean agregar=true;
                    for(int con=0;con<cursos.size();con++){
                    
                        Curso verificar=(Curso)auxiliar.get(con);
                        
                        if(verificar.getDescripcionMateria().equalsIgnoreCase(nombreMateria)){
                        agregar=false;
                        }
                        
                    }                    
                   
                    if(agregar){
                cursos.add(curso);
                    }
                }
                
                
            }
            
            
            request.setAttribute("cursos", cursos);
            
            return mapping.findForward("continuar");
            
            }
            
            if(opcion.equals("2")){
                String curso = request.getParameter("curso"); 
                
                String [] arreglo=curso.split("-");
                
                Curso cursoObj=new Curso(0, arreglo[0], arreglo[2],arreglo[1]);
                HttpSession sesion = ControlSesion.obtenerSesion(request);
                sesion.setAttribute("cursoCoordin", cursoObj);
              
                if(cc.verificarEsquema(conexion, curso)){
                    //System.out.println("ya tiene esquema!");
                return mapping.findForward("configuracion");
                }else{
                   // System.out.println("no tiene esquema!");
                return mapping.findForward("config_ini");
                }
                    
                //aqui hacer la validacion de pa donde va!
                //falta ver que parametros hay q enviar (los mismos q envia el listar cursos)
                
                //return mapping.findForward("continuar");
                
                
                
            }
            
            
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName());
            System.out.println("Usuario: " + request.getRemoteUser());
            System.out.println("Descripción:");
            e.printStackTrace();
        } finally {
            ControlRecursos.liberarRecursos(conexion);
        }
        return mapping.findForward("aErrorJefe");
    }
   
   
    
    
    
}
