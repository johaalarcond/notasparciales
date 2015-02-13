/*
 * Created on 16-jun-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.form;
 
import java.io.Serializable;

/**
 * @author drojas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ElementoEstructura implements Serializable {
	private int consecutivo;
	private String nombre; 
	private double porcentaje;
	private String fechaRealizacion;
	private String temas;
	private String descripcion;
	private boolean checked;
	
	/**
	 * @return Returns the checked.
	 */
	public boolean isChecked() {
		return checked;
	}
	/**
	 * @param checked The checked to set.
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	/**
	 * @return Returns the consecutivo.
	 */
	public int getConsecutivo() {
		return consecutivo;
	}
	/**
	 * @param consecutivo The consecutivo to set.
	 */
	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return Returns the fechaRealizacion.
	 */
	public String getFechaRealizacion() {
		return fechaRealizacion;
	}
	/**
	 * @param fechaRealizacion The fechaRealizacion to set.
	 */
	public void setFechaRealizacion(String fechaRealizacion) {
		this.fechaRealizacion = fechaRealizacion;
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
	 * @return Returns the porcentaje.
	 */
	public double getPorcentaje() {
		return porcentaje;
	}
	
	/**
	 * @param porcentaje The porcentaje to set.
	 */
	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}
	/**
	 * @return Returns the temas.
	 */
	public String getTemas() {
		return temas;
	}
	/**
	 * @param temas The temas to set.
	 */
	public void setTemas(String temas) {
		this.temas = temas;
	}
	
	
}
