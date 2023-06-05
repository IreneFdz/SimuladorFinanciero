package ontology.predicados;
import jadex.adapter.fipa.AgentIdentifier;
import java.util.ArrayList;

import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Dinero;

public class Venta_accion_exitosa extends Predicado {
	private Dinero cuanto;
    private ArrayList <Carta_acciones> lista_acciones = new ArrayList <Carta_acciones>();
    private AgentIdentifier quien;
	
	public Venta_accion_exitosa() {}

	public Dinero getCuanto() {
		return cuanto;
	}
	public void setCuanto(Dinero cuanto) {
		this.cuanto = cuanto;
	}
	public AgentIdentifier getQuien() {
		return quien;
	}
	public void setQuien(AgentIdentifier quien) {
		this.quien = quien;
	}
    public ArrayList <Carta_acciones> getLista() {
		return lista_acciones;
	}
	public void setLista(ArrayList <Carta_acciones> lista) {
		this.lista_acciones = lista;
	}
}