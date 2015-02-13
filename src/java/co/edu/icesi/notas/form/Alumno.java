/*
 * Created on Dec 14, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.form;


import java.io.Serializable;
import java.util.*;

import co.edu.icesi.notas.utilidades.OperacionesMatematicas;
/**
 * @author rescobar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Alumno implements Serializable {
	private ArrayList asistencias=new ArrayList();	
	private String codigo;
	private String nombre;
	private String apellidos;
	private int promedioAsistenciaMes;
	
	public Asistencia getAsistenciaProgramacionIndexada(int index){
		return (Asistencia)asistencias.get(index);
	}
	
	public void setAsistenciaProgramacionIndexada(int index,Asistencia a){
		asistencias.add(index,a);
	}
	/**
	 * @return Returns the asistencias.
	 */
	public ArrayList getAsistencias() {
		return asistencias;
	}
	/**
	 * @param asistencias The asistencias to set.
	 */
	public void setAsistencias(ArrayList asistencias) {
		this.asistencias = asistencias;
	}
	/**
	 * @return Returns the apellidos.
	 */
	public String getApellidos() {
		return apellidos;
	}
	/**
	 * @param apellidos The apellidos to set.
	 */
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	/**
	 * @return Returns the codigo.
	 */
	public String getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return Returns the promedioAsistenciaMes.
	 */
	public int getPromedioAsistenciaMes() {
		double acum = 0;
		for(int i = 0; i < asistencias.size(); i++){
			try{
				acum += Integer.parseInt(((Asistencia)asistencias.get(i)).getPorcentajeAsistencia());
			}catch(NumberFormatException exc){
				acum+=0;
			}
		}
		if(asistencias.size()>0){
			return (int)OperacionesMatematicas.redondear(acum/asistencias.size(),0);
		}else{
			return 0;
		}
	}
	/**
	 * @param promedioAsistenciaMes The promedioAsistenciaMes to set.
	 */
	public void setPromedioAsistenciaMes(int promedioAsistenciaMes) {
		this.promedioAsistenciaMes = promedioAsistenciaMes;
	}
}
