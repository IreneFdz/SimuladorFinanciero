package ontology.predicados;
import java.util.HashMap;
import java.util.ArrayList;
import jadex.adapter.fipa.AgentIdentifier;
import ontology.conceptos.Carta_acciones;

import ontology.conceptos.Pila_subasta;

public class Puja_terminada extends Predicado {
	private HashMap<Pila_subasta, AgentIdentifier> mapa = new HashMap<Pila_subasta, AgentIdentifier>();
	private ArrayList<Carta_acciones> lista = new ArrayList<Carta_acciones>();

	public Puja_terminada() {}

	public HashMap<Pila_subasta, AgentIdentifier> getMapa() {
		return mapa;
	}
	public void setMapa(HashMap<Pila_subasta, AgentIdentifier> mapa) {
		this.mapa = mapa;
	}
	public ArrayList<Carta_acciones> getLista() {
		return lista;
	}
	public void setLista(ArrayList<Carta_acciones> lista) {
		this.lista = lista;
	}
}