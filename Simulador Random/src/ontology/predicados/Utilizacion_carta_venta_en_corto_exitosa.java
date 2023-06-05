package ontology.predicados;
import java.util.ArrayList;
import jadex.adapter.fipa.AgentIdentifier;

import ontology.conceptos.Carta_acciones;

public class Utilizacion_carta_venta_en_corto_exitosa extends Predicado {

    private AgentIdentifier quien;
    private Carta_acciones que;
	
	public Utilizacion_carta_venta_en_corto_exitosa() {}

    public AgentIdentifier getQuien() {
		return quien;
	}
	public void setQuien(AgentIdentifier quien) {
		this.quien = quien;
	}
    public Carta_acciones getQue() {
		return que;
	}
	public void setQue(Carta_acciones que) {
		this.que = que;
	}
}