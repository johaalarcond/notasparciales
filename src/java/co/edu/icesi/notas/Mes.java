/*
 * Created on Dec 18, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas;

import java.io.Serializable;
/**
 * Tal como su nombre lo indica, esta clase representa uno de los meses del año.
 * 
 * @author rescobar
 */
public class Mes implements Serializable{
	private String id;
	private String nombre;
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
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
	
	public Mes(String id, String nombre){
		this.id=id;
		this.nombre=nombre;
	}
}
