package clases;

public class Entrenador extends Integrante {
	private static final long serialVersionUID = 1L;
	private int codEntr;
	private TipoEntr tipo;

	public Entrenador(String nombre, String pais, int codEntr, TipoEntr tipo) {
		super(nombre, pais);
		this.codEntr = codEntr;
		this.tipo = tipo;
	}

	public int getCodEntr() {
		return codEntr;
	}

	public void setCodEntr(int codEntr) {
		this.codEntr = codEntr;
	}

	public TipoEntr getTipo() {
		return tipo;
	}

	public void setTipo(TipoEntr tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "\n <ENTRENADOR ["+super.toString()+" Codigo de entrenador: " + codEntr + " /" + " Tipo de entrenador: " + tipo + "]";
	}

	@Override
	public void visualizar() {
		System.out.println(super.toString());
		System.out.println("Codigo de Entrenador: "+codEntr);
		System.out.println("Tipo de Entrenador: "+tipo);
	}
}