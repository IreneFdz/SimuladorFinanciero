package ontology.acciones;
import java.util.ArrayList;

import ontology.conceptos.Carta_estrategia_de_inversion;
import ontology.conceptos.Carta_acciones;

public class Utilizar_manipulacion_a_la_baja extends Accion{
	private ArrayList<Carta_acciones> lista;
    private Carta_estrategia_de_inversion carta;
	
	public Utilizar_manipulacion_a_la_baja() {}

	public ArrayList<Carta_acciones> getLista() {
		return lista;
	}
	public void setLista(ArrayList<Carta_acciones> lista) {
		this.lista = lista;
	}
    public Carta_estrategia_de_inversion getCarta() {
		return carta;
	}
	public void setCarta(Carta_estrategia_de_inversion carta) {
		this.carta = carta;
	}
}