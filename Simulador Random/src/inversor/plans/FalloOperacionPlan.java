package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.predicados.Causa_fallo_carta_operaciones;
import ontology.conceptos.Detalle_causa_fallo_carta_operaciones;
import inversor.beliefs.*;

import java.util.Random;

public class FalloOperacionPlan extends Plan {

    public void body()
    {	
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Causa_fallo_carta_operaciones causaFallo = (Causa_fallo_carta_operaciones)  respuestaTablero.getContent();
        Detalle_causa_fallo_carta_operaciones detalle = causaFallo.getQue();
        System.out.println("Mensaje leido de fallo: "+detalle.getDescripcion());
        getBeliefbase().getBelief("manipulacion").setFact(true); 
    }
}