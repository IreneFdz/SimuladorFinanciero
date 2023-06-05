package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import inversor.beliefs.*;
import java.util.ArrayList;
import java.util.Collections;
import ontology.acciones.Utilizar_manipulacion_a_la_baja;
import ontology.conceptos.Carta_acciones;


import java.util.Random;

public class ManipulacionPlan extends Plan {

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
            Empresas empresas= (Empresas) getBeliefbase().getBelief("Empresas").getFact();
            InformacionGeneral informacion= (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();
            Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
            //Comprobar si tengo carta de manipulacion a la baja
            boolean tengo_carta = false;
            if (carta_estrategia.getCartaEstrategia().getTipo().equals("manipulacion a la baja")){
                if (carta_estrategia.getCartaEstrategia().getBorrada() == false)
                    tengo_carta = true;
            }

            boolean usar_manipulacion = true;
            if(tengo_carta){
                // Decidir si usar la carta.
                // Ordenar mis accion de menor a mayor valor de mercado.
                ArrayList<Carta_acciones> listaAcciones = new ArrayList<Carta_acciones>();
                ArrayList<Integer> valorAcciones = new ArrayList<Integer>();
                for (int i = 0; i< mis_cartas.getMisCartas().size(); i++) {
                    if(mis_cartas.getMisCartas().get(i).getTipo() == 1){
                        for (int j = 0; j< empresas.getEmpresas().size(); j++){
                            if (empresas.getEmpresas().get(j).getNombre().equals(mis_cartas.getMisCartas().get(i).getEmpresa())){
                                mis_cartas.getMisCartas().get(i).setValor(empresas.getEmpresas().get(j).getValorAcciones());  
                            }
                        }
                        listaAcciones.add(mis_cartas.getMisCartas().get(i));
                        valorAcciones.add(mis_cartas.getMisCartas().get(i).getValor());
                    }       
                }
                int limite_beneficios = 6000;
                int beneficios_acciones = 0;
                int ingresos_por_venta = 8000;
                int contador = 0;
                Collections.sort(valorAcciones);
                for (Integer x : valorAcciones) {
                    if (contador < 3){
                        beneficios_acciones += ingresos_por_venta - x * 1000;
                        contador++;
                    }
                }
                if (beneficios_acciones >= limite_beneficios)
                    usar_manipulacion = true;
                if (informacion.getRonda() == 7)
                    usar_manipulacion = true;

                if(usar_manipulacion){
                    contador = 0;
                    ArrayList<Carta_acciones> accionesManipulacion = new ArrayList<Carta_acciones>();
                    for (Integer x : valorAcciones) {
                        if (contador < 3){
                            for (Carta_acciones carta : listaAcciones) {
                                if (carta.getValor() == x){
                                    boolean ya_esta = false;
                                    for (Carta_acciones accion : accionesManipulacion) {
                                        if (accion.getId()==carta.getId())
                                            ya_esta = true;
                                    }
                                    if (ya_esta == false){
                                        accionesManipulacion.add(carta);
                                    }
                                    break;
                                }
                            }
                            contador++;
                        }
                    }
                    Utilizar_manipulacion_a_la_baja utilizar_manipulacion = new Utilizar_manipulacion_a_la_baja();
                    utilizar_manipulacion.setCarta(carta_estrategia.getCartaEstrategia());
                    utilizar_manipulacion.setLista(accionesManipulacion);
                    IMessageEvent msgsend = createMessageEvent("Request_Generico");
                    AgentIdentifier tablero = result[0].getName();
                    msgsend.getParameterSet(SFipa.RECEIVERS).addValue(tablero);
                    msgsend.setContent(utilizar_manipulacion);
                    sendMessage(msgsend);   
                }
            }
            if (!tengo_carta){
                if (carta_estrategia.getCartaEstrategia().getBorrada() == true){
                    getBeliefbase().getBelief("cambiar_turno").setFact(true);  
                    getBeliefbase().getBelief("mi_turno").setFact(false); 
                }else
                    getBeliefbase().getBelief("venta_en_corto").setFact(true);
            }
            if (tengo_carta && usar_manipulacion == false){
                getBeliefbase().getBelief("cambiar_turno").setFact(true);
                getBeliefbase().getBelief("mi_turno").setFact(false);
                getBeliefbase().getBelief("manipulacion").setFact(false); 
            }
        }
        else
            System.out.println("Tablero no encontrado");  

    }
}