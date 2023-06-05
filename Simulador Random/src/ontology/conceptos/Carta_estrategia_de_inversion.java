package ontology.conceptos;


public class Carta_estrategia_de_inversion extends Concepto {
    private String descripcion;
	private String tipo;
	private boolean borrada;
	
	public Carta_estrategia_de_inversion() {}

    public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public boolean getBorrada() {
		return borrada;
	}
	public void setBorrada(boolean borrada) {
		this.borrada = borrada;
	}

}
