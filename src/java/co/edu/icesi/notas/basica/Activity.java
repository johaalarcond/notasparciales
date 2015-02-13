package co.edu.icesi.notas.basica;

import java.io.Serializable;

import co.edu.icesi.notas.utilidades.OperacionesMatematicas;

/**
 * Clase empleada para la vista básica de la aplicación. Es similar a un objeto Actividad, con 
 * la diferencia de que contiene información extra (que normalmente se encontraba separada en otras clases) 
 * como el nombre de la categoria a la cual pertenece -category-, si es individual o grupal -type-.<br><br>
 * El atributo 'consecutive' es igual a cero cuando el objeto Activity no ha sido registrado en la base de 
 * datos; toma un valor distinto cuando el objeto Actividad equivalente ha sido registrado en la base de datos,
 * y en tal caso asigna el consecutivo de ese objeto Actividad.<br><br>
 * El atributo 'other' será diferente de null cuando el nombre de la categorrìa no exista en la base de datos.
 * En tal caso, este atributo tendrá el nombre de esa nueva categoría.
 * @author mzapata
 */
public class Activity implements Serializable,Comparable{
	
	private String name;
	private	String category;
	private String type; //I(Individual) o G(Grupal)
	private String topics;
	private String description;
	private double percentage;
	private String other; //Other category
	private String date; //Fecha de realización.
	private int consecutive;
	
	public Activity(){
		super();
		//valores por defecto
		consecutive=0;
		name="";
		type="I";
	}
	
	public boolean equals(Object obj){
		if(obj instanceof Activity){
			Activity act = (Activity)obj;
			return act.consecutive==this.consecutive;
		}
		return false;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	/**
	 * Retorna el atributo 'category' cuando este es diferente de null; en caso contrario
	 * retorna el atributo 'other'.
	 * @return Cadena
	 */
	public String getCategoryName(){
		return this.getCategory()!=null?this.getCategory():this.getOther();
	}
	
	/** Determina si el objeto por parámetro es mayor, menor o igual al actual, dependiendo
	 * del nombre del mismo.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object comparar) {	
		if(comparar!=null && comparar instanceof Activity){
			Activity parametro = (Activity)comparar;
			return this.name.compareToIgnoreCase(parametro.name);
		}
		return 1;
	}
	
	public String getOther() {
		return other;
	}
	
	public void setOther(String other) {
		this.other = other;
	}
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	/**
         * Se obtiene el nombre de la actividad.
         * Cambio por ffceballos, ya que se tenia problemas con las contraciones de las palabras en ingles.
         * @return 
         */
	public String getName() {
           /* if(name.contains("'"))
            {
               return name.replace("'", "`");
            }*/
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getPercentage() {
		//return percentage;
		return OperacionesMatematicas.redondear(percentage,2);
	}
	
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	
	public String getTopics() {
		return topics;
	}
	
	public void setTopics(String topics) {
		this.topics = topics;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public int getConsecutive() {
		return consecutive;
	}
	
	public void setConsecutive(int consecutive) {
		this.consecutive = consecutive;
	}
}
