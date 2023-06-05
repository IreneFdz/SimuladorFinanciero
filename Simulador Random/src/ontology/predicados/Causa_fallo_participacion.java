package ontology.predicados;

import ontology.conceptos.Detalle_causa_fallo_participacion;

public class Causa_fallo_participacion extends Predicado {
	private Detalle_causa_fallo_participacion que;
	public Causa_fallo_participacion() {}
	
	public Detalle_causa_fallo_participacion getQue() {
		return que;
	}
	public void setQue(Detalle_causa_fallo_participacion que) {
		this.que = que;
	}
}
