//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.3/xslt/JavaClass.xsl

package co.edu.icesi.notas.form;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import co.edu.icesi.notas.utilidades.DateValidator;
import org.apache.struts.action.*;

import java.text.*;

/**
 * MyEclipse Struts Creation date: 06-14-2005
 * 
 * XDoclet definition:
 * 
 * @struts:form name="estructuraAdministradorForm"
 */
public class EstructuraAdministradorForm extends ActionForm {
	private List estructura;
	private double porcentaje;
	private boolean checked;
	private String opcion = "0";

	public String getOpcion() {
		return opcion;
	}

	public void setOpcion(String opcion) {
		this.opcion = opcion;
	}

	/**
	 * @return Returns the checked.
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @param checked
	 *            The checked to set.
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	/**
	 * @return Returns the porcentaje.
	 */
	public double getPorcentaje() {
		return porcentaje;
	}

	/**
	 * @param porcentaje
	 *            The porcentaje to set.
	 */
	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}

	public void reset() {
		estructura = new ArrayList();
	}

	public EstructuraAdministradorForm() {
		reset();
	}

	/**
	 * @return Returns the estructura.
	 */
	public List getEstructura() {
		return estructura;
	}

	public ElementoEstructura getElemento(int index) {
		while (index >= estructura.size()) {
			estructura.add(new ElementoEstructura());
		}
		return (ElementoEstructura) estructura.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.struts.action.ActionForm#validate(org.apache.struts.action
	 * .ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
		ActionErrors errores = new ActionErrors();
		double suma = 0;
		int contP = 0;
		int contF = 0;
		for (int i = 0; i < estructura.size(); i++) {
			ElementoEstructura est = (ElementoEstructura) estructura.get(i);
			suma += est.getPorcentaje();
			if (est.getPorcentaje() == 0 && (contP == 0)) {
				errores.add("porcentajeE", new ActionError("porcentaje.error"));
				contP++;
			}
			if ((est.getFechaRealizacion() == null || est.getFechaRealizacion()
					.trim().equals(""))
					&& (contF == 0)) {
				errores.add("fechaE", new ActionError("fecha.error"));
				contF++;
			}
			// para validar el formato de la fecha
			String typedDate = est.getFechaRealizacion();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
			try {
				Date parsedDate = dateFormat.parse(typedDate);
				if (!validarMesDia(est.getFechaRealizacion())) {
					errores.add("fechaErrDos", new ActionError("fecha.error4"));
				}
			} catch (ParseException pe) {
				errores.add("fechaErrDos", new ActionError("fecha.error1"));
			}

			// fin de la validación
		}
		if (estructura.size() > 0 && Math.round(suma) != 100) {
			errores.add("sumaPorcentaje", new ActionError(
					"suma.estructura.error"));
		}
		return errores;
	}

	private boolean validarMesDia(String fecha) {
		return DateValidator.validarFecha(fecha);
	}
}