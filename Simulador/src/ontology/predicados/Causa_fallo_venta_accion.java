package ontology.predicados;

import ontology.conceptos.Detalle_causa_fallo_vender_accion;

public class Causa_fallo_venta_accion extends Predicado {
	private Detalle_causa_fallo_vender_accion que;
	public Causa_fallo_venta_accion() {}
	
	public Detalle_causa_fallo_vender_accion getQue() {
		return que;
	}
	public void setQue(Detalle_causa_fallo_vender_accion que) {
		this.que = que;
	}
}
