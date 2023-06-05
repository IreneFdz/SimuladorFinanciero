package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import inversor.beliefs.*;
import ontology.predicados.Ronda_cambiada;
import ontology.conceptos.Ronda;

import java.util.Random;

public class CambiarRondaPlan extends Plan {

    public void body()
    {
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Ronda_cambiada ronda_cambiada = (Ronda_cambiada)  respuestaTablero.getContent();
        InformacionGeneral informacion= (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();

        informacion.setRonda(ronda_cambiada.getQue().getNumero());
        informacion.setFase("Informacion");

        getBeliefbase().getBelief("InformacionGeneral").setFact(informacion);

        
    }
}