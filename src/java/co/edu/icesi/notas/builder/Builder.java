package co.edu.icesi.notas.builder;

import java.sql.Connection;
import java.util.List;

import co.edu.icesi.notas.Actividad;
import co.edu.icesi.notas.Clasificacion;
import co.edu.icesi.notas.Curso;
import co.edu.icesi.notas.TipoCategoria;
import co.edu.icesi.notas.basica.Activity;

/**
 * Interfaz que define el comportamiento de las clases encargadas de crear los objetos Curso (y su contenido).
 * Esta interfaz implementa el patrón de diseño de creación orientado a objetos llamado Builder.
 * @author mzapata
 */
public interface Builder {
	
	/**
	 * Este método se encarga de crear un objeto Curso a partir de un objeto que herede de la 
	 * clase CursoAbstracto. 
	 * @param curso CursoAbstracto a partir del cual se creará el objeto Curso.
	 * @param tipoCategorias Colección con los tipos de categoría asociados al departamento académico 
	 * al cual pertecene el curso.
	 * @param conexion Conexión a la base de datos.
	 * @return Curso creado a partir del CursoAbstracto por parámetro.
	 */
	public Curso crearCurso(CursoAbstracto curso,List tipoCategorias,Connection conexion);
	
	/**
	 * Este método se encarga de actualizar el objeto Curso y todos los objetos que los contiene.
	 * @param curso Curso al acual se 
	 * @param cursoAbstracto CursoAbstracto
	 * @param tipoCategorias Colección con los tipos de categoría asociados al departamento académico 
	 * al cual pertecene el curso.
	 * @param conexion Conexión a la base de datos.
	 */
	public void modificarCurso(Curso curso, CursoAbstracto cursoAbstracto,List tipoCategorias,Connection conexion);
	
	/**
	 * Elimina la actividad del Curso cuyo consecutivo coincida con el parámetro.
	 * @param curso Curso del cual se eliminará la Actividad.
	 * @param cursoAbstracto CursoAbstracto
	 * @param conexion Conexión a la base de datos.
	 * @param consecutivoActividad Consecutivo de la Actividad a Eliminar.
	 * @return true si la Actividad fue eliminada.
	 */
	public boolean eliminarActividad(Curso curso,CursoAbstracto cursoAbstracto,Connection conexion,int consecutivoActividad);
	
	/**
	 * Crea al curso con sus respectivas clasificaciones.
	 * @param curso Curso al cual se le crearán las clasificaciones.
	 */
	void construir(Curso curso);
	
	/**
	 * Crea las categorías asociadas a una clasificación.
	 * @param curso
	 * @param cla
	 * @param tipoCategorias
	 * @param conexion
	 */
	void crearCategorias(Curso curso,Clasificacion cla,List tipoCategorias,Connection conexion);
	
	/**
	 * Determina si el tipo de categoría (representada por el parámetro nombre)
	 * está en la colección de tipos de categorías.
	 * @param tipoCategorias Colección con los tipos de categoría asociados al departamento académico 
	 * al cual pertecene el curso.
	 * @param nombre Nombre del tipo de categoría a buscar.
	 * @return En caso de que la categoría esté, se retorna. En caso contrario, se
	 * devuelve null.
	 */
	TipoCategoria estaEnLista(List tipoCategorias,String nombre);
	/**
	 * Crea un nuevo tipo de categoría y la guarda en la base de datos.
	 * @param nombre Nombre del objeto TipoCategoria a crear.
	 * @param tipoCategorias Colección con los tipos de categoría asociados al departamento académico 
	 * al cual pertecene el curso.
	 * @param conexion Conexión a la base de datos.
	 * @return Objeto TipoCategoria creado y guardado en la base de datos
	 */
	TipoCategoria guardarNuevoTipoCateg(String nombre,List tipoCategorias, Connection conexion);
	
	/**
	 * Determina si la categoría (representada por tipoCat) está contenida en la Clasificación.
	 * @param cla Clasificación
	 * @param tipoCat TipoCategoria a buscar en el objeto Clasificación
	 * @return true si el tipo de categoría existe en el objeto Clasificación
	 */
	boolean existeCategoria(Clasificacion cla,TipoCategoria tipoCat);
	
	/**
	 * Calcula el porcentaje que le corresponde a cada categoría, en base a los objetos Activity respectivos.
	 * @param cla Objeto Clasificación que contiene las categorías.
	 * **/
	void porcentajeCategorias(Clasificacion cla);
	
	/**
	 * Crea las actividades y las asocia a su respectiva categoría, dentro de la Clasificación 
	 * por parámetro.
	 * @param cla Clasificacion
	 */
	void crearActividades(Clasificacion cla);
	
	/**
	 * Almacena los objetos creados en la BD.
	 * @param curso Curso cuyas clasificaciones, categorias y actividades se guardarán en la base de datos.
	 * @param conexion Conexión a la base de datos.
	 */
	void guardarBD(Curso curso,Connection conexion);
	
	/**
	 * Retorna el objeto Activity asociado con la Actividad por parámetro.
	 * @param actividad Actividad
	 * @return Activity
	 */
	Activity obtenerActivity(Actividad actividad);
	
	/**
	 * Crea una nueva Actividad con la información del objeto Activity por parámetro.
	 * @param actual Activity  para la cual se creará una Actividad.
	 * @param tipoCategorias Colección con los tipos de categoría asociados al departamento académico 
	 * al cual pertecene el curso.
	 * @param conexion Conexión a la base de datos.
	 * @param curso Curso al cual se agregará una nueva actividad.
	 */
	void agregarActivity(Activity actual,List tipoCategorias,Connection conexion,Curso curso);
	
	/**
	 * Este método llama a los métodos respectivos encargados de calcular los porcentajes para
	 * las clasificaciones, las categorias y los porcentajes.
	 * @param curso Curso al que se le recalcularán los porcentajes.
	 */
	void recalcularPorcentajes(Curso curso);
	
	/**
	 * Como su nombre lo indica, modifica el porcentaje de los actividades.
	 * @param cla Clasificación a la que se modificaran los porcentajes de sus actividades.
	 */
	void modificarPorcentajesActividades(Clasificacion cla);
	
	/**
	 * Recorre la colección de Activities del Subject, y para cada una actualiza su objeto Actividad
	 * homólogo, el cual está contenido en el curso.
	 * @param curso Curso al cual se actualizarán sus actividades.
	 */
	void actualizarInfoActividades(Curso curso);
	
	/**
	 * Actualiza la información del objeto Actividad equivalente al objeto Activity por parámetro. 
	 * @param actual Objeto tipo Activity
	 * @param curso Curso al cual pertenece la Actividad a actualizar.
	 */
	void actualizarActividad(Activity actual,Curso curso);
	
	/**
	 * Este método se invoca luego de haber modificado los porcentajes de la actividades.
	 * Realiza las actualizaciones respectivas en la base de datos para los objetos Clasificacion,
	 * Categoria y Actividad, del curso.
	 * @param curso Curso al cual se actualizarán sus ítems.
	 * @param conexion Conexión a la base de datos.
	 */
	void actualizarBD(Curso curso, Connection conexion);
}
