package inversor.plans;
import inversor.beliefs.*;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import java.util.HashMap;
import jadex.adapter.fipa.AgentIdentifier;
import ontology.predicados.Venta_accion_exitosa;
import ontology.conceptos.Dinero;
import ontology.conceptos.Carta_acciones;
import inversor.beliefs.*;
import java.util.Iterator;

import java.util.Random;

public class VentaAccionExitosaPlan extends Plan {

    public void body()
    {
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Venta_accion_exitosa venta_exitosa = (Venta_accion_exitosa)  respuestaTablero.getContent();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        MisCartasAcciones mis_cartas = (MisCartasAcciones) getBeliefbase().getBelief("MisCartasAcciones").getFact();
        Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();

        //Borrar la accion del inversor
        for(Iterator<Carta_acciones> iterator = pilas_cartas.getMapaCartaInversor().keySet().iterator(); iterator.hasNext();){
            Carta_acciones key = iterator.next();
            for (Carta_acciones accion: venta_exitosa.getLista()){  
                if(accion.getId() == key.getId())
                    iterator.remove();         
            }
        }
        //Borrar la carta de accion en caso de que sea mia
        AgentIdentifier id = new AgentIdentifier(getAgentName(), true); 
        if (id.equals(venta_exitosa.getQuien())){
            for (int i = 0; i<mis_cartas.getMisCartas().size(); i++) {
                for (Carta_acciones accion: venta_exitosa.getLista()){  
                    if(mis_cartas.getMisCartas().get(i).getId()== accion.getId())
                        mis_cartas.getMisCartas().remove(i);        
                }
            }
        }
        //Sumar el dinero coorespondiente a la venta al inversor
        inversores.sumarDinero(venta_exitosa.getQuien(), venta_exitosa.getCuanto());
        
        getBeliefbase().getBelief("Inversores").setFact(inversores);
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
        getBeliefbase().getBelief("MisCartasAcciones").setFact(mis_cartas);
        getBeliefbase().getBelief("cambiar_turno").setFact(true);   
        
    }
}