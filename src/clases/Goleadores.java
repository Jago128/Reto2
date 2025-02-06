package clases;

public class Goleadores implements Comparable<Goleadores> {

	private int goles;
	private String nombEquipo;
	private String nombreJugador;

	public Goleadores(int goles, String nombEquipo, String nombre) {
		this.goles = goles;
		this.nombEquipo = nombEquipo;
		this.nombreJugador = nombre;
	}

	public int getGoles() {
		return goles;
	}

	public void setGoles(int goles) {
		this.goles = goles;
	}

	public String getNombEquipo() {
		return nombEquipo;
	}

	public void setNombEquipo(String nombEquipo) {
		this.nombEquipo = nombEquipo;
	}

	public String getNombreJugador() {
		return nombreJugador;
	}

	public void setNombreJugador(String nombre) {
		this.nombreJugador = nombre;
	}

	@Override
	public String toString() {
		return "Goleadores [goles=" + goles + ", nombEquipo=" + nombEquipo + ", nombreJugador=" + nombreJugador + "]";
	}

	@Override
	public int compareTo(Goleadores o) {
		// Comparar por cantidad de goles (descendente)
		int resultado = Integer.compare(o.getGoles(), this.goles);

		if (resultado == 0) {
			resultado = this.nombreJugador.compareTo(o.getNombreJugador());
		}
		return resultado;
	}

}
