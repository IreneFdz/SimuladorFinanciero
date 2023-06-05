package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import jadex.adapter.fipa.AgentIdentifier;
import inversor.beliefs.*;
import ontology.acciones.Comprar_bono;
import ontology.conceptos.Dinero;
import ontology.conceptos.Cantidad;
import ontology.conceptos.Bono;

import java.util.Random;

public class ComprarBonoPlan extends Plan {

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
                //Parametros bono
                InformacionGeneral informacion= (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();
                int valorBono = 1000;
                
                //Decidir la cantidad de bonos de forma random
                Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
                Dinero dinero = new Dinero();
                AgentIdentifier id = new AgentIdentifier(getAgentName(), true);
                dinero = inversores.getDineroPorId(id);
                int cantidadBonos = 0;
                Random random = new Random();

                if(1000 <= dinero.getCantidad()){
                    cantidadBonos = random.nextInt(2);
                }
                if(2000 <= dinero.getCantidad()){
                    cantidadBonos = random.nextInt(3);                   
                }
                if(3000 <= dinero.getCantidad()){
                    cantidadBonos = random.nextInt(4);                    
                }               
                
                if (valorBono * cantidadBonos <= dinero.getCantidad()){
                    Comprar_bono comprar_bono = new Comprar_bono();
                    Bono bono = new Bono();
                    bono.setValor(valorBono);
                    Cantidad cantidad = new Cantidad();
                    cantidad.setCuanto(cantidadBonos);
                    comprar_bono.setBono(bono);
                    comprar_bono.setCuantos(cantidad);
                    IMessageEvent msgsend = createMessageEvent("Request_Generico");
                    AgentIdentifier tablero = result[0].getName();
                    msgsend.getParameterSet(SFipa.RECEIVERS).addValue(tablero);
                    msgsend.setContent(comprar_bono);
                    sendMessage(msgsend);
                }
                getBeliefbase().getBelief("mi_turno").setFact(false);
            }
            else
                System.out.println("Tablero no encontrado");
        
    }
}