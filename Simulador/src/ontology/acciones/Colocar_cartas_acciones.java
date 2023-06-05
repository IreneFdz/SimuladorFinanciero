package ontology.acciones;
import jadex.adapter.fipa.AgentIdentifier;

import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Pila_subasta;

public class Colocar_cartas_acciones extends Accion{
	private Carta_acciones carta_publica;
    private Carta_acciones carta_oculta;
    private Pila_subasta oculta;
    private Pila_subasta publica;
	
	public Colocar_cartas_acciones() {}
	
	public Carta_acciones getCartaOculta() {
		return carta_oculta;
	}
	public void setCartaOculta(Carta_acciones carta_oculta) {
		this.carta_oculta = carta_oculta;
	}
	public Carta_acciones getCartaPublica() {
		return carta_publica;
	}
	public void setCartaPublica(Carta_acciones carta_publica) {
		this.carta_publica = carta_publica;
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