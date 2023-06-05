package ontology.predicados;
import jadex.adapter.fipa.AgentIdentifier;

import ontology.conceptos.Bono;
import ontology.conceptos.Cantidad;

public class Compra_bono_exitosa extends Predicado {
	private Bono que;
	private Cantidad cantidad;
    private AgentIdentifier quien;
	
	public Compra_bono_exitosa() {}

	public Bono getQue() {
		return que;
	}
	public void setQue(Bono que) {
		this.que = que;
	}
	public Cantidad getCuantos() {
		return cantidad;
	}
	public void setCuantos(Cantidad cantidad) {
		this.cantidad = cantidad;
	}
	public AgentIdentifier getQuien() {
		return quien;
	}
	public void setQuien(AgentIdentifier quien) {
		this.quien = quien;
	}
    
}