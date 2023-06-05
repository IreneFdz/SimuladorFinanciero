package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.predicados.Turno_pasado;
import tablero.beliefs.*;

import java.util.Random;

public class PasarTurnoPlan extends Plan {

    public void body()
    {
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Turno_pasado turno = (Turno_pasado)  respuestaTablero.getContent();

        AgentIdentifier id = new AgentIdentifier(getAgentName(), true);
        //Comprobar si es mi turno
        if (turno.getInversor().equals(id)){
            getBeliefbase().getBelief("mi_turno").setFact(true); 
            getBeliefbase().getBelief("fase").setFact("Demanda");  
        }
                
    }
}