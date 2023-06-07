package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.acciones.Comprar_bono;
import ontology.predicados.Compra_bono_exitosa;
import ontology.predicados.Causa_fallo_compra_bono;
import ontology.conceptos.Detalle_causa_fallo_compra_bono;
import ontology.conceptos.Dinero;
import ontology.conceptos.Bono;
import tablero.beliefs.*;

import java.util.Random;

public class ComprarBonoPlan extends Plan {

    public void body()
    {
        
        //leer mensaje recibido
		IMessageEvent msgrec = (IMessageEvent) getInitialEvent();
		//guardamos el emisor
		AgentIdentifier sender= (AgentIdentifier) msgrec.getParameter(SFipa.SENDER).getValue();
		Comprar_bono contenido = (Comprar_bono) msgrec.getContent();
		String id = sender.getLocalName();
    
        //Comprobar si el bono tiene un valor correcto
        //Comprobar si el inversor tiene dinero
        //Comprobar que el inversor ha comprado menos de tres bonos en esta ronda

        Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        Bonos bonos = (Bonos) getBeliefbase().getBelief("Bonos").getFact();
        if (sender.equals(inversores.getIds().get(0)))
            System.out.println ("COMPRA DE BONOS");
        if (contenido.getBono().getValor()==1000 && contenido.getBono().getValor()*contenido.getCuantos().getCuanto() <= inversores.getDineroPorId(sender).getCantidad() && contenido.getCuantos().getCuanto() <= 3){
			//crear mensaje inform-result bono comprado
            System.out.println("--- El inversor: "+ Character.getNumericValue(sender.getLocalName().charAt(8))+" ha comprado "+ contenido.getCuantos().getCuanto() +" bonos de 1000$");
            Compra_bono_exitosa compra_exitosa = new Compra_bono_exitosa();
			compra_exitosa.setQue(contenido.getBono());
            compra_exitosa.setQuien(sender);
            compra_exitosa.setCuantos(contenido.getCuantos());

            for (int i = 0; i< contenido.getCuantos().getCuanto(); i++){
                Bono bono = new Bono();
                bono.setValor(contenido.getBono().getValor());
                bonos.asignarBono(bono, sender);
            }
            getBeliefbase().getBelief("Bonos").setFact(bonos);
           
            //Actualizar dinero del comprador del bono
            int dinero_actual = inversores.getDineroPorId(sender).getCantidad() - (contenido.getBono().getValor() * contenido.getCuantos().getCuanto());
            Dinero dinero = new Dinero();
            dinero.setCantidad(dinero_actual);
            inversores.addIdDinero(sender, dinero);
            getBeliefbase().getBelief("Inversores").setFact(inversores);
         
            for (AgentIdentifier i : inversores.getIds()) {
				IMessageEvent msg = createMessageEvent("Inform_Generico");
				msg.getParameterSet(SFipa.RECEIVERS).addValue(i);
            	msg.setContent(compra_exitosa);
				sendMessage(msg);
			}
        }else{
            System.out.println ("Fallo compra bono");
            //crear mensaje con predicado y concepto
			
            Detalle_causa_fallo_compra_bono detalle_causa_fallo_compra_bono= new Detalle_causa_fallo_compra_bono();
            detalle_causa_fallo_compra_bono.setDescripcion("La compra de los bonos no se ha podido realizar");

            Causa_fallo_compra_bono causa_fallo_compra_bono = new Causa_fallo_compra_bono();
            causa_fallo_compra_bono.setQue(detalle_causa_fallo_compra_bono);

            //crear mensaje de failure
            IMessageEvent msg = createMessageEvent("Failure_Generico");
            msg.setContent(causa_fallo_compra_bono);
			msg.getParameterSet(SFipa.RECEIVERS).addValue(sender);
            sendMessage(msg);
        }
        getBeliefbase().getBelief("Bonos").setFact(bonos);
        getBeliefbase().getBelief("cambiar_turno").setFact(false);
  
    }
}