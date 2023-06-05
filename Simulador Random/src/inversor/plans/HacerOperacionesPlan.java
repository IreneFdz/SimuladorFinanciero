package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.acciones.Utilizar_carta_operaciones;
import ontology.conceptos.Empresa;
import ontology.conceptos.Carta_acciones;
import inversor.beliefs.*;
import java.util.HashMap;
import java.util.Collections;
import java.util.ArrayList;


import java.util.Random;

public class HacerOperacionesPlan extends Plan {

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
            Carta_estrategia_inversion carta_estrategia = (Carta_estrategia_inversion) getBeliefbase().getBelief("Carta_estrategia_inversion").getFact();
            MisCartasAcciones mis_cartas = (MisCartasAcciones) getBeliefbase().getBelief("MisCartasAcciones").getFact();
            Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
            Empresas empresas= (Empresas) getBeliefbase().getBelief("Empresas").getFact();
            InformacionGeneral informacion= (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();
            PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
            boolean tengo_carta = false;
            int tipo_operaciones = 0;

            for (int i = 0; i< mis_cartas.getMisCartas().size(); i++) {
                if(mis_cartas.getMisCartas().get(i).getTipo() == 2){
                    tengo_carta = true;
                }
            }
            AgentIdentifier id = new AgentIdentifier(getAgentName(), true);
           
            Random random = new Random();          
            if (tengo_carta){
                HashMap <Carta_acciones, Empresa> mapa = new HashMap<Carta_acciones, Empresa>();
                boolean hacer_operaciones = true;
                int numero_cartas = 0;
                for (int i = 0; i< mis_cartas.getMisCartas().size(); i++) {
                    if(mis_cartas.getMisCartas().get(i).getTipo() == 2){
                        tipo_operaciones = mis_cartas.getMisCartas().get(i).getTipoOperaciones();
                        
                        if (hacer_operaciones){
                            int sizeLista = empresas.getEmpresas().size();
                            int indiceAleatorio = random.nextInt(sizeLista);
                            Empresa empresaAleatoria = empresas.getEmpresas().get(indiceAleatorio);
                            mapa.put(mis_cartas.getMisCartas().get(i), empresaAleatoria);
                            numero_cartas++;   
                        }
                    }
                }
                Utilizar_carta_operaciones utilizar_carta = new Utilizar_carta_operaciones();
                utilizar_carta.setMapa(mapa);
                IMessageEvent msgsend = createMessageEvent("Request_Generico");
                AgentIdentifier tablero = result[0].getName();
                msgsend.getParameterSet(SFipa.RECEIVERS).addValue(tablero);
                msgsend.setContent(utilizar_carta);
                sendMessage(msgsend);
            }
            if (tengo_carta == false)
                 getBeliefbase().getBelief("manipulacion").setFact(true); 
        }
        else
                System.out.println("Tablero no encontrado");
    }
}