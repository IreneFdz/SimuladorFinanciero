package inversor.plans;
import inversor.beliefs.*;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import java.util.HashMap;
import jadex.adapter.fipa.AgentIdentifier;
import ontology.predicados.Puja_exitosa;
import ontology.conceptos.Dinero;
import ontology.conceptos.Pila_subasta;
import inversor.beliefs.*;

import java.util.Random;

public class PujaExitosaPlan extends Plan {

    public void body()
    {
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Puja_exitosa puja_exitosa = (Puja_exitosa)  respuestaTablero.getContent();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        Inversores inversores= (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        InformacionGeneral informacion= (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();
        int puja_minima = puja_exitosa.getCuanto().getCantidad() + 1000;
        pilas_cartas.addInversorPila(puja_exitosa.getQue(), puja_exitosa.getQuien());
        pilas_cartas.setPujaMinima(puja_exitosa.getQue(), puja_minima);

        for (Pila_subasta pila_puja : pilas_cartas.getPujasMinimas().keySet()) {
            boolean tiene_inversor = false;
			for (Pila_subasta pila_inversor : pilas_cartas.getMapaPilaInversor().keySet()) {
                if (pila_puja.getNumero() == pila_inversor.getNumero())
                    tiene_inversor = true;
			}
            if (tiene_inversor == false)
                pilas_cartas.setPujaMinima(pila_puja, 0);
		}

        AgentIdentifier id = new AgentIdentifier(getAgentName(), true); 
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);

        if (id.equals(inversores.getIds().get(2)))
            getBeliefbase().getBelief("cambiar_turno").setFact(true);
            
    }
}