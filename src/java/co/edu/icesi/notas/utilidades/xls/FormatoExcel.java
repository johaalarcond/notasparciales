package co.edu.icesi.notas.utilidades.xls;

import java.sql.Connection;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import co.edu.icesi.notas.Curso;
import co.edu.icesi.notas.Profesor;

/**
 * @author mzapata
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class FormatoExcel {
	
	protected Curso curso;
	protected Profesor profesor;
	protected String ruta;
	protected HSSFWorkbook archivo;
	
	private static final int FUENTE_NEGRILLA_IZQUIERDA=0;
	private static final int FUENTE_SIN_NEGRILLA=1;
	private static final int FUENTE_NEGRILLA_DERECHA=2;
	private static final int FUENTE_NEGRILLA=3;
	private static final int FUENTE_NEGRILLA_SIN_FONDO_BORDE=4;
	
	public FormatoExcel(Curso curso, Profesor profesor, String ruta){
		super();
		this.curso=curso;
		this.profesor=profesor;
		this.ruta=ruta;
	}
	
	public abstract boolean crearArchivo(Connection conexion);
	
	protected HSSFCellStyle obtenerEstilo( int constante){
		HSSFCellStyle estilo = archivo.createCellStyle();
		//colocamos bordes a la celda
		estilo.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		estilo.setBottomBorderColor(HSSFColor.BLACK.index);
		estilo.setBorderTop(HSSFCellStyle.BORDER_THIN);
		estilo.setTopBorderColor(HSSFColor.BLACK.index);
		

		HSSFFont fuente;
		switch(constante){
			case FUENTE_NEGRILLA_IZQUIERDA:
				fuente = archivo.createFont();
				fuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				estilo.setFont(fuente);
				
			/**	estilo.setBorderRight(HSSFCellStyle.BORDER_THIN);
				estilo.setRightBorderColor(HSSFColor.BLACK.index);***/
				
				estilo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				estilo.setLeftBorderColor(HSSFColor.BLACK.index);
				
				//fondo de la celda: gris
				
				estilo.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
				
			break;
			
			case FUENTE_SIN_NEGRILLA:
				estilo.setBorderRight(HSSFCellStyle.BORDER_THIN);
				estilo.setRightBorderColor(HSSFColor.BLACK.index);
				
				estilo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				estilo.setLeftBorderColor(HSSFColor.BLACK.index);
			break;
			
			case FUENTE_NEGRILLA_DERECHA:
				fuente = archivo.createFont();
				fuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				estilo.setFont(fuente);
				
				estilo.setBorderRight(HSSFCellStyle.BORDER_THIN);
				estilo.setRightBorderColor(HSSFColor.BLACK.index);
				
				//fondo de la celda: gris
				
				estilo.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
			break;
			
			case FUENTE_NEGRILLA:
				fuente = archivo.createFont();
				fuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				estilo.setFont(fuente);
				
				estilo.setBorderRight(HSSFCellStyle.BORDER_THIN);
				estilo.setRightBorderColor(HSSFColor.BLACK.index);
				estilo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				estilo.setLeftBorderColor(HSSFColor.BLACK.index);
				
				estilo.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
			break;
			
			case FUENTE_NEGRILLA_SIN_FONDO_BORDE:
				fuente = archivo.createFont();
				fuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				estilo.setFont(fuente);
			break;
					
		}
		return estilo;
	}

}
