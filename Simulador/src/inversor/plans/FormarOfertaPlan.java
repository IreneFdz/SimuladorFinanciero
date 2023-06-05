package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import java.util.Random;
import jadex.adapter.fipa.AgentIdentifier;
import ontology.predicados.Cartas_acciones_colocadas;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Pila_subasta;
import inversor.beliefs.*;

import java.util.Random;

public class FormarOfertaPlan extends Plan {

    public void body()
    {
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Cartas_acciones_colocadas cartas_colocadas = (Cartas_acciones_colocadas)  respuestaTablero.getContent();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        InformacionGeneral informacion= (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();
        pilas_cartas.addCartaPila(cartas_colocadas.getQue(), cartas_colocadas.getDondeCartaPublica());
        pilas_cartas.addOculta(cartas_colocadas.getDondeCartaOculta());
		getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
        AgentIdentifier id = new AgentIdentifier(getAgentName(), true); 
        if (id.equals(inversores.getIds().get(2)))
            getBeliefbase().getBelief("cambiar_turno").setFact(true); 
     
    }
}