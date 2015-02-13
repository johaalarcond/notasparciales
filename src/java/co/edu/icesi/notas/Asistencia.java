/*
 * Created on Dec 12, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas;

import java.util.Date;
import java.io.*;
import java.text.*;

/**
 * Esta clase representa la asistencia de un estudiante a una clase en
 * particular para un curso dado. Para representar esa asistencia, se cuenta con
 * la hora inicial y final de la clase, la fecha en que se realizó y el
 * porcentaje de asistencia del alumno a la clase.
 * 
 * @author rescobar
 */
public class Asistencia implements Serializable, Comparable {
	private int porcentajeAsistencia;

	private String fecha;

	private int horaInicio;

	private int horaFin;

	/**
	 * @return Returns the fecha.
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha
	 *            The fecha to set.
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return Returns the horaFin.
	 */
	public int getHoraFin() {
		return horaFin;
	}

	/**
	 * @param horaFin
	 *            The horaFin to set.
	 */
	public void setHoraFin(int horaFin) {
		this.horaFin = horaFin;
	}

	/**
	 * @return Returns the horaInicio.
	 */
	public int getHoraInicio() {
		return horaInicio;
	}

	/**
	 * @param horaInicio
	 *            The horaInicio to set.
	 */
	public void setHoraInicio(int horaInicio) {
		this.horaInicio = horaInicio;
	}

	/**
	 * @return Returns the porcentajeAsistencia.
	 */
	public int getPorcentajeAsistencia() {
		return porcentajeAsistencia;
	}

	/**
	 * @param porcentajeAsistencia
	 *            The porcentajeAsistencia to set.
	 */
	public void setPorcentajeAsistencia(int porcentajeAsistencia) {
		this.porcentajeAsistencia = porcentajeAsistencia;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		Asistencia a = (Asistencia) o;
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yy");
		try {
			Date d1 = formato.parse(a.fecha);
			Date d2 = formato.parse(this.fecha);
			if (d2.after(d1)) {
				return 1;
			} else if (d2.before(d1)) {
				return -1;
			} else {
				if (this.horaInicio > a.getHoraInicio()) {
					return 1;
				} else if (this.horaInicio < a.getHoraInicio()) {
					return -1;
				} else {
					return 0;
				}
			}
		} catch (ParseException exc) {
			exc.printStackTrace();
			return 0;
		}
	}

}
