package ontology.acciones;
import java.util.ArrayList;

import ontology.conceptos.Carta_estrategia_de_inversion;
import ontology.conceptos.Empresa;

public class Utilizar_dividendos_especiales extends Accion{
	private Empresa empresa;
    private Carta_estrategia_de_inversion carta;
	
	public Utilizar_dividendos_especiales() {}

	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
    public Carta_estrategia_de_inversion getCarta() {
		return carta;
	}
	public void setCarta(Carta_estrategia_de_inversion carta) {
		this.carta = carta;
	}
}