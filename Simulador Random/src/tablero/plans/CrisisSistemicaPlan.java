package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.acciones.Utilizar_crisis_sistemica;
import ontology.predicados.Utilizacion_carta_crisis_sistemica_exitosa;
import ontology.predicados.Causa_fallo_crisis_sistemica;
import ontology.conceptos.Detalle_causa_fallo_estrategia_inversion;
import ontology.conceptos.Dinero;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Empresa;
import tablero.beliefs.*;
import java.util.Random;
import java.util.Iterator;

import java.util.Random;

public class CrisisSistemicaPlan extends Plan {

    public void body()
    {
        System.out.println ("CRISIS SISTEMICA TABLERO");
        //leer mensaje recibido
		IMessageEvent msgrec = (IMessageEvent) getInitialEvent();
		//guardamos el emisor
		AgentIdentifier sender= (AgentIdentifier) msgrec.getParameter(SFipa.SENDER).getValue();
		Utilizar_crisis_sistemica contenido = (Utilizar_crisis_sistemica) msgrec.getContent();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        Inversores inversores= (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        Empresas empresas= (Empresas) getBeliefbase().getBelief("Empresas").getFact();

        //Comprobar si el inversor tiene la carta y es de tipo estrategia
        boolean tiene_carta = false;
        if (contenido.getCartaEstrategia().getTipo().equals("Crisis sistemica") && inversores.getInversionPorId(sender).getTipo().equals(contenido.getCartaEstrategia().getTipo())){
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
        //Comprobar si las decisiones son correctas
        int numeroDecisiones = contenido.getMapa().size();
        int contador = 0;
        boolean decision_correcta = true;
        for (Empresa key: contenido.getMapa().keySet()){  
            for (int i = 0; i<empresas.getEmpresas().size(); i++){
                if (empresas.getEmpresas().get(i).getNombre().equals(key.getNombre())){
                    contador++;
                    if (contenido.getMapa().get(key).getDecision() != 1 && contenido.getMapa().get(key).getDecision() != -1)
                        decision_correcta = false;
                }
            }
        }
        if (numeroDecisiones != contador)
            decision_correcta = false;

        if (carta_correcta && tiene_carta && decision_correcta){
            Utilizacion_carta_crisis_sistemica_exitosa crisis_exitosa = new Utilizacion_carta_crisis_sistemica_exitosa();
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
            System.out.println ("--- El inversor: "+ Character.getNumericValue(sender.getLocalName().charAt(8)) + " ha utilizado la carta de crisis sistemica.");
            for (Empresa key: contenido.getMapa().keySet()){  
                System.out.println ("--- La empresa: "+ key.getNombre() + " va a modificar su valor en: "+ contenido.getMapa().get(key).getDecision());
            }
            //Actualizar valor de las empresas
            for (Empresa key: contenido.getMapa().keySet()){  
                for (int i = 0; i<empresas.getEmpresas().size(); i++){
                    if (empresas.getEmpresas().get(i).getNombre().equals(key.getNombre())){
                        empresas.getEmpresas().get(i).setValorAcciones(contenido.getMapa().get(key).getDecision() + empresas.getEmpresas().get(i).getValorAcciones());
                        System.out.println ("--- La empresa: "+ empresas.getEmpresas().get(i).getNombre() + " tiene un valor de: "+ empresas.getEmpresas().get(i).getValorAcciones());
                        //Si la empresa esta en bancarrota
                        if (empresas.getEmpresas().get(i).getValorAcciones() < 1){
                            System.out.println("--- Empresa en bancarrota");
                            empresas.getEmpresas().get(i).setBancarrota(true);
                            empresas.getEmpresas().get(i).setValorAcciones(5);
                            for(Iterator<Carta_acciones> iterator = pilas_cartas.getMapaCartaInversor().keySet().iterator(); iterator.hasNext();){
                                Carta_acciones carta = iterator.next();
                                if(carta.getTipo() == 1 && carta.getEmpresa().equals(empresas.getEmpresas().get(i).getNombre())) 
                                    iterator.remove();
                            }
                        }
                        //Si la empresa tiene un valor maximo
                        if (empresas.getEmpresas().get(i).getValorAcciones() > 10){
                            System.out.println("--- Empresa con maximo valor");
                            empresas.getEmpresas().get(i).setMaximo(true);
                            empresas.getEmpresas().get(i).setValorAcciones(6);
                            for (Carta_acciones carta: pilas_cartas.getMapaCartaInversor().keySet()){  
                                //Si las acciones de un inversor estan dobladas
                                if(carta.getEmpresa().equals(empresas.getEmpresas().get(i).getNombre()) && carta.getDoblada() == true){
                                    Dinero dinero = new Dinero();
                                    dinero.setCantidad(10000);
                                    inversores.sumarDinero(pilas_cartas.getMapaCartaInversor().get(carta), dinero);
                                }
                                if(carta.getEmpresa().equals(empresas.getEmpresas().get(i).getNombre()))
                                    carta.setDoblada(true);
                            } 
                        }
                    }
                }
            }
            
            crisis_exitosa.setMapa(contenido.getMapa());
            crisis_exitosa.setQuien(sender);
            crisis_exitosa.setQue(contenido.getCartaOperaciones());
                for (AgentIdentifier x : inversores.getIds()) {
                    IMessageEvent msg = createMessageEvent("Inform_Generico");
                    msg.getParameterSet(SFipa.RECEIVERS).addValue(x);
                    msg.setContent(crisis_exitosa);
                    sendMessage(msg);
                }
        }

        if (tiene_carta == false){
            //System.out.println ("El inversor no tiene la carta necesaria para utilizar la crisis sistemica.");
            //crear mensaje con predicado y concepto
			
            Detalle_causa_fallo_estrategia_inversion detalle_causa_fallo_estrategia_inversion= new Detalle_causa_fallo_estrategia_inversion();
            detalle_causa_fallo_estrategia_inversion.setDescripcion("El inversor no tiene la carta necesaria para utilizar la crisis sistemica.");

            Causa_fallo_crisis_sistemica causa_fallo_crisis_sistemica = new Causa_fallo_crisis_sistemica();
            causa_fallo_crisis_sistemica.setQue(detalle_causa_fallo_estrategia_inversion);

            //crear mensaje de failure
            IMessageEvent msg = createMessageEvent("Failure_Generico");
            msg.setContent(causa_fallo_crisis_sistemica);
			msg.getParameterSet(SFipa.RECEIVERS).addValue(sender);
            sendMessage(msg);
        }

        if (carta_correcta == false){
            //System.out.println ("La carta de operaciones no es correcta, no se puede realizar la crisis sistemica.");
            //crear mensaje con predicado y concepto
			
            Detalle_causa_fallo_estrategia_inversion detalle_causa_fallo_estrategia_inversion= new Detalle_causa_fallo_estrategia_inversion();
            detalle_causa_fallo_estrategia_inversion.setDescripcion("La carta de operaciones no es correcta, no se puede realizar la crisis sistemica.");

            Causa_fallo_crisis_sistemica causa_fallo_crisis_sistemica = new Causa_fallo_crisis_sistemica();
            causa_fallo_crisis_sistemica.setQue(detalle_causa_fallo_estrategia_inversion);

            //crear mensaje de failure
            IMessageEvent msg = createMessageEvent("Failure_Generico");
            msg.setContent(causa_fallo_crisis_sistemica);
			msg.getParameterSet(SFipa.RECEIVERS).addValue(sender);
            sendMessage(msg);
        }

        if (decision_correcta == false){
            //System.out.println ("La decision hecha no es correcta, no se puede realizar la crisis sistemica.");
            //crear mensaje con predicado y concepto
			
            Detalle_causa_fallo_estrategia_inversion detalle_causa_fallo_estrategia_inversion= new Detalle_causa_fallo_estrategia_inversion();
            detalle_causa_fallo_estrategia_inversion.setDescripcion("La decision hecha no es correcta, no se puede realizar la crisis sistemica.");

            Causa_fallo_crisis_sistemica causa_fallo_crisis_sistemica = new Causa_fallo_crisis_sistemica();
            causa_fallo_crisis_sistemica.setQue(detalle_causa_fallo_estrategia_inversion);

            //crear mensaje de failure
            IMessageEvent msg = createMessageEvent("Failure_Generico");
            msg.setContent(causa_fallo_crisis_sistemica);
			msg.getParameterSet(SFipa.RECEIVERS).addValue(sender);
            sendMessage(msg);
        }
       
        getBeliefbase().getBelief("Inversores").setFact(inversores);
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
        getBeliefbase().getBelief("Empresas").setFact(empresas);
        getBeliefbase().getBelief("cambiar_turno").setFact(false);
        
    }
}