package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import tablero.beliefs.*;
import ontology.predicados.Fase_cambiada;
import ontology.conceptos.Fase;

public class CambiarFasePlan extends Plan {

    public void body()
    {
        InformacionGeneral informacion= (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();
        Inversores inversores= (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        Fase_cambiada fase_cambiada = new Fase_cambiada();

        Fase fase = new Fase();
        if(informacion.getFase().equals("Informacion")){
            fase.setNombre("Oferta");
            getBeliefbase().getBelief("fase").setFact("Oferta"); 
            getBeliefbase().getBelief("cambiar_turno").setFact(true);
        }
        if(informacion.getFase().equals("Oferta")){
            fase.setNombre("Demanda");
            getBeliefbase().getBelief("fase").setFact("Demanda"); 
            informacion.setPrimeraPuja(true);
            getBeliefbase().getBelief("InformacionGeneral").setFact(informacion);
            getBeliefbase().getBelief("cambiar_turno").setFact(true);
        }
        if(informacion.getFase().equals("Demanda") && informacion.getRepetirPuja()){
            fase.setNombre("Demanda");
            getBeliefbase().getBelief("fase").setFact("Demanda"); 
            getBeliefbase().getBelief("cambiar_turno").setFact(true);
        }
        if(informacion.getFase().equals("Demanda") && informacion.getRepetirPuja() == false){
            fase.setNombre("Acciones");
            getBeliefbase().getBelief("fase").setFact("Acciones"); 
            System.out.println ("FASE ACCIONES");
            getBeliefbase().getBelief("cambiar_turno").setFact(false);
        }
        if(informacion.getFase().equals("Acciones")){
            fase.setNombre("Venta");
            getBeliefbase().getBelief("fase").setFact("Venta"); 
            getBeliefbase().getBelief("cambiar_turno").setFact(true);
        }
        if(informacion.getFase().equals("Venta")){
            fase.setNombre("Movimiento");
            getBeliefbase().getBelief("fase").setFact("Movimiento"); 
        }
        if(informacion.getFase().equals("Movimiento")){
            getBeliefbase().getBelief("cambiar_ronda").setFact(true); 
        }
        if(!informacion.getFase().equals("Movimiento")){
            fase_cambiada.setQue(fase);
            for (AgentIdentifier x : inversores.getIds()) {
                IMessageEvent msg = createMessageEvent("Inform_Generico");
                msg.getParameterSet(SFipa.RECEIVERS).addValue(x);
                msg.setContent(fase_cambiada);
                sendMessage(msg);
            } 
        }
        if(fase.getNombre()==null)
            fase.setNombre("Informacion");   
        informacion.setFase(fase.getNombre());
        getBeliefbase().getBelief("cambiar_fase").setFact(false);
        getBeliefbase().getBelief("InformacionGeneral").setFact(informacion);
    }
}