package clases;

import java.io.Serializable;

public class Liga implements Serializable {
	private static final long serialVersionUID = 1L;
	private int codL;
	private String nomL;
	
	public Liga(int codL, String nomL) {
		this.codL = codL;
		this.nomL = nomL;
	}

	public int getCodL() {
		return codL;
	}

	public void setCodL(int codL) {
		this.codL = codL;
	}

	public String getNomL() {
		return nomL;
	}

	public void setNomL(String nomL) {
		this.nomL = nomL;
	}

	@Override
	public String toString() {
		return "Liga [Codigo de liga:" + codL + ", Nombre de liga:" + nomL + "]";
	}
}
