package ontology.acciones;

import jadex.adapter.fipa.AgentIdentifier;
import ontology.conceptos.Bono;
import ontology.conceptos.Cantidad;

public class Comprar_bono extends Accion{
	private Cantidad cantidad;
	private Bono bono;
	
	public Comprar_bono() {}

	public Bono getBono() {
		return bono;
	}
	public void setBono(Bono bono) {
		this.bono = bono;
	}
	public Cantidad getCuantos() {
		return cantidad;
	}
	public void setCuantos(Cantidad cantidad) {
		this.cantidad = cantidad;
	}
}
