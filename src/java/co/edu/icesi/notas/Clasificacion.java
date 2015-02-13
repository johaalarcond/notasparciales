package co.edu.icesi.notas;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

import co.edu.icesi.notas.control.ControlRecursos;

/**
 * Esta clase describe al objeto Clasificacion. Este objeto tiene la jerarquía
 * más alta dentro de la organización de porcentajes en el esquema de un curso;
 * por lo tanto tiene la posibilidad de agrupar varios Categorias (el siguiente
 * nivel en la jerarquía). Los tipos de Clasificacion pueden ser individuales o
 * grupales, cuya suma debe ser 100% dentro de un esquema de un curso, por
 * ejemplo: Individuales --------------> 80% (Clasificacion) [Parciales ------>
 * 50% (Categoria), Quices ------> 50% (Categoria)]. Grupales -------------->
 * 20% (Clasificacion) [Proyecto grupal-->60% (Categoria), Quices en sala -->40%
 * (Categoria)].
 * 
 * @author mzapata, lmdiaz
 */
public class Clasificacion implements Serializable {

	private String nombre;

	private List categorias;

	private double porcentaje;

	private int consecutivo;

	private boolean existeBD;

	private double copiaPorcentaje;

	private Curso curso;

	private String tipo;

	private String copiaTipo;

	/**
	 * Retorna la categoría que se encuentra asociada con el tipo de categoría
	 * especificado.
	 * 
	 * @param tipo
	 *            Objeto TipoCategoria cuya Categoria se quiere encontrar
	 * @return Categoria asociada al objeto TipoCategoria por parámetro; o null
	 *         si no se encuentra ninguno asociado.
	 */
	public Categoria getCategoria(TipoCategoria tipo) {
		Iterator itera = categorias.iterator();
		Categoria actual;
		while (itera.hasNext()) {
			actual = (Categoria) itera.next();
			if (actual.getTipoCategoria().getNombre().equalsIgnoreCase(
					tipo.getNombre())) {
				return actual;
			}
		}
		return null;
	}

	/**
	 * Este método retorna cuantas Categorias activas tiene la Clasificacion.
	 * 
	 * @return Número de categorias activas para esta Clasificacion.
	 */
	public int calcularCategoriasActivas() {
		int suma = 0;
		for (int i = 0; i < categorias.size(); i++) {
			Categoria pg = (Categoria) categorias.get(i);
			if (!pg.isCancelado()) {
				suma++;
			}
		}
		return suma;
	}

	public boolean isExisteBD() {
		return existeBD;
	}

	public void setExisteBD(boolean existeBD) {
		this.existeBD = existeBD;
	}

	public int getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Clasificacion() {
		super();
		categorias = new ArrayList();
		copiaPorcentaje = -1;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List getCategorias() {
		return categorias;
	}

	public void setCategorias(ArrayList padresGrupo) {
		this.categorias = padresGrupo;
	}

	public double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}

	/**
	 * Este método permite determinar si la suma de las categorias es diferente
	 * de 100%.
	 * 
	 * @return true si la suma de las categorías es diferente de 100%.
	 */
	public boolean diferenteCienSumaCategorias() {
		double suma = 0;
		ListIterator itera = categorias.listIterator();
		Categoria pg;
		while (itera.hasNext()) {
			pg = (Categoria) itera.next();
			suma += pg.getPorcentaje();
		}
		return Math.round(suma) != 100;
	}

	/**
	 * Este método actualiza los valores en los porcentajes de las
	 * clasificaciones en la base de datos.
	 * 
	 * @param conexion
	 *            Conexión a la base de datos.
	 */
	public void actualizarBD(Connection conexion) {
		String sql = "update tntp_clasif_categ ";
		sql += "set porcentaje=" + this.porcentaje + ",";
		sql += "tipo='" + this.tipo + "'";
		sql += " where consecutivo=" + this.consecutivo;
		Statement stm = null;
		try {
			stm = conexion.createStatement();
			stm.executeUpdate(sql);
			ControlRecursos.liberarRecursos(null, stm);
			// a continuación registramos el cambio en la tabla de auditorías!!!
			String accion = "";
			if (this.huboCambioPorcentaje()) {
				accion = "Modificación del porcentaje de la clasificación "
						+ this.consecutivo;
				sql = "insert into tntp_auditorias(fecha_hora,dato_anterior,dato_nuevo,accion,consec_curso, consecutivo)";
				sql += "values(sysdate,'"
						+ copiaPorcentaje
						+ "','"
						+ porcentaje
						+ "','"
						+ accion
						+ "',"
						+ this.getCurso().getConsecutivo()
						+ ", (select (nvl(max(consecutivo), 0) + 1) from tntp_auditorias))";
				stm = conexion.createStatement();
				stm.executeUpdate(sql);
				ControlRecursos.liberarRecursos(null, stm);
				copiaPorcentaje = porcentaje;
			}

			if (this.huboCambioTipo() && copiaTipo != null) {
				accion = "Modificación del tipo(Individual/Grupal) de la clasificación "
						+ this.consecutivo;
				sql = "insert into tntp_auditorias(fecha_hora,dato_anterior,dato_nuevo,accion,consec_curso, (select (nvl(max(consecutivo), 0) + 1) from tntp_auditorias))";
				sql += "values(sysdate,'"
						+ copiaTipo
						+ "','"
						+ tipo
						+ "','"
						+ accion
						+ "',"
						+ this.getCurso().getConsecutivo()
						+ ", (select (nvl(max(consecutivo), 0) + 1) from tntp_auditorias))";
				stm = conexion.createStatement();
				stm.executeUpdate(sql);
				ControlRecursos.liberarRecursos(null, stm);
				copiaTipo = tipo;
			}

		} catch (SQLException e) {
			System.out.println("Error: " + this.getClass().getName()
					);
			System.out.println("Descripción:");
			System.out
					.println("Error actualizando porcentaje de clasificación "
							+ e.getMessage());
			e.printStackTrace();
		}
		ControlRecursos.liberarRecursos(null, stm);
	}

	/**
	 * Este método guarda en la base de datos la Clasificación actual.
	 * 
	 * @param conexion
	 *            Conexión a la base de datos.
	 */
	public void guardarBD(Connection conexion) {

		String sql = "insert into tntp_clasif_categ (porcentaje,tipo,consec_curso)";
		sql += "values(" + this.porcentaje + ",'" + this.tipo + "',"
				+ this.curso.getConsecutivo() + ")";

		Statement stm = null;
		try {
			stm = conexion.createStatement();
			stm.executeUpdate(sql);
			consecutivo = getConsecutivoClasificacion(conexion);
			copiaPorcentaje = porcentaje;
			existeBD = true;
		} catch (Exception e) {
			System.out.println("Error: " + this.getClass().getName()
					);
			System.out.println("Descripción:");
			System.out.println("Error guardando la clasificación "
					+ e.getMessage());
			e.printStackTrace();
		}
		ControlRecursos.liberarRecursos(null, stm);
	}

	/**
	 * Obtiene el consecutivo de la Clasificacion actual desde la base de datos.
	 * 
	 * @param conexion
	 *            Conexión a la Base de Datos.
	 * @return Consecutivo asociado a la Clasificacion actual.
	 */
	public int getConsecutivoClasificacion(Connection conexion) {
		int cons = 0;
		String sql = "select consecutivo from tntp_clasif_categ ";
		sql += "where tipo='" + this.tipo + "'";
		sql += " and consec_curso=" + this.curso.getConsecutivo();

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
			System.out
					.println("Error cargando consecutivo de la clasificación "
							+ e.getMessage());
			e.printStackTrace();
		}
		ControlRecursos.liberarRecursos(rs, stm);
		return cons;
	}

	/**
	 * Determina si el porcentaje y su copia son distintos. Es decir, si hubo un
	 * cambio en el porcentaje de la Clasificación.
	 * 
	 * @return true si el porcentaje y su copia son distintos.
	 */
	public boolean huboCambioPorcentaje() {
		return porcentaje != copiaPorcentaje;
	}

	/**
	 * Determina si el tipo(I/G) de la Clasificación y su copia son distintos.
	 * 
	 * @return true si el porcentaje y su copia son distintos.
	 */
	public boolean huboCambioTipo() {
		return !tipo.equals(copiaTipo);
	}

	public double getCopiaPorcentaje() {
		return copiaPorcentaje;
	}

	public void setCopiaPorcentaje(double copiaPorcentaje) {
		this.copiaPorcentaje = copiaPorcentaje;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCopiaTipo() {
		return copiaTipo;
	}

	public void setCopiaTipo(String copiaTipo) {
		this.copiaTipo = copiaTipo;
	}

	public int getNumActividades() {
		int suma = 0;
		for (int i = 0; i < categorias.size(); i++) {
			Categoria cat = (Categoria) categorias.get(i);
			if (!cat.isCancelado()) {
				suma += cat.getActividades().size();
			}
		}
		return suma;
	}
}
