package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.predicados.Cartas_inversion_repartidas;
import inversor.beliefs.*;

import java.util.Random;

public class RepartirCartasInversionPlan extends Plan {

    public void body()
    {
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Cartas_inversion_repartidas carta_repartida = (Cartas_inversion_repartidas)  respuestaTablero.getContent();
        Carta_estrategia_inversion carta = new Carta_estrategia_inversion();
        carta.setCartaEstrategia(carta_repartida.getCarta());
        getBeliefbase().getBelief("Carta_estrategia_inversion").setFact(carta);   
    }
}