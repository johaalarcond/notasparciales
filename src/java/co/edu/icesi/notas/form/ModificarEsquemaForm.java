//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package co.edu.icesi.notas.form;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import co.edu.icesi.notas.basica.*;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** 
 * MyEclipse Struts
 * Creation date: 12-12-2006
 * 
 * XDoclet definition:
 * @struts:form name="modificarEsquemaForm"
 */
public class ModificarEsquemaForm extends ActionForm {

	// --------------------------------------------------------- Instance Variables

	public ModificarEsquemaForm(){
		super();
		activities=new ArrayList();
	}
	
	/** activities property */
	private List activities;
	private String opcion;
	private String indice;

	/**
	 * @return Returns the indice.
	 */
	public String getIndice() {
		return indice;
	}
	/**
	 * @param indice The indice to set.
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}
	// --------------------------------------------------------- Methods

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(
		ActionMapping mapping,
		HttpServletRequest request) {

		return new ActionErrors();
	}


	/** 
	 * Returns the activities.
	 * @return ArrayList
	 */
	public List getActivities() {
		return activities;
	}

	/** 
	 * Set the activities.
	 * @param activities The activities to set
	 */
	public void setActivities(List activities) {
		this.activities = activities;
	}
	
	public Activity getElemento(int index){
		while(index>=activities.size()){
			activities.add(new Activity());
		}
		return (Activity)activities.get(index);
	}

	/**
	 * @return Returns the opcion.
	 */
	public String getOpcion() {
		return opcion;
	}
	/**
	 * @param opcion The opcion to set.
	 */
	public void setOpcion(String opcion) {
		this.opcion = opcion;
	}
}