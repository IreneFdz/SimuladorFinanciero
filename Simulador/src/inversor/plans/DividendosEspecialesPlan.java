package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.acciones.Utilizar_dividendos_especiales;
import ontology.conceptos.Empresa;
import ontology.conceptos.Carta_acciones;
import inversor.beliefs.*;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Random;

public class DividendosEspecialesPlan extends Plan {

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
            InformacionGeneral informacion= (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();
            Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
            PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
            MisCartasAcciones mis_cartas = (MisCartasAcciones) getBeliefbase().getBelief("MisCartasAcciones").getFact();
            Empresas empresas= (Empresas) getBeliefbase().getBelief("Empresas").getFact();
            //Comprobar si tengo carta de dividendos especiales
            boolean tengo_carta = false;
            if (carta_estrategia.getCartaEstrategia().getTipo().equals("Dividendos especiales")){
                if (carta_estrategia.getCartaEstrategia().getBorrada() == false)
                    tengo_carta = true;
            }
            boolean usar_dividendos_especiales = true;
            if(tengo_carta){
                // Decidir si usar la carta.
                // Obtener una valoracion de cada empresa que es igual a las acciones del inversor menos las acciones de los otro inversores
                AgentIdentifier id = new AgentIdentifier(getAgentName(), true);
                HashMap <Empresa, Integer> valoracion_empresas = new HashMap <Empresa, Integer>();
                AgentIdentifier principal_rival = id;
                int dinero_rival = 0;
                for (AgentIdentifier x : inversores.getIds()) {
                    int dinero_inversor = inversores.getDineroPorId(x).getCantidad();   
                    if (dinero_inversor > dinero_rival && !x.equals(id)){
                        dinero_rival = dinero_inversor;
                        principal_rival = x;
                    }
                }
                ArrayList <Integer> valoraciones = new ArrayList <Integer>();
                for (int i = 0; i< empresas.getEmpresas().size(); i++){
                    int valoracionEmpresa = 0;
                    for (Carta_acciones carta : pilas_cartas.getMapaCartaInversor().keySet()) {
                        if (carta.getTipo() == 1 && empresas.getEmpresas().get(i).getNombre().equals(carta.getEmpresa())){
                            if (pilas_cartas.getMapaCartaInversor().get(carta).equals(principal_rival))
                                valoracionEmpresa--;
                        }
                    }
                    for (Carta_acciones carta : mis_cartas.getMisCartas()) {
                        if (carta.getTipo() == 1 && empresas.getEmpresas().get(i).getNombre().equals(carta.getEmpresa()))
                            valoracionEmpresa++;
                    }
                    valoracion_empresas.put(empresas.getEmpresas().get(i), valoracionEmpresa);
                    valoraciones.add(valoracionEmpresa);
                }
                //Ordenar las valoraciones de mayor a menor
                Collections.sort(valoraciones, Collections.reverseOrder());
                int limite_beneficios = 3000;
                int beneficios = valoraciones.get(0)*1000;

                if (beneficios >= limite_beneficios)
                    usar_dividendos_especiales = true;
                if (informacion.getRonda() == 7 && beneficios > 0)
                    usar_dividendos_especiales = true;
                
                if(usar_dividendos_especiales){
                    // Elegir empresa.
                    boolean elegida = false;
                    Empresa empresa_elegida = new Empresa();
                    for (Integer x : valoraciones) {
                        if (elegida == false){
                            for (Empresa empresa : valoracion_empresas.keySet()) {
                                if (valoracion_empresas.get(empresa) == x){
                                    empresa_elegida = empresa;
                                    elegida = true;
                                    break;
                                }
                            }
                        }       
                    }
                    Utilizar_dividendos_especiales utilizar_dividendos = new Utilizar_dividendos_especiales();
                    utilizar_dividendos.setCarta(carta_estrategia.getCartaEstrategia());
                    utilizar_dividendos.setEmpresa(empresa_elegida);
                    IMessageEvent msgsend = createMessageEvent("Request_Generico");
                    AgentIdentifier tablero = result[0].getName();
                    msgsend.getParameterSet(SFipa.RECEIVERS).addValue(tablero);
                    msgsend.setContent(utilizar_dividendos);
                    sendMessage(msgsend);     
                }
            }
            if (tengo_carta && usar_dividendos_especiales == false){
                getBeliefbase().getBelief("cambiar_turno").setFact(true);
                getBeliefbase().getBelief("mi_turno").setFact(false);
                getBeliefbase().getBelief("dividendos_especiales").setFact(false); 
            } 
        }
        else
            System.out.println("Tablero no encontrado");  
        
        
        
    }
}