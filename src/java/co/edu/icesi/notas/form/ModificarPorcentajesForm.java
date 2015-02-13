//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.4/xslt/JavaClass.xsl

package co.edu.icesi.notas.form;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;

/** 
 * MyEclipse Struts
 * Creation date: 12-12-2006
 * 
 * XDoclet definition:
 * @struts:form name="modificarPorcentajesFormForm"
 */
public class ModificarPorcentajesForm extends ActionForm {

	// --------------------------------------------------------- Instance Variables

	/** opcion property */
	private String opcion;
	private String indiceUno;
	private String indiceDos;
	/** listaCategorias property */
	private ArrayList listaCategorias=new ArrayList();
	private String redistribuir;
	private double porcentajeIndividual;
	private double porcentajeGrupal;

	
	public Category getElemento(int index){
		while(index>=listaCategorias.size()){
			listaCategorias.add(new Category());
		}
		return (Category)listaCategorias.get(index);
	}
	
	public void setElemento(int index,Category nuevo){
		while(index>=(listaCategorias.size()+1)){
			listaCategorias.add(new Category());
		}
		listaCategorias.add(index,nuevo);
	}
	
	public String getIndiceDos() {
		return indiceDos;
	}
	
	public void setIndiceDos(String indiceDos) {
		this.indiceDos = indiceDos;
	}
	
	public String getIndiceUno() {
		return indiceUno;
	}
	
	public void setIndiceUno(String indiceUno) {
		this.indiceUno = indiceUno;
	}

	/** 
	 * Returns the opcion.
	 * @return String
	 */
	public String getOpcion() {
		return opcion;
	}

	/** 
	 * Set the opcion.
	 * @param opcion The opcion to set
	 */
	public void setOpcion(String opcion) {
		this.opcion = opcion;
	}

	/** 
	 * Returns the listaCategorias.
	 * @return ArrayList
	 */
	public ArrayList getListaCategorias() {
		return listaCategorias;
	}

	/** 
	 * Set the listaCategorias.
	 * @param listaCategorias The listaCategorias to set
	 */
	public void setListaCategorias(ArrayList listaCategorias) {
		this.listaCategorias = listaCategorias;
	}

	/**
	 * @return Returns the redistribuir.
	 */
	public String getRedistribuir() {
		return redistribuir;
	}
	/**
	 * @param redistribuir The redistribuir to set.
	 */
	public void setRedistribuir(String redistribuir) {
		this.redistribuir = redistribuir;
	}

	public double getPorcentajeGrupal() {
		return porcentajeGrupal;
	}

	public void setPorcentajeGrupal(double porcentajeGrupal) {
		this.porcentajeGrupal = porcentajeGrupal;
	}

	public double getPorcentajeIndividual() {
		return porcentajeIndividual;
	}

	public void setPorcentajeIndividual(double porcentajeIndividual) {
		this.porcentajeIndividual = porcentajeIndividual;
	}
}