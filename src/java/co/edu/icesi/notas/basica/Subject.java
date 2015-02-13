package co.edu.icesi.notas.basica;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import co.edu.icesi.notas.Curso;
import co.edu.icesi.notas.builder.Builder;
import co.edu.icesi.notas.builder.BuilderCursoAbstracto;
import co.edu.icesi.notas.builder.CursoAbstracto;
import co.edu.icesi.notas.builder.FabricaBuilder;
import co.edu.icesi.notas.control.ControlSecuencia;
import co.edu.icesi.notas.utilidades.OperacionesMatematicas;

/**
 * Representa a un objeto Curso empleado para la vista básica. Esta clase tiene estructura mucho mas sencilla.
 * Simplemente está conformada por una colección de objetos Activity. Por ejemplo, si un curso tiene dos parciales 
 * (cada uno del 25%), un examen final (30%) y un quiz (20%), el objeto Subject estaría constituido de la siguiente
 * manera:<br><br>
 * Subject: MateriaX<br>
 * &nbsp;Parcial_A:    25%
 * &nbsp;Parcial_B:    25%
 * &nbsp;Examen final: 30%
 * &nbsp;Quiz:         20%
 * */
public class Subject extends CursoAbstracto{
	
	private List activities;
	private double porcentajeIndividual;
	private double porcentajeGrupal;
	
	public Subject(){
		super();
		activities=new ArrayList();
	}
	
	/**
	 * Agrega el número de activities al subject, que vengan por parámetro.
	 * @param cantidad Cantidad de actividades nuevas a agregar al Subject.
	 */
	public void agregarActivities(int cantidad){
		for(int i=0;i<cantidad;i++){
			activities.add(new Activity());
		}
	}
	
	/**
	 * Este método se encarga de eliminar tanto la Actividad como el objeto Activity que coincidan
	 * con el parámetro; los elimina de las colecciones donde se encuentre, y de la base de datos.
	 * Para ello, usa el Builder respectivo.
	 * @param conexion Conexión a la base de datos
	 * @param consecutivoActividad Consecutivo de la Actividad y del Activity a eliminar.
	 * @return true si la Actividad y el Activity fueron eliminados.
	 */
	public boolean eliminarActitivy(Connection conexion,int consecutivoActividad){
		Builder constructorCurso=FabricaBuilder.getBuilder("basica");
		return constructorCurso.eliminarActividad(this.getCurso(),this,conexion,consecutivoActividad);
	}
	
	/**
	 * Busca un Activity dado su consecutivo.
	 * @param consecutivo Consecutivo del Activity a recuperar
	 * @return Activity que coincide con el consecutivo.
	 */
	public Activity getActivity(int consecutivo){
		Iterator itera = activities.iterator();
		Activity actual;
		while(itera.hasNext()){
			actual=(Activity)itera.next();
			if(actual.getConsecutive()==consecutivo){
				return actual;
			}
		}
		return null;
	}
	/***
	public Activity getActivity(String categoria,String nombre){
		Iterator itera = activities.iterator();
		Activity actual;
		while(itera.hasNext()){
			actual=(Activity)itera.next();
			if(actual.getCategoryName().equalsIgnoreCase(categoria) && actual.getName().equals(nombre)){
				return actual;
			}
		}
		return null;
			
	}***/
	
	public Subject(Curso c){
		super();
		activities=new ArrayList();
		this.setCurso(c);
		Subject temp=cargarSubject();
              
		this.activities.addAll(temp.getActivities());
                 
		if(c.getTipoConfiguracion().equals("I")){
			this.setPorcentajeIndividual(temp.getPorcentajeIndividual());
			this.setPorcentajeGrupal(temp.getPorcentajeGrupal());
		}
	}
	
	public List getActivities() {
		return activities;
	}
	
	public void setActivities(List activities) {
		this.activities = activities;
	}
	
	/**
	 * Crea el objeto Subject, con todas sus actividades, a partir de un esquema de evaluación existente
	 * (representado mediante un objeto Curso). Para hacer esto, se emplea el objeto BuilderCursoAbstracto
	 * correspondiente.
	 * @return Subject
	 */
	private Subject cargarSubject(){
         
		BuilderCursoAbstracto constructor = FabricaBuilder.getBuilderCursoAbstracto(ControlSecuencia.getBuilderCursoAbstracto(this.getCurso().getTipoConfiguracion()));
		return (Subject)constructor.getCursoAbstracto(this.getCurso());
	}
	
	/**
	 * Este método se emplea cuando se está configurando por PRIMERA VEZ el curso. Se encarga de crear el objeto Curso
	 * a partir del objeto Subject actual; para hace esto, se apoya en el objeto Builder respectivo.
	 * @param tiposCategoria Colección con los tipos de categoría asociados al departamento académico 
	 * al cual pertecene el curso.
	 * @param conexion Conexión a la base de datos
	 */
	public void crearCurso(List tiposCategoria,Connection conexion,String tipoConf){
		Builder constructorCurso=FabricaBuilder.getBuilder(ControlSecuencia.getBuilderCurso(tipoConf));
		this.setCurso(constructorCurso.crearCurso(this,tiposCategoria,conexion));
	/*	getCurso().setConsecutivo(temp.getConsecutivo());
		getCurso().setConsecutivoMateria(temp.getConsecutivoMateria());
		getCurso().setConsecutivoPeriodo(temp.getConsecutivoPeriodo());
		getCurso().setDescripcionMateria(temp.getDescripcionMateria());
		getCurso().setPeriodoAcademico(temp.getPeriodoAcademico());
		getCurso().setGrupo(temp.getGrupo());*/
	}
	
	/**
	 * Modifica el objeto Curso, de acuerdo a los cambios realizados en la estructura del objeto Subject.
	 * Para ello, usa el Builder respectivo.
	 * @param tiposCategoria Colección con los tipos de categoría asociados al departamento académico 
	 * al cual pertecene el curso.
	 * @param conexion Conexión a la base de datos
	 */
	public void modificarCurso(List tiposCategoria,Connection conexion, String tipoConf){
		Builder constructorCurso=FabricaBuilder.getBuilder(ControlSecuencia.getBuilderCurso(tipoConf));
		constructorCurso.modificarCurso(this.getCurso(),this,tiposCategoria,conexion);
	}

	/**
	 * Determina si la suma de los objetos Activity es 100%.
	 * @return true si la suma es equivalente al 100%.
	 */
	public boolean validarSuma(){
 		double suma=0;
 		for(int i=0;i<activities.size();i++){
 		suma+=((Activity)activities.get(i)).getPercentage();	
 		}
 		return OperacionesMatematicas.redondear(suma,0)==100.0;
 	}
	
	/**
	 * Determina si alguno de los porcentajes de las Activities del Subject
	 * es igual o menor a cero.
	 * @return true si la suma de ninguno de los porcentajes de los Activities es menor o igual a cero. 
	 */
	public boolean verificarPorcentajesMenoresCero(){
		Iterator itera = activities.iterator();
		Activity actual;
		while(itera.hasNext()){
			actual=(Activity)itera.next();
			if(actual.getPercentage()<=0){
				return false;
			}
		}
		return true;
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
