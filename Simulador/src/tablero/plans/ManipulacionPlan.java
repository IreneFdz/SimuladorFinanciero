package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.acciones.Utilizar_manipulacion_a_la_baja;
import ontology.conceptos.Carta_acciones;
import ontology.predicados.Causa_fallo_manipulacion;
import ontology.predicados.Utilizacion_manipulacion_exitosa;
import ontology.conceptos.Detalle_causa_fallo_estrategia_inversion;
import ontology.conceptos.Carta_estrategia_de_inversion;
import ontology.conceptos.Dinero;
import tablero.beliefs.*;
import java.util.Iterator;

import java.util.Random;

public class ManipulacionPlan extends Plan {

    public void body()
    {
        System.out.println ("MANIPULACION A LA BAJA");
        //leer mensaje recibido
		IMessageEvent msgrec = (IMessageEvent) getInitialEvent();
		//guardamos el emisor
		AgentIdentifier sender= (AgentIdentifier) msgrec.getParameter(SFipa.SENDER).getValue();
		Utilizar_manipulacion_a_la_baja contenido = (Utilizar_manipulacion_a_la_baja) msgrec.getContent();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        Inversores inversores= (Inversores) getBeliefbase().getBelief("Inversores").getFact();

        //Comprobar si el inversor tiene la carta y es de tipo operaciones
        boolean tiene_carta = false;
        if (contenido.getCarta().getTipo().equals("manipulacion a la baja")){
            if (contenido.getCarta().getBorrada() == false)
                tiene_carta = true;
        }
                	 
        //Comprobar si la lista de acciones es correcta
        boolean numero_correcto = false;
        boolean acciones_correctas = true;
        boolean existe_carta = false;
        if (contenido.getLista().size() <= 3)
            numero_correcto = true;
        for (int i = 0; i< contenido.getLista().size(); i++){
            for (Carta_acciones key: pilas_cartas.getMapaCartaInversor().keySet()){  
                if (contenido.getLista().get(i).getId() == key.getId()){
                    existe_carta = true;
                    if(key.getTipo() != 1 || !pilas_cartas.getMapaCartaInversor().get(key).equals(sender))
                        acciones_correctas = false;
                }   
            }
            if (existe_carta == false)
                acciones_correctas = false;
            existe_carta = false;
        }
        if (numero_correcto && tiene_carta && acciones_correctas){
            Utilizacion_manipulacion_exitosa manipulacion_exitosa = new Utilizacion_manipulacion_exitosa();
            //Borrar las acciones del inversor
            for (int i = 0; i< contenido.getLista().size(); i++){
                for(Iterator<Carta_acciones> iterator = pilas_cartas.getMapaCartaInversor().keySet().iterator(); iterator.hasNext();){
                    Carta_acciones key = iterator.next();
                    if(contenido.getLista().get(i).getId() == key.getId()){
                        iterator.remove();
                    }
                }
            }
            //Borrar la carta de estrategia de inversion del inversor
            for(Iterator<AgentIdentifier> iterator = inversores.getMapaInversion().keySet().iterator(); iterator.hasNext();){
                    AgentIdentifier key = iterator.next();
                    if(contenido.getCarta().getDescripcion().equals(inversores.getMapaInversion().get(key).getDescripcion())){
                        inversores.getMapaInversion().get(key).setBorrada(true);
                    }
            }
            System.out.println ("--- El inversor: "+ Character.getNumericValue(sender.getLocalName().charAt(8)) + " ha utilizado la carta de manipulacion a la baja.");
            System.out.println ("--- Ha vendido las siguientes acciones: ");
            for (int i = 0; i< contenido.getLista().size(); i++){
                System.out.println ("------ Accion con id: "+ contenido.getLista().get(i).getId() + " de la empresa: "+ contenido.getLista().get(i).getEmpresa());
            }
            manipulacion_exitosa.setLista(contenido.getLista());
            manipulacion_exitosa.setQuien(sender);
                for (AgentIdentifier x : inversores.getIds()) {
                    IMessageEvent msg = createMessageEvent("Inform_Generico");
                    msg.getParameterSet(SFipa.RECEIVERS).addValue(x);
                    msg.setContent(manipulacion_exitosa);
                    sendMessage(msg);
                }
            //Sumar el dinero coorespondiente a la venta al inversor
            Dinero dinero = new Dinero();
            dinero.setCantidad(contenido.getLista().size()*8000);
            inversores.sumarDinero(sender, dinero);
        }

        if (tiene_carta == false){
            //crear mensaje con predicado y concepto
			
            Detalle_causa_fallo_estrategia_inversion detalle_causa_fallo_estrategia_inversion= new Detalle_causa_fallo_estrategia_inversion();
            detalle_causa_fallo_estrategia_inversion.setDescripcion("El inversor no tiene la carta necesaria para hacer manipulacion a la baja.");

            Causa_fallo_manipulacion causa_fallo_manipulacion = new Causa_fallo_manipulacion();
            causa_fallo_manipulacion.setQue(detalle_causa_fallo_estrategia_inversion);

            //crear mensaje de failure
            IMessageEvent msg = createMessageEvent("Failure_Generico");
            msg.setContent(causa_fallo_manipulacion);
			msg.getParameterSet(SFipa.RECEIVERS).addValue(sender);
            sendMessage(msg);
        }

        if (numero_correcto == false){
            //crear mensaje con predicado y concepto
			
            Detalle_causa_fallo_estrategia_inversion detalle_causa_fallo_estrategia_inversion= new Detalle_causa_fallo_estrategia_inversion();
            detalle_causa_fallo_estrategia_inversion.setDescripcion("No se pueden vender mas de tres acciones con la carta de manipulacion a la baja.");

            Causa_fallo_manipulacion causa_fallo_manipulacion = new Causa_fallo_manipulacion();
            causa_fallo_manipulacion.setQue(detalle_causa_fallo_estrategia_inversion);

            //crear mensaje de failure
            IMessageEvent msg = createMessageEvent("Failure_Generico");
            msg.setContent(causa_fallo_manipulacion);
			msg.getParameterSet(SFipa.RECEIVERS).addValue(sender);
            sendMessage(msg);
        }

        if (acciones_correctas == false){
            //crear mensaje con predicado y concepto
			
            Detalle_causa_fallo_estrategia_inversion detalle_causa_fallo_estrategia_inversion= new Detalle_causa_fallo_estrategia_inversion();
            detalle_causa_fallo_estrategia_inversion.setDescripcion("Las acciones que se quieren vender no son correctas.");

            Causa_fallo_manipulacion causa_fallo_manipulacion = new Causa_fallo_manipulacion();
            causa_fallo_manipulacion.setQue(detalle_causa_fallo_estrategia_inversion);

            //crear mensaje de failure
            IMessageEvent msg = createMessageEvent("Failure_Generico");
            msg.setContent(causa_fallo_manipulacion);
			msg.getParameterSet(SFipa.RECEIVERS).addValue(sender);
            sendMessage(msg);
        }
       
        getBeliefbase().getBelief("Inversores").setFact(inversores);
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
        getBeliefbase().getBelief("cambiar_turno").setFact(false);

        
    }
}