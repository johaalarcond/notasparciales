/*
 * Created on 20/12/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.utilidades.xls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import co.edu.icesi.notas.Actividad;
import co.edu.icesi.notas.Alumno;
import co.edu.icesi.notas.Curso;
import co.edu.icesi.notas.Matricula;
import co.edu.icesi.notas.Profesor;
import co.edu.icesi.notas.basica.Activity;
import co.edu.icesi.notas.basica.Subject;

/**
 * @author mzapata
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class FormatoExcelBasico extends FormatoExcel {

	private Subject subject;

	public FormatoExcelBasico(Curso curso, Profesor profesor, String ruta) {
		super(curso, profesor, ruta);
	}

	public boolean crearArchivo(Connection conexion) {
		boolean creado = false;

		/*
		 * Creamos un objeto Workbook, que representa la hoja de trabajo de
		 * excel, en la cual guardaremos los datos.
		 */
		archivo = new HSSFWorkbook();
		// Creamos una hoja de cálculo
		HSSFSheet hoja = archivo.createSheet("Hoja1");
		int posFila = 1;
		short posCelda = 2;
		// HSSFCellStyle negrilla =
		// obtenerEstilo(FormatoExcelAvanzado.FUENTE_NEGRILLA_SIN_FONDO_BORDE);

		// Creamos una fila e indicamos su posición (empieza en la posición 0)
		HSSFRow fila = hoja.createRow(posFila);
		// Creamos una celda, que empezará en la segunda columna de la fila
		// creada, y cambiamos su valor.
		HSSFCell celda = fila.createCell((short) 1);
		// celda.setCellStyle(negrilla);
		celda.setCellValue(new HSSFRichTextString("Materia:"));
		celda = fila.createCell(posCelda);
		celda.setCellValue(new HSSFRichTextString(curso.getDescripcionMateria()));

		posCelda = 1;
		fila = hoja.createRow(++posFila);
		celda = fila.createCell(posCelda);
		celda.setCellValue(new HSSFRichTextString("Código:"));
		// celda.setCellStyle(negrilla);
		celda = fila.createCell(++posCelda);
		celda.setCellValue(new HSSFRichTextString(curso.getCodigoMateria()));

		posCelda = 1;
		fila = hoja.createRow(++posFila);
		celda = fila.createCell(posCelda);
		celda.setCellValue(new HSSFRichTextString("Grupo:"));
		// celda.setCellStyle(negrilla);
		celda = fila.createCell(++posCelda);
		celda.setCellValue(new HSSFRichTextString(curso.getGrupo()));

		posFila += 2;

		if (curso.getTipoConfiguracion().equals("I")) {
			// Colocamos los textos 'Evaluaciones Individuales' y 'Evaluaciones
			// Grupales'
			posCelda = 3;
			fila = hoja.createRow(++posFila);
			celda = fila.createCell(posCelda);
			celda.setCellValue(new HSSFRichTextString(curso.getIndividuales()
					.getNombre()));
			hoja.addMergedRegion(new Region(posFila, posCelda, posFila,
					(short) ((posCelda - 1) + curso.getTotalItems(true))));

			posCelda = (short) (posCelda + curso.getTotalItems(true));
			celda = fila.createCell(posCelda);
			celda.setCellValue(new HSSFRichTextString(curso.getGrupales()
					.getNombre()));
			hoja.addMergedRegion(new Region(posFila, posCelda, posFila,
					(short) ((posCelda - 1) + curso.getTotalItems(false))));
		}

		posCelda = 1;
		fila = hoja.createRow(++posFila);
		celda = fila.createCell(posCelda);
		// celda.setCellStyle(negrilla);
		celda.setCellValue(new HSSFRichTextString("Código"));
		celda = fila.createCell(++posCelda);
		// celda.setCellStyle(negrilla);
		celda.setCellValue(new HSSFRichTextString("Nombre"));

		// colocamos los nombres de las actividades individuales
		posCelda = crearFilaItems(fila, posCelda);

		// Colocamos el porcentaje de asistencia de ser el caso
		String controlAsist = subject.getCurso().getControlAsistencia();
		if (controlAsist.equals("S")) {
			celda = fila.createCell(++posCelda);
			celda.setCellValue(new HSSFRichTextString("Porcentaje Asistencia"));
		}

		celda = fila.createCell(++posCelda);
		// celda.setCellStyle(negrilla);
		celda.setCellValue(new HSSFRichTextString("Definitiva"));

		// ahora invocamos el siguiente método para imprimir los estudiantes.
		crearFilasEstudiantes(hoja, posFila, posCelda, conexion);

		// Escribimos a un archivo
		try {
			File file = new File(ruta);
			if (file.exists()) {
				file.delete();
			}
			FileOutputStream flujo = new FileOutputStream(ruta, true);
			archivo.write(flujo);
			flujo.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return creado;
	}

	private void crearFilasEstudiantes(HSSFSheet hoja, int posFila,
			short posCelda, Connection conexion) {
		Iterator itera = curso.getAlumnos().iterator(), iteraAct;
		Activity activity;
		Actividad actividad;
		Alumno alumno;
		Matricula mat;
		HSSFRow fila;
		HSSFCell celda;
		// HSSFCellStyle sinNegrilla =
		// obtenerEstilo(FormatoExcelAvanzado.FUENTE_SIN_NEGRILLA);
		// HSSFCellStyle conNegrilla =
		// obtenerEstilo(FormatoExcelAvanzado.FUENTE_NEGRILLA);
		String controlAsist = subject.getCurso().getControlAsistencia();
		while (itera.hasNext()) {
			alumno = (Alumno) itera.next();
			if (alumno.getEstado().equals("A")) {
				fila = hoja.createRow(++posFila);
				posCelda = 1;
				celda = fila.createCell(posCelda);
				celda.setCellValue(new HSSFRichTextString(alumno.getCodigo()));
				celda = fila.createCell(++posCelda);
				celda.setCellValue(new HSSFRichTextString(alumno.getNombre()
						.toUpperCase()));

				iteraAct = subject.getActivities().iterator();

				while (iteraAct.hasNext()) {
					activity = (Activity) iteraAct.next();
					actividad = curso.getActividad(activity.getConsecutive());
					mat = actividad.getMatricula(alumno.getCodigo());
					celda = fila.createCell(++posCelda);
					celda.setCellValue(mat.getCalificacion());
					// celda.setCellStyle(sinNegrilla);
					hoja.addMergedRegion(new Region(posFila, posCelda, posFila,
							(short) (++posCelda)));
				}
				if (controlAsist.equals("S")) {
					// Se coloca el porcentaje de asistencia del alumno, si es
					// el caso.
					celda = fila.createCell(++posCelda);
					celda.setCellValue(new HSSFRichTextString(""
							+ alumno.calcularAsistenciaTotal(conexion, curso)
							+ "%"));
				}

				// Se coloca la definitiva del estudiante en el curso
				celda = fila.createCell(++posCelda);
				celda.setCellValue(curso.getDefinitiva(alumno, conexion));
			}
		}
		crearUltimaFila(hoja, posFila, posCelda, conexion);
	}

	private void crearUltimaFila(HSSFSheet hoja, int posFila, short posCelda,
			Connection conexion) {
		// la ultima fila, donde se colocan los promedios por cada item
		posCelda = 1;
		HSSFRow fila = hoja.createRow(++posFila);
		// HSSFCellStyle estilo =
		// obtenerEstilo(FormatoExcelAvanzado.FUENTE_NEGRILLA);
		HSSFCell celda = fila.createCell(++posCelda);
		celda.setCellValue(new HSSFRichTextString("Promedio"));
		// celda.setCellStyle(estilo);
		Iterator iterador = subject.getActivities().iterator();
		Activity activity;
		Actividad actividad;

		while (iterador.hasNext()) {
			activity = (Activity) iterador.next();
			celda = fila.createCell(++posCelda);
			actividad = curso.getActividad(activity.getConsecutive());

			celda.setCellValue(actividad.getPromedio());
			hoja.addMergedRegion(new Region(posFila, posCelda, posFila,
					(short) (++posCelda)));
		}

		// colocamos la definitiva
		celda = fila.createCell(++posCelda);
		// celda.setCellStyle(estilo);
		celda.setCellValue(curso.getPromedio(conexion));
	}

	// Este método coloca los encabezados de las columnas!
	private short crearFilaItems(HSSFRow fila, short posCelda) {
		HSSFCell celda;

		Iterator iterador = subject.getActivities().iterator();
		Activity activity;

		while (iterador.hasNext()) {
			activity = (Activity) iterador.next();
			celda = fila.createCell(++posCelda);
			// celda.setCellStyle(izquierda);
			celda.setCellValue(new HSSFRichTextString(activity.getName()));

			celda = fila.createCell(++posCelda);
			// celda.setCellStyle(derecha);
			celda.setCellValue(new HSSFRichTextString(activity.getPercentage()
					+ "%"));
		}

		return posCelda;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

}