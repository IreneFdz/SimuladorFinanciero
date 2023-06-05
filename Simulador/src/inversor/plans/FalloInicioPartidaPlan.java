package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.predicados.Causa_fallo_participacion;
import ontology.conceptos.Detalle_causa_fallo_participacion;

import java.util.Random;

public class FalloInicioPartidaPlan extends Plan {

    public void body()
    {	
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Causa_fallo_participacion causaFallo = (Causa_fallo_participacion)  respuestaTablero.getContent();
        Detalle_causa_fallo_participacion detalle = causaFallo.getQue();
        System.out.println("Mensaje de fallo: "+detalle.getDescripcion());
    
    }
}