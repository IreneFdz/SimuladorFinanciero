package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import java.util.Random;
import ontology.acciones.Pedir_cartas_acciones;
import inversor.beliefs.*;

import java.util.Random;

public class PedirCartasAccionesPlan extends Plan {

    public void body()
    {
        ServiceDescription sd = new ServiceDescription();
        sd.setName("tablero");
        AgentDescription dfadesc = new AgentDescription();
        dfadesc.addService(sd);
        SearchConstraints	sc	= new SearchConstraints();
        sc.setMaxResults(-1);
        IGoal ft = createGoal("df_search");
        ft.getParameter("description").setValue(dfadesc);
        ft.getParameter("constraints").setValue(sc);
        dispatchSubgoalAndWait(ft);
        AgentDescription[] result	= (AgentDescription[])ft.getParameterSet("result").getValues();
        if (result.length>0){
            
            Pedir_cartas_acciones pedir_cartas = new Pedir_cartas_acciones();
            IMessageEvent msgsend = createMessageEvent("Request_Generico");
            AgentIdentifier tablero = result[0].getName();
            msgsend.getParameterSet(SFipa.RECEIVERS).addValue(tablero);
            msgsend.setContent(pedir_cartas);
            sendMessage(msgsend); 
            getBeliefbase().getBelief("mi_turno").setFact(false);  
               
        }
        else
            System.out.println("Tablero no encontrado");      
    }      
}
