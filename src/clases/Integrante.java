package clases;

import java.io.Serializable;

public abstract class Integrante implements Serializable {
	
	private static final long serialVersionUID = 1L;
	protected String nombre;
	protected String pais;
	
	public Integrante(String nombre, String pais) {
		this.nombre = nombre;
		this.pais = pais;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	@Override
	public String toString() {
		return "Nombre: "+nombre+"\nPais: "+pais;
	}
	
	public abstract void visualizar();
}
