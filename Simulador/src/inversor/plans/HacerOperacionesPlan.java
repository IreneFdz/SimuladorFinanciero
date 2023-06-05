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

            // Decidir la empresa en la que se quiere hacer la operacion.
            // Obtener una valoracion de cada empresa que es igual a las acciones del inversor menos las acciones de los otro inversores
            AgentIdentifier id = new AgentIdentifier(getAgentName(), true);
            HashMap <Empresa, Integer> valoracion_empresas = new HashMap <Empresa, Integer>();
            ArrayList <Integer> valoraciones = new ArrayList <Integer>();
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
                        if (pilas_cartas.getMapaCartaInversor().get(carta).equals(principal_rival))
                            valoracionEmpresa--;
                    }
                }
                for (Carta_acciones key : mis_cartas.getMisCartas()) {
                    if (key.getTipo() == 1 && empresas.getEmpresas().get(i).getNombre().equals(key.getEmpresa()))
                        valoracionEmpresa++;
                }
                valoracion_empresas.put(empresas.getEmpresas().get(i), valoracionEmpresa);
                valoraciones.add(valoracionEmpresa);
            }
             
            if (tengo_carta){
                HashMap <Carta_acciones, Empresa> mapa = new HashMap<Carta_acciones, Empresa>();
                boolean hacer_operaciones = true;
                int numero_cartas = 0;
                for (int i = 0; i< mis_cartas.getMisCartas().size(); i++) {
                    if(mis_cartas.getMisCartas().get(i).getTipo() == 2){
                        tipo_operaciones = mis_cartas.getMisCartas().get(i).getTipoOperaciones();

                        if (tipo_operaciones == 2){
                            Collections.sort(valoraciones, Collections.reverseOrder());
                        }
                        if (tipo_operaciones == -2){
                            Collections.sort(valoraciones);
                        }
                            
                        int limite_beneficios = 4000;
                        int beneficios = Math.abs(valoraciones.get(0)*2000);

                        if(beneficios >= limite_beneficios)
                            hacer_operaciones = true;
                        if (informacion.getRonda() == 7 && tipo_operaciones == 2 && valoraciones.get(0) >= 0) 
                            hacer_operaciones = true;
                        if (informacion.getRonda() == 7 && tipo_operaciones == -2 && valoraciones.get(0) <= 0) 
                            hacer_operaciones = true;
                        
                        if (numero_cartas == 0){
                            if (carta_estrategia.getCartaEstrategia().getTipo().equals("Venta en corto descubierta")){
                                if (carta_estrategia.getCartaEstrategia().getBorrada() == false)
                                    hacer_operaciones = false;
                            }
                            if (carta_estrategia.getCartaEstrategia().getTipo().equals("Crisis sistemica")){
                                if (carta_estrategia.getCartaEstrategia().getBorrada() == false)
                                    hacer_operaciones = false;
                            }
                        }
                        if (hacer_operaciones){
                            int valoracion_mejor = valoraciones.get(0);
                            // Buscar la empresa con esta valoracion 
                            for (Empresa empresa : valoracion_empresas.keySet()) {
                                if (valoracion_empresas.get(empresa) == valoracion_mejor){
                                    mapa.put(mis_cartas.getMisCartas().get(i), empresa);
                                    break;
                                }
                            }
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