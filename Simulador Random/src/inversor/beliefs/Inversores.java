package inversor.beliefs;
import java.util.ArrayList;
import java.util.Collections;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Dinero;
import jadex.adapter.fipa.AgentIdentifier;
import java.util.HashMap;

public class Inversores
{
    private ArrayList<AgentIdentifier> ids;
    private HashMap<AgentIdentifier, Dinero> map_id_dinero;

    public Inversores(){
        ids = new ArrayList<AgentIdentifier>();
        map_id_dinero = new HashMap<AgentIdentifier, Dinero>();
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
    public void setMapaDineroPorId(HashMap<AgentIdentifier, Dinero> mapa){
        this.map_id_dinero = mapa;
    }
    public Dinero getDineroPorId(AgentIdentifier id){
        return map_id_dinero.get(id);
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
    public void modificarDineroPorId(AgentIdentifier id, Dinero dinero){
         map_id_dinero.put(id, dinero);
    } 
    public HashMap<AgentIdentifier, Dinero> getMapDineroPorId(){
        return map_id_dinero;
    } 
    public Dinero getDineroPorIdString(String id){
        Dinero dinero = new Dinero();
        for (AgentIdentifier clave:map_id_dinero.keySet()) {
            if (map_id_dinero.get(clave).toString().equals(id)){
                dinero = map_id_dinero.get(clave);
            }
        }
        return dinero;
            
        }
    }
