package inversor.plans;
import inversor.beliefs.*;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import java.util.HashMap;
import jadex.adapter.fipa.AgentIdentifier;
import ontology.predicados.Utilizacion_manipulacion_exitosa;
import ontology.conceptos.Dinero;
import ontology.conceptos.Carta_acciones;
import inversor.beliefs.*;
import java.util.Iterator;

import java.util.Random;

public class ManipulacionExitosaPlan extends Plan {

    public void body()
    {
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Utilizacion_manipulacion_exitosa manipulacion_exitosa = (Utilizacion_manipulacion_exitosa)  respuestaTablero.getContent();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        Carta_estrategia_inversion mi_carta = (Carta_estrategia_inversion) getBeliefbase().getBelief("Carta_estrategia_inversion").getFact();
        MisCartasAcciones mis_cartas = (MisCartasAcciones) getBeliefbase().getBelief("MisCartasAcciones").getFact();
        Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();

              
        //Borrar las acciones del inversor
        for (int i = 0; i< manipulacion_exitosa.getLista().size(); i++){
            for(Iterator<Carta_acciones> iterator = pilas_cartas.getMapaCartaInversor().keySet().iterator(); iterator.hasNext();){
                Carta_acciones key = iterator.next();
                if(manipulacion_exitosa.getLista().get(i).getId() == key.getId()){
                    iterator.remove();
                }
            }
        }
        //Borrar las cartas de accion y la carta de estrategia
        AgentIdentifier id = new AgentIdentifier(getAgentName(), true); 
        if (id.equals(manipulacion_exitosa.getQuien())){
            for (int i = 0; i<manipulacion_exitosa.getLista().size(); i++) {
                mis_cartas.borrarCartaPorId(manipulacion_exitosa.getLista().get(i).getId());
            }
            mi_carta.getCartaEstrategia().setBorrada(true);
        }
        //Sumar el dinero coorespondiente a la venta al inversor
        Dinero dinero = new Dinero();
        dinero.setCantidad(manipulacion_exitosa.getLista().size()*8000);
        inversores.sumarDinero(manipulacion_exitosa.getQuien(), dinero);
        
        getBeliefbase().getBelief("Inversores").setFact(inversores);
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
        getBeliefbase().getBelief("MisCartasAcciones").setFact(mis_cartas);
        getBeliefbase().getBelief("Carta_estrategia_inversion").setFact(mi_carta);
        getBeliefbase().getBelief("manipulacion").setFact(true);
        getBeliefbase().getBelief("cambiar_turno").setFact(true);
        getBeliefbase().getBelief("mi_turno").setFact(false);
        
    }
}