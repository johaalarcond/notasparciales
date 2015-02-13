/*
 * Created on 28/07/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.utilidades.xls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.ListIterator;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import co.edu.icesi.notas.Clasificacion;
import co.edu.icesi.notas.Alumno;
import co.edu.icesi.notas.Curso;
import co.edu.icesi.notas.Actividad;
import co.edu.icesi.notas.Matricula;
import co.edu.icesi.notas.Categoria;
import co.edu.icesi.notas.Profesor;

/**
 * @author mzapata
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FormatoExcelAvanzado extends FormatoExcel{
	
	public FormatoExcelAvanzado(Curso curso, Profesor profesor, String ruta){
		super(curso,profesor,ruta);
	}
	
	public boolean crearArchivo(Connection conexion){
		boolean creado=false;
		
		/*Creamos un objeto Workbook, que representa la hoja de trabajo de excel, en la
		  cual guardaremos los datos.*/
		archivo = new HSSFWorkbook();
		//Creamos una hoja de cálculo
		HSSFSheet hoja = archivo.createSheet("Hoja1");
		int posFila=1;
		short posCelda=2;
		//HSSFCellStyle negrilla = obtenerEstilo(FormatoExcelAvanzado.FUENTE_NEGRILLA_SIN_FONDO_BORDE);
		
		//Creamos una fila e indicamos su posición (empieza en la posición 0)
		HSSFRow fila = hoja.createRow(posFila);
		//Creamos una celda, que empezará en la segunda columna de la fila creada, y cambiamos su valor.
		HSSFCell celda = fila.createCell((short)1);
		//celda.setCellStyle(negrilla);
		celda.setCellValue(new HSSFRichTextString("Materia:"));
		celda=fila.createCell(posCelda);
		celda.setCellValue(new HSSFRichTextString(curso.getDescripcionMateria()));
		
		posCelda=1;
		fila = hoja.createRow(++posFila);
		celda = fila.createCell(posCelda);
		celda.setCellValue(new HSSFRichTextString("Código:"));
		//celda.setCellStyle(negrilla);
		celda=fila.createCell(++posCelda);
		celda.setCellValue(new HSSFRichTextString(curso.getCodigoMateria()));
		
		posCelda=1;
		fila = hoja.createRow(++posFila);
		celda = fila.createCell(posCelda);
		celda.setCellValue(new HSSFRichTextString("Grupo:"));
		//celda.setCellStyle(negrilla);
		celda=fila.createCell(++posCelda);
		celda.setCellValue(new HSSFRichTextString(curso.getGrupo()));
		
		posFila+=2;
		//negrilla = obtenerEstilo(FormatoExcelAvanzado.FUENTE_NEGRILLA);
		fila = hoja.createRow(posFila);
		celda = fila.createCell(++posCelda);
		celda.setCellValue(new HSSFRichTextString(curso.getIndividuales().getNombre()));
		//celda.setCellStyle(negrilla);
		//creamos una celda que ocupa varias filas y columnas.
		hoja.addMergedRegion(new Region(posFila,posCelda,posFila,(short)((posCelda-1)+curso.getTotalItems(true))));
		
		if(curso.getTotalItems(false)>0){
			posCelda=(short)(posCelda+curso.getTotalItems(true)+2);
			celda = fila.createCell(posCelda);
		//	celda.setCellStyle(negrilla);
			celda.setCellValue(new HSSFRichTextString(curso.getGrupales().getNombre()));
			hoja.addMergedRegion(new Region(posFila,posCelda,posFila,(short)((posCelda-1)+curso.getTotalItems(false))));
		}
		
		posCelda=1;
		fila = hoja.createRow(++posFila);
		celda = fila.createCell(posCelda);
		//celda.setCellStyle(negrilla);
		celda.setCellValue(new HSSFRichTextString("Código"));
		celda = fila.createCell(++posCelda);
		//celda.setCellStyle(negrilla);
		celda.setCellValue(new HSSFRichTextString("Nombre"));
		
		//colocamos los nombres de las estructuras individuales
		posCelda=crearFilaItems(true,fila,posCelda);
		
		celda = fila.createCell(++posCelda);
		//celda.setCellStyle(negrilla);
		celda.setCellValue(new HSSFRichTextString("Definitiva individual"));
		celda = fila.createCell(++posCelda);
		celda.setCellValue(new HSSFRichTextString(curso.getIndividuales().getPorcentaje()+"%"));
		
		//colocamos los nombres de las estructuras grupales
		if(curso.getTotalItems(false)>0){
			posCelda=crearFilaItems(false,fila,posCelda);
			celda = fila.createCell(++posCelda);
			//celda.setCellStyle(negrilla);
			celda.setCellValue(new HSSFRichTextString("Definitiva grupal"));
			celda = fila.createCell(++posCelda);
			celda.setCellValue(new HSSFRichTextString(curso.getGrupales().getPorcentaje()+"%"));
		}
		
		celda = fila.createCell(++posCelda);
		//celda.setCellStyle(negrilla);
		celda.setCellValue(new HSSFRichTextString("Definitiva"));
		
		//ahora invocamos el siguiente método para imprimir los estudiantes.
		crearFilasEstudiantes(hoja,posFila,posCelda,conexion);		
		
		
		//Escribimos a un archivo
		try{
			File file = new File(ruta);
			if(file.exists()){
				file.delete();
			}
			FileOutputStream flujo = new FileOutputStream(ruta,true);
			archivo.write(flujo);
			flujo.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		return creado;
	}
	
	private void crearFilasEstudiantes(HSSFSheet hoja, int posFila, short posCelda,Connection conexion){
		ListIterator itera = curso.getAlumnos().listIterator(),iteraPadre,iteraEst;
		Alumno alumno;
		Categoria pg;
		Actividad est;
		Matricula mat;
		HSSFRow fila;
		HSSFCell celda;
		//HSSFCellStyle sinNegrilla = obtenerEstilo(FormatoExcelAvanzado.FUENTE_SIN_NEGRILLA);
		//HSSFCellStyle conNegrilla = obtenerEstilo(FormatoExcelAvanzado.FUENTE_NEGRILLA);
		while(itera.hasNext()){
			alumno=(Alumno)itera.next();
			if(alumno.getEstado().equals("A")){
				fila=hoja.createRow(++posFila);
				posCelda=1;
				celda=fila.createCell(posCelda);
				celda.setCellValue(new HSSFRichTextString(alumno.getCodigo()));
				celda=fila.createCell(++posCelda);
				celda.setCellValue(new HSSFRichTextString(alumno.getNombre().toUpperCase()));
				iteraPadre=curso.getIndividuales().getCategorias().listIterator();
				//para las evaluaciones individuales
				while(iteraPadre.hasNext()){
					pg=(Categoria)iteraPadre.next();
					if(!pg.isCancelado()){
						iteraEst=pg.getActividades().listIterator();
						while(iteraEst.hasNext()){
							est=(Actividad)iteraEst.next();
							mat=est.getMatricula(alumno.getCodigo());
							celda=fila.createCell(++posCelda);
			//				celda.setCellStyle(sinNegrilla);
							celda.setCellValue(mat.getCalificacion());
							hoja.addMergedRegion(new Region(posFila,posCelda,posFila,(short)(++posCelda)));
						}
						celda=fila.createCell(++posCelda);
				//		celda.setCellStyle(conNegrilla);
						celda.setCellValue(pg.calcularNotaSinPorcentaje(alumno));
						hoja.addMergedRegion(new Region(posFila,posCelda,posFila,(short)(++posCelda)));
					}
				}
				celda=fila.createCell(++posCelda);
				celda.setCellValue(curso.getDefinitivaIndividual(alumno,conexion));
				//celda.setCellStyle(conNegrilla);
				hoja.addMergedRegion(new Region(posFila,posCelda,posFila,(short)(++posCelda)));
				//lo mismo para las evaluaciones grupales
				if(curso.getTotalItems(false)>0){
					iteraPadre=curso.getGrupales().getCategorias().listIterator();
					//para las evaluaciones individuales
					while(iteraPadre.hasNext()){
						pg=(Categoria)iteraPadre.next();
						if(!pg.isCancelado()){
							iteraEst=pg.getActividades().listIterator();
							while(iteraEst.hasNext()){
								est=(Actividad)iteraEst.next();
								mat=est.getMatricula(alumno.getCodigo());
								celda=fila.createCell(++posCelda);
					//			celda.setCellStyle(sinNegrilla);
								celda.setCellValue(mat.getCalificacion());
								hoja.addMergedRegion(new Region(posFila,posCelda,posFila,(short)(++posCelda)));
							}
							celda=fila.createCell(++posCelda);
						//	celda.setCellStyle(conNegrilla);
							celda.setCellValue(pg.calcularNotaSinPorcentaje(alumno));
							hoja.addMergedRegion(new Region(posFila,posCelda,posFila,(short)(++posCelda)));
						}
					}
					celda=fila.createCell(++posCelda);
					celda.setCellValue(curso.getDefinitivaGrupal(alumno,conexion));
					//celda.setCellStyle(conNegrilla);
					hoja.addMergedRegion(new Region(posFila,posCelda,posFila,(short)(++posCelda)));
				}
				celda=fila.createCell(++posCelda);
				celda.setCellValue(curso.getDefinitiva(alumno,conexion));
			}
		}
		crearUltimaFila(hoja,posFila,posCelda,conexion);
	}
	
	private void crearUltimaFila(HSSFSheet hoja, int posFila, short posCelda,Connection conexion){
		//la ultima fila, donde se colocan los promedios por cada item
		posCelda=1;
		HSSFRow fila = hoja.createRow(++posFila);
		//HSSFCellStyle estilo = obtenerEstilo(FormatoExcelAvanzado.FUENTE_NEGRILLA);
		HSSFCell celda = fila.createCell(++posCelda);
		celda.setCellValue(new HSSFRichTextString("Promedio"));
		//celda.setCellStyle(estilo);
		ListIterator itera = curso.getIndividuales().getCategorias().listIterator(),iteraEst;
		Categoria pg;
		Actividad est;
		while(itera.hasNext()){
			pg = (Categoria)itera.next();
			if(!pg.isCancelado()){
				iteraEst=pg.getActividades().listIterator();
				while(iteraEst.hasNext()){
					est = (Actividad)iteraEst.next();
					celda = fila.createCell(++posCelda);
					celda.setCellValue(est.getPromedio());
			//		celda.setCellStyle(estilo);
					hoja.addMergedRegion(new Region(posFila,posCelda,posFila,(short)(++posCelda)));
				}
				celda = fila.createCell(++posCelda);
				celda.setCellValue(pg.getPromedio(curso.getAlumnos()));
				//celda.setCellStyle(estilo);
				hoja.addMergedRegion(new Region(posFila,posCelda,posFila,(short)(++posCelda)));
			}
		}
		//colocamos la definitiva individual
		celda = fila.createCell(++posCelda);
		celda.setCellValue(curso.getDefinitivaIndividual(conexion));
		//celda.setCellStyle(estilo);
		hoja.addMergedRegion(new Region(posFila,posCelda,posFila,(short)(++posCelda)));
		if(curso.getTotalItems(false)>0){
			itera=curso.getGrupales().getCategorias().listIterator();
			while(itera.hasNext()){
				pg = (Categoria)itera.next();
				if(!pg.isCancelado()){
					iteraEst=pg.getActividades().listIterator();
					while(iteraEst.hasNext()){
						est = (Actividad)iteraEst.next();
						celda = fila.createCell(++posCelda);
						celda.setCellValue(est.getPromedio());
						//celda.setCellStyle(estilo);
						hoja.addMergedRegion(new Region(posFila,posCelda,posFila,(short)(++posCelda)));
					}
					celda = fila.createCell(++posCelda);
					celda.setCellValue(pg.getPromedio(curso.getAlumnos()));
				//	celda.setCellStyle(estilo);
					hoja.addMergedRegion(new Region(posFila,posCelda,posFila,(short)(++posCelda)));
				}
			}
			//colocamos la definitiva grupal
			celda = fila.createCell(++posCelda);
			celda.setCellValue(curso.getDefinitivaGrupal(conexion));
			//celda.setCellStyle(estilo);
			hoja.addMergedRegion(new Region(posFila,posCelda,posFila,(short)(++posCelda)));
		}
		
		//colocamos la definitiva
		celda = fila.createCell(++posCelda);
		//celda.setCellStyle(estilo);
		celda.setCellValue(curso.getPromedio(conexion));
	}
	
	//Este método coloca los encabezados de las columnas!
	private short crearFilaItems(boolean individuales, HSSFRow fila, short posCelda){
		Clasificacion agrupacion = individuales?curso.getIndividuales():curso.getGrupales();
		ListIterator itera = agrupacion.getCategorias().listIterator(), itEst;
		Categoria actual;
		Actividad est;
		HSSFCell celda;
		//HSSFCellStyle derecha=obtenerEstilo(FormatoExcelAvanzado.FUENTE_NEGRILLA_DERECHA);
		//HSSFCellStyle izquierda=obtenerEstilo(FormatoExcelAvanzado.FUENTE_NEGRILLA_IZQUIERDA);
		while(itera.hasNext()){
			actual=(Categoria)itera.next();
			if(!actual.isCancelado()){
				itEst=actual.getActividades().listIterator();
				while(itEst.hasNext()){
					est=(Actividad)itEst.next();
					celda=fila.createCell(++posCelda);
			//		celda.setCellStyle(izquierda);
					celda.setCellValue(new HSSFRichTextString(est.getNombre()));
					
					celda=fila.createCell(++posCelda);
				//	celda.setCellStyle(derecha);
					celda.setCellValue(new HSSFRichTextString(est.getPorcentaje()+"%"));
				}
				celda=fila.createCell(++posCelda);
		//		celda.setCellStyle(izquierda);
				celda.setCellValue(new HSSFRichTextString("Definitiva "+actual.getNombre()));
				
				celda=fila.createCell(++posCelda);
			//	celda.setCellStyle(derecha);
				celda.setCellValue(new HSSFRichTextString(actual.getPorcentaje()+"%"));
			}
		}
		return posCelda;
	}

}