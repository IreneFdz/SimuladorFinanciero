package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.predicados.Causa_fallo_puja;
import ontology.conceptos.Detalle_causa_fallo_puja;
import inversor.beliefs.*;

import java.util.Random;

public class FalloPujaPlan extends Plan {

    public void body()
    {	
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Causa_fallo_puja causaFallo = (Causa_fallo_puja)  respuestaTablero.getContent();
        Detalle_causa_fallo_puja detalle = causaFallo.getQue();
        System.out.println("Mensaje leido de fallo: "+detalle.getDescripcion());

        Inversores inversores= (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        AgentIdentifier id = new AgentIdentifier(getAgentName(), true); 
        getBeliefbase().getBelief("cambiar_turno").setFact(true);
            
    }
}