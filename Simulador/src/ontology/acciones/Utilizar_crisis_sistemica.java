package ontology.acciones;
import java.util.HashMap;

import ontology.conceptos.Carta_estrategia_de_inversion;
import ontology.conceptos.Empresa;
import ontology.conceptos.Decision_crisis_sistemica;
import ontology.conceptos.Carta_acciones;

public class Utilizar_crisis_sistemica extends Accion{
	private Carta_acciones carta_operaciones;
    private Carta_estrategia_de_inversion carta_estrategia;
    private HashMap<Empresa, Decision_crisis_sistemica> mapa = new HashMap<Empresa, Decision_crisis_sistemica>();
	
	public Utilizar_crisis_sistemica() {}

	public Carta_acciones getCartaOperaciones() {
		return carta_operaciones;
	}
	public void setCartaOperaciones(Carta_acciones carta_operaciones) {
		this.carta_operaciones = carta_operaciones;
	}
    public Carta_estrategia_de_inversion getCartaEstrategia() {
		return carta_estrategia;
	}
	public void setCartaEstrategia(Carta_estrategia_de_inversion carta_estrategia) {
		this.carta_estrategia = carta_estrategia;
	}
    public HashMap<Empresa, Decision_crisis_sistemica> getMapa() {
		return mapa;
	}
	public void setMapa(HashMap<Empresa, Decision_crisis_sistemica> mapa) {
		this.mapa = mapa;
	}
}