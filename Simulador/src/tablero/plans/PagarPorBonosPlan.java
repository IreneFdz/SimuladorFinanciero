package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.predicados.Pago_por_bono_hecho;
import ontology.conceptos.Dinero;
import ontology.conceptos.Bono;
import tablero.beliefs.*;
import java.util.HashMap;
import java.util.ArrayList;
import jadex.adapter.fipa.AgentIdentifier;

public class PagarPorBonosPlan extends Plan {

    public void body()
    {
        System.out.println ("PAGAR POR BONOS");
        Bonos bonos = (Bonos) getBeliefbase().getBelief("Bonos").getFact();
        Inversores inversores= (Inversores) getBeliefbase().getBelief("Inversores").getFact();

        Pago_por_bono_hecho pago_hecho = new Pago_por_bono_hecho();
        HashMap <AgentIdentifier, Integer> inversor_numero_bonos = new HashMap <AgentIdentifier, Integer>();
        HashMap <AgentIdentifier, Dinero> inversor_dinero = new HashMap <AgentIdentifier, Dinero>();
        //Inicializar mapa: numero de bonos que tiene cada inversor
        for (AgentIdentifier x : inversores.getIds()) {
            inversor_numero_bonos.put(x, 0);
        }
        //Guardar todos los bonos que tiene un inversor
        for (Bono bono: bonos.getBonos().keySet())
            inversor_numero_bonos.put(bonos.getBonos().get(bono), inversor_numero_bonos.get(bonos.getBonos().get(bono))+1);
        
               
        // Obtener el dinero por los bonos de cada inversor
        for (AgentIdentifier id: inversor_numero_bonos.keySet()) {
            Dinero dinero = new Dinero();
            dinero.setCantidad(inversor_numero_bonos.get(id)*1000);
            System.out.println("--- El inversor "+ Character.getNumericValue(id.getLocalName().charAt(8)) + " ha recibido por los bonos "+ inversor_numero_bonos.get(id)*1000+"$");
            inversor_dinero.put(id, dinero);
            inversores.sumarDinero(id, dinero);
        }
        pago_hecho.setMapa(inversor_dinero);
        for (AgentIdentifier x : inversores.getIds()) {
            IMessageEvent msg = createMessageEvent("Inform_Generico");
            msg.getParameterSet(SFipa.RECEIVERS).addValue(x);
            msg.setContent(pago_hecho);
            sendMessage(msg);
        }
        getBeliefbase().getBelief("Inversores").setFact(inversores);  
        getBeliefbase().getBelief("cambiar_fase").setFact(true);  
    }
}