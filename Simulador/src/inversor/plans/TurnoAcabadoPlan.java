package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import inversor.beliefs.*;
import java.util.ArrayList;
import ontology.predicados.Turno_terminado;


import java.util.Random;

public class TurnoAcabadoPlan extends Plan {

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
            Turno_terminado turno_terminado = new Turno_terminado();
            IMessageEvent msgsend = createMessageEvent("Inform_Generico");
            AgentIdentifier tablero = result[0].getName();
            msgsend.getParameterSet(SFipa.RECEIVERS).addValue(tablero);
            msgsend.setContent(turno_terminado);
            sendMessage(msgsend);   
        }
        else
            System.out.println("Tablero no encontrado");  
        
        getBeliefbase().getBelief("cambiar_turno").setFact(false); 
    }
}
