package co.edu.icesi.notas.builder;

import co.edu.icesi.notas.Curso;

/**
 * Esta interfaz define el comportamiento requerido para crear objetos que hereden de la clase
 * CursoAbstracto, a partir de un objeto Curso que se reciba por parámetro.
 * @author mzapata
 */
public interface BuilderCursoAbstracto {
	
	/**
	 * Método encargado de crear una estructura de objetos (accesible mediante el objeto Subject que se retorna)
	 * empleando el objeto Curso como parámetro.
	 * @param curso Curso
	 * @return CursoAbstracto
	 */
	public CursoAbstracto getCursoAbstracto(Curso curso);

}
