package ontology.predicados;
import java.util.HashMap;
import jadex.adapter.fipa.AgentIdentifier;

import ontology.conceptos.Empresa;
import ontology.conceptos.Carta_acciones;

public class Utilizacion_carta_dividendos_especiales_exitosa extends Predicado {
    private Empresa que;
    private AgentIdentifier quien;
	private HashMap<Carta_acciones, AgentIdentifier> mapa = new HashMap<Carta_acciones, AgentIdentifier>();
	
	public Utilizacion_carta_dividendos_especiales_exitosa() {}

    public Empresa getQue() {
		return que;
	}
	public void setQue(Empresa que) {
		this.que = que;
	}
    public AgentIdentifier getQuien() {
		return quien;
	}
	public void setQuien(AgentIdentifier quien) {
		this.quien = quien;
	}
	public HashMap<Carta_acciones, AgentIdentifier> getMapa() {
		return mapa;
	}
	public void setMapa(HashMap<Carta_acciones, AgentIdentifier> mapa) {
		this.mapa = mapa;
	}
}