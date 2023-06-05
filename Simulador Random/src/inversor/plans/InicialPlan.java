package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.adapter.fipa.AgentIdentifier;
import jadex.runtime.IGoal;
import jadex.runtime.IMessageEvent;
import jadex.runtime.Plan;
import ontology.acciones.Participar_en_simulacion;

public class InicialPlan extends Plan {

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
            if (result.length>0)
            {
				Participar_en_simulacion participar_en_simulacion = new Participar_en_simulacion();
                IMessageEvent msgsend = createMessageEvent("Request_Generico");
                AgentIdentifier tablero = result[0].getName();
                msgsend.getParameterSet(SFipa.RECEIVERS).addValue(tablero);
				msgsend.setContent(participar_en_simulacion);
                sendMessage(msgsend);
            }
            else
                System.out.println("Tablero no encontrado");
        
    }
}