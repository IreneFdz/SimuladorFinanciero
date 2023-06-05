package ontology.predicados;
import java.util.ArrayList;
import java.util.HashMap;
import jadex.adapter.fipa.AgentIdentifier;

import ontology.conceptos.Empresa;
import ontology.conceptos.Prevision;
import ontology.conceptos.Carta_acciones;

public class Cotizacion_empresas_actualizadas extends Predicado {
	private HashMap<Carta_acciones, AgentIdentifier> mapa = new HashMap<Carta_acciones, AgentIdentifier>();
	private HashMap<Empresa, Prevision> previsiones = new HashMap<Empresa, Prevision>();
	
	public Cotizacion_empresas_actualizadas() {}

	public HashMap<Carta_acciones, AgentIdentifier> getMapa() {
		return mapa;
	}
	public void setMapa(HashMap<Carta_acciones, AgentIdentifier> mapa) {
		this.mapa = mapa;
	}
	public void setPrevisiones(HashMap<Empresa, Prevision> previsiones) {
		this.previsiones = previsiones;
	}
    public Prevision getPrevision(Empresa empresa){
        return previsiones.get(empresa);
    }
	public int getPrevisionTipo(Empresa empresa){
		int result = 0;
		for (Empresa key: previsiones.keySet()){   
            if (key.getNombre().equals(empresa.getNombre()))
                result = previsiones.get(key).getTipo();    
        }
        return result;
    }
	public int getPrevisionCantidad(Empresa empresa){
		int result = 0;
		for (Empresa key: previsiones.keySet()){   
            if (key.getNombre().equals(empresa.getNombre()))
                result = previsiones.get(key).getCantidad();  
        }
        return result;
    }
	public HashMap<Empresa, Prevision> getPrevisiones (){
		return this.previsiones;
	}
}