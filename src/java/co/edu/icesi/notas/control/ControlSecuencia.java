package co.edu.icesi.notas.control;

import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase, dependiendo de si el curso posee una configuración básica o intermedia,
 * establece cuales son los métodos, páginas o acciones a invocar.
 * 
 * @author mzapata
 */
public abstract class ControlSecuencia {
	
	private static Map mapModificarEsquema, mapRedistribucionPorcentaje;
	private static Map mapFracasoPorcentaje, mapBuilderCurso;
	private static Map mapBuilderCursoAbstracto;
	private static Map mapRegresarPorcentaje;
	
	static{
		mapModificarEsquema = new HashMap();
		mapModificarEsquema.put("B","seguirBasico");
		mapModificarEsquema.put("I","seguirIntermedio");
		
		mapRedistribucionPorcentaje = new HashMap();
		mapRedistribucionPorcentaje.put("B","continuarRedistBasico");
		mapRedistribucionPorcentaje.put("I","continuarRedistIntermedio");
		
		mapFracasoPorcentaje = new HashMap();
		mapFracasoPorcentaje.put("B","fracasoBasico");
		mapFracasoPorcentaje.put("I","fracasoIntermedio");
		
		mapBuilderCurso = new HashMap();
		mapBuilderCurso.put("B","basica");
		mapBuilderCurso.put("I","intermedio");
		
		mapBuilderCursoAbstracto = new HashMap();
		mapBuilderCursoAbstracto.put("B","subjectBasico");
		mapBuilderCursoAbstracto.put("I","subjectIntermedio");
		
		mapRegresarPorcentaje = new HashMap();
		mapRegresarPorcentaje.put("B","exitoBasico");
		mapRegresarPorcentaje.put("I","exitoIntermedio");
		
	}
	
	public static String getNextModificarEsquema(String key){
		return (String)mapModificarEsquema.get(key);
	}
	
	public static String getNextModificarPorcentaje(String key){
		return (String)mapRedistribucionPorcentaje.get(key);
	}
	
	public static String getNextFracasoPorcentaje(String key){
		return (String)mapFracasoPorcentaje.get(key);
	}
	
	public static String getBuilderCurso(String key){
		return (String)mapBuilderCurso.get(key);
	}
	
	public static String getBuilderCursoAbstracto(String key){
		return (String)mapBuilderCursoAbstracto.get(key);
	}
	
	public static String getNextRegresarPorcentaje(String key){
		return (String)mapRegresarPorcentaje.get(key);
	}
	
}
