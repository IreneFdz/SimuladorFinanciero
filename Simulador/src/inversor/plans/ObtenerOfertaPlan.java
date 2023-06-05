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
            //Decidir que carta poner publica y que privada
            //Valorar las cartas
            Carta_acciones carta_1 = cartas_repartidas.getCarta1();
            double valoracion1 = getValorCarta(carta_1, mis_cartas.getMisCartas(), empresas.getEmpresas());
            Carta_acciones carta_2 = cartas_repartidas.getCarta2();
            double valoracion2 = getValorCarta(carta_2, mis_cartas.getMisCartas(), empresas.getEmpresas());

            //Valorar las pilas
            double valorPila1 = getValorPila(pilas_cartas.getMapa(),1, empresas.getEmpresas(), pilas_cartas.getNumOcultas(1), mis_cartas.getMisCartas());
            double valorPila2 = getValorPila(pilas_cartas.getMapa(),2, empresas.getEmpresas(), pilas_cartas.getNumOcultas(2), mis_cartas.getMisCartas());
            double valorPila3 = getValorPila(pilas_cartas.getMapa(),3, empresas.getEmpresas(), pilas_cartas.getNumOcultas(3), mis_cartas.getMisCartas());

            int mejorPila = 0;
            int pilaMedia = 0;
            int peorPila = 0;

            if (valorPila1 > valorPila2) {
                if (valorPila1 > valorPila3)
                    mejorPila = 1;                                          
                else
                    mejorPila = 3;  
            } else if (valorPila2 >= valorPila3)
                mejorPila = 2;
            else
                mejorPila = 3;

            if (valorPila1 < valorPila2) {
                if (valorPila1 < valorPila3)
                    peorPila = 1;                                            
                else
                    peorPila = 3;  
            } else if (valorPila2 < valorPila3)
                peorPila = 2;
            else
                peorPila = 3;

            if (peorPila == 1 && mejorPila == 2)
                pilaMedia = 3;
            if (peorPila == 1 && mejorPila == 3)
                pilaMedia = 2;
            if (peorPila == 2 && mejorPila == 3)
                pilaMedia = 1;
            if (peorPila == 2 && mejorPila == 1)
                pilaMedia = 3;
            if (peorPila == 3 && mejorPila == 1)
                pilaMedia = 2;
            if (peorPila == 3 && mejorPila == 2)
                pilaMedia = 1;

            // La mejor carta privada y la peor publica
            // Si mejor carta es positiva se pone en la mejor pila.
            // Si la peor carta es positiva se pone en la pila media.
            // Si la peor carta es negativa se pone en la peor pila.
            Colocar_cartas_acciones colocar = new Colocar_cartas_acciones();
            Pila_subasta pila_publica = new Pila_subasta();
            Pila_subasta pila_privada = new Pila_subasta();
            if (valoracion1 >= valoracion2){
                colocar.setCartaOculta(carta_1);
                colocar.setCartaPublica(carta_2);
                if (valoracion1 > 0)
                    pila_privada.setNumero(mejorPila);
                if (valoracion1 <= 0)
                    pila_privada.setNumero(peorPila);
                if (valoracion2 > 0)
                    pila_publica.setNumero(pilaMedia);
                if (valoracion2 <= 0)
                    pila_publica.setNumero(peorPila);
                colocar.setDondeCartaOculta(pila_privada);
                colocar.setDondeCartaPublica(pila_publica);
            }else{
                colocar.setCartaOculta(carta_2);
                colocar.setCartaPublica(carta_1);
                if (valoracion1 > 0)
                    pila_publica.setNumero(pilaMedia);
                if (valoracion1 <= 0)
                    pila_publica.setNumero(peorPila);
                if (valoracion2 > 0)
                    pila_privada.setNumero(mejorPila);
                if (valoracion2 <= 0)
                    pila_privada.setNumero(peorPila);
                colocar.setDondeCartaOculta(pila_privada);
                colocar.setDondeCartaPublica(pila_publica);
            }
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
