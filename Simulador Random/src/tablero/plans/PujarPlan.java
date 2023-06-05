package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.conceptos.Pila_subasta;
import ontology.conceptos.Dinero;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Detalle_causa_fallo_puja;
import ontology.acciones.Pujar;
import ontology.predicados.Puja_exitosa;
import ontology.predicados.Causa_fallo_puja;
import tablero.beliefs.*;
import jadex.adapter.fipa.AgentIdentifier;
import java.lang.Math;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

import java.util.Random;

public class PujarPlan extends Plan {

    public void body()
    {
        //leer mensaje recibido
		IMessageEvent msgrec = (IMessageEvent) getInitialEvent();
		//guardamos el emisor
		AgentIdentifier sender= (AgentIdentifier) msgrec.getParameter(SFipa.SENDER).getValue();
		Pujar contenido = (Pujar) msgrec.getContent();
		String id = sender.getLocalName();

        //Comprobar si la puja es correcta
        //Comprobar si el inversor tiene dinero para la puja
        Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        InformacionGeneral informacion= (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();
        if (contenido.getDinero().getCantidad() >= pilas_cartas.getPujaMinima(contenido.getPila()) && inversores.getDineroPorId(sender).getCantidad() >= contenido.getDinero().getCantidad()){
			//crear mensaje inform-result puja exitosa
            pilas_cartas.addInversorPila(contenido.getPila(), sender);
            pilas_cartas.setPujaMinima(contenido.getPila(), contenido.getDinero().getCantidad()+1000);
            for (Pila_subasta pila_puja : pilas_cartas.getPujasMinimas().keySet()) {
                boolean tiene_inversor = false;
				for (Pila_subasta pila_inversor : pilas_cartas.getMapaPilaInversor().keySet()) {
                    if (pila_puja.getNumero() == pila_inversor.getNumero())
                        tiene_inversor = true;
			    }
                if (tiene_inversor == false)
                     pilas_cartas.setPujaMinima(pila_puja, 0);
			}

            pilas_cartas.setPrecioPila(contenido.getPila(), contenido.getDinero().getCantidad());

            Puja_exitosa puja_exitosa = new Puja_exitosa();
            Dinero dinero = new Dinero();
            dinero.setCantidad(contenido.getDinero().getCantidad());
			puja_exitosa.setCuanto(dinero);
            puja_exitosa.setQuien(sender);
            puja_exitosa.setQue(contenido.getPila());
            

            IMessageEvent msg = createMessageEvent("Inform_Generico");
            for (AgentIdentifier i : inversores.getIds()) {
				msg.getParameterSet(SFipa.RECEIVERS).addValue(i);
            	msg.setContent(puja_exitosa);
			}
            sendMessage(msg);
        }else{

            if (contenido.getPila().getNumero() > 3 || contenido.getPila().getNumero() < 1){
                //crear mensaje con predicado y concepto
                Detalle_causa_fallo_puja detalle_causa_fallo_puja = new Detalle_causa_fallo_puja();
                detalle_causa_fallo_puja.setDescripcion("La puja no se ha podido realizar porque la pila indicada no existe");

                Causa_fallo_puja causa_fallo_puja = new Causa_fallo_puja();
                causa_fallo_puja.setQue(detalle_causa_fallo_puja);

                //crear mensaje de failure
                IMessageEvent msg = createMessageEvent("Failure_Generico");
                msg.setContent(causa_fallo_puja);
                msg.getParameterSet(SFipa.RECEIVERS).addValue(sender);
                sendMessage(msg);
            }
            if (contenido.getDinero().getCantidad() < pilas_cartas.getPujaMinima(contenido.getPila())){
                //crear mensaje con predicado y concepto
                Detalle_causa_fallo_puja detalle_causa_fallo_puja = new Detalle_causa_fallo_puja();
                detalle_causa_fallo_puja.setDescripcion("La puja no se ha podido realizar porque no supera la puja minima");

                Causa_fallo_puja causa_fallo_puja = new Causa_fallo_puja();
                causa_fallo_puja.setQue(detalle_causa_fallo_puja);

                //crear mensaje de failure
                IMessageEvent msg = createMessageEvent("Failure_Generico");
                msg.setContent(causa_fallo_puja);
                msg.getParameterSet(SFipa.RECEIVERS).addValue(sender);
                sendMessage(msg);
            }
            if ( inversores.getDineroPorId(sender).getCantidad() < contenido.getDinero().getCantidad()){
                //crear mensaje con predicado y concepto
                Detalle_causa_fallo_puja detalle_causa_fallo_puja = new Detalle_causa_fallo_puja();
                detalle_causa_fallo_puja.setDescripcion("La puja no se ha podido realizar porque no tiene dinero suficiente");

                Causa_fallo_puja causa_fallo_puja = new Causa_fallo_puja();
                causa_fallo_puja.setQue(detalle_causa_fallo_puja);

                //crear mensaje de failure
                IMessageEvent msg = createMessageEvent("Failure_Generico");
                msg.setContent(causa_fallo_puja);
                msg.getParameterSet(SFipa.RECEIVERS).addValue(sender);
                sendMessage(msg);
            }
        }
        //COMPROBAR SI SE HA TERMINADO LA PUJA
        ArrayList<AgentIdentifier> lista_inversores = new ArrayList<AgentIdentifier>();
        ArrayList<Integer> num_pila = new ArrayList<Integer>();
        for (Pila_subasta key: pilas_cartas.getMapaPilaInversor().keySet()){  
                lista_inversores.add(pilas_cartas.getMapaPilaInversor().get(key));
                num_pila.add(key.getNumero());
		}
       
        
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
        getBeliefbase().getBelief("Inversores").setFact(inversores);
        getBeliefbase().getBelief("cambiar_turno").setFact(false);

    }
}