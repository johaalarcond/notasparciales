//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package co.edu.icesi.notas.form;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 06-17-2005
 * 
 * XDoclet definition:
 * 
 * @struts:form name="calificacionForm"
 */
public class CalificacionForm extends ActionForm {

	private ArrayList matriculas;
	private String nombre;
	private String tipoSubmit;
	private String boton;
	private CalificacionForm copia;
	// 'category' es un atributo empleado para identificar la categoría cuyas
	// actividades se están mostrando.
	private String category;
	// la escala del curso.
	private double escala;

	public void reset() {
		matriculas = new ArrayList();
	}

	public CalificacionForm() {
		reset();
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();
		Calificacion actual;
		boolean errorRango = false, errorFormato = false;
		for (int i = 0; i < matriculas.size(); i++) {
			actual = this.getCalificacion(i);
			for (int j = 0; j < actual.getNotas().size(); j++) {
				String nota = request.getParameter("calificacion[" + i
						+ "].notaIndexada[" + j + "].nota");
				nota = nota.replace(',', '.');
				if (nota != null) {
					// if(!nota.equals("")){
					try {
						double note = Double.parseDouble(nota);
						if (note < 0.0 || note > this.escala) {
							if (!errorRango) {
								errores.add("boton", new ActionError(
										"rango.error", new Double(this.escala)));
								errorRango = true;
							}
							errores.add("calificacion[" + i + "].notaIndexada["
									+ j + "].nota",
									new ActionError("asterisco"));
							// copiarAnterior(i,j);
						}
					} catch (Exception e) {
						if (!errorFormato) {
							errores.add("boton", new ActionError(
									"nota.formato.error"));
							errorFormato = true;
						}
						errores.add("calificacion[" + i + "].notaIndexada[" + j
								+ "].nota", new ActionError("asterisco.dos"));
						// copiarAnterior(i,j);
					}
					// }
				}
			}
		}
		return errores;
	}

	/***
	 * Este método copia la nota existente en el CalificacionForm de copia y lo
	 * pasa al original
	 ***/
	public void copiarAnterior(int indexCalificacion, int indexNota) {
		Nota notaActual = this.getCalificacion(indexCalificacion)
				.getNotaIndexada(indexNota);
		Nota notaCopia = copia.getCalificacion(indexCalificacion)
				.getNotaIndexada(indexNota);
		notaActual.setNota(notaCopia.getNota());
	}

	public ArrayList getMatriculas() {
		return matriculas;
	}

	public void setMatriculas(ArrayList nueva) {
		matriculas = nueva;
	}

	public void setCalificacion(int index, Calificacion calif) {
		Calificacion nueva;
		while (index >= matriculas.size()) {
			nueva = new Calificacion();
			nueva.setEscala(this.escala);
			matriculas.add(nueva);
		}
		matriculas.add(index, calif);
	}

	public Calificacion getCalificacion(int index) {
		Calificacion nueva;
		while (index >= matriculas.size()) {
			nueva = new Calificacion();
			nueva.setEscala(this.escala);
			matriculas.add(nueva);
		}
		return (Calificacion) matriculas.get(index);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipoSubmit() {
		return tipoSubmit;
	}

	public void setTipoSubmit(String tipoSubmit) {
		this.tipoSubmit = tipoSubmit;
	}

	public String getBoton() {
		return boton;
	}

	public void setBoton(String boton) {
		this.boton = boton;
	}

	public CalificacionForm getCopia() {
		return copia;
	}

	public void setCopia(CalificacionForm copia) {
		this.copia = copia;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setEscala(double escala) {
		this.escala = escala;
	}

	/**
	 * @return Returns the escala.
	 */
	public double getEscala() {
		return escala;
	}
}