package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.predicados.Turno_terminado;
import tablero.beliefs.*;

import java.util.Random;

public class TurnoAcabadoPlan extends Plan {

    public void body()
    {
        //leer mensaje recibido
		IMessageEvent msgrec = (IMessageEvent) getInitialEvent();
		//guardamos el emisor
		AgentIdentifier sender= (AgentIdentifier) msgrec.getParameter(SFipa.SENDER).getValue();
        InformacionGeneral informacion= (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();
		Turno_terminado contenido = (Turno_terminado) msgrec.getContent(); 
        getBeliefbase().getBelief("cambiar_turno").setFact(true);  
    }
}