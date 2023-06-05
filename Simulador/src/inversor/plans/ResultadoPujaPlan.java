package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.predicados.Puja_terminada;
import ontology.conceptos.Dinero;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Pila_subasta;
import inversor.beliefs.*;
import java.util.ArrayList;
import jadex.adapter.fipa.AgentIdentifier;


import java.util.Random;

public class ResultadoPujaPlan extends Plan {

    public void body()
    {
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Puja_terminada puja_terminada = (Puja_terminada)  respuestaTablero.getContent();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        MisCartasAcciones mis_cartas = (MisCartasAcciones) getBeliefbase().getBelief("MisCartasAcciones").getFact();
        Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        AgentIdentifier id = new AgentIdentifier(getAgentName(), true);
        pilas_cartas.setMapaPilaInversor(puja_terminada.getMapa());             
        for (Carta_acciones carta : puja_terminada.getLista()) {
            mis_cartas.addCarta(carta);
            //System.out.println("--- El inversor: "+ Character.getNumericValue(id.getLocalName().charAt(8))+ " tiene la carta: "+carta.getId()+" de tipo: "+carta.getTipo());
        }
        Dinero dinero = new Dinero();
        for (Pila_subasta key: pilas_cartas.getMapaPilaInversor().keySet()){  
                dinero.setCantidad(key.getPrecio()); 
                inversores.restarDinero(pilas_cartas.getMapaPilaInversor().get(key), dinero);
                //System.out.println("Dinero de "+pilas_cartas.getMapaPilaInversor().get(key)+" : "+inversores.getDineroPorId(pilas_cartas.getMapaPilaInversor().get(key)).getCantidad());
		}
        pilas_cartas.unirMapas();
        getBeliefbase().getBelief("MisCartasAcciones").setFact(mis_cartas);
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
        getBeliefbase().getBelief("Inversores").setFact(inversores);    
             
    }
}