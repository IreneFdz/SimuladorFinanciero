package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.acciones.Pagar_comisiones;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Pila_subasta;
import tablero.beliefs.*;

import java.util.Random;

public class PagarComisionesPlan extends Plan {

    public void body(){

        System.out.println("PAGO COMISIONES");
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        Inversores inversores= (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        Pagar_comisiones pagar_comisiones = new Pagar_comisiones();
        getBeliefbase().getBelief("cambiar_turno").setFact(false); 

        boolean hay_carta = false;
        for (Carta_acciones key: pilas_cartas.getMapaCartaInversor().keySet()){  
            //Si hay carta de comisiones
            if(key.getTipo()==3){
                //Mandar mensaje al inversor que tiene una carta de comisiones.
                pagar_comisiones.setCarta(key);
                hay_carta = true;
                IMessageEvent msg = createMessageEvent("Request_Generico");
                msg.getParameterSet(SFipa.RECEIVERS).addValue(pilas_cartas.getMapaCartaInversor().get(key));
                msg.setContent(pagar_comisiones);
                sendMessage(msg);
            }       
        }
        if (hay_carta == false){
            getBeliefbase().getBelief("cambiar_fase").setFact(true);
            getBeliefbase().getBelief("pago_comisiones").setFact(false); 
        }	    
    }
}