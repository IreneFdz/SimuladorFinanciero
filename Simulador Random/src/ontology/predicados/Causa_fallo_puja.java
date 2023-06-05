package ontology.predicados;

import ontology.conceptos.Detalle_causa_fallo_puja;

public class Causa_fallo_puja extends Predicado {
	private Detalle_causa_fallo_puja que;
	public Causa_fallo_puja() {}
	
	public Detalle_causa_fallo_puja getQue() {
		return que;
	}
	public void setQue(Detalle_causa_fallo_puja que) {
		this.que = que;
	}
}
