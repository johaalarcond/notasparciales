package co.edu.icesi.notas.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import co.edu.icesi.notas.basica.Activity;

/**
 * @author mzapata
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Category implements Serializable, Comparable{
	
	private List listActivities;
	private String name;
	private double percentage;
	
	public Category(){
		super();
		listActivities=new ArrayList();
	}
	
	public boolean equals(Object obj){
		if(obj instanceof Category){
			Category cat = (Category)obj;
			return cat.name.equalsIgnoreCase(this.name);
		}
		return false;
	}
	
	public Activity getActivity(int index){
		while(index>=listActivities.size()){
			listActivities.add(new Activity());
		}
		return (Activity)listActivities.get(index);
	}
	
	public void setActivity(int index,Activity act){
		while(index>=(listActivities.size()+1)){
			listActivities.add(new Category());
		}
		listActivities.add(index,act);
	}

	
	public List getListActivities() {
		return listActivities;
	}
	
	
	public void setListActivities(List listActivities) {
		this.listActivities = listActivities;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	/** Este método permite el ordenamiento de una colección de objetos Category, de acuerdo al
	 *  nombre de estos.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object comparar) {
		if(comparar!=null && comparar instanceof Category){
			Category parametro = (Category)comparar;
			//Tener cuidado con la siguiente condicion
			if(this.getActivitiesType()== null || parametro.getActivitiesType()== null || (this.getActivitiesType().equals(parametro.getActivitiesType()))){
				return this.name.compareToIgnoreCase(parametro.name);
			}
			return this.getActivitiesType().compareTo(parametro.getActivitiesType());
		}
		return 1;
	}
	
	
	public String getActivitiesType(){
		if(this.getListActivities().size()==0){return null;}
		Activity act = (Activity)this.getListActivities().get(0);
		return act.getType();
	}
	
	
	public double getPercentage() {
		return percentage;
	}
	
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
}
