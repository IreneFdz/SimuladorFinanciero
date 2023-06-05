package ontology.predicados;

import ontology.conceptos.Detalle_causa_fallo_compra_bono;

public class Causa_fallo_compra_bono extends Predicado {
	private Detalle_causa_fallo_compra_bono que;
	public Causa_fallo_compra_bono() {}
	
	public Detalle_causa_fallo_compra_bono getQue() {
		return que;
	}
	public void setQue(Detalle_causa_fallo_compra_bono que) {
		this.que = que;
	}
}
