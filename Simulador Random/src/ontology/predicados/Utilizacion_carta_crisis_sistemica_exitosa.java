package ontology.predicados;
import java.util.HashMap;
import jadex.adapter.fipa.AgentIdentifier;

import ontology.conceptos.Empresa;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Decision_crisis_sistemica;

public class Utilizacion_carta_crisis_sistemica_exitosa extends Predicado {
    private HashMap<Empresa, Decision_crisis_sistemica> mapa;
    private Carta_acciones que;
    private AgentIdentifier quien;
	
	public Utilizacion_carta_crisis_sistemica_exitosa() {}

	public HashMap<Empresa, Decision_crisis_sistemica> getMapa() {
		return mapa;
	}
	public void setMapa(HashMap<Empresa, Decision_crisis_sistemica> mapa) {
		this.mapa = mapa;
	}
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