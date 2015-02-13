package co.edu.icesi.notas.jefes;

import java.util.*;

public class DeptoJefes {
	private String codigo;

	private String nombre;

	private ArrayList cursos;

	public DeptoJefes() {
		this.cursos = new ArrayList();
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public ArrayList getCursos() {
		return cursos;
	}

	public String toString() {
		return this.codigo;
	}
}
