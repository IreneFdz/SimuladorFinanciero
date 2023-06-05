package ontology.predicados;
import java.util.HashMap;

import ontology.conceptos.Empresa;
import ontology.conceptos.Prevision;

public class Previsiones_asignadas extends Predicado {
	private HashMap<Empresa, Prevision> mapa = new HashMap<Empresa, Prevision>();
	public Previsiones_asignadas() {}

	public HashMap<Empresa, Prevision> getMapa() {
		return mapa;
	}
	public void setMapa(HashMap<Empresa, Prevision> mapa) {
		this.mapa = mapa;
	}
	public void addPrevision(Empresa empresa, Prevision prevision) {
		this.mapa.put(empresa,prevision);
	}
}