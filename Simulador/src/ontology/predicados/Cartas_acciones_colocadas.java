package ontology.predicados;

import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Pila_subasta;

public class Cartas_acciones_colocadas extends Predicado {
	private Carta_acciones que;
    private Pila_subasta oculta;
    private Pila_subasta publica;
	
	public Cartas_acciones_colocadas() {}

	public Carta_acciones getQue() {
		return que;
	}
	public void setQue(Carta_acciones que) {
		this.que = que;
	}
    public Pila_subasta getDondeCartaOculta() {
		return oculta;
	}
	public void setDondeCartaOculta(Pila_subasta oculta) {
		this.oculta = oculta;
	}
    public Pila_subasta getDondeCartaPublica() {
		return publica;
	}
	public void setDondeCartaPublica(Pila_subasta publica) {
		this.publica = publica;
	}
}