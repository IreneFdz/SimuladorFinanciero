package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.predicados.Causa_fallo_dividendos_especiales;
import ontology.conceptos.Detalle_causa_fallo_estrategia_inversion;
import inversor.beliefs.*;

import java.util.Random;

public class FalloDividendosEspecialesPlan extends Plan {

    public void body()
    {	
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Causa_fallo_dividendos_especiales causaFallo = (Causa_fallo_dividendos_especiales)  respuestaTablero.getContent();
        Detalle_causa_fallo_estrategia_inversion detalle = causaFallo.getQue();
        System.out.println("Mensaje leido de fallo: "+detalle.getDescripcion());
        getBeliefbase().getBelief("cambiar_turno").setFact(true);
        getBeliefbase().getBelief("mi_turno").setFact(false);
        getBeliefbase().getBelief("dividendos_especiales").setFact(false); 
    
    }
}