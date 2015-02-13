package co.edu.icesi.notas.basica.builder;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import co.edu.icesi.notas.Actividad;
import co.edu.icesi.notas.Alumno;
import co.edu.icesi.notas.Categoria;
import co.edu.icesi.notas.Clasificacion;
import co.edu.icesi.notas.Curso;
import co.edu.icesi.notas.Matricula;
import co.edu.icesi.notas.TipoCategoria;
import co.edu.icesi.notas.basica.Activity;
import co.edu.icesi.notas.basica.Subject;
import co.edu.icesi.notas.builder.Builder;
import co.edu.icesi.notas.builder.CursoAbstracto;
import co.edu.icesi.notas.utilidades.OperacionesMatematicas;

/**
 * Esta clase se encargará de crear un Curso de NOTPA, a partir de un
 * CursoAbstracto (tipo Subject) que corresponde a la vista básica.
 * 
 * @author mzapata, lmdiaz
 */
public class BuilderCursoBasico implements Builder {

	private Subject cursoBasico;
	private String dptoAcademico;

	public BuilderCursoBasico() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.edu.icesi.notas.builder.Builder#crearCurso(co.edu.icesi.notas.builder
	 * .CursoAbstracto, java.util.List, java.sql.Connection)
	 */
	public Curso crearCurso(CursoAbstracto curso, List tipoCategorias,
			Connection conexion) {
		this.cursoBasico = (Subject) curso;
		construir(cursoBasico.getCurso());
		Curso retorno = cursoBasico.getCurso();
		dptoAcademico = retorno.buscarDeptoCurso(conexion);

		crearCategorias(retorno, retorno.getIndividuales(), tipoCategorias,
				conexion);
		crearCategorias(retorno, retorno.getGrupales(), tipoCategorias,
				conexion);

		porcentajeCategorias(retorno.getIndividuales());
		porcentajeCategorias(retorno.getGrupales());

		crearActividades(retorno.getIndividuales());
		crearActividades(retorno.getGrupales());

		ArrayList categorias = retorno.getCategorias();

		for (int i = 0; i < categorias.size(); i++) {
			Categoria cat = (Categoria) categorias.get(i);

			cat.cargarActividad(conexion);
			for (int j = 0; j < cat.getActividades().size(); j++) {
				Actividad est = (Actividad) cat.getActividades().get(j);
				for (int k = 0; k < retorno.getAlumnos().size(); k++) {
					Matricula matricula = new Matricula(retorno,
							(Alumno) retorno.getAlumnos().get(k));
					matricula.setActividad(est);
					matricula.cargarCalificacion(conexion);
					est.getMatriculas().add(matricula);
				}
			}
		}

		guardarBD(retorno, conexion);

		return retorno;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.edu.icesi.notas.builder.Builder#modificarCurso(co.edu.icesi.notas.
	 * Curso, co.edu.icesi.notas.builder.CursoAbstracto, java.util.List,
	 * java.sql.Connection)
	 */
	public void modificarCurso(Curso curso, CursoAbstracto cursoAbstracto,
			List tipoCategorias, Connection conexion) {
		this.cursoBasico = (Subject) cursoAbstracto;

		dptoAcademico = curso.buscarDeptoCurso(conexion);
		// Recorremos la lista de activities
		Iterator itera = cursoBasico.getActivities().iterator();
		Activity actual;
		while (itera.hasNext()) {
			actual = (Activity) itera.next();
			// En caso de que sea una nueva actividad.
			if (actual.getConsecutive() == 0) {
				agregarActivity(actual, tipoCategorias, conexion, curso);
				// actualizarActivity(actual,curso,tipoCategorias,conexion);
			}
		}
		// luego de agregar o borrar activities, resulta necesario recalcular
		// porcentajes
		this.recalcularPorcentajes(curso);
		this.actualizarInfoActividades(curso);
		this.actualizarBD(curso, conexion);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.edu.icesi.notas.builder.Builder#actualizarInfoActividades(co.edu.icesi
	 * .notas.Curso)
	 */
	public void actualizarInfoActividades(Curso curso) {
		Iterator itera = this.cursoBasico.getActivities().iterator();
		Activity actual;
		while (itera.hasNext()) {
			actual = (Activity) itera.next();
			actualizarActividad(actual, curso);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.edu.icesi.notas.builder.Builder#eliminarActividad(co.edu.icesi.notas
	 * .Curso, co.edu.icesi.notas.builder.CursoAbstracto, java.sql.Connection,
	 * int)
	 */
	public boolean eliminarActividad(Curso curso,
			CursoAbstracto cursoAbstracto, Connection conexion,
			int consecutivoActividad) {
		// eliminamos la actividad del curso.
		Actividad actividad = curso.getActividad(consecutivoActividad);
		int respuesta = actividad.eliminarBd(conexion);

		dptoAcademico = curso.buscarDeptoCurso(conexion);

		if (respuesta == 2) {
			actividad.registrarEliminacion(conexion, curso);
			actividad.getCategoria().getActividades().remove(actividad);
			// eliminamos la activity de subject
			Subject subject = (Subject) cursoAbstracto;
			Activity activity = subject.getActivity(actividad.getConsecutivo());
			subject.getActivities().remove(activity);
			this.cursoBasico = (Subject) subject;
			// this.recalcularPorcentajes(curso);
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.edu.icesi.notas.builder.Builder#agregarActivity(co.edu.icesi.notas
	 * .basica.Activity, java.util.List, java.sql.Connection,
	 * co.edu.icesi.notas.Curso)
	 */
	public void agregarActivity(Activity actual, List tipoCategorias,
			Connection conexion, Curso curso) {
		TipoCategoria tipoCateg = estaEnLista(tipoCategorias,
				actual.getCategoryName());
		if (tipoCateg == null) {
			tipoCateg = guardarNuevoTipoCateg(actual.getCategoryName(),
					tipoCategorias, conexion);
			actual.setCategory(tipoCateg.getNombre());
			actual.setOther(null);
		}
		// Determinamos si la categoría ya existe en la clasificación
		Clasificacion cla = actual.getType().equals("I") ? curso
				.getIndividuales() : curso.getGrupales();
		Clasificacion cla2 = actual.getType().equals("I") ? curso.getGrupales()
				: curso.getIndividuales();
		Categoria categoria;
		if (!existeCategoria(cla, tipoCateg)) {
			if (!existeCategoria(cla2, tipoCateg)) {
				categoria = new Categoria();
				categoria.setTipoCategoria(tipoCateg);
				categoria.setClasificacion(cla);
				categoria.setActivo("S");
				cla.getCategorias().add(categoria);
				curso.getCategorias().add(categoria);
				// guardamos la nueva categoría
				categoria.guardarBd(conexion);
			} else {
				categoria = cla2.getCategoria(tipoCateg);
				cla.getCategorias().add(categoria);
				cla2.getCategorias().remove(categoria);
				categoria.setClasificacion(cla);
				categoria.actualizarClasificacionBd(conexion);

			}
		} else {
			categoria = cla.getCategoria(tipoCateg);
		}
		// creamos el objeto actividad
		Actividad actividad = new Actividad();
		actividad.setCategoria(categoria);
		actividad.setDescripcion(actual.getDescription());
		actividad.setFechaRealizacion(actual.getDate());
		actividad.setNombre(actual.getName());
		actividad.setTemas(actual.getTopics());
		actividad.agregarNuevasMatriculas();
		categoria.getActividades().add(actividad);
		// guardamos la nueva actividad
		// Se debe verificar si alguna vez se guardo y luego se elimino
		int cons = actividad.getConsecutivoActividad(conexion);
		if (cons == 0) {
			actividad.guardarBd(conexion);
		} else {
			actividad.setConsecutivo(cons);
			actividad.reactivarBd(conexion);
		}

		actual.setConsecutive(actividad.getConsecutivo());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.edu.icesi.notas.builder.Builder#recalcularPorcentajes(co.edu.icesi
	 * .notas.Curso)
	 */
	public void recalcularPorcentajes(Curso curso) {

		// para clasificaciones
		curso.getIndividuales().setPorcentaje(
				porcentajeClasificacion(curso.getIndividuales().getTipo()));
		curso.getGrupales().setPorcentaje(
				porcentajeClasificacion(curso.getGrupales().getTipo()));

		// para categorias
		porcentajeCategorias(curso.getIndividuales());
		porcentajeCategorias(curso.getGrupales());

		// para actividades
		modificarPorcentajesActividades(curso.getIndividuales());
		modificarPorcentajesActividades(curso.getGrupales());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.edu.icesi.notas.builder.Builder#modificarPorcentajesActividades(co
	 * .edu.icesi.notas.Clasificacion)
	 */
	public void modificarPorcentajesActividades(Clasificacion cla) {
		Categoria cat = null;
		Iterator iteraCat = cla.getCategorias().iterator();
		Iterator iteraAct = null;
		Activity act = null;
		String nombreCat = "";
		Actividad actividad;
		double porcentajeAct = 0;
		while (iteraCat.hasNext()) {
			cat = (Categoria) iteraCat.next();
			iteraAct = cursoBasico.getActivities().iterator();
			while (iteraAct.hasNext()) {
				act = (Activity) iteraAct.next();
				nombreCat = act.getCategoryName();

				if (nombreCat.equalsIgnoreCase(cat.getTipoCategoria()
						.getNombre())
						&& act.getConsecutive() != 0
						&& cla.getTipo().equals(act.getType())) {
					actividad = cat.getActividad(act.getConsecutive());
					porcentajeAct = OperacionesMatematicas.dividir(
							act.getPercentage() / 100,
							(cat.getPorcentaje() / 100)
									* (cla.getPorcentaje() / 100));
					actividad.setPorcentaje(porcentajeAct * 100);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.edu.icesi.notas.builder.Builder#actualizarActividad(co.edu.icesi.notas
	 * .basica.Activity, co.edu.icesi.notas.Curso)
	 */
	public void actualizarActividad(Activity actual, Curso curso) {
		Actividad actividad = curso.getActividad(actual.getConsecutive());
		actividad.setDescripcion(actual.getDescription());
		actividad.setFechaRealizacion(actual.getDate());
		actividad.setTemas(actual.getTopics());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.edu.icesi.notas.builder.Builder#actualizarBD(co.edu.icesi.notas.Curso,
	 * java.sql.Connection)
	 */
	public void actualizarBD(Curso curso, Connection conexion) {
		if (curso.getIndividuales().getPorcentaje() != 0) {
			curso.getIndividuales().actualizarBD(conexion);
		}
		if (curso.getGrupales().getPorcentaje() != 0) {
			curso.getGrupales().actualizarBD(conexion);
		}
		Iterator itera = curso.getCategorias().iterator();
		Iterator itera2 = null;
		Categoria cat = null;
		Actividad act = null;

		// Recorremos colección de categorías.
		while (itera.hasNext()) {
			cat = (Categoria) itera.next();
			cat.actualizarBd(conexion);
			itera2 = cat.getActividades().iterator();
			while (itera2.hasNext()) {
				act = (Actividad) itera2.next();
				act.actualizarBd(conexion);
				act.registrarCambios(conexion, curso);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.edu.icesi.notas.builder.Builder#guardarBD(co.edu.icesi.notas.Curso,
	 * java.sql.Connection)
	 */
	public void guardarBD(Curso curso, Connection conexion) {
		curso.getIndividuales().guardarBD(conexion);
		curso.getGrupales().guardarBD(conexion);
		Iterator itera = curso.getCategorias().iterator();
		Iterator itera2 = null;
		Categoria cat = null;
		Actividad act = null;

		Activity activity = null;
		// Recorremos colección de categorías.
		while (itera.hasNext()) {
			cat = (Categoria) itera.next();
			cat.guardarBd(conexion);
			itera2 = cat.getActividades().iterator();
			while (itera2.hasNext()) {
				act = (Actividad) itera2.next();
				act.guardarBd(conexion);
				activity = obtenerActivity(act);
				activity.setConsecutive(act.getConsecutivo());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.edu.icesi.notas.builder.Builder#obtenerActivity(co.edu.icesi.notas
	 * .Actividad)
	 */
	public Activity obtenerActivity(Actividad actividad) {
		Iterator itera = cursoBasico.getActivities().iterator();
		Activity actual;
		String nombreCat = actividad.getCategoria().getTipoCategoria()
				.getNombre();
		while (itera.hasNext()) {
			actual = (Activity) itera.next();
			if (actual.getCategory().equalsIgnoreCase(nombreCat)
					&& actual.getName().equalsIgnoreCase(actividad.getNombre())) {
				return actual;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.edu.icesi.notas.builder.Builder#crearActividades(co.edu.icesi.notas
	 * .Clasificacion)
	 */
	public void crearActividades(Clasificacion cla) {
		Categoria cat = null;
		Iterator iteraCat = cla.getCategorias().iterator();
		Iterator iteraAct = null;
		Activity act = null;
		String nombreCat = "";
		Actividad actividad;
		double porcentajeAct = 0;
		while (iteraCat.hasNext()) {
			cat = (Categoria) iteraCat.next();
			iteraAct = cursoBasico.getActivities().iterator();
			while (iteraAct.hasNext()) {
				act = (Activity) iteraAct.next();
				nombreCat = act.getCategoryName();

				if (nombreCat.equalsIgnoreCase(cat.getTipoCategoria()
						.getNombre())) {
					actividad = new Actividad();
					actividad.setCategoria(cat);
					actividad.setDescripcion(act.getDescription());

					actividad.setFechaRealizacion(act.getDate());
					actividad.setNombre(act.getName());
					actividad.setTemas(act.getTopics());
					porcentajeAct = OperacionesMatematicas.dividir(
							act.getPercentage() / 100,
							(cat.getPorcentaje() / 100)
									* (cla.getPorcentaje() / 100));
					actividad.setPorcentaje(porcentajeAct * 100);
					cat.getActividades().add(actividad);
				}
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.edu.icesi.notas.builder.Builder#porcentajeCategorias(co.edu.icesi.
	 * notas.Clasificacion)
	 */
	public void porcentajeCategorias(Clasificacion cla) {
		Categoria cat = null;
		Iterator iteraCat = cla.getCategorias().iterator();
		Iterator iteraAct = null;
		Activity act = null;
		double totalItems;
		String nombreCat = "";
		while (iteraCat.hasNext()) {
			totalItems = 0;
			cat = (Categoria) iteraCat.next();
			iteraAct = cursoBasico.getActivities().iterator();
			while (iteraAct.hasNext()) {
				act = (Activity) iteraAct.next();
				nombreCat = act.getCategoryName();
				/*
				 * Calculamos la suma total de los porcentajes de los items de
				 * la categoría.
				 */
				if (nombreCat.equalsIgnoreCase(cat.getTipoCategoria()
						.getNombre()) && cla.getTipo().equals(act.getType())) {
					totalItems += act.getPercentage();
				}
			}
			/*
			 * Dividimos totalItems/porcentajeClasificacion para obtener el
			 * porcentaje de la categoría.
			 */
			cat.setPorcentaje(OperacionesMatematicas.dividir(totalItems,
					cla.getPorcentaje()) * 100);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.edu.icesi.notas.builder.Builder#crearCategorias(co.edu.icesi.notas
	 * .Curso, co.edu.icesi.notas.Clasificacion, java.util.List,
	 * java.sql.Connection)
	 */
	public void crearCategorias(Curso curso, Clasificacion cla,
			List tipoCategorias, Connection conexion) {
		Iterator iterador = cursoBasico.getActivities().iterator();
		Activity actual;
		String nombreTipoCateg = "";
		TipoCategoria tipoCateg = null;
		Categoria categoria = null;
		while (iterador.hasNext()) {
			actual = (Activity) iterador.next();
			if (actual.getType().equals(cla.getTipo())) {
				nombreTipoCateg = actual.getCategoryName();
				tipoCateg = estaEnLista(tipoCategorias, nombreTipoCateg);
				if (tipoCateg == null) {
					tipoCateg = guardarNuevoTipoCateg(nombreTipoCateg,
							tipoCategorias, conexion);
				}
				/*
				 * En caso de que la categoría se halla ingresado como 'other',
				 * esta se asigna al atributo category.
				 */
				actual.setCategory(tipoCateg.getNombre());
				actual.setOther(null);
				if (!existeCategoria(cla, tipoCateg)) {
					categoria = new Categoria();
					categoria.setTipoCategoria(tipoCateg);
					categoria.setClasificacion(cla);
					categoria.setActivo("S");
					cla.getCategorias().add(categoria);
					curso.getCategorias().add(categoria);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.edu.icesi.notas.builder.Builder#existeCategoria(co.edu.icesi.notas
	 * .Clasificacion, co.edu.icesi.notas.TipoCategoria)
	 */
	public boolean existeCategoria(Clasificacion cla, TipoCategoria tipoCat) {
		Iterator itera = cla.getCategorias().iterator();
		Categoria cat = null;
		while (itera.hasNext()) {
			cat = (Categoria) itera.next();
			if (cat.getTipoCategoria().getNombre().equals(tipoCat.getNombre())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.edu.icesi.notas.builder.Builder#guardarNuevoTipoCateg(java.lang.String
	 * , java.util.List, java.sql.Connection)
	 */
	public TipoCategoria guardarNuevoTipoCateg(String nombre,
			List tipoCategorias, Connection conexion) {

		TipoCategoria nueva = new TipoCategoria();
		nueva.setDepartamentoAcademico(dptoAcademico);
		nueva.setEstado("A");
		nueva.setNombre(nombre);
		nueva.guardarBD(conexion);
		nueva.setConsecutivo(nueva.getConsecutivoTipoCateg(conexion));
		tipoCategorias.add(nueva);
		return nueva;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see co.edu.icesi.notas.builder.Builder#estaEnLista(java.util.List,
	 * java.lang.String)
	 */
	public TipoCategoria estaEnLista(List tipoCategorias, String nombre) {
		TipoCategoria tipoCateg = null;
		Iterator iterador = tipoCategorias.iterator();
		while (iterador.hasNext()) {
			tipoCateg = (TipoCategoria) iterador.next();
			if (tipoCateg.getNombre().equalsIgnoreCase(nombre)) {
				return tipoCateg;
			}
		}
		return null;
	}

	public void construir(Curso curso) {
		// Creamos la clasificación individual.
		Clasificacion individual = new Clasificacion();
		individual.setTipo("I");
		individual.setNombre("Evaluaciones individuales");
		individual.setPorcentaje(this.porcentajeClasificacion("I"));
		individual.setCurso(curso);
		curso.setIndividuales(individual);

		// Creamos la clasificación grupal.
		Clasificacion grupal = new Clasificacion();
		grupal.setTipo("G");
		grupal.setNombre("Evaluaciones grupales");
		grupal.setPorcentaje(this.porcentajeClasificacion("G"));
		grupal.setCurso(curso);
		curso.setGrupales(grupal);
		// return curso;
	}

	/**
	 * Determina el porcentaje total, debido a la suma de porcentajes de los
	 * objetos Activity, cuyo type corresponda a la variable por parámetro.
	 * 
	 * @param tipo
	 *            Tipo de la Clasificación a la cual se le calculará el
	 *            porcentaje
	 * @return Porcentaje calculado para la Clasificación
	 */
	private double porcentajeClasificacion(String tipo) {
		Iterator iterador = cursoBasico.getActivities().iterator();
		Activity actual;
		double total = 0;
		while (iterador.hasNext()) {
			actual = (Activity) iterador.next();
			if (actual.getType().equals(tipo)) {
				total += actual.getPercentage();
			}
		}
		return total;
	}
}
