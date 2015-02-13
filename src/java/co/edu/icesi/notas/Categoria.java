package co.edu.icesi.notas;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import org.apache.commons.beanutils.BeanUtils;

import co.edu.icesi.notas.control.ControlRecursos;
import co.edu.icesi.notas.utilidades.OperacionesMatematicas;

/**
 * En esta clase se describe el objeto Categoria, el cual hace parte de la
 * jerarquía que compone el esquema del curso. Cada Categoria pertenece a un
 * objeto Clasificación (Individual o grupal), y este, a su vez agrupa varias
 * actividades. De igual forma, la suma de porcentajes de las Categorías debe
 * ser 100%. Por otro lado, cada Categoría debe corresponder a un tipo
 * específico de categoría (TipoCategoria).
 * 
 * Nota: los porcentajes de la Categoria no son los que ve el usuario de la
 * aplicación
 * 
 * @author lmdiaz, mzapata
 */
public class Categoria implements Serializable {

	private int consecutivo;

	private double porcentaje;

	private TipoCategoria tipoCategoria;

	private List actividades;

	private boolean existeBd = false;

	// Copias para auditoria//
	private double copiaPorcentaje;

	private boolean actualizado = false;

	private String activo;

	private Clasificacion clasificacion;

	/**
	 * Métodos de acceso para obtener el atributo 'acceso' negado.
	 * 
	 * @return El atributo 'acceso' negado.
	 */
	public String getSeleccionado() {
		return (activo.equals("S")) ? "on" : "off";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Categoria) {
			Categoria pg = (Categoria) obj;
			return this.tipoCategoria.equals(pg.tipoCategoria);
		}
		return false;
	}

	public void setSeleccionado(String nuevo) {
		activo = nuevo.equalsIgnoreCase("on") ? "S" : "N";
	}

	/**
	 * Calcula el promedio de esta Categoria para la colección de alumnos por
	 * parámetro.
	 * 
	 * @param alumnos
	 *            Colección de alumnos
	 * @return Promedio
	 */
	public double getPromedio(ArrayList alumnos) {
		ListIterator iterator = alumnos.listIterator();
		double prom = 0;
		Alumno actual;
		int contador = 0;
		while (iterator.hasNext()) {
			actual = (Alumno) iterator.next();
			if (actual.getEstado().equals("A")) {
				prom += this.calcularNotaSinPorcentaje(actual);
				contador++;
			}
		}
		if (contador == 0)
			return 0;
		prom /= contador;
		return OperacionesMatematicas.redondear(prom, 1);
	}

	public String getNombre() {
		if (tipoCategoria != null)
			return tipoCategoria.getNombre();
		return "";
	}

	public boolean isExisteBd() {
		return existeBd;
	}

	public void setExisteBd(boolean existeBd) {
		this.existeBd = existeBd;
	}

	public void setActividades(List estructura) {
		this.actividades = estructura;
	}

	public Categoria() {
		super();
		activo = "N";
		actividades = new ArrayList();
	}

	public List getActividades() {
		return actividades;
	}

	public TipoCategoria getTipoCategoria() {
		return tipoCategoria;
	}

	public void setTipoCategoria(TipoCategoria tipoCalificacion) {
		this.tipoCategoria = tipoCalificacion;
	}

	public Categoria(int consecutivo, double porcentaje) {
		super();
		this.consecutivo = consecutivo;
		this.porcentaje = porcentaje;
		activo = "N";
		actividades = new ArrayList();
	}

	public int getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	public double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}

	public int getConsecutivoTipo() {
		return tipoCategoria.getConsecutivo();
	}

	public String getNombreTipo() {
		return tipoCategoria.getNombre();
	}

	/**
	 * Determina si el porcentaje y su copia son distintos. Es decir, si hubo un
	 * cambio en el porcentaje de la Categoría.
	 * 
	 * @return true si el porcentaje y su copia son distintos.
	 */
	public boolean huboCambioPorcentaje() {
		return copiaPorcentaje != porcentaje;
	}

	public void setConsecutivoTipo(int conse) {
		if (tipoCategoria == null)
			tipoCategoria = new TipoCategoria();
		tipoCategoria.setConsecutivo(conse);
	}

	public void setDescripcionTipo(String nuevo) {
		if (tipoCategoria == null)
			tipoCategoria = new TipoCategoria();
		tipoCategoria.setNombre(nuevo);
	}

	/**
	 * Retorna el tipo (I/G) del tipo de configuración asociado a la Categoría.
	 */
	public String getTipoTipo() {
		return this.clasificacion.getTipo();
	}

	public void setTipoTipo(String nuevo) {
		this.clasificacion.setTipo(nuevo);
	}

	/**
	 * Este método guarda en la base de datos la Categoría actual.
	 * 
	 * @param conexion
	 *            Conexión a la base de datos
	 */
	public synchronized void guardarBd(Connection conexion) {

		String sql = "insert into tntp_categorias (porcentaje, consec_tipo_categ, activo,consec_clasif_categ) "
				+ "values ("
				+ getPorcentaje()
				+ ", "
				+ tipoCategoria.getConsecutivo()
				+ ",'"
				+ activo
				+ "',"
				+ clasificacion.getConsecutivo() + ")";
		Statement stm = null;
		try {
			stm = conexion.createStatement();
			stm.executeUpdate(sql);
			consecutivo = getConsecutivoCategoria(conexion);
			existeBd = true;
			copiaPorcentaje = porcentaje;
		} catch (SQLException e) {
			System.out.println("Error: " + this.getClass().getName()
					);
			System.out.println("Descripción:");
			System.out
					.println("Error guardando la categoría " + e.getMessage());
			e.printStackTrace();
		}
		ControlRecursos.liberarRecursos(null, stm);
	}

	/**
	 * Carga de la base de datos, todas las actividades que posea el Categoria.
	 * 
	 * @param conexion
	 *            Conexión a la base de datos
	 */
	public void cargarActividad(Connection conexion) {
		String sql = "select consecutivo, nombre, porcentaje, fecha_realizacion, temas, descripcion, fecha_ingreso_nota from tntp_actividades a "
				+ "where a.CONSEC_CATEGORIA="
				+ getConsecutivo()
				+ " and a.FECHA_CANCELACION is null order by a.CONSECUTIVO";

		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = conexion.createStatement();
			rs = stm.executeQuery(sql);
			while (rs.next()) {
				int cons = rs.getInt("consecutivo");
				String nombre = rs.getString("nombre");
				double porc = rs.getDouble("porcentaje");
				String fechaR = rs.getString("fecha_realizacion");
				if (fechaR != null && fechaR.length() > 5)
					fechaR = fechaR.substring(0, 10);
				String temas = rs.getString("temas");
				String desc = rs.getString("descripcion");
                               
				Actividad es = new Actividad(cons, nombre, porc, fechaR, temas,
						desc);
				// es.setTipo(this.getTipoCategoria().getDescripcion());
                                 //ffceballos
                                Date fecha=rs.getDate("fecha_ingreso_nota");                               
                                es.setFechaIngreso(fecha);
                                 //fin ffceballos
				this.actividades.add(es);
                                
				es.setCategoria(this);

			}
		} catch (SQLException e) {
			System.out.println("Error: " + this.getClass().getName());
			System.out.println("Descripción:");
			System.out.println("Error cargando actividad " + e.getMessage());
			e.printStackTrace();
		}
		ControlRecursos.liberarRecursos(rs, stm);
	}

	/**
	 * Registra en la BD los cambios realizados al objeto en memoria.
	 * 
	 * @param conexion
	 *            Conexión a la base de datos
	 */
	public void actualizarBd(Connection conexion) {
		String sql = "update tntp_categorias set porcentaje=" + porcentaje
				+ ",activo='" + activo + "' where consecutivo=" + consecutivo;
		Statement stm = null;
		try {
			stm = conexion.createStatement();
			stm.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Error: " + this.getClass().getName()
					);
			System.out.println("Descripción:");
			System.out.println("Error actualizando la categoría "
					+ e.getMessage());
			e.printStackTrace();
		}
		ControlRecursos.liberarRecursos(null, stm);
	}

	/***************************************************************************
	 * Actualiza la clasificacion a la cual pertenece esa categoria en la base
	 * de datos. Se debe tener cuidado con este método. No se puede utilizar en
	 * cualquier parte.
	 * 
	 * @param conexion
	 *            Conexión a la base de datos
	 */
	public void actualizarClasificacionBd(Connection conexion) {
		int cons = this.clasificacion.getConsecutivo();
		String sql = "update tntp_categorias set consec_clasif_categ=" + cons
				+ " where consecutivo=" + consecutivo;
		Statement stm = null;
		try {
			stm = conexion.createStatement();
			stm.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Error: " + this.getClass().getName()
					);
			System.out.println("Descripción:");
			System.out.println("Error actualizando la clasificación "
					+ e.getMessage());
			e.printStackTrace();
		}
		ControlRecursos.liberarRecursos(null, stm);
	}

	/**
	 * Obtiene una actividad de la lista, basándose en el consecutivo que llega
	 * por parámetro.
	 * 
	 * @param cons
	 *            Consecutivo de la actividad a buscar.
	 * @return Actividad que corresponde con el consecutivo por parámetro.
	 */
	// cons=Consecutivo de la actividades
	public Actividad getActividad(int cons) {
		for (int i = 0; i < actividades.size(); i++) {
			Actividad est = (Actividad) actividades.get(i);
			if (est.getConsecutivo() == cons)
				return est;
		}
		return null;
	}

	 /**
         * Obtiene desde la base de datos el tipo de categoria que se realizara
         * la eliminacion de la nota mas baja.
         * jalarcon--- N2 para proyecto de notas parciales
         * @param conexion
         *            Conexión a la base de datos
         * @return Consecutivo asociado a la categoría actual.
         */
         public int getTipoCategoriaPruebaCorta(Connection conexion) {
		int cons = 0;
		String sql = "select fprebus_constantes('217','03','') from dual";
                
		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = conexion.createStatement();
			rs = stm.executeQuery(sql);
			if (rs.next())
				cons = rs.getInt(1);
		} catch (SQLException e) {
			System.out.println("Error: " + this.getClass().getName()
					);
			System.out.println("Descripción:");
			System.out.println("Error cargando el tipo de la categoría "
					+ e.getMessage());
			e.printStackTrace();
		}
		ControlRecursos.liberarRecursos(rs, stm);
		return cons;
	}
	
         
         /**
         * Obtiene desde la base de datos el tipo de categoria que se realizara
         * la eliminacion de la nota mas baja.
         * jalarcon--- N2 para proyecto de notas parciales
         * @param conexion
         *            Conexión a la base de datos
         * @return Consecutivo asociado a la categoría actual.
         */
         public int getConteoPruebasCortas(Connection conexion) {
		int cons = 0;
		String sql = "select fprebus_constantes('218','03','') from dual";
                
		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = conexion.createStatement();
			rs = stm.executeQuery(sql);
			if (rs.next())
				cons = rs.getInt(1);
		} catch (SQLException e) {
			System.out.println("Error: " + this.getClass().getName()
					);
			System.out.println("Descripción:");
			System.out.println("Error cargando el numero de pruebas cortas "
					+ e.getMessage());
			e.printStackTrace();
		}
		ControlRecursos.liberarRecursos(rs, stm);
		return cons;
	}
	
         
         /**
	 * Devuelve S o N dependiendo si la categoría pertenece a un curso al que aplica la regla del quiz mas bajo o no
	 * 
	 * @param  conexion
         * ffceballos - N2
	 * Conexión para realizar la consulta
	 * @return S ó N.
	 */
         
         public String getReglaQuiz(Connection conexion){
         
             String regla = "N";
             
		String sql = "select CU.REGLA_QUIZ " +
                                " from tntp_cursos cu, tntp_clasif_categ cla, tntp_categorias cat" +
                                " where CAT.CONSECUTIVO=" +this.consecutivo+
                                " and CLA.CONSECUTIVO=CAT.CONSEC_CLASIF_CATEG" +
                                " and CLA.CONSEC_CURSO=CU.CONSECUTIVO";
                
		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = conexion.createStatement();
			rs = stm.executeQuery(sql);
			if (rs.next())
				regla = rs.getString(1);
		} catch (SQLException e) {
			System.out.println("Error: " + this.getClass().getName()
					);
			System.out.println("Descripción:");
			System.out.println("Error cargando el numero de pruebas cortas "
					+ e.getMessage());
			e.printStackTrace();
		}
		ControlRecursos.liberarRecursos(rs, stm);
		return regla;
             
            
         }
         
         
         

	/**
	 * Devuelve la nota definitiva de un estudiante para esta Categoría.
	 * 
	 * @param alumno, conexion
         * jalarcon - N2
	 * Alumno al cual se le calculará la definitiva
	 * @return Definitiva del respectivo estudiante para la Categoría.
	 */
	public double calcularNota(Alumno alumno,Connection conexion) {
		double nota = 0;
		Actividad est;
		Matricula mat;
                
              //  ArrayList auxiliar=new ArrayList();
                        
                double menor=5;
                int codigoMenor=0;  
                int porcentajeTotal=0;
                
                
		if (this.actividades.size() == 0) {
			this.porcentaje = 0;
			return 0;
		}
		for (int i = 0; i < actividades.size(); i++) {                  
                 
                    	est = (Actividad) actividades.get(i);
			mat = est.getMatricula(alumno.getCodigo());
			nota += mat.getCalificacion() * est.getPorcentaje() / 100;
                         if(alumno.getCodigo().equals("14120026")){
                   System.out.println("-----Actividad-------"+mat.getCalificacion()+" * "+(est.getPorcentaje() / 100));
               }
                   /* ffceballos -- jalarcon */                    
                    if(mat.getCalificacion()<menor){ 
                        menor=mat.getCalificacion();
                        codigoMenor=est.getConsecutivo();                       
                    } 
                     porcentajeTotal+=est.getPorcentaje();
                    /* fin ffceballos  -- jalarcon */
                        
                        
		}
                
                 /* ffceballos- jalarcon para la definitiva y eliminar la nota mas baja de Pruebas cortas*/   
                int categoriaQuices=this.getTipoCategoriaPruebaCorta(conexion); // aqui consultar el numero de la categoria en la constante 278
                int cantpruebascortas =this.getConteoPruebasCortas(conexion);
                double porcentajeDistribuido=0;
                int consecutivoTipoCategoria=((TipoCategoria)this.getTipoCategoria()).getConsecutivo();
                
                //ffceballos 30/01/2014
                String reglaQuiz=this.getReglaQuiz(conexion);
                            
              
                //ffceballos 08/04/2014 - caso 
                if(reglaQuiz==null){ reglaQuiz="";}
                
                if(reglaQuiz.equalsIgnoreCase("S")){
                
                if(consecutivoTipoCategoria==categoriaQuices){
                
                    /*
                    if(actividades.size()>=cantpruebascortas){
                        porcentajeDistribuido=porcentajeTotal/(actividades.size()-1);
                    System.out.println("++++++PorcentajeDistribuido+++++++"+porcentajeDistribuido);
                    nota=0;
                    for (int i = 0; i < actividades.size(); i++) {                  
                                        
                    	est = (Actividad) actividades.get(i);
                        
                       if(est.getConsecutivo()!=codigoMenor){
			mat = est.getMatricula(alumno.getCodigo());
			nota += mat.getCalificacion() * porcentajeDistribuido / 100;
                        }
                    }
                }
                    */
                    
                    if(actividades.size()>=cantpruebascortas){
                       // porcentajeDistribuido=porcentajeTotal/(actividades.size()-1);
                  
                    nota=0;
                    for (int i = 0; i < actividades.size(); i++) {                  
                                        
                    	est = (Actividad) actividades.get(i);
                        
                       if(est.getConsecutivo()!=codigoMenor){
			mat = est.getMatricula(alumno.getCodigo());
			nota += mat.getCalificacion();// * porcentajeDistribuido / 100;
                        }
                    }
                    
                    nota=nota/(actividades.size()-1);
                }
                    
                }
                     }
                /* fin ffceballos- jalarcon */
                
		nota *= (porcentaje / 100.0);
               if(alumno.getCodigo().equals("14120026")){
                   System.out.println("------Categoría------"+nota);
               }
		return nota;
	}

	/***************************************************************************
	 * La única diferencia de este método con el de calcularNota es que en este
	 * se retorna la nota de la Categoria sin tomar en cuenta el porcentaje de
	 * la misma.
	 * 
	 * @param alumno
	 *            Alumno al cual se le calculará la definitiva
	 * @return Definitiva del respectivo estudiante para la Categoría.
	 */
	public double calcularNotaSinPorcentaje(Alumno alumno) {
		double nota = 0;
		Actividad est;
		Matricula mat;
		for (int i = 0; i < actividades.size(); i++) {
			est = (Actividad) actividades.get(i);
			mat = est.getMatricula(alumno.getCodigo());
			nota += mat.getCalificacion() * est.getPorcentaje() / 100;
		}
		return nota;
	}

	/**
	 * Retorna la suma de porcentajes de cada una de las actividades
	 * pertenecientes a la Categoria.
	 * 
	 * @return true la suma de los porcentajes es 100%.
	 */
	public boolean sumaPorcentajes() {
		double suma = 0;
		for (int i = 0; i < actividades.size(); i++) {
			Actividad est = (Actividad) actividades.get(i);
			suma += est.getPorcentaje();
		}
		boolean resp = Math.round(suma) != 100 && porcentaje > 0;
		return resp;
	}

	/**
	 * Indica si la suma de porcentajes de las actividades, en caso de que haya
	 * más de una actividad, de la Categoria es igual a 100%.
	 * 
	 * @return true si la suma de porcentajes de las actividades es igual a
	 *         100%.
	 */
	public boolean sumaPorcentajes2() {
		double suma = 0;
		for (int i = 0; i < actividades.size(); i++) {
			Actividad est = (Actividad) actividades.get(i);
			suma += est.getPorcentaje();
		}
		return actividades.size() > 0 ? (Math.round(suma) == 100) : true;
	}

	/**
	 * Este método verifica si al agregar un porcentaje más a la suma de los
	 * porcentajes de las actividades esta suma sigue siendo menor o igual a
	 * 100%.
	 * 
	 * @param porc
	 *            Porcentaje que se desea sumar a la suma de los porcentajes de
	 *            las actividades.
	 * @return true si la suma de los porcentajes de las actividades más el
	 *         porcentaje por parámetro es menor o igual a 100%.
	 */
	public boolean sumaPorcentajes(double porc) {
		double suma = 0;
		for (int i = 0; i < actividades.size(); i++) {
			Actividad est = (Actividad) actividades.get(i);
			suma += est.getPorcentaje();
		}
		return Math.round(suma + porc) <= 100;
	}

	/**
	 * Verifica la suma de los porcentajes de las actividades, sin tener en
	 * cuenta la actividad real que corresponde a la actividad ficticia que se
	 * pasa por parámetro, y sumando el porcentaje de la ficticia.
	 * 
	 * @param est
	 *            Actividad ficticia (es decir, no existe dentro de la categoría
	 *            actual)
	 * @return true si la suma de los porcentajes es menor o igual a 100%.
	 */
	public boolean sumaPorcentajes(Actividad est) {
		double suma = 0;
		for (int i = 0; i < actividades.size(); i++) {
			Actividad est2 = (Actividad) actividades.get(i);
			if (!est.equals(est2))
				suma += est2.getPorcentaje();
		}
		return Math.round(suma + est.getPorcentaje()) <= 100;
	}

	/**
	 * Elimina la categoría de la BD.
	 * 
	 * @param conexion
	 *            Conexión a la base de datos
	 * @return Valor booleano indicando si fue posible o no eliminar la
	 *         categoría.
	 */
	public boolean eliminarBd(Connection conexion) {
		boolean respuesta = false;
		String sql = "delete from tntp_categorias where consecutivo="
				+ consecutivo;
		Statement stm = null;
		try {
			stm = conexion.createStatement();
			stm.executeUpdate(sql);
			respuesta = true;
		} catch (SQLException e) {
			System.out.println("Error: " + this.getClass().getName()
					);
			System.out.println("Descripción:");
			System.out.println("Error eliminando categoría " + e.getMessage());
			e.printStackTrace();
		}
		ControlRecursos.liberarRecursos(null, stm);
		return respuesta;
	}

	/**
	 * Obtiene desde la base de datos el consecutivo que caracteriza a la
	 * Categoria
	 * 
	 * @param conexion
	 *            Conexión a la base de datos
	 * @return Consecutivo asociado a la categoría actual.
	 */
	public int getConsecutivoCategoria(Connection conexion) {
		int cons = 0;
		String sql = "select consecutivo from tntp_categorias";
		sql += " where consec_clasif_categ="
				+ this.clasificacion.getConsecutivo();
		sql += " and consec_tipo_categ=" + this.tipoCategoria.getConsecutivo();

		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = conexion.createStatement();
			rs = stm.executeQuery(sql);
			if (rs.next())
				cons = rs.getInt(1);
		} catch (SQLException e) {
			System.out.println("Error: " + this.getClass().getName()
					);
			System.out.println("Descripción:");
			System.out.println("Error cargando el consecutivo de la categoría "
					+ e.getMessage());
			e.printStackTrace();
		}
		ControlRecursos.liberarRecursos(rs, stm);
		return cons;
	}

	/**
	 * Actualiza las actividades pertenecientes a la categoría actual.
	 * 
	 * @param conexion
	 *            Conexión a la base de datos
	 * @param curso
	 *            Curso al que pertenece la categoría
	 */
	public void actualizarActividades(Connection conexion, Curso curso) {
		for (int i = 0; i < actividades.size(); i++) {
			Actividad est = (Actividad) actividades.get(i);
			est.actualizarBd(conexion);
			est.registrarCambios(conexion, curso);
		}
	}

	/**
	 * Registra los cambios de la auditoría en la base datos, colocando la
	 * acción respectiva dependiendo del elemento que haya cambiado.
	 * 
	 * @param conexion
	 *            Conexión a la base de datos
	 * @param curso
	 *            Curso al que pertenece la categoría
	 */
	public void registrarCambios(Connection conexion, Curso curso) {
		if (this.porcentaje != copiaPorcentaje) {
			actualizarCambioBd(conexion, curso, "" + copiaPorcentaje, ""
					+ this.porcentaje, "El porcentaje de la categoría "
					+ consecutivo + " fue modificado");
		}
		// ...Recorrer las estructuras y compararlas
	}

	/**
	 * Realiza el proceso de registrar auditorías en la base de datos, de
	 * acuerdo a las modificaciones realizadas.
	 * 
	 * @param conexion
	 *            Conexión a la base de datos
	 * @param curso
	 *            Curso al que pertenece el estudiante
	 * @param datoViejo
	 *            Dato anterior que tenía alguno de los atributos del objeto.
	 * @param datoNuevo
	 *            Dato actual que tiene alguno de los atributos de la objetos.
	 * @param accion
	 *            Texto descriptivo explicando el cambio realizado al objeto.
	 */
	public void actualizarCambioBd(Connection conexion, Curso curso,
			String datoViejo, String datoNuevo, String accion) {
		Statement stm = null;
		try {
			String sql = "";
			sql = "insert into tntp_auditorias (fecha_hora,dato_anterior,dato_nuevo,accion,consec_curso, consecutivo)";
			sql += "values(sysdate,'"
					+ datoViejo
					+ "','"
					+ datoNuevo
					+ "','"
					+ accion
					+ "',"
					+ curso.getConsecutivo()
					+ ", (select (nvl(max(consecutivo), 0) + 1) from tntp_auditorias))";
			stm = conexion.createStatement();
			stm.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Error: " + this.getClass().getName()
					);
			System.out.println("Descripción:");
			System.out.println("Error auditando cambios " + e.getMessage());
			e.printStackTrace();
		}
		ControlRecursos.liberarRecursos(null, stm);
	}

	/**
	 * Similar al método clone de la interfaz java.lang.Cloneable.
	 * 
	 * @return Una copia del objeto Categoria actual.
	 */
	public Categoria clonar() {
		try {
			Categoria clon = new Categoria();
			clon.actualizado = this.actualizado;
			clon.consecutivo = this.consecutivo;
			clon.copiaPorcentaje = this.copiaPorcentaje;
			clon.existeBd = this.existeBd;
			clon.porcentaje = this.porcentaje;
			clon.tipoCategoria = this.tipoCategoria;
			clon.activo = this.activo;

			clon.actividades = new ArrayList();
			for (int i = 0; i < actividades.size(); i++) {
				Actividad temporal = (Actividad) actividades.get(i);
				Actividad nueva = (Actividad) BeanUtils.cloneBean(temporal);
				clon.actividades.add(nueva);
			}
			return clon;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Registra en la tabla de auditorías la inserción de la categoría actual.
	 * 
	 * @param conexion
	 *            Conexión a la base de datos
	 * @param curso
	 *            Curso al que pertenece la categoría
	 */
	public void registrarInsercion(Connection conexion, Curso curso) {
		actualizarCambioBd(conexion, curso, "", "",
				"Inserción de una nueva categoría de evaluación con consecutivo "
						+ consecutivo);
	}

	/**
	 * Registra en la tabla de auditorías la eliminación de la categoría actual.
	 * 
	 * @param conexion
	 *            Conexión a la base de datos
	 * @param curso
	 *            Curso al que pertenece la categoría
	 */
	public void registrarEliminacion(Connection conexion, Curso curso) {
		actualizarCambioBd(conexion, curso, "", "",
				"Eliminación de una categoría de evaluación con consecutivo "
						+ consecutivo);
	}

	/**
	 * Actualiza el atributo copiaPorcentaje, con el valor del atributo
	 * porcentaje.
	 */
	public void crearCopia() {
		copiaPorcentaje = porcentaje;
	}

	public boolean isActualizado() {
		return actualizado;
	}

	public void setActualizado(boolean actualizado) {
		this.actualizado = actualizado;
	}

	public boolean isCancelado() {
		return activo.equals("S") ? false : true;
	}

	public void setCancelado(boolean cancelado) {
		this.activo = cancelado ? "N" : "S";
	}

	public double getCopiaPorcentaje() {
		return copiaPorcentaje;
	}

	public void setCopiaPorcentaje(double copiaPorcentaje) {
		this.copiaPorcentaje = copiaPorcentaje;
	}

	/**
	 * Similar al método cargarActividad, con la diferencia de que este se
	 * emplea en el módulo de estudiantes.
	 * 
	 * @param conexion
	 *            Conexión a la base de datos
	 */
	public void cargarActividadEstudiante(Connection conexion) {

		String sql = "select consecutivo, nombre, porcentaje, fecha_realizacion, temas, descripcion from tntp_actividades a "
				+ "where a.CONSEC_CATEGORIA="
				+ getConsecutivo()
				+ " and a.FECHA_CANCELACION is null order by a.CONSECUTIVO";

		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = conexion.createStatement();
			rs = stm.executeQuery(sql);
			while (rs.next()) {
				int cons = rs.getInt("consecutivo");
				String nombre = rs.getString("nombre");
				double porc = rs.getDouble("porcentaje");
				String fechaR = rs.getString("fecha_realizacion");
				if (fechaR != null && fechaR.length() > 5)
					fechaR = fechaR.substring(0, 10);
				String temas = rs.getString("temas");
				String desc = rs.getString("descripcion");
				Actividad es = new Actividad(cons, nombre, porc, fechaR, temas,
						desc);
				// es.setTipo(this.getTipoCategoria().getDescripcion());
				this.actividades.add(es);

			}
		} catch (SQLException e) {
			System.out.println("Error: " + this.getClass().getName()
					);
			System.out.println("Descripción:");
			System.out.println("Error cargando las actividades "
					+ e.getMessage());
			e.printStackTrace();
		}
		ControlRecursos.liberarRecursos(rs, stm);
	}

	public Clasificacion getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(Clasificacion clasificacion) {
		this.clasificacion = clasificacion;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}
}