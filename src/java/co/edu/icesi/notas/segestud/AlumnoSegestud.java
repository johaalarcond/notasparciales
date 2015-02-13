package co.edu.icesi.notas.segestud;

import java.util.*;

public class AlumnoSegestud {
	private String codigo;

	private String nombre;

	private ArrayList cursos;

	public AlumnoSegestud(String codigo) {
		this.codigo = codigo;
		this.cursos = new ArrayList();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public ArrayList getCursos() {
		return cursos;
	}

	public void setCursos(ArrayList cursos) {
		this.cursos = cursos;
	}

	public String toString() {
		return this.codigo;
	}
}
