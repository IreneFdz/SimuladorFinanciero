package ontology.acciones;

import ontology.conceptos.Carta_acciones;

public class Pagar_comisiones extends Accion{
	private Carta_acciones carta;
	
	public Pagar_comisiones() {}

	public Carta_acciones getCarta() {
		return carta;
	}
	public void setCarta(Carta_acciones carta) {
		this.carta = carta;
	}
}
