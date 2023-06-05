package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.acciones.Utilizar_venta_en_corto;
import ontology.predicados.Causa_fallo_venta_en_corto;
import ontology.predicados.Utilizacion_carta_venta_en_corto_exitosa;
import ontology.conceptos.Detalle_causa_fallo_estrategia_inversion;
import ontology.conceptos.Carta_estrategia_de_inversion;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Dinero;
import tablero.beliefs.*;
import java.util.Iterator;

import java.util.Random;

public class VentaEnCortoPlan extends Plan {

    public void body()
    {
        System.out.println ("VENTA EN CORTO");
        //leer mensaje recibido
		IMessageEvent msgrec = (IMessageEvent) getInitialEvent();
		//guardamos el emisor
		AgentIdentifier sender= (AgentIdentifier) msgrec.getParameter(SFipa.SENDER).getValue();
		Utilizar_venta_en_corto contenido = (Utilizar_venta_en_corto) msgrec.getContent();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        Inversores inversores= (Inversores) getBeliefbase().getBelief("Inversores").getFact();

        //Comprobar si el inversor tiene la carta y es de tipo estrategia
        boolean tiene_carta = false;
        if (contenido.getCartaEstrategia().getTipo().equals("Venta en corto descubierta")){
            if (contenido.getCartaEstrategia().getBorrada() == false)
                tiene_carta = true;
        }
                	 
        //Comprobar si el inversor tiene la carta de operaciones               
        boolean carta_correcta = false;
        for (Carta_acciones key: pilas_cartas.getMapaCartaInversor().keySet()){  
            if (contenido.getCartaOperaciones().getId() == key.getId()){
                if(key.getTipo() == 2 && pilas_cartas.getMapaCartaInversor().get(key).equals(sender))
                    carta_correcta = true;
            }
        }
        if (carta_correcta && tiene_carta){
            Utilizacion_carta_venta_en_corto_exitosa venta_exitosa = new Utilizacion_carta_venta_en_corto_exitosa();
            //Borrar la carta de operaciones del inversor
            for(Iterator<Carta_acciones> iterator = pilas_cartas.getMapaCartaInversor().keySet().iterator(); iterator.hasNext();){
                Carta_acciones key = iterator.next();
                if(contenido.getCartaOperaciones().getId() == key.getId())
                    iterator.remove();
            }
            
            //Borrar la carta de estrategia de inversion del inversor
            for(Iterator<AgentIdentifier> iterator = inversores.getMapaInversion().keySet().iterator(); iterator.hasNext();){
                AgentIdentifier key = iterator.next();
                if(contenido.getCartaEstrategia().getDescripcion().equals(inversores.getMapaInversion().get(key).getDescripcion())){
                    inversores.getMapaInversion().get(key).setBorrada(true);
                }
            }
            System.out.println( "--- El inversor: "+Character.getNumericValue(sender.getLocalName().charAt(8))+ " ha recibido 12.000 euros");
            venta_exitosa.setQue(contenido.getCartaOperaciones());
            venta_exitosa.setQuien(sender);
                for (AgentIdentifier x : inversores.getIds()) {
                    IMessageEvent msg = createMessageEvent("Inform_Generico");
                    msg.getParameterSet(SFipa.RECEIVERS).addValue(x);
                    msg.setContent(venta_exitosa);
                    sendMessage(msg);
                }
        
            //Sumar el dinero coorespondiente a la venta al inversor
            Dinero dinero = new Dinero();
            dinero.setCantidad(12000);
            inversores.sumarDinero(sender, dinero);
        }

        if (tiene_carta == false){
            System.out.println ("El inversor no tiene la carta necesaria para hacer la venta en corto.");
            //crear mensaje con predicado y concepto
			
            Detalle_causa_fallo_estrategia_inversion detalle_causa_fallo_estrategia_inversion= new Detalle_causa_fallo_estrategia_inversion();
            detalle_causa_fallo_estrategia_inversion.setDescripcion("El inversor no tiene la carta necesaria para hacer la venta en corto.");

            Causa_fallo_venta_en_corto causa_fallo_venta_en_corto = new Causa_fallo_venta_en_corto();
            causa_fallo_venta_en_corto.setQue(detalle_causa_fallo_estrategia_inversion);

            //crear mensaje de failure
            IMessageEvent msg = createMessageEvent("Failure_Generico");
            msg.setContent(causa_fallo_venta_en_corto);
			msg.getParameterSet(SFipa.RECEIVERS).addValue(sender);
            sendMessage(msg);
        }

        if (carta_correcta == false){
            System.out.println ("La carta de operaciones no es correcta, no se puede realizar la venta en corto.");
            //crear mensaje con predicado y concepto
			
            Detalle_causa_fallo_estrategia_inversion detalle_causa_fallo_estrategia_inversion= new Detalle_causa_fallo_estrategia_inversion();
            detalle_causa_fallo_estrategia_inversion.setDescripcion("La carta de operaciones no es correcta, no se puede realizar la venta en corto.");

            Causa_fallo_venta_en_corto causa_fallo_venta_en_corto = new Causa_fallo_venta_en_corto();
            causa_fallo_venta_en_corto.setQue(detalle_causa_fallo_estrategia_inversion);

            //crear mensaje de failure
            IMessageEvent msg = createMessageEvent("Failure_Generico");
            msg.setContent(causa_fallo_venta_en_corto);
			msg.getParameterSet(SFipa.RECEIVERS).addValue(sender);
            sendMessage(msg);
        }
       
        getBeliefbase().getBelief("Inversores").setFact(inversores);
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
        getBeliefbase().getBelief("cambiar_turno").setFact(false);

        
    }
}