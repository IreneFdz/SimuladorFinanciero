package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.predicados.Puja_terminada;
import ontology.conceptos.Dinero;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Pila_subasta;
import tablero.beliefs.*;
import java.util.ArrayList;

import java.util.Random;

public class ResultadoPujaPlan extends Plan {

    public void body()
    {
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        Inversores inversores= (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        InformacionGeneral informacion= (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();
        Puja_terminada puja_terminada = new Puja_terminada();
        puja_terminada.setMapa(pilas_cartas.getMapaPilaInversor());
        boolean puja_final = true;
        for (AgentIdentifier i : inversores.getIds()) {
            boolean inversor_encontrado = false;
            for (Pila_subasta key: pilas_cartas.getMapaPilaInversor().keySet()){ 
                if(pilas_cartas.getMapaPilaInversor().get(key).equals(i)){
                    inversor_encontrado = true;
                }
		    }
            if (inversor_encontrado == false)
                puja_final = false;
		}
        if (puja_final == false){
            informacion.setRepetirPuja(true);
            getBeliefbase().getBelief("cambiar_turno").setFact(true);
        }
        if (puja_final){
            informacion.setRepetirPuja(false);
            getBeliefbase().getBelief("puja_terminada").setFact(false);
            System.out.println ("RESULTADO DE LA PUJA");
            for (Pila_subasta key: pilas_cartas.getMapaPilaInversor().keySet()){ 
                for (Pila_subasta pila: pilas_cartas.getPujasMinimas().keySet()){ 
                    if(pila.getNumero() == key.getNumero()){
                        int precio_puja = pilas_cartas.getPujasMinimas().get(pila) - 1000;
                        System.out.println("--- El inversor: "+ Character.getNumericValue(pilas_cartas.getMapaPilaInversor().get(key).getLocalName().charAt(8))+ " ha conseguido la pila: "+
                            pila.getNumero()+" a un precio de: "+precio_puja+"$");
                    }
                } 
		    }
            System.out.println ("CARTAS DE CADA JUGADOR");
            for (Carta_acciones carta: pilas_cartas.getMapaInicial().keySet()){  
                pilas_cartas.addCartaPila(carta, pilas_cartas.getMapaInicial().get(carta));
            } 
            for (AgentIdentifier i : inversores.getIds()) {
                ArrayList<Carta_acciones> lista_cartas = new ArrayList<Carta_acciones>();
                for (Pila_subasta key: pilas_cartas.getMapaPilaInversor().keySet()){  
                    if(pilas_cartas.getMapaPilaInversor().get(key).equals(i)){
                        for (int x = 0; x < pilas_cartas.getCartasPila(key).size(); x++){
                            lista_cartas.add(pilas_cartas.getCartasPila(key).get(x));
                            if (pilas_cartas.getCartasPila(key).get(x).getTipo() == 1)
                                System.out.println("--- El inversor: "+ Character.getNumericValue(i.getLocalName().charAt(8))+ " tiene la carta: "+pilas_cartas.getCartasPila(key).get(x).getId()+" de tipo: 1 de la empresa "+pilas_cartas.getCartasPila(key).get(x).getEmpresa());
                            else
                                System.out.println("--- El inversor: "+ Character.getNumericValue(i.getLocalName().charAt(8))+ " tiene la carta: "+pilas_cartas.getCartasPila(key).get(x).getId()+" de tipo: "+pilas_cartas.getCartasPila(key).get(x).getTipo());
                        }   
                    }
		        }
                puja_terminada.setLista(lista_cartas);
                IMessageEvent msg = createMessageEvent("Inform_Generico");
                msg.getParameterSet(SFipa.RECEIVERS).addValue(i);
                msg.setContent(puja_terminada);
                sendMessage(msg);
            }
            Dinero dinero = new Dinero();
            for (Pila_subasta key: pilas_cartas.getMapaPilaInversor().keySet()){  
                    dinero.setCantidad(key.getPrecio()); 
                    inversores.restarDinero(pilas_cartas.getMapaPilaInversor().get(key), dinero);
            }
            pilas_cartas.unirMapas(); 
            getBeliefbase().getBelief("pago_comisiones").setFact(true);
            getBeliefbase().getBelief("cambiar_turno").setFact(false);
        }
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
        getBeliefbase().getBelief("Inversores").setFact(inversores);
        getBeliefbase().getBelief("puja_terminada").setFact(false);
                        
    }
}