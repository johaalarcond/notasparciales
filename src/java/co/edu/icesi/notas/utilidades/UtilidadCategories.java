package co.edu.icesi.notas.utilidades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import co.edu.icesi.notas.basica.Activity;
import co.edu.icesi.notas.form.Category;

public abstract class UtilidadCategories {
	
	
	/**Determina si las activities del tipo especificado por parámetro (I o G)
	 * suman 100%. Esto aplica en caso de que el tipo de configuración del curso
	 * sea intermedio.
	 * 
	 * @return true si la suma es 100%.
	 * */
	public static boolean validarSumaClasif(List activities, String tipoClasif){
		Iterator iteraAct = activities.iterator();
		Activity actual;
		double total=0;
		boolean entro = false;
		while(iteraAct.hasNext()){
			entro = true;
			actual = (Activity)iteraAct.next();
			if(actual.getType().equals(tipoClasif)){
				total += actual.getPercentage();
			}
		}
		total = OperacionesMatematicas.redondear(total,1);
		if((total == 100) || !entro){
			return true;
		}
		return false;
	}
	
	/**
	 * Este método se encarga de distribuir equitativamente el porcentaje del objeto Category
	 * entre cada una de las Activities que lo conforman.
	 * */
	public static void redistribuirPorcentajes(Category category){
		double porcentajeCateg = category.getPercentage();
		int totalCateg = category.getListActivities().size();
		double porcentajeAct = OperacionesMatematicas.dividir(porcentajeCateg,totalCateg); //Obtenemos el porcentaje por categoría.
		porcentajeAct = OperacionesMatematicas.redondear(porcentajeAct,1);
		asignarPorcentajeActivity(category,porcentajeAct);
		/* Si la suma de los porcentajes de las activities no es EXACTAMENTE el porcentaje
		 * del objeto Category, entonces resulta necesario hacer un ajuste 
		 * (Ver documento de requerimientos a Junio 2007). */
		double porcentajeTotal = OperacionesMatematicas.multiplicar(porcentajeAct,totalCateg);
		porcentajeTotal = OperacionesMatematicas.redondear(porcentajeTotal,1);
		
		if(porcentajeTotal != porcentajeCateg){
			Iterator iterador;
			Activity act;
			double diferencia = OperacionesMatematicas.restar(porcentajeCateg,porcentajeTotal);
			diferencia = OperacionesMatematicas.redondear(diferencia,1);
			boolean continuar;
			double suma;
			while(diferencia!=0){
				iterador = category.getListActivities().iterator();
				continuar = true;
				while(iterador.hasNext() && continuar){
					act = (Activity)iterador.next();
					if(diferencia>0){
						suma = OperacionesMatematicas.sumar(act.getPercentage(),0.1);
						suma = OperacionesMatematicas.redondear(suma,1);
						act.setPercentage(suma);
						diferencia = OperacionesMatematicas.restar(diferencia,0.1);
						diferencia = OperacionesMatematicas.redondear(diferencia,1);
					}else{
						if(diferencia<0){
							suma = OperacionesMatematicas.restar(act.getPercentage(),0.1);
							suma = OperacionesMatematicas.redondear(suma,1);
							act.setPercentage(suma);
							
							diferencia = OperacionesMatematicas.sumar(diferencia,0.1);
							diferencia = OperacionesMatematicas.redondear(diferencia,1);
						}else{
							continuar = false;
						}
					}
				}
			}
		}
	}
	
	/**
	 * Asigna el porcentaje por parámetro a todas las activities de la Category por parámetro. 
	 * */
	private static void asignarPorcentajeActivity(Category cat, double porcentaje){
		Iterator iterador = cat.getListActivities().iterator();
		while(iterador.hasNext()){
			((Activity)iterador.next()).setPercentage(porcentaje);
		}
	}
	
	public static ArrayList organizarActividades(List actividades){
		
		ArrayList categorias=obtenerCategorias(actividades);
		
		Activity actividad;
		int pos;
		for(int i=0;i<actividades.size();i++){
			actividad=(Activity)actividades.get(i);
			if(actividad.getCategory().equals("Otra")){
				actividad.setCategory(actividad.getOther());
			}
			pos=obtenerPosicionCategoria(actividad.getCategory(),categorias);
			((Category)categorias.get(pos)).getListActivities().add(actividad);
		}
		calcularPorcentajesCategorias(categorias);
		Collections.sort(categorias);
		Collections.reverse(categorias); // Las Categories quedan ordenadas en sentido 'opuesto' al definido en el compareTo
		return categorias;
	}
	
	/**
	 * Este método calcula el porcentaje del objeto Category a partir de la suma de los
	 * porcentajes de las activities que contiene.
	 * */
	private static void calcularPorcentajesCategorias(List categorias){
		
		ListIterator iteraCat = categorias.listIterator();
		Category cat;
		Activity act;
		double porcentaje;
		while(iteraCat.hasNext()){
			cat = (Category)iteraCat.next();
			ListIterator iteraAct = cat.getListActivities().listIterator();
			porcentaje = 0;
			while(iteraAct.hasNext()){
				act = (Activity)iteraAct.next();
				porcentaje += act.getPercentage();
			}
			cat.setPercentage(OperacionesMatematicas.redondear(porcentaje,1));
		}
	} 
	
	public static ArrayList obtenerCategorias(List lista){
		ArrayList cat=new ArrayList();
		Category nueva = null;
		String categ,otro;
		for(int i=0;i<lista.size();i++){
			categ=((Activity)lista.get(i)).getCategory();
			nueva=new Category();
			nueva.setName(categ);
			
			otro=((Activity)lista.get(i)).getOther();
			if(otro!=null && !otro.equals("")){
				nueva.setName(otro);
			}
			if(!cat.contains(nueva)){
				
				cat.add(nueva);
			}
			
		}
		//Ordenamos la lista de categorìas a ordenar.
		Collections.sort(cat);
		return cat;
	}
	
	public static int obtenerPosicionCategoria(String cat,ArrayList categorias){
		Category actual;
		for(int i=0;i<categorias.size();i++){
			actual=(Category)categorias.get(i);
			if(actual.getName().equalsIgnoreCase(cat)){
				return i;
			}
		}
		return 0;
	}
}
