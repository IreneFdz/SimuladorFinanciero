package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.acciones.Utilizar_venta_en_corto;
import ontology.conceptos.Carta_acciones;
import inversor.beliefs.*;

import java.util.Random;

public class VentaEnCortoPlan extends Plan {

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
            //Comprobar si tengo carta de venta en corto
            boolean tengo_carta = false;
            if (carta_estrategia.getCartaEstrategia().getTipo().equals("Venta en corto descubierta")){
                if (carta_estrategia.getCartaEstrategia().getBorrada() == false)
                    tengo_carta = true;
            }                                           
            boolean tengo_carta_operaciones = false;
            for (int i = 0; i< mis_cartas.getMisCartas().size(); i++) {
                if(mis_cartas.getMisCartas().get(i).getTipo() == 2){
                    tengo_carta_operaciones= true;
                }
            }            
            //USAR LA CARTA SIEMPRE QUE SEA POSIBLE
            boolean usar_venta_en_corto = true;
            if(tengo_carta && tengo_carta_operaciones){
                Carta_acciones carta = new Carta_acciones();
                if(usar_venta_en_corto){
                    for (int i = 0; i< mis_cartas.getMisCartas().size(); i++) {
                        if(mis_cartas.getMisCartas().get(i).getTipo() == 2){
                            carta = mis_cartas.getMisCartas().get(i);
                            break;
                        }
                    }
                    Utilizar_venta_en_corto utilizar_venta = new Utilizar_venta_en_corto();
                    utilizar_venta.setCartaEstrategia(carta_estrategia.getCartaEstrategia());
                    utilizar_venta.setCartaOperaciones(carta);
                    IMessageEvent msgsend = createMessageEvent("Request_Generico");
                    AgentIdentifier tablero = result[0].getName();
                    msgsend.getParameterSet(SFipa.RECEIVERS).addValue(tablero);
                    msgsend.setContent(utilizar_venta);
                    sendMessage(msgsend);    
                }
            } 
            if (tengo_carta && (tengo_carta_operaciones == false || usar_venta_en_corto == false)){
                getBeliefbase().getBelief("cambiar_turno").setFact(true); 
                getBeliefbase().getBelief("mi_turno").setFact(false);  
                getBeliefbase().getBelief("venta_en_corto").setFact(false);
            }
            if (!tengo_carta)
                getBeliefbase().getBelief("crisis_sistemica").setFact(true);   
        }
        else
            System.out.println("Tablero no encontrado");  
        
    }
}