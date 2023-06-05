package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.predicados.Causa_fallo_venta_en_corto;
import ontology.conceptos.Detalle_causa_fallo_estrategia_inversion;
import inversor.beliefs.*;

import java.util.Random;

public class FalloVentaEnCortoPlan extends Plan {

    public void body()
    {	
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Causa_fallo_venta_en_corto causaFallo = (Causa_fallo_venta_en_corto)  respuestaTablero.getContent();
        Detalle_causa_fallo_estrategia_inversion detalle = causaFallo.getQue();
        System.out.println("Mensaje leido de fallo: "+detalle.getDescripcion());
        getBeliefbase().getBelief("cambiar_turno").setFact(true);
        getBeliefbase().getBelief("mi_turno").setFact(false);
    }
}