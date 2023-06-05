package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.predicados.Causa_fallo_compra_bono;
import ontology.conceptos.Detalle_causa_fallo_compra_bono;
import inversor.beliefs.*;

import java.util.Random;

public class FalloCompraBonoPlan extends Plan {

    public void body()
    {	
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Causa_fallo_compra_bono causaFallo = (Causa_fallo_compra_bono)  respuestaTablero.getContent();
        Detalle_causa_fallo_compra_bono detalle = causaFallo.getQue();
        System.out.println("Mensaje leido de fallo: "+detalle.getDescripcion());
        getBeliefbase().getBelief("posible_comprar_bono").setFact(false);
        getBeliefbase().getBelief("cambiar_turno").setFact(true);   
    }
}