package ontology.conceptos;


public class Prevision extends Concepto {
    // tipo = 1 -> subida
    // tipo = 2 -> bajada
    // tipo = 3 -> reparto acciones
	private int tipo;
    private int cantidad;
	
	public Prevision() {}

	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
    public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
}
