package tablero.beliefs;
import ontology.conceptos.Bono;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import jadex.adapter.fipa.AgentIdentifier;

public class Bonos
{
	private HashMap<Bono, AgentIdentifier> bono_inversor;
    private HashMap<AgentIdentifier, Integer> bonos_por_ronda;
    
    public Bonos(){
        bono_inversor = new HashMap<Bono, AgentIdentifier>();
        bonos_por_ronda = new HashMap<AgentIdentifier, Integer>();
    }
    public void asignarBono(Bono bono, AgentIdentifier id){
        bono_inversor.put(bono, id);
    }
    public HashMap<Bono, AgentIdentifier> getBonos(){
        return this.bono_inversor;
    }
    public void addBonoRonda(AgentIdentifier id){
        int numero = bonos_por_ronda.get(id);
        bonos_por_ronda.put(id, numero);
    }
    public void putBonoRonda(AgentIdentifier id, int numero){
        bonos_por_ronda.put(id, numero);
    }
    public int getBonosRonda(AgentIdentifier id){
        return bonos_por_ronda.get(id);
    }

    

}