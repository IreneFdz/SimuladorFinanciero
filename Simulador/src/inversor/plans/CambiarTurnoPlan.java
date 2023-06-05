package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import inversor.beliefs.*;
import ontology.predicados.Turno_cambiado;
import ontology.conceptos.Fase;
import jadex.adapter.fipa.AgentIdentifier;

import java.util.Random;

public class CambiarTurnoPlan extends Plan {

    public void body()
    {
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Turno_cambiado turno_cambiado = (Turno_cambiado)  respuestaTablero.getContent();
        InformacionGeneral informacion= (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();
        
        informacion.setTurno(turno_cambiado.getInversor()); 
        AgentIdentifier id = new AgentIdentifier(getAgentName(), true); 
        if (id.equals(turno_cambiado.getInversor())){
            getBeliefbase().getBelief("mi_turno").setFact(true);
        }else{
            getBeliefbase().getBelief("mi_turno").setFact(false);
        }
        if (informacion.getFase().equals("Informacion"))
            getBeliefbase().getBelief("posible_comprar_bono").setFact(true);
        
        if (informacion.getFase().equals("Demanda"))
            getBeliefbase().getBelief("fase").setFact("Demanda");
        
        if (informacion.getFase().equals("Oferta"))
            getBeliefbase().getBelief("posible_comprar_bono").setFact(false);

        getBeliefbase().getBelief("InformacionGeneral").setFact(informacion);   
        getBeliefbase().getBelief("cambiar_turno").setFact(false);   
    }
}