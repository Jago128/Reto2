package clases;

public class Jugador extends Integrante {
	private static final long serialVersionUID = 1L;
	private String codJ;
	private boolean lesionado;
	private int goles;
	
	public Jugador(String nombre, String pais, boolean lesionado, int goles) {
		super(nombre, pais);
		this.codJ = this.nombre.substring(0, 3).toUpperCase() +"-"+ this.pais.substring(0, 3).toUpperCase();
		this.lesionado = lesionado;
		this.goles = goles;
	}

	public String getCodJ() {
		return codJ;
	}

	public void setCodJ(String codJ) {
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
	public String toString() {
		return "\n <JUGADOR "+super.toString()+" Codigo de jugador: " + codJ + " /" + " Lesionado: " + lesionado + " /" +  " Goles: " + goles + "]" ;
	}

	@Override
	public void visualizar() {
		System.out.println(super.toString());
		System.out.println("Codigo de Jugador: "+codJ);
		System.out.println("Goles: "+goles);
		System.out.println("Esta lesionado: "+lesionado);
	}
}