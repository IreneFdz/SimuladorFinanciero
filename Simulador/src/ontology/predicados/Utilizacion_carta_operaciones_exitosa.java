package ontology.predicados;
import java.util.ArrayList;
import jadex.adapter.fipa.AgentIdentifier;

import ontology.conceptos.Empresa;
import ontology.conceptos.Carta_acciones;
import java.util.HashMap;

public class Utilizacion_carta_operaciones_exitosa extends Predicado {
    private AgentIdentifier quien;
	private HashMap <Carta_acciones, Empresa> mapa = new HashMap<Carta_acciones, Empresa>();
	
	public Utilizacion_carta_operaciones_exitosa() {}

    public HashMap <Carta_acciones, Empresa> getMapa() {
		return mapa;
	}
	public void setMapa(HashMap <Carta_acciones, Empresa> mapa) {
		this.mapa = mapa;
	}
    public AgentIdentifier getQuien() {
		return quien;
	}
	public void setQuien(AgentIdentifier quien) {
		this.quien = quien;
	}
}