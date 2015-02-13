/*
 * Created on 17-jun-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package co.edu.icesi.notas.form; 

import java.io.Serializable;
import java.util.*;

/**
 * @author drojas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Calificacion implements Serializable {
		private String codigo;
		private ArrayList notas;
		private String nombre;
		private String estado;
		private double escala;
		
		public Calificacion(){
			notas=new ArrayList();	
		}
		
		/**
		 * @return Returns the estado.
		 */
		public String getEstado() {
			return estado;
		}
		/**
		 * @param estado The estado to set.
		 */
		public void setEstado(String estado) {
			this.estado = estado;
		}
		/**
		 * @return Returns the nombre.
		 */
		public String getNombre() {
			return nombre;
		}
		/**
		 * @param nombre The nombre to set.
		 */
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		
		/**
		 * @return Returns the codigo.
		 */
		public String getCodigo() {
			return codigo;
		}
		
		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}
		
		public ArrayList getNotas() {
			return notas;
		}
		
		public void setNotas(ArrayList notas) {
			this.notas = notas;
		}
		
		public Nota getNotaIndexada(int index){
			Nota nueva;
			while(index>=notas.size()){
				nueva = new Nota();
				nueva.setEscala(this.getEscala());
	    		notas.add(nueva);
			}
	    	return (Nota) notas.get(index);
		}
		
		public void setNotaIndexada(int index, Nota nueva){
			Nota temp;
			while(index>=notas.size()){
				temp = new Nota();
				temp.setEscala(this.getEscala());
	    		notas.add(temp);
			}
			notas.add(index,nueva);
		}
		
		public Nota getNota(int index){
			Nota nueva;
			while(index>=notas.size()){
				nueva = new Nota();
				nueva.setEscala(this.getEscala());
	    		notas.add(nueva);
			}
	    	return (Nota) notas.get(index);
		}

		public double getEscala() {
			return escala;
		}

		public void setEscala(double escala) {
			this.escala = escala;
		}
}
