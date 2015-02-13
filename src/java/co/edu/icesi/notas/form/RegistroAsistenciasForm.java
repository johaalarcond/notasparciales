/*
 * Created on Dec 13, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.form;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import co.edu.icesi.notas.form.Alumno;
import co.edu.icesi.notas.form.Asistencia;
/**
 * @author rescobar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RegistroAsistenciasForm extends ActionForm {
	private ArrayList alumnos = new ArrayList();
	private String mesAsistencias;
	private String boton;
	private String mesAsistenciasOld;
	
	public void resetAlumnos(){
		alumnos = new ArrayList();
	}
	/**
	 * @return Returns the alumnos.
	 */
	public ArrayList getAlumnos() {
		return alumnos;
	}
	/**
	 * @param alumnos The alumnos to set.
	 */
	public void setAlumnos(ArrayList alumnos) {
		this.alumnos = alumnos;
	}
	public Alumno getAlumno(int index){
		try{
			return (Alumno)alumnos.get(index);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public void setAlumno(int index,Alumno a){
		alumnos.add(index,a);
	}
	/**
	 * @return Returns the mes.
	 */
	public String getMesAsistencias() {
		return mesAsistencias;
	}
	/**
	 * @param mes The mes to set.
	 */
	public void setMesAsistencias(String mesAsistencias) {
		mesAsistenciasOld=this.mesAsistencias;
		this.mesAsistencias = mesAsistencias;
	}
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		boolean agregarErrorProp =  true;
		for(int i=0; i < alumnos.size(); i++){
			Alumno alumno = (Alumno)alumnos.get(i);
			if(alumno.getAsistencias()!=null){
				for(int j=0; j < alumno.getAsistencias().size(); j++){
					Asistencia asistencia = (Asistencia) alumno.getAsistencias().get(j);
					try{
						if(!asistencia.getPorcentajeAsistencia().equals("")){
							if(Integer.parseInt(asistencia.getPorcentajeAsistencia())<0 || Integer.parseInt(asistencia.getPorcentajeAsistencia()) > 100){
								errors.add("alumno["+i+"].asistenciaProgramacionIndexada["+j+"].porcentajeAsistencia",new ActionError("asterisco"));
								if(agregarErrorProp){
									errors.add("errorEntero",new ActionError("entero.error"));
									agregarErrorProp=false;
									request.setAttribute("error","error");
									this.mesAsistencias=mesAsistenciasOld;
								}
							}
						}
					}catch(NumberFormatException exc){
						errors.add("alumno["+i+"].asistenciaProgramacionIndexada["+j+"].porcentajeAsistencia",new ActionError("asterisco"));
						if(agregarErrorProp){
							errors.add("errorEntero",new ActionError("entero.error"));
							agregarErrorProp=false;
							request.setAttribute("error","error");
							this.mesAsistencias=mesAsistenciasOld;
						}
					}
				}
			}
		}
		return errors;
	}	
	/**
	 * @return Returns the boton.
	 */
	public String getBoton() {
		return boton;
	}
	/**
	 * @param boton The boton to set.
	 */
	public void setBoton(String boton) {
		this.boton = boton;
	}
}
