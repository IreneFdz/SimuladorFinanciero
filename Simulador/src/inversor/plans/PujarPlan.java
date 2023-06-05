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

            double valoracionPila1 = -100000000;
            double valoracionPila2 = -100000000;
            double valoracionPila3 = -100000000;

            if (pilas_cartas.getPujaMinima(1) <= dinero_disponible) {
                valoracionPila1 = getValorPila(pilas_cartas.getMapa(),1, empresas.getEmpresas(), pilas_cartas.getNumOcultas(1), mis_cartas.getMisCartas())*1000 - pilas_cartas.getPujaMinima(1);
            }

            if (pilas_cartas.getPujaMinima(2) <= dinero_disponible) {
                valoracionPila2 = getValorPila(pilas_cartas.getMapa(),2, empresas.getEmpresas(), pilas_cartas.getNumOcultas(2), mis_cartas.getMisCartas())*1000 - pilas_cartas.getPujaMinima(2);
            }

            if (pilas_cartas.getPujaMinima(3) <= dinero_disponible) {
                valoracionPila3 = getValorPila(pilas_cartas.getMapa(),3, empresas.getEmpresas(), pilas_cartas.getNumOcultas(3), mis_cartas.getMisCartas())*1000 - pilas_cartas.getPujaMinima(3);
            }
            
            Pila_subasta pila = new Pila_subasta();
            int numeroPila = 0;
            if (valoracionPila1 > valoracionPila2) {
                if (valoracionPila1 > valoracionPila3)
                    numeroPila = 1;                                          
                else
                    numeroPila = 3;  
            } else if (valoracionPila2 > valoracionPila3)
                numeroPila = 2;
            else
                numeroPila = 3;

            pila.setNumero(numeroPila);
            //Decidir cuanto pagar por la pila
            int precio = pilas_cartas.getPujaMinima(pila);
            Dinero dinero = new Dinero();
            dinero.setCantidad(precio);
            //Comprobar si tengo dinero para participar en la subasta que he decidido 
                
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
    public double getValorCarta (Carta_acciones carta, ArrayList<Carta_acciones> lista_cartas, ArrayList<Empresa> lista_empresas){
        double valoracion = 0;
            if (carta.getTipo() == 1){
                int accionesIguales = 0;
                for (int i = 0; i< lista_cartas.size(); i++){
                    if (lista_cartas.get(i).getTipo() == 1 && lista_cartas.get(i).getEmpresa().equals(carta.getEmpresa()))
                        accionesIguales ++;
                }
                double multiplicador = 1.1;
                int valorEmpresa = 0;
                for (int i = 0; i< lista_empresas.size(); i++){
                    if (lista_empresas.get(i).getNombre().equals(carta.getEmpresa()))
                        valorEmpresa = lista_empresas.get(i).getValorAcciones();
                }
                valoracion = valorEmpresa + accionesIguales*multiplicador;
            }
            if (carta.getTipo() == 2)
                valoracion = 2;
            if (carta.getTipo() == 3)
                valoracion = -3;
        return valoracion;
    }   
    public double getValorPila (HashMap<Carta_acciones, Pila_subasta> mapa, int numeroPila, ArrayList<Empresa> lista_empresas, int numeroOcultas, ArrayList<Carta_acciones> mis_cartas){
        double valorPila = 0;
        //Valoracion de las cartas publicas
        for (Carta_acciones carta: mapa.keySet()){  
			if(mapa.get(carta).getNumero() == numeroPila){
			    valorPila += getValorCarta(carta, mis_cartas, lista_empresas);
            }
		}
        //Valoracion de las cartas ocultas
        for (int x = 0; x< numeroOcultas; x++){
            for (int i = 0; i< lista_empresas.size(); i++){
                valorPila += (double)(lista_empresas.get(i).getValorAcciones()/8);
            }
            valorPila += (double)2/10;
            valorPila += (double)-3*12/80;
        }
        return valorPila;
    }      
}