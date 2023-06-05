package ontology.predicados;

import ontology.conceptos.Detalle_causa_fallo_estrategia_inversion;

public class Causa_fallo_manipulacion extends Predicado {
	private Detalle_causa_fallo_estrategia_inversion que;
	public Causa_fallo_manipulacion() {}
	
	public Detalle_causa_fallo_estrategia_inversion getQue() {
		return que;
	}
	public void setQue(Detalle_causa_fallo_estrategia_inversion que) {
		this.que = que;
	}
}
