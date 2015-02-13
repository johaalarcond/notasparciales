package co.edu.icesi.notas.form; 


import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import co.edu.icesi.notas.Clasificacion;
import co.edu.icesi.notas.Categoria;


public class ConfiguracionForm extends ActionForm{ 
	
	private Clasificacion individuales, grupales;
	private double porcentaje;
	
	public void reset(){
		individuales=new Clasificacion();
		grupales=new Clasificacion();
	}
	
	public ConfiguracionForm(){
		reset();
	}
	
	
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request) {
        ActionErrors errores=new ActionErrors();
        if(individuales.getPorcentaje()<0 || Math.round(individuales.getPorcentaje())>100){
        	errores.add("individuales.porcentaje",new ActionError("porcentaje.individual.error"));
        }
        if(grupales.getPorcentaje()<0 || Math.round(grupales.getPorcentaje())>100){
        	errores.add("grupales.porcentaje",new ActionError("porcentaje.grupal.error"));
        }
        if(Math.round(individuales.getPorcentaje()+grupales.getPorcentaje())!=100){
        	errores.add("fallas",new ActionError("suma.total.error"));
        }
        if(individuales.diferenteCienSumaCategorias()){
        	errores.add("fallas",new ActionError("suma.individual.error"));
        }
        if((grupales.getPorcentaje()>0) && grupales.diferenteCienSumaCategorias()){
        	errores.add("fallas",new ActionError("suma.grupal.error"));
        }
        validarCategoria(errores);
        return errores;
	}
	
	/**
	 * Este método se encarga de validar que cada padreGrupo que haya sido seleccionado en 
	 * configuracion.jsp tenga un porcentaje mayor que cero.
	 * **/
	private void validarCategoria(ActionErrors errores){
		//para los padres individuales
		Categoria actual;
		for(int i=0;i<individuales.getCategorias().size();i++){
			actual=(Categoria)individuales.getCategorias().get(i);
			if(!actual.isCancelado()){
				if(actual.getPorcentaje()<=0 || Math.round(actual.getPorcentaje())>100){
					/**creamos un ActionError pasándole por parámetro el valor que había digitado
					   el usuario en el campo.**/
					errores.add("errorIndividual"+i,new ActionError("porcentaje.mayor.error"));
				}
			}
		}
		//para los padres grupales
		for(int i=0;i<grupales.getCategorias().size();i++){
			actual=(Categoria)grupales.getCategorias().get(i);
			if(!actual.isCancelado()){
				if(actual.getPorcentaje()<=0|| Math.round(actual.getPorcentaje())>100){
					errores.add("errorGrupal"+i,new ActionError("porcentaje.mayor.error"));
				}
			}
		}
		
	}
	
	public Clasificacion getGrupales() {
		return grupales;
	}
	
	public void setGrupales(Clasificacion grupales) {
		this.grupales = grupales;
	}
	
	public Clasificacion getIndividuales() {
		return individuales;
	}
	public void setIndividuales(Clasificacion individuales) {
		this.individuales = individuales;
	}
	
	public Categoria getCategoriaIndividual(int index){
		while(index>=individuales.getCategorias().size()){
			individuales.getCategorias().add(new Categoria());
		}
    	return (Categoria)individuales.getCategorias().get(index);
	}
	
	public void setCategoriaIndividual(int index,Categoria padre){
		while(index>=individuales.getCategorias().size()){
			individuales.getCategorias().add(new Categoria());
		}
		individuales.getCategorias().add(index,padre);
	}
	
	public Categoria getCategoriaGrupal(int index){
		while(index>=grupales.getCategorias().size()){
			grupales.getCategorias().add(new Categoria());
		}
    	return (Categoria)grupales.getCategorias().get(index);
	}
	
	public void setCategoriaGrupal(int index,Categoria padre){
		while(index>=grupales.getCategorias().size()){
			grupales.getCategorias().add(new Categoria());
		}
		grupales.getCategorias().add(index,padre);
	}
	
	
	
	public double getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}
}
