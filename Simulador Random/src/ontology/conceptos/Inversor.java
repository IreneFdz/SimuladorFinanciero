package ontology.conceptos;
import jadex.adapter.fipa.AgentIdentifier;

public class Inversor extends Concepto {
	private AgentIdentifier agentid;
	
	public Inversor() {}

	public AgentIdentifier getAgentId() {
		return this.agentid;
	}

	public void setAgentId(AgentIdentifier agentid) {
		this.agentid = agentid;
	}

}
