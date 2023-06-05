package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.predicados.Turno_pasado;
import ontology.conceptos.Pila_subasta;
import tablero.beliefs.*;

import java.util.Random;

public class PasarTurnoPlan extends Plan {

    public void body()
    {
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        InformacionGeneral informacion= (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();
        Inversores inversores= (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        boolean pasarTurno = false;
        boolean puja_final = false;
        //Comprobar si el inversore tiene asignada alguna pila
        for (Pila_subasta key: pilas_cartas.getMapaPilaInversor().keySet()){  
            if(pilas_cartas.getMapaPilaInversor().get(key).equals(informacion.getTurno())){
                pasarTurno = true;
            }
		}
        Turno_pasado turno = new Turno_pasado();
        if (pasarTurno){
            AgentIdentifier turno_actual = informacion.getTurno();
            if (inversores.getIds().get(0).equals(turno_actual))
                informacion.setTurno(inversores.getIds().get(1));            
            if (inversores.getIds().get(1).equals(turno_actual)) 
                informacion.setTurno(inversores.getIds().get(2)); 
            if (inversores.getIds().get(2).equals(turno_actual))                    
                informacion.setTurno(inversores.getIds().get(0));
            puja_final = true;
            for (AgentIdentifier i : inversores.getIds()) {
                boolean inversor_encontrado = false;
                for (Pila_subasta key: pilas_cartas.getMapaPilaInversor().keySet()){ 
                    if(pilas_cartas.getMapaPilaInversor().get(key).equals(i)){
                        inversor_encontrado = true;
                    }
                }
                if (inversor_encontrado == false)
                    puja_final = false;
            }
        }  
        if (puja_final == true){
            getBeliefbase().getBelief("puja_terminada").setFact(true);
        }else{
            turno.setInversor(informacion.getTurno());
            for (AgentIdentifier i : inversores.getIds()) {
                IMessageEvent msg = createMessageEvent("Inform_Generico");
                msg.getParameterSet(SFipa.RECEIVERS).addValue(i);
                msg.setContent(turno);
                sendMessage(msg);
            }
        }         
        getBeliefbase().getBelief("pasar_turno").setFact(false);
        getBeliefbase().getBelief("Inversores").setFact(inversores);
	}
        
}
