package ontology.predicados;

import ontology.conceptos.Carta_estrategia_de_inversion;

public class Cartas_inversion_repartidas extends Predicado {
	private Carta_estrategia_de_inversion carta;
	
	public Cartas_inversion_repartidas() {}

	public Carta_estrategia_de_inversion getCarta() {
		return carta;
	}
	public void setCarta(Carta_estrategia_de_inversion carta) {
		this.carta = carta;
	}
    
}