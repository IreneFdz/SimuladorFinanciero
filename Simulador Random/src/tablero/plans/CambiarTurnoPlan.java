package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import tablero.beliefs.*;
import ontology.predicados.Turno_cambiado;
import ontology.conceptos.Fase;
import jadex.adapter.fipa.AgentIdentifier;

import java.util.Random;

public class CambiarTurnoPlan extends Plan {

    public void body()
    {
        
        //System.out.println ("CAMBIO DE TURNO");
        InformacionGeneral informacion= (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();
        Inversores inversores= (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        Turno_cambiado turno_cambiado = new Turno_cambiado();
        
        if (informacion.getRepetirPuja())
            getBeliefbase().getBelief("cambiar_fase").setFact(true); 
        
        if (informacion.getHanParticipado()){
            informacion.setTurno(inversores.getIds().get(0));
            if (informacion.getFase().equals("Demanda")){
                getBeliefbase().getBelief("puja_terminada").setFact(true);
                getBeliefbase().getBelief("cambiar_turno").setFact(false);
            }
            else{
                getBeliefbase().getBelief("cambiar_fase").setFact(true);  
                getBeliefbase().getBelief("cambiar_turno").setFact(false);
            } 
            informacion.setHanParticipado(false);
        }else{
            for (int i = 0; i<inversores.getIds().size(); i++){
                if (inversores.getIds().get(i).equals(informacion.getTurno())){
                    if (i < (inversores.getIds().size()-1)){
                        turno_cambiado.setInversor(inversores.getIds().get(i));
                        informacion.setTurno(inversores.getIds().get(i+1));
                        getBeliefbase().getBelief("InformacionGeneral").setFact(informacion);
                        if (informacion.getFase().equals("Demanda")){
                            if (informacion.getPrimeraPuja() == true)
                                informacion.setTurno(inversores.getIds().get(0));
                            getBeliefbase().getBelief("pasar_turno").setFact(true);
                            informacion.setPrimeraPuja(false);
                        }else{
                            for (AgentIdentifier x : inversores.getIds()) {
                                IMessageEvent msg = createMessageEvent("Inform_Generico");
                                msg.getParameterSet(SFipa.RECEIVERS).addValue(x);
                                msg.setContent(turno_cambiado);
                                sendMessage(msg);
                            } 
                        }
                        break;
                    }
                    if (i == (inversores.getIds().size()-1)){
                        turno_cambiado.setInversor(inversores.getIds().get(i));
                        getBeliefbase().getBelief("InformacionGeneral").setFact(informacion);
                        if (informacion.getFase().equals("Demanda")){
                            getBeliefbase().getBelief("pasar_turno").setFact(true);
                        }else{
                            for (AgentIdentifier x : inversores.getIds()) {
                                IMessageEvent msg = createMessageEvent("Inform_Generico");
                                msg.getParameterSet(SFipa.RECEIVERS).addValue(x);
                                msg.setContent(turno_cambiado);
                                sendMessage(msg);
                            }
                        }
                        informacion.setHanParticipado(true); 
                        break;   
                    }
                }
            }
        }
        if (informacion.getFase().equals("Acciones"))
            getBeliefbase().getBelief("cambiar_turno").setFact(false);
        getBeliefbase().getBelief("InformacionGeneral").setFact(informacion);
           
    }
}