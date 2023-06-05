package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.predicados.Cartas_acciones_colocadas;
import ontology.acciones.Colocar_cartas_acciones;
import ontology.conceptos.Carta_acciones;
import tablero.beliefs.*;

import java.util.Random;

public class FormarOfertaPlan extends Plan {

    public void body()
    {   
        IMessageEvent respuestaInversor = (IMessageEvent) getInitialEvent();
        Colocar_cartas_acciones colocar_cartas = (Colocar_cartas_acciones)  respuestaInversor.getContent();
        InformacionGeneral informacion= (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();
        AgentIdentifier sender= (AgentIdentifier) respuestaInversor.getParameter(SFipa.SENDER).getValue();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        pilas_cartas.addCartaPila(colocar_cartas.getCartaPublica(), colocar_cartas.getDondeCartaPublica());
        pilas_cartas.addCartaPila(colocar_cartas.getCartaOculta(), colocar_cartas.getDondeCartaOculta());
        Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        System.out.println ("--- El inversor: "+ Character.getNumericValue(sender.getLocalName().charAt(8))+ 
          " ha colocado la carta publica: "+ colocar_cartas.getCartaPublica().getId()+ " en la pila: "+ colocar_cartas.getDondeCartaPublica().getNumero());
        System.out.println ("--- El inversor: "+ Character.getNumericValue(sender.getLocalName().charAt(8))+ 
          " ha colocado la carta privada en la pila: "+ colocar_cartas.getDondeCartaOculta().getNumero());

        pilas_cartas.borrarMapaPujaMinima();
        pilas_cartas.borrarMapaPilaInversor();
        IMessageEvent msg = createMessageEvent("Inform_Generico");
        Cartas_acciones_colocadas cartas_colocadas = new Cartas_acciones_colocadas();
        cartas_colocadas.setQue(colocar_cartas.getCartaPublica());
        cartas_colocadas.setDondeCartaOculta(colocar_cartas.getDondeCartaOculta());
        cartas_colocadas.setDondeCartaPublica(colocar_cartas.getDondeCartaPublica());
        //Envia a cada uno de los inversores donde se ha colocado la carta privada, cual es la carta publica y donde se ha colocado.
        for (AgentIdentifier i : inversores.getIds()) {
          msg.getParameterSet(SFipa.RECEIVERS).addValue(i);
          msg.setContent(cartas_colocadas);
          sendMessage(msg);
        }
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
        if(sender.equals(inversores.getIds().get(2))){
          getBeliefbase().getBelief("cambiar_fase").setFact(true);
        }
        getBeliefbase().getBelief("InformacionGeneral").setFact(informacion);
    }
}