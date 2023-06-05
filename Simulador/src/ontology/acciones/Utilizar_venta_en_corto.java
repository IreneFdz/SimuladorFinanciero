package ontology.acciones;

import ontology.conceptos.Carta_estrategia_de_inversion;
import ontology.conceptos.Carta_acciones;

public class Utilizar_venta_en_corto extends Accion{
	private Carta_acciones carta_operaciones;
    private Carta_estrategia_de_inversion carta_estrategia;
	
	public Utilizar_venta_en_corto() {}

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
}