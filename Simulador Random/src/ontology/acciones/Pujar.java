package ontology.acciones;

import ontology.conceptos.Pila_subasta;
import ontology.conceptos.Dinero;

public class Pujar extends Accion{
	private Pila_subasta pila;
    private Dinero dinero;
	
	public Pujar() {}

	public Pila_subasta getPila() {
		return pila;
	}
	public void setPila(Pila_subasta pila) {
		this.pila = pila;
	}
    public Dinero getDinero() {
		return dinero;
	}
	public void setDinero(Dinero dinero) {
		this.dinero = dinero;
	}
}