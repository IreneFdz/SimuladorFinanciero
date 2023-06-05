package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import inversor.beliefs.*;
import java.util.HashMap;
import ontology.predicados.Previsiones_asignadas;
import ontology.conceptos.Empresa;
import ontology.conceptos.Prevision;

import java.util.Random;

public class AsignarPrevisionesPlan extends Plan {

    public void body()
    {
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        Previsiones_asignadas previsiones = (Previsiones_asignadas)  respuestaTablero.getContent();
        HashMap<Empresa, Prevision> mapa = previsiones.getMapa();
        PrevisionesEmpresas previsionesEmpresas = new PrevisionesEmpresas();
        previsionesEmpresas.setPrevisiones(mapa);
        pilas_cartas.establecerCondicionesIniciales();
        getBeliefbase().getBelief("PrevisionesEmpresas").setFact(previsionesEmpresas);
                   
    }
}