package ontology.predicados;
import java.util.HashMap;
import jadex.adapter.fipa.AgentIdentifier;


public class Recuento_final_hecho extends Predicado {
	private HashMap<AgentIdentifier, Integer> mapa = new HashMap<AgentIdentifier, Integer>();
	
	public Recuento_final_hecho() {}

	public HashMap<AgentIdentifier, Integer> getMapa() {
		return mapa;
	}
	public void setMapa(HashMap<AgentIdentifier, Integer> mapa) {
		this.mapa = mapa;
	}
}