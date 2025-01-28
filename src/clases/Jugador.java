package clases;

public class Jugador extends Integrante {
	private int codJ;
	private boolean lesionado;
	private int goles;
	
	public Jugador(String nombre, String pais, int codJ, boolean lesionado, int goles) {
		super(nombre, pais);
		this.codJ = codJ;
		this.lesionado = lesionado;
		this.goles = goles;
	}

	public int getCodJ() {
		return codJ;
	}

	public void setCodJ(int codJ) {
		this.codJ = codJ;
	}

	public boolean isLesionado() {
		return lesionado;
	}

	public void setLesionado(boolean lesionado) {
		this.lesionado = lesionado;
	}

	public int getGoles() {
		return goles;
	}

	public void setGoles(int goles) {
		this.goles = goles;
	}

	@Override
	public void visualizar() {
		System.out.println(super.toString());
		System.out.println("Codigo de Jugador: "+codJ);
		System.out.println("Goles: "+goles);
		System.out.println("Esta lesionado: "+lesionado);
	}
}
