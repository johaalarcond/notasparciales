/*
 * Created on 23-feb-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.form; 

import java.io.Serializable;


public class Nota implements Serializable{
	
	private String nota;
	private String copiaNota;
	private String nombreEstructura;
	private int consecutivo;
	private double escala;
	
	
	public double getNotaDouble(){ 
		try{  
			double notaDouble=Double.parseDouble(nota);
			return notaDouble;
		}catch(NumberFormatException nfe){
			try{
				int notaDoble = Integer.parseInt(nota);
				return notaDoble;
			}catch(NumberFormatException nfe2){
				return -1;
			}
			//En caso de que la nota se haya digitado como un entero, arrojará una excepción
		}
	}
	
	/*Indica si existe un error en el formato del atributo Nota*/
	public boolean isError(){
		if(nota.equals("-"))
			return false;
		double notaDoble=getNotaDouble();
		if(notaDoble==-1){
			return true;
		}
		if(notaDoble<0.0 || notaDoble>this.getEscala()){
			return true;
		}
		return false;
		
	}
	
	public String getNombreEstructura() {
		return nombreEstructura;
	}
	
	public void setNombreEstructura(String nombreEstructura) {
		this.nombreEstructura = nombreEstructura;
	}
	
	public String getNota() {
		return nota;
	}
	
	public void setNota(String nota) {
		this.nota = nota;
	}
	
	public Nota(){
		super();
	}
	
	public int getConsecutivo() {
		return consecutivo;
	}
	
	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}
	
	public String getCopiaNota() {
		return copiaNota;
	}
	public void setCopiaNota(String copiaNota) {
		this.copiaNota = copiaNota;
	}
	
	public double getCopiaNotaDoble(){
		try{
			double notaCopi=Double.parseDouble(copiaNota);
			return notaCopi;
		}catch(NumberFormatException nfe){
			nfe.printStackTrace();
			return -1;
		}
	}

	public double getEscala() {
		return escala;
	}

	public void setEscala(double escala) {
		this.escala = escala;
	}
}
