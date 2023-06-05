package ontology.conceptos;


public class Ronda extends Concepto {
	private int numero;
    private String nombre;
	
	public Ronda() {}

	public int getNumero() {
		return numero;
	}
	public void setNumero(int valor) {
		this.numero = valor;
	}
    public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
