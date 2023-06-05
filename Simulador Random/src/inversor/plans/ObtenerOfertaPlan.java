package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import ontology.predicados.Cartas_acciones_repartidas;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Empresa;
import ontology.conceptos.Pila_subasta;
import ontology.acciones.Colocar_cartas_acciones;
import inversor.beliefs.*;

import java.util.Random;

public class ObtenerOfertaPlan extends Plan {

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
        getBeliefbase().getBelief("cambiar_turno").setFact(false);
        AgentDescription[] result	= (AgentDescription[])ft.getParameterSet("result").getValues();
        if (result.length>0){
            
            IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
            Cartas_acciones_repartidas cartas_repartidas = (Cartas_acciones_repartidas) respuestaTablero.getContent();
            PilasCartasAccion pilas_cartas= (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
            MisCartasAcciones mis_cartas= (MisCartasAcciones) getBeliefbase().getBelief("MisCartasAcciones").getFact();
            Empresas empresas= (Empresas) getBeliefbase().getBelief("Empresas").getFact();
            HashMap <Carta_acciones, Pila_subasta> reparto_inicial =  cartas_repartidas.getMapa();
            AgentIdentifier id = new AgentIdentifier(getAgentName(), true);
            for (Carta_acciones key: cartas_repartidas.getMapa().keySet()){  
                pilas_cartas.addCartaPila(key, cartas_repartidas.getMapa().get(key));
            }
            
            //Decidir que carta poner publica y cual privada
            Carta_acciones carta_1 = cartas_repartidas.getCarta1();
            Carta_acciones carta_2 = cartas_repartidas.getCarta2();
            Colocar_cartas_acciones colocar = new Colocar_cartas_acciones();
            colocar.setCartaOculta(carta_1);
            colocar.setCartaPublica(carta_2);
            Pila_subasta pila_publica = new Pila_subasta();
            Pila_subasta pila_privada = new Pila_subasta();
            Random random = new Random();
            int numeroPublico = random.nextInt(3) + 1;
            int numeroPrivado = random.nextInt(3) + 1;
            pila_publica.setNumero(numeroPublico);
            pila_privada.setNumero(numeroPrivado);
            colocar.setDondeCartaOculta(pila_privada);
            colocar.setDondeCartaPublica(pila_publica);
                                   
            IMessageEvent msgsend2 = createMessageEvent("Request_Generico");
            AgentIdentifier tablero = result[0].getName();
            msgsend2.getParameterSet(SFipa.RECEIVERS).addValue(tablero);
		    msgsend2.setContent(colocar);
            sendMessage(msgsend2);
            getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);  
                
        }
        else
            System.out.println("Tablero no encontrado");      
    }       
}
