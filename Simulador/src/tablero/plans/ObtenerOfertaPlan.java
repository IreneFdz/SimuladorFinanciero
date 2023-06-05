package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.acciones.Pedir_cartas_acciones;
import ontology.predicados.Cartas_acciones_repartidas;
import ontology.conceptos.Pila_subasta;
import ontology.conceptos.Carta_acciones;
import tablero.beliefs.*;

import java.util.Random;

public class ObtenerOfertaPlan extends Plan {

    public void body()
    {   
        
        IMessageEvent msgrec = (IMessageEvent) getInitialEvent();
		AgentIdentifier sender= (AgentIdentifier) msgrec.getParameter(SFipa.SENDER).getValue();
		Pedir_cartas_acciones contenido = (Pedir_cartas_acciones) msgrec.getContent();

        MazoCartasAcciones mazo = (MazoCartasAcciones) getBeliefbase().getBelief("MazoCartasAcciones").getFact();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        InformacionGeneral informacion = (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();
        if (sender.equals(inversores.getIds().get(0)))
            System.out.println ("OFERTA FORMADA");
        //Preparar las pilas con una carta cada una 
        if (sender.equals(inversores.getIds().get(0))){
            pilas_cartas.borrarMapaCartaPila();
            pilas_cartas.borrarMapaInicial();
            for (int i = 0; i< inversores.getNumero(); i++) {
                Pila_subasta pila = new Pila_subasta();
                pila.setNumero(i+1);
                pilas_cartas.addCartaPilaInicial(mazo.sacarCarta(), pila);        
            }
        }
        Cartas_acciones_repartidas acciones_repartidas = new Cartas_acciones_repartidas();
        //Envia a cada uno de los inversores sus dos cartas de las que deberan elegir cual mantener publica y cual privada.
		IMessageEvent msg = createMessageEvent("Inform_Generico");
		msg.getParameterSet(SFipa.RECEIVERS).addValue(sender);
        acciones_repartidas.setMapa(pilas_cartas.getMapaInicial());
        acciones_repartidas.setCarta1(mazo.sacarCarta());
        acciones_repartidas.setCarta2(mazo.sacarCarta());
        msg.setContent(acciones_repartidas);
		sendMessage(msg);

		getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
		getBeliefbase().getBelief("MazoCartasAcciones").setFact(mazo);
        getBeliefbase().getBelief("cambiar_turno").setFact(false);  
  
    }
}