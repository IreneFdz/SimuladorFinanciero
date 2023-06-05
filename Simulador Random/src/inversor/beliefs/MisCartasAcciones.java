package inversor.beliefs;
import java.util.ArrayList;
import java.util.Collections;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Dinero;
import jadex.adapter.fipa.AgentIdentifier;
import java.util.HashMap;

public class MisCartasAcciones
{
    private ArrayList<Carta_acciones> mis_cartas;

    public MisCartasAcciones(){
        mis_cartas = new ArrayList<Carta_acciones>();
    }

    public void addCarta(Carta_acciones carta){
        mis_cartas.add(carta);
    }
    public ArrayList<Carta_acciones> getMisCartas(){
        return mis_cartas;
    }  
    public void borrarCartaPorId (int id){
        for (int i = 0; i<mis_cartas.size(); i++) {
            if(mis_cartas.get(i).getId()== id){
                mis_cartas.remove(i);
                break;
            }
        }
    } 
}