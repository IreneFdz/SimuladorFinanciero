package ontology.predicados;

import ontology.conceptos.Detalle_causa_fallo_estrategia_inversion;

public class Causa_fallo_venta_en_corto extends Predicado {
	private Detalle_causa_fallo_estrategia_inversion que;
	public Causa_fallo_venta_en_corto() {}
	
	public Detalle_causa_fallo_estrategia_inversion getQue() {
		return que;
	}
	public void setQue(Detalle_causa_fallo_estrategia_inversion que) {
		this.que = que;
	}
}
