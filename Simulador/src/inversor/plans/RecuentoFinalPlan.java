package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.predicados.Recuento_final_hecho;

import java.util.Random;

public class RecuentoFinalPlan extends Plan {

    public void body()
    {
        IMessageEvent msgrec = (IMessageEvent) getInitialEvent();
        AgentIdentifier tablero= (AgentIdentifier) msgrec.getParameter(SFipa.SENDER).getValue();
		Recuento_final_hecho recuento_hecho = (Recuento_final_hecho) msgrec.getContent();

    }
}