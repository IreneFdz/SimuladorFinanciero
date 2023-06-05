package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.conceptos.Pila_subasta;
import ontology.conceptos.Dinero;
import ontology.acciones.Pujar;
import inversor.beliefs.*;
import jadex.adapter.fipa.AgentIdentifier;
import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Empresa;

import java.util.Random;

public class PujarPlan extends Plan {

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
        getBeliefbase().getBelief("mi_turno").setFact(false);
        AgentDescription[] result	= (AgentDescription[])ft.getParameterSet("result").getValues();
        if (result.length>0)
        {
            PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
            Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
            Empresas empresas= (Empresas) getBeliefbase().getBelief("Empresas").getFact();
            MisCartasAcciones mis_cartas= (MisCartasAcciones) getBeliefbase().getBelief("MisCartasAcciones").getFact();
            AgentIdentifier id = new AgentIdentifier(getAgentName(), true);
            //Decidir la pila por la que pujar
            Dinero dinero_inversor = new Dinero();
            dinero_inversor = inversores.getDineroPorId(id);
            int dinero_disponible = dinero_inversor.getCantidad();

            Random random = new Random();
            int numeroAleatorio = random.nextInt(dinero_disponible / 1000 + 1);
            
            Pila_subasta pila = new Pila_subasta();
            int numeroPila = Integer.parseInt(Character.toString(id.getLocalName().charAt(8)));

            pila.setNumero(numeroPila);
            //Decidir cuanto pagar por la pila
            int precio = numeroAleatorio * 1000;
            Dinero dinero = new Dinero();
            dinero.setCantidad(precio);
            
            Pujar pujar = new Pujar();
            pujar.setPila(pila);
            pujar.setDinero(dinero);
            IMessageEvent msgsend = createMessageEvent("Request_Generico");
            AgentIdentifier tablero = result[0].getName();
            msgsend.getParameterSet(SFipa.RECEIVERS).addValue(tablero);
            msgsend.setContent(pujar);
            sendMessage(msgsend);
        }
        else
            System.out.println("Tablero no encontrado");   
    } 
}