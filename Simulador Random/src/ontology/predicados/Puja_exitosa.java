package ontology.predicados;
import jadex.adapter.fipa.AgentIdentifier;

import ontology.conceptos.Pila_subasta;
import ontology.conceptos.Dinero;

public class Puja_exitosa extends Predicado {
	private Dinero cuanto;
    private Pila_subasta que;
    private AgentIdentifier quien;
	
	public Puja_exitosa() {}

	public Dinero getCuanto() {
		return cuanto;
	}
	public void setCuanto(Dinero cuanto) {
		this.cuanto = cuanto;
	}
	public AgentIdentifier getQuien() {
		return quien;
	}
	public void setQuien(AgentIdentifier quien) {
		this.quien = quien;
	}
    public Pila_subasta getQue() {
		return que;
	}
	public void setQue(Pila_subasta que) {
		this.que = que;
	}
}