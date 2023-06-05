package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import inversor.beliefs.*;
import java.util.HashMap;
import java.util.ArrayList;
import ontology.acciones.Utilizar_crisis_sistemica;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Decision_crisis_sistemica;
import ontology.conceptos.Empresa;
import java.util.Collections;


import java.util.Random;

public class CrisisSistemicaPlan extends Plan {

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
            InformacionGeneral informacion= (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();
            PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
            Empresas empresas= (Empresas) getBeliefbase().getBelief("Empresas").getFact();
            //Comprobar si tengo carta de crisis sistemica
            boolean tengo_carta = false;
            if (carta_estrategia.getCartaEstrategia().getTipo().equals("Crisis sistemica")){
                if (carta_estrategia.getCartaEstrategia().getBorrada() == false)
                    tengo_carta = true;
            }
            boolean tengo_carta_operaciones = false;
            for (int i = 0; i< mis_cartas.getMisCartas().size(); i++) {
                if(mis_cartas.getMisCartas().get(i).getTipo() == 2){
                    tengo_carta_operaciones= true;
                }
            }
            boolean usar_crisis_sistemica = false;
            if(tengo_carta && tengo_carta_operaciones){
                //Obtener la valoracion de la empresa teniendo en cuenta las cartas de acciones del resto de inversores y las mias
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
                for (int i = 0; i< empresas.getEmpresas().size(); i++){
                    int valoracionEmpresa = 0;
                    for (Carta_acciones carta : pilas_cartas.getMapaCartaInversor().keySet()) {
                        if (carta.getTipo() == 1 && empresas.getEmpresas().get(i).getNombre().equals(carta.getEmpresa())){
                            if (pilas_cartas.getMapaCartaInversor().get(carta).equals(principal_rival)){
                                valoracionEmpresa--;
                            }  
                        }
                    }
                    for (Carta_acciones carta : mis_cartas.getMisCartas()) {
                        if (carta.getTipo() == 1 && empresas.getEmpresas().get(i).getNombre().equals(carta.getEmpresa())){
                            valoracionEmpresa++;
                        }
                    }
                    valoracion_empresas.put(empresas.getEmpresas().get(i), valoracionEmpresa);
                }
                //Cambiar a valor absoluto las valoraciones
                ArrayList <Integer> valoracion_absoluta = new ArrayList <Integer>();
                for (Empresa x : valoracion_empresas.keySet()){
                    int valor = Math.abs(valoracion_empresas.get(x)) * 1000;
                    valoracion_absoluta.add(valor);
                }
                Collections.sort(valoracion_absoluta, Collections.reverseOrder());

                int limite_beneficios = 3000;
                int beneficios = 0;
                int contador = 0;
                for (Integer x : valoracion_absoluta) {
                    if (contador < 3){
                        beneficios += x;
                        contador++;
                    }
                }
                if (beneficios >= limite_beneficios)
                    usar_crisis_sistemica = true;
                if (informacion.getRonda() == 7)
                    usar_crisis_sistemica = true;

                if(usar_crisis_sistemica){
                    // Obtener carta de operaciones 
                    Carta_acciones carta = new Carta_acciones();
                    for (int i = 0; i< mis_cartas.getMisCartas().size(); i++) {
                        if(mis_cartas.getMisCartas().get(i).getTipo() == 2){
                            carta = mis_cartas.getMisCartas().get(i);
                            break;
                        }
                    }
                    contador = 0;
                    // Elegir la decision de la empresa
                    HashMap<Empresa, Decision_crisis_sistemica> decisiones = new HashMap<Empresa, Decision_crisis_sistemica>();
                    for (Integer x : valoracion_absoluta) {
                        if (contador < 3){
                            for (Empresa empresa : valoracion_empresas.keySet()) {
                                if (Math.abs(valoracion_empresas.get(empresa))*1000 == x){
                                    Decision_crisis_sistemica decision = new Decision_crisis_sistemica();
                                    if (valoracion_empresas.get(empresa) > 0){
                                        decision.setDecision(1);
                                    }else{
                                        decision.setDecision(-1);
                                    }
                                    decisiones.put(empresa, decision);
                                    break;
                                }
                            }
                            contador++;
                        }
                    }

                    Utilizar_crisis_sistemica utilizar_crisis = new Utilizar_crisis_sistemica();
                    utilizar_crisis.setCartaEstrategia(carta_estrategia.getCartaEstrategia());
                    utilizar_crisis.setCartaOperaciones(carta);
                    utilizar_crisis.setMapa(decisiones);
                    IMessageEvent msgsend = createMessageEvent("Request_Generico");
                    AgentIdentifier tablero = result[0].getName();
                    msgsend.getParameterSet(SFipa.RECEIVERS).addValue(tablero);
                    msgsend.setContent(utilizar_crisis);
                    sendMessage(msgsend);    
                }
            }
            if (tengo_carta && (tengo_carta_operaciones == false || usar_crisis_sistemica == false)){ 
                getBeliefbase().getBelief("cambiar_turno").setFact(true);  
                getBeliefbase().getBelief("mi_turno").setFact(false); 
                getBeliefbase().getBelief("crisis_sistemica").setFact(false);   
            }
            if (!tengo_carta){
                getBeliefbase().getBelief("dividendos_especiales").setFact(true); 
            }
                 
        }
        else
            System.out.println("Tablero no encontrado");  

        
    }
}