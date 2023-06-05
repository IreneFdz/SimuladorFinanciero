package ontology.predicados;

import jadex.adapter.fipa.AgentIdentifier;

public class Turno_pasado extends Predicado {
	private AgentIdentifier inversor;
	
	public Turno_pasado() {}

	public AgentIdentifier getInversor() {
		return inversor;
	}
	public void setInversor(AgentIdentifier inversor) {
		this.inversor = inversor;
	}
    
}