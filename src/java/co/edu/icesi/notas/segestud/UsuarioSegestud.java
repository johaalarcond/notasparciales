package co.edu.icesi.notas.segestud;

public class UsuarioSegestud {
	private String identificacion;

	private String nombreUsuario;

	private String tipo;

	public UsuarioSegestud() {
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String toString() {
		return this.identificacion + " " + this.nombreUsuario;
	}
}
