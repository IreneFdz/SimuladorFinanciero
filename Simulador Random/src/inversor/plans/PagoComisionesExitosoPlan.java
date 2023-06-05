package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import jadex.adapter.fipa.AgentIdentifier;
import ontology.predicados.Pago_comisiones_exitosa;
import ontology.conceptos.Dinero;
import ontology.conceptos.Carta_acciones;
import inversor.beliefs.*;

import java.util.Random;

public class PagoComisionesExitosoPlan extends Plan {

    public void body()
    {
        IMessageEvent msgrec = (IMessageEvent) getInitialEvent();
		Pago_comisiones_exitosa comisiones = (Pago_comisiones_exitosa) msgrec.getContent();
        
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        MisCartasAcciones mis_cartas = (MisCartasAcciones) getBeliefbase().getBelief("MisCartasAcciones").getFact();
        AgentIdentifier id = new AgentIdentifier(getAgentName(), true);

        for (Carta_acciones key: pilas_cartas.getMapaCartaInversor().keySet()){  
            //Buscar la carta de comisiones en caso de que este guardada y borrarla
            if(key.getId() == comisiones.getQue().getId()){
                pilas_cartas.getMapa().remove(key);
                break;
            }
		} 
        Dinero dinero = new Dinero();
        dinero.setCantidad(comisiones.getQue().getA_pagar());
        inversores.restarDinero(comisiones.getQuien(), dinero);

        if(comisiones.getQuien().equals(id)){
            for (int i = 0; i< mis_cartas.getMisCartas().size(); i++) {
                if(mis_cartas.getMisCartas().get(i).getId() == comisiones.getQue().getId()){
                    mis_cartas.getMisCartas().remove(i); 
                } 
            }
        }
        getBeliefbase().getBelief("Inversores").setFact(inversores);
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
        getBeliefbase().getBelief("MisCartasAcciones").setFact(mis_cartas);
        
    }
}