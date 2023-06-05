package inversor.plans;
import inversor.beliefs.*;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import java.util.HashMap;
import jadex.adapter.fipa.AgentIdentifier;
import ontology.predicados.Partida_iniciada;
import ontology.conceptos.Dinero;
import ontology.conceptos.Carta_acciones;

import java.util.Random;

public class PartidaIniciadaPlan extends Plan {

    public void body()
    {
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Partida_iniciada partida = (Partida_iniciada)  respuestaTablero.getContent();
        Inversores inversores = new Inversores();
        MisCartasAcciones misCartas = new MisCartasAcciones();
        InformacionGeneral informacion = new InformacionGeneral();
        PilasCartasAccion pilas_cartas = new PilasCartasAccion();
        informacion.setRonda(0);
        informacion.setFase("Informacion");
        Empresas empresas = new Empresas();
        AgentIdentifier id = new AgentIdentifier(getAgentName(), true);
        for (AgentIdentifier key:partida.getMapa().keySet()) {
            inversores.addId(key);
        }
        inversores.setMapaDineroPorId(partida.getMapa());
        misCartas.addCarta(partida.getAccion());
        for (int x = 0; x < misCartas.getMisCartas().size(); x++){
            if (misCartas.getMisCartas().get(x).getTipo() == 1)
                System.out.println("--- El inversor: "+ Character.getNumericValue(id.getLocalName().charAt(8))+ " tiene la carta: "+misCartas.getMisCartas().get(x).getId()+" de tipo: 1 de la empresa "+misCartas.getMisCartas().get(x).getEmpresa());
            else
                System.out.println("--- El inversor: "+ Character.getNumericValue(id.getLocalName().charAt(8))+ " tiene la carta: "+misCartas.getMisCartas().get(x).getId()+" de tipo: "+misCartas.getMisCartas().get(x).getTipo());
        }  
        empresas.setEmpresas(partida.getListaEmpresas());
        getBeliefbase().getBelief("Empresas").setFact(empresas);
        getBeliefbase().getBelief("Inversores").setFact(inversores);
		getBeliefbase().getBelief("MisCartasAcciones").setFact(misCartas);
        getBeliefbase().getBelief("InformacionGeneral").setFact(informacion);
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
        
    }
}