package ontology.predicados;
import jadex.adapter.fipa.AgentIdentifier;
import java.util.HashMap;

import ontology.conceptos.Dinero;

public class Pago_por_bono_hecho extends Predicado {
	private HashMap <AgentIdentifier, Dinero> mapa = new HashMap <AgentIdentifier, Dinero>();
	
	public Pago_por_bono_hecho() {}

	public void setMapa(HashMap <AgentIdentifier, Dinero> mapa) {
		this.mapa = mapa;
	}
	public HashMap <AgentIdentifier, Dinero> getMapa() {
		return mapa;
	}
    
}