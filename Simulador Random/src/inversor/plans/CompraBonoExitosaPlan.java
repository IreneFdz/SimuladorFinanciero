package inversor.plans;
import inversor.beliefs.*;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import java.util.HashMap;
import jadex.adapter.fipa.AgentIdentifier;
import ontology.predicados.Compra_bono_exitosa;
import ontology.conceptos.Dinero;
import inversor.beliefs.*;

import java.util.Random;

public class CompraBonoExitosaPlan extends Plan {

    public void body()
    {
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Compra_bono_exitosa compra_exitosa = (Compra_bono_exitosa)  respuestaTablero.getContent();
        Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        int dinero_actual = inversores.getDineroPorId(compra_exitosa.getQuien()).getCantidad() - (compra_exitosa.getQue().getValor() * compra_exitosa.getCuantos().getCuanto());
        Dinero dinero = new Dinero();
        dinero.setCantidad(dinero_actual);
        inversores.modificarDineroPorId(compra_exitosa.getQuien(), dinero);
        getBeliefbase().getBelief("Inversores").setFact(inversores);
        getBeliefbase().getBelief("posible_comprar_bono").setFact(false);

        AgentIdentifier id = new AgentIdentifier(getAgentName(), true); 
        if (id.equals(inversores.getIds().get(2)))
            getBeliefbase().getBelief("cambiar_turno").setFact(true);       
    }
}