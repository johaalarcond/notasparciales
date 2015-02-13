/*
 * Created on 12/12/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.builder;

import java.util.HashMap;
import java.util.Map;

import co.edu.icesi.notas.basica.builder.BuilderCursoBasico;
import co.edu.icesi.notas.basica.builder.BuilderCursoIntermedio;
import co.edu.icesi.notas.basica.builder.BuilderSubjectBasico;
import co.edu.icesi.notas.basica.builder.BuilderSubjectIntermedio;

/**
 * Clase abstracta y con métodos estáticos, que se encarga de retornar los builders para
 * construir el objeto Curso y los builders para construir  un objeto que herede de CursoAbstracto.
 * @author mzapata
 */
public abstract class FabricaBuilder {
	
	private static Map builderMap, builderMapAbstracto;
	
	static{
		builderMap=new HashMap();
		builderMap.put("basica",new BuilderCursoBasico());
		builderMap.put("intermedio",new BuilderCursoIntermedio());
		
		builderMapAbstracto=new HashMap();
		builderMapAbstracto.put("subjectBasico",new BuilderSubjectBasico());
		builderMapAbstracto.put("subjectIntermedio",new BuilderSubjectIntermedio());
	}
	
	/**
	 * Retorna un objeto que implemente la interfaz BuilderCursoBasico y que coincida con el nombre
	 * por parámetro.
	 * @param nombre Nombre del Builder que se desea retornar
	 * @return Builder
	 */
	public static Builder getBuilder(String nombre){
		if(nombre!=null){
			return (Builder)builderMap.get(nombre);
		}
		return null;
	}
	
	/**
	 * Retorna un objeto que implemente la interfaz BuilderCursoAbstracto y que coincida con el nombre
	 * por parámetro.
	 * @param nombre Nombre del BuilderCursoAbstracto que se desea retornar
	 * @return BuilderCursoAbstracto
	 */
	public static BuilderCursoAbstracto getBuilderCursoAbstracto(String nombre){
		if(nombre!=null){
			return (BuilderCursoAbstracto)builderMapAbstracto.get(nombre);
		}
		return null;
	}

}
