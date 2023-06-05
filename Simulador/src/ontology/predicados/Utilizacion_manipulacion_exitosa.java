package ontology.predicados;
import java.util.ArrayList;
import jadex.adapter.fipa.AgentIdentifier;

import ontology.conceptos.Carta_acciones;

public class Utilizacion_manipulacion_exitosa extends Predicado {
    private ArrayList<Carta_acciones> lista;
    private AgentIdentifier quien;
	
	public Utilizacion_manipulacion_exitosa() {}

	public ArrayList<Carta_acciones> getLista() {
		return lista;
	}
	public void setLista(ArrayList<Carta_acciones> lista) {
		this.lista = lista;
	}
    public AgentIdentifier getQuien() {
		return quien;
	}
	public void setQuien(AgentIdentifier quien) {
		this.quien = quien;
	}
}