package ontology.predicados;

import ontology.conceptos.Detalle_causa_fallo_pago_comisiones;

public class Causa_fallo_pago_comisiones extends Predicado {
	private Detalle_causa_fallo_pago_comisiones que;
	public Causa_fallo_pago_comisiones() {}
	
	public Detalle_causa_fallo_pago_comisiones getQue() {
		return que;
	}
	public void setQue(Detalle_causa_fallo_pago_comisiones que) {
		this.que = que;
	}
}
