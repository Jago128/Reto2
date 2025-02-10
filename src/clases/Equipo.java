package clases;

import java.io.Serializable;
import java.util.ArrayList;

public class Equipo implements Serializable {
	private static final long serialVersionUID = 1L;
	private int codE;
	private String nomE;
	private ArrayList <Integrante> listIntegrante;
	private int codL;
	
	public Equipo(int codE, String nomE, int codL) {
		this.codE = codE;
		this.nomE = nomE;
		this.listIntegrante=new ArrayList<>();;
		this.codL = codL;
	}

	public int getCodE() {
		return codE;
	}

	public void setCodE(int codE) {
		this.codE = codE;
	}

	public String getNomE() {
		return nomE;
	}

	public void setNomE(String nomE) {
		this.nomE = nomE;
	}

	public ArrayList<Integrante> getListIntegrante() {
		return listIntegrante;
	}

	public void setListIntegrante(ArrayList<Integrante> listIntegrante) {
		this.listIntegrante = listIntegrante;
	}

	public int getCodL() {
		return codL;
	}

	public void setCodL(int codL) {
		this.codL = codL;
	}

	@Override
	public String toString() {
		return "\nEQUIPO [Codigo de equipo:" + codE + ", Nombre de equipo:" + nomE + ", Codigo de liga:" + codL + ", Integrantes=" + listIntegrante;
	}
}