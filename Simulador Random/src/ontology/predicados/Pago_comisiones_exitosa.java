package ontology.predicados;
import jadex.adapter.fipa.AgentIdentifier;

import ontology.conceptos.Carta_acciones;

public class Pago_comisiones_exitosa extends Predicado {
	private Carta_acciones que;
    private AgentIdentifier quien;
	
	public Pago_comisiones_exitosa() {}

	public Carta_acciones getQue() {
		return que;
	}
	public void setQue(Carta_acciones que) {
		this.que = que;
	}
	public AgentIdentifier getQuien() {
		return quien;
	}
	public void setQuien(AgentIdentifier quien) {
		this.quien = quien;
	}
    
}