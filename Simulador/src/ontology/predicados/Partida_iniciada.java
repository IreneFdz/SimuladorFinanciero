package ontology.predicados;
import java.util.HashMap;
import java.util.ArrayList;
import jadex.adapter.fipa.AgentIdentifier;

import ontology.conceptos.Dinero;
import ontology.conceptos.Empresa;
import ontology.conceptos.Carta_acciones;

public class Partida_iniciada extends Predicado {
    private HashMap<AgentIdentifier, Dinero> mapa = new HashMap<AgentIdentifier, Dinero>();
    private Carta_acciones accion;
	private ArrayList<Empresa> empresas = new ArrayList<Empresa>();
	
	public Partida_iniciada() {}

	public HashMap<AgentIdentifier, Dinero> getMapa() {
		return mapa;
	}
	public void setMapa(HashMap<AgentIdentifier, Dinero> mapa) {
		this.mapa = mapa;
	}
    public Carta_acciones getAccion() {
		return accion;
	}
	public void setAccion(Carta_acciones accion) {
		this.accion = accion;
	}
	public ArrayList<Empresa> getListaEmpresas() {
		return this.empresas;
	}
	public void setListaEmpresas(ArrayList<Empresa> empresas) {
		this.empresas = empresas;
	}
}