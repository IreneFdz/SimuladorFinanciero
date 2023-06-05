package tablero.beliefs;
import java.util.ArrayList;
import java.util.Collections;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Carta_estrategia_de_inversion;
import ontology.conceptos.Dinero;
import jadex.adapter.fipa.AgentIdentifier;
import java.util.HashMap;

public class Inversores
{
    private ArrayList<AgentIdentifier> ids;
    private HashMap<AgentIdentifier, Dinero> map_id_dinero;
    private HashMap<AgentIdentifier, Carta_acciones> map_id_carta;
    private HashMap<AgentIdentifier, Carta_estrategia_de_inversion> map_id_inversion;
    private AgentIdentifier turno;

    public Inversores(){
        ids = new ArrayList<AgentIdentifier>();
        map_id_dinero = new HashMap<AgentIdentifier, Dinero>();
        map_id_carta = new HashMap<AgentIdentifier, Carta_acciones>();
        map_id_inversion = new HashMap<AgentIdentifier, Carta_estrategia_de_inversion>();
        turno = new AgentIdentifier();
    }

    public int getNumero(){
        return ids.size();
    } 
    public void addId(AgentIdentifier id){
        ids.add(id);
    }
    public ArrayList<AgentIdentifier> getIds(){
        return ids;
    }   
    public AgentIdentifier getTurno(){
        return this.turno;
    } 
    public void setTurno(AgentIdentifier turno){
        this.turno = turno;
    }
    public void addIdDinero(AgentIdentifier id, Dinero dinero){
        map_id_dinero.put(id, dinero);
    }
    public Dinero getDineroPorId(AgentIdentifier id){
        return map_id_dinero.get(id);
    }  
    public HashMap<AgentIdentifier, Dinero> getMapDineroPorId(){
        return map_id_dinero;
    } 
    public void addIdCarta(AgentIdentifier id, Carta_acciones carta){
        map_id_carta.put(id, carta);
    }
    public Carta_acciones getCartaPorId(AgentIdentifier id){
        return map_id_carta.get(id);
    } 
    public HashMap<AgentIdentifier, Carta_acciones> getMapCartaPorId(){
        return map_id_carta;
    } 
    public void addIdInversion(AgentIdentifier id, Carta_estrategia_de_inversion carta){
        map_id_inversion.put(id, carta);
    }
    public Carta_estrategia_de_inversion getInversionPorId(AgentIdentifier id){
        return map_id_inversion.get(id);
    } 
    public HashMap<AgentIdentifier, Carta_estrategia_de_inversion> getMapaInversion(){
        return this.map_id_inversion;
    }  
    public void printIdInversion(){
        for (AgentIdentifier key: map_id_inversion.keySet()){  
			System.out.println("--- El inversor: "+Character.getNumericValue(key.getLocalName().charAt(8))+ " tiene la carta: " + map_id_inversion.get(key).getTipo());
		} 
    }
    public void restarDinero(AgentIdentifier id, Dinero dinero){
        Dinero dinero_actual = new Dinero();
        dinero_actual = map_id_dinero.get(id);
        Dinero dinero_nuevo = new Dinero();
        dinero_nuevo.setCantidad(dinero_actual.getCantidad()-dinero.getCantidad()); 
        map_id_dinero.put(id, dinero_nuevo);
    } 
    public void sumarDinero(AgentIdentifier id, Dinero dinero){
        Dinero dinero_actual = new Dinero();
        dinero_actual = map_id_dinero.get(id);
        Dinero dinero_nuevo = new Dinero();
        dinero_nuevo.setCantidad(dinero_actual.getCantidad()+dinero.getCantidad()); 
        map_id_dinero.put(id, dinero_nuevo);
    }
} 
