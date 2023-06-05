package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import inversor.beliefs.*;
import ontology.predicados.Fase_cambiada;
import ontology.conceptos.Fase;

import java.util.Random;

public class CambiarFasePlan extends Plan {

    public void body()
    {
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Fase_cambiada fase_cambiada = (Fase_cambiada)  respuestaTablero.getContent();
        InformacionGeneral informacion= (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();

        informacion.setFase(fase_cambiada.getQue().getNombre()); 
        getBeliefbase().getBelief("fase").setFact(fase_cambiada.getQue().getNombre());
        getBeliefbase().getBelief("InformacionGeneral").setFact(informacion);  

        if(fase_cambiada.getQue().getNombre().equals("Acciones"))
             getBeliefbase().getBelief("cambiar_turno").setFact(true); 

        if(fase_cambiada.getQue().getNombre().equals("Venta")){
            getBeliefbase().getBelief("manipulacion").setFact(false);    
            getBeliefbase().getBelief("venta_en_corto").setFact(false);   
            getBeliefbase().getBelief("crisis_sistemica").setFact(false);   
            getBeliefbase().getBelief("dividendos_especiales").setFact(false);   
        }
    }
}