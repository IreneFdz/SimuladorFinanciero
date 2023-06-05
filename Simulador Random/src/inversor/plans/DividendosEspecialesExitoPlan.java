package inversor.plans;
import inversor.beliefs.*;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import java.util.HashMap;
import jadex.adapter.fipa.AgentIdentifier;
import ontology.predicados.Utilizacion_carta_dividendos_especiales_exitosa;
import ontology.conceptos.Dinero;
import ontology.conceptos.Carta_acciones;
import inversor.beliefs.*;
import java.util.Iterator;

import java.util.Random;

public class DividendosEspecialesExitoPlan extends Plan {

    public void body()
    {
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Utilizacion_carta_dividendos_especiales_exitosa dividendos_exitosa = (Utilizacion_carta_dividendos_especiales_exitosa)  respuestaTablero.getContent();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        Carta_estrategia_inversion mi_carta = (Carta_estrategia_inversion) getBeliefbase().getBelief("Carta_estrategia_inversion").getFact();
        MisCartasAcciones mis_cartas = (MisCartasAcciones) getBeliefbase().getBelief("MisCartasAcciones").getFact();
        Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();

        //Actualizar mapa de cartas
        for (Carta_acciones carta: dividendos_exitosa.getMapa().keySet()){  
            pilas_cartas.addCartaInversor(carta, dividendos_exitosa.getMapa().get(carta));
        }
        //Borrar la carta de estrategia
        AgentIdentifier id = new AgentIdentifier(getAgentName(), true); 
        if (id.equals(dividendos_exitosa.getQuien()))
            mi_carta.getCartaEstrategia().setBorrada(true);
        
        //Pagar 1000 de dividendos por accion a los inversores con acciones de esa empresa
        HashMap<AgentIdentifier, Integer> sumar_inversor_dinero  = new HashMap<AgentIdentifier, Integer>();
        for (AgentIdentifier x : inversores.getIds()) {
            sumar_inversor_dinero.put(x, 0);         
        }
        
        for (Carta_acciones carta: pilas_cartas.getMapaCartaInversor().keySet()){  
            if (carta.getTipo() == 1){
                if(carta.getEmpresa().equals(dividendos_exitosa.getQue().getNombre())){
                    AgentIdentifier agent = pilas_cartas.getMapaCartaInversor().get(carta); 
                    sumar_inversor_dinero.put(agent, sumar_inversor_dinero.get(agent)+1000);
                }
            }
        } 
        
        for (AgentIdentifier x : inversores.getIds()) {
            Dinero dinero = new Dinero();
            dinero.setCantidad(sumar_inversor_dinero.get(x));
            inversores.sumarDinero(x, dinero);
        }

        getBeliefbase().getBelief("Inversores").setFact(inversores);
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
        getBeliefbase().getBelief("Carta_estrategia_inversion").setFact(mi_carta); 
        getBeliefbase().getBelief("cambiar_turno").setFact(true);
        getBeliefbase().getBelief("mi_turno").setFact(false);
        getBeliefbase().getBelief("dividendos_especiales").setFact(false); 
    }
}