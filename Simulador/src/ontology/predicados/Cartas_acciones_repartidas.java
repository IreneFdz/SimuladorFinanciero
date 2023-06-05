package ontology.predicados;
import java.util.HashMap;
import java.util.ArrayList;

import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Pila_subasta;

public class Cartas_acciones_repartidas extends Predicado {
	private Carta_acciones carta1;
    private Carta_acciones carta2;
	private HashMap<Carta_acciones, Pila_subasta> mapa = new HashMap<Carta_acciones, Pila_subasta>();
	
	
	public Cartas_acciones_repartidas() {}

	public Carta_acciones getCarta1() {
		return carta1;
	}
	public void setCarta1(Carta_acciones carta) {
		this.carta1 = carta;
	}
    public Carta_acciones getCarta2() {
		return carta2;
	}
	public void setCarta2(Carta_acciones carta) {
		this.carta2 = carta;
	}
	public HashMap<Carta_acciones, Pila_subasta> getMapa() {
		return mapa;
	}
	public void setMapa(HashMap<Carta_acciones, Pila_subasta> mapa) {
		this.mapa = mapa;
	}
	
}