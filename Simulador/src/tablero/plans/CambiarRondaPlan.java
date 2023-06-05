package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import tablero.beliefs.*;
import ontology.predicados.Ronda_cambiada;
import ontology.conceptos.Ronda;
import ontology.conceptos.Fase;

import java.util.Random;

public class CambiarRondaPlan extends Plan {

    public void body()
    {
        InformacionGeneral informacion= (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();
        Inversores inversores= (Inversores) getBeliefbase().getBelief("Inversores").getFact();

        Ronda_cambiada ronda_cambiada = new Ronda_cambiada();
        if(informacion.getRonda() < 7){
            Ronda ronda = new Ronda();
            int numeroRonda = informacion.getRonda()+1;
            ronda.setNumero(numeroRonda);
            ronda_cambiada.setQue(ronda);
            for (AgentIdentifier x : inversores.getIds()) {
                IMessageEvent msg = createMessageEvent("Inform_Generico");
                msg.getParameterSet(SFipa.RECEIVERS).addValue(x);
                msg.setContent(ronda_cambiada);
                sendMessage(msg);
            }
            informacion.setRonda(informacion.getRonda()+1);
            informacion.setFase("Informacion");
            System.out.println ("RONDA: "+informacion.getRonda());
            getBeliefbase().getBelief("fase").setFact("Informacion");
            getBeliefbase().getBelief("InformacionGeneral").setFact(informacion); 
            getBeliefbase().getBelief("cambiar_ronda").setFact(false);
        }else{
            getBeliefbase().getBelief("final_partida").setFact(true); 
        }        
    }
}