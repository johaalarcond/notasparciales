package co.edu.icesi.notas.basica.builder;

import java.util.Iterator;

import co.edu.icesi.notas.Actividad;
import co.edu.icesi.notas.Categoria;
import co.edu.icesi.notas.Clasificacion;
import co.edu.icesi.notas.Curso;
import co.edu.icesi.notas.basica.Activity;
import co.edu.icesi.notas.basica.Subject;
import co.edu.icesi.notas.builder.BuilderCursoAbstracto;
import co.edu.icesi.notas.builder.CursoAbstracto;
import co.edu.icesi.notas.utilidades.OperacionesMatematicas;

/**
 * Esta clase, que implementa la interfaz BuilderCursoAbstracto, se encarga de crear un objeto
 * de la clase Subject a partir del objeto Curso que se recibe por parámetro en el método getCursoAbstracto.
 * @author mzapata
 */
public class BuilderSubjectBasico implements BuilderCursoAbstracto{

	
	/* (non-Javadoc)
	 * @see co.edu.icesi.notas.builder.BuilderCursoAbstracto#getCursoAbstracto(co.edu.icesi.notas.Curso)
	 */
	public CursoAbstracto getCursoAbstracto(Curso curso) {
		Subject subject = new Subject();

		
		crearActividades(subject,curso.getIndividuales());
		crearActividades(subject,curso.getGrupales());
		return subject;
	}
	
	/**
	 * Recorre las categorias y actividades de la Clasificacion por parámetro, para crear los
	 * objetos Activity del Subject por parámetro.
	 * @param subject Subject
	 * @param cla Clasificación
	 */
	private void crearActividades(Subject subject,Clasificacion cla){
		
		Iterator iteraCat = cla.getCategorias().iterator();
		Iterator iteraAct = null;
		Categoria cat= null;
		Actividad act=null;
		Activity activity = null;
		double porcentaje=0;
		
		while(iteraCat.hasNext()){
			cat=(Categoria)iteraCat.next();
			iteraAct=cat.getActividades().iterator();
			while(iteraAct.hasNext()){
				act=(Actividad)iteraAct.next();
				activity =  new Activity();
				activity.setCategory(cat.getTipoCategoria().getNombre());
				activity.setDate(act.getFechaRealizacion());
				activity.setDescription(act.getDescripcion());
				activity.setName(act.getNombre());
				activity.setTopics(act.getTemas());
				activity.setType(cla.getTipo());
				activity.setConsecutive(act.getConsecutivo());
				
				//Asignación del porcentaje
				porcentaje = OperacionesMatematicas.multiplicar(act.getPorcentaje()/100,cat.getPorcentaje()/100,cla.getPorcentaje()/100);
				activity.setPercentage(porcentaje*100);
				subject.getActivities().add(activity);
			}
		}
	}

}
