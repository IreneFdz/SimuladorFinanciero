package ontology.conceptos;


public class Pila_subasta extends Concepto {
	private int numero;
	private int precio_subasta;
	
	public Pila_subasta() {	}

	public int getNumero() {
		return numero;
	}
	public void setNumero(int valor) {
		this.numero = valor;
	}
	public int getPrecio() {
		return precio_subasta;
	}
	public void setPrecio(int precio_subasta) {
		this.precio_subasta = precio_subasta;
	}
}
