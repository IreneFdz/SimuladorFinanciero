package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.predicados.Pago_por_bono_hecho;
import ontology.conceptos.Dinero;
import inversor.beliefs.*;
import java.util.HashMap;
import jadex.adapter.fipa.AgentIdentifier;

public class PagarPorBonosPlan extends Plan {

    public void body()
    {
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Pago_por_bono_hecho pago_hecho = (Pago_por_bono_hecho)  respuestaTablero.getContent();
        Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();

        // Obtener el dinero por los bonos de cada inversor
        for (AgentIdentifier id: pago_hecho.getMapa().keySet()) 
            inversores.sumarDinero(id, pago_hecho.getMapa().get(id));
                           
        getBeliefbase().getBelief("Inversores").setFact(inversores);
        
    }
}