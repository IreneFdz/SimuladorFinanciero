package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import tablero.beliefs.*;
import ontology.predicados.Causa_fallo_pago_comisiones;
import ontology.conceptos.Carta_acciones;

import java.util.Random;

public class FalloPagoComisionesPlan extends Plan {

    public void body()
    {
        IMessageEvent msgrec = (IMessageEvent) getInitialEvent();
		Causa_fallo_pago_comisiones causa_fallo = (Causa_fallo_pago_comisiones) msgrec.getContent();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        System.out.println(causa_fallo.getQue().getDescripcion());

        getBeliefbase().getBelief("cambiar_fase").setFact(true); 
        getBeliefbase().getBelief("pago_comisiones").setFact(false);
    }
}