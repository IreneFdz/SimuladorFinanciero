package ontology.acciones;

import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Empresa;
import java.util.HashMap;

public class Utilizar_carta_operaciones extends Accion{
	private HashMap <Carta_acciones, Empresa> mapa = new HashMap<Carta_acciones, Empresa>();
	
	public Utilizar_carta_operaciones() {}

	public HashMap <Carta_acciones, Empresa> getMapa() {
		return mapa;
	}
	public void setMapa(HashMap <Carta_acciones, Empresa> mapa) {
		this.mapa = mapa;
	}
}
