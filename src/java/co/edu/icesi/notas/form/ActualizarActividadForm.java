package co.edu.icesi.notas.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import co.edu.icesi.notas.utilidades.DateValidator;

/** 
 * MyEclipse Struts
 * Creation date: 12-15-2006
 * 
 * XDoclet definition:
 * @struts:form name="actualizarActividadForm"
 */
public class ActualizarActividadForm extends ActionForm {

	// --------------------------------------------------------- Instance Variables

	/** description property */
	private String description;

	/** category property */
	private String category;

	/** date property */
	private String date;

	/** name property */
	private String name;

	/** topics property */
	private String topics;

	/** percentage property */
	private String percentage;

	// --------------------------------------------------------- Methods

	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request) {

		ActionErrors errores = new ActionErrors();
		if(date!=null || !date.trim().equals("")){
			boolean validoFecha = DateValidator.validarFecha(date); 
			if(!validoFecha){
				errores.add("fechaRealizacion",new ActionError("fecha.error3"));
			}
		}
		return errores;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTopics() {
		return topics;
	}

	public void setTopics(String topics) {
		this.topics = topics;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

}