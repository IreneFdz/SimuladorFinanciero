package ontology.acciones;

import ontology.conceptos.Carta_acciones;
import java.util.ArrayList;

public class Vender_accion extends Accion{
	private ArrayList <Carta_acciones> lista_acciones = new ArrayList <Carta_acciones>();
	
	public Vender_accion() {}

	public ArrayList <Carta_acciones> getLista() {
		return lista_acciones;
	}
	public void setLista(ArrayList <Carta_acciones> lista) {
		this.lista_acciones = lista;
	}
}