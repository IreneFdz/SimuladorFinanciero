package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.predicados.Causa_fallo_venta_accion;
import ontology.conceptos.Detalle_causa_fallo_vender_accion;
import inversor.beliefs.*;

import java.util.Random;

public class FalloVenderAccionPlan extends Plan {

    public void body()
    {	
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Causa_fallo_venta_accion causaFallo = (Causa_fallo_venta_accion)  respuestaTablero.getContent();
        Detalle_causa_fallo_vender_accion detalle = causaFallo.getQue();
        System.out.println("Mensaje leido de fallo: "+detalle.getDescripcion());
        getBeliefbase().getBelief("cambiar_turno").setFact(true);  
    }
}