package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.acciones.Pagar_comisiones;
import ontology.predicados.Pago_comisiones_exitosa;
import ontology.predicados.Causa_fallo_pago_comisiones;
import ontology.conceptos.Detalle_causa_fallo_pago_comisiones;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Pila_subasta;
import ontology.conceptos.Dinero;
import inversor.beliefs.*;

import java.util.Random;

public class PagarComisionesPlan extends Plan {

    public void body()
    {
		getBeliefbase().getBelief("cambiar_turno").setFact(false); 
        getBeliefbase().getBelief("mi_turno").setFact(false); 
        IMessageEvent msgrec = (IMessageEvent) getInitialEvent();
        AgentIdentifier tablero= (AgentIdentifier) msgrec.getParameter(SFipa.SENDER).getValue();
		Pagar_comisiones pagar_comisiones = (Pagar_comisiones) msgrec.getContent();
        MisCartasAcciones mis_cartas = (MisCartasAcciones) getBeliefbase().getBelief("MisCartasAcciones").getFact();
        Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        AgentIdentifier id = new AgentIdentifier(getAgentName(), true);

        boolean contiene = false;
        boolean suficiente_dinero = false;
        for (Carta_acciones carta : mis_cartas.getMisCartas()) {
            if(carta.getId() == pagar_comisiones.getCarta().getId())
                contiene = true;
        }
        if(inversores.getDineroPorId(id).getCantidad() >= pagar_comisiones.getCarta().getA_pagar())
            suficiente_dinero = true;
        
        if (contiene && suficiente_dinero){
            Pago_comisiones_exitosa exito = new Pago_comisiones_exitosa();
            exito.setQue(pagar_comisiones.getCarta());
            exito.setQuien(id);
            IMessageEvent msg = createMessageEvent("Inform_Generico");
			msg.getParameterSet(SFipa.RECEIVERS).addValue(tablero);
            msg.setContent(exito);
			sendMessage(msg);
        }
        if (contiene == false){
            Detalle_causa_fallo_pago_comisiones detalle_causa_fallo_pago_comisiones= new Detalle_causa_fallo_pago_comisiones();
            detalle_causa_fallo_pago_comisiones.setDescripcion("El inversor no tiene la carta de pago de comisiones");

            Causa_fallo_pago_comisiones causa_fallo_pago_comisiones = new Causa_fallo_pago_comisiones();
            causa_fallo_pago_comisiones.setQue(detalle_causa_fallo_pago_comisiones);

            //crear mensaje de failure
            IMessageEvent msg = createMessageEvent("Failure_Generico");
            msg.setContent(causa_fallo_pago_comisiones);
			msg.getParameterSet(SFipa.RECEIVERS).addValue(tablero);
            sendMessage(msg);
        } 
        if (suficiente_dinero == false){
            Detalle_causa_fallo_pago_comisiones detalle_causa_fallo_pago_comisiones= new Detalle_causa_fallo_pago_comisiones();
            detalle_causa_fallo_pago_comisiones.setDescripcion("El inversor no tiene suficiente dinero para el pago de comisiones");

            Causa_fallo_pago_comisiones causa_fallo_pago_comisiones = new Causa_fallo_pago_comisiones();
            causa_fallo_pago_comisiones.setQue(detalle_causa_fallo_pago_comisiones);

            //crear mensaje de failure
            IMessageEvent msg = createMessageEvent("Failure_Generico");
            msg.setContent(causa_fallo_pago_comisiones);
			msg.getParameterSet(SFipa.RECEIVERS).addValue(tablero);
            sendMessage(msg);
        }        
    }
}