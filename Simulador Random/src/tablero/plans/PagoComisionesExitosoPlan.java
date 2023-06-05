package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import tablero.beliefs.*;
import ontology.predicados.Pago_comisiones_exitosa;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Pila_subasta;
import ontology.conceptos.Dinero;

import java.util.Random;

public class PagoComisionesExitosoPlan extends Plan {

    public void body()
    {
		IMessageEvent msgrec = (IMessageEvent) getInitialEvent();
        AgentIdentifier sender= (AgentIdentifier) msgrec.getParameter(SFipa.SENDER).getValue();
		Pago_comisiones_exitosa comisiones = (Pago_comisiones_exitosa) msgrec.getContent();
        
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        
        for (Carta_acciones key: pilas_cartas.getMapaCartaInversor().keySet()){  
            //Buscar la carta de comisiones y borrarla
            if(key.getId()== comisiones.getQue().getId()){
                Dinero dinero = new Dinero();
                dinero.setCantidad(key.getA_pagar());
                pilas_cartas.getMapaCartaInversor().remove(key);
                inversores.restarDinero(sender, dinero);
                break;
            }     
        }
        System.out.println("--- El inversor: "+ Character.getNumericValue(sender.getLocalName().charAt(8))+ " ha pagado las comisiones.");
        Pago_comisiones_exitosa respuesta = new Pago_comisiones_exitosa();
        respuesta.setQue(comisiones.getQue());
        respuesta.setQuien(comisiones.getQuien());
        for (AgentIdentifier i : inversores.getIds()) {
				IMessageEvent msg = createMessageEvent("Inform_Generico");
				msg.getParameterSet(SFipa.RECEIVERS).addValue(i);
            	msg.setContent(respuesta);
				sendMessage(msg);
			} 
        
        boolean hay_carta = false;
        for (Carta_acciones key: pilas_cartas.getMapaCartaInversor().keySet()){  
            if(key.getTipo()==3)
                hay_carta = true;      
        }
        if (hay_carta == false){
            getBeliefbase().getBelief("cambiar_fase").setFact(true); 
            getBeliefbase().getBelief("pago_comisiones").setFact(false); 
        }

        getBeliefbase().getBelief("Inversores").setFact(inversores);  
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);  
    }
}