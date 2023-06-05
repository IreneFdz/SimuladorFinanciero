package tablero.beliefs;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Pila_subasta;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import jadex.adapter.fipa.AgentIdentifier;

public class PilasCartasAccion
{
	private HashMap<Carta_acciones, Pila_subasta> mapa_carta_pila;
    private HashMap<Carta_acciones, Pila_subasta> mapa_inicial;
    private HashMap<Pila_subasta, Integer> pila_puja_minima;
    private HashMap<Pila_subasta, AgentIdentifier> pila_inversor;
    private HashMap<Carta_acciones, AgentIdentifier> carta_inversor;

    public PilasCartasAccion(){
        this.mapa_inicial = new HashMap<Carta_acciones, Pila_subasta>();
        this.mapa_carta_pila = new HashMap<Carta_acciones, Pila_subasta>();
        this.pila_puja_minima = new HashMap<Pila_subasta, Integer>();
        this.pila_inversor = new HashMap<Pila_subasta, AgentIdentifier>();
        this.carta_inversor = new HashMap<Carta_acciones, AgentIdentifier>();
    }

    public HashMap<Carta_acciones, Pila_subasta> getMapaInicial(){
        return this.mapa_inicial;
    }
    public void addCartaPilaInicial(Carta_acciones carta, Pila_subasta pila){
        this.mapa_inicial.put(carta, pila);
    }
    public void borrarMapaInicial(){
        this.mapa_inicial.clear();
    }
    public void addCartaPila(Carta_acciones carta, Pila_subasta pila){
        this.mapa_carta_pila.put(carta, pila);
    }
    public void borrarMapaCartaPila(){
        this.mapa_carta_pila.clear();
    }
    public void borrarMapaPujaMinima(){
        this.pila_puja_minima.clear();
    }
    public void borrarMapaPilaInversor(){
        this.pila_inversor.clear();
    }
    public ArrayList<Carta_acciones> getCartasPila (Pila_subasta pila){
        ArrayList<Carta_acciones> lista_cartas = new ArrayList<Carta_acciones>();
        for (Carta_acciones key: mapa_carta_pila.keySet()){  
			if(mapa_carta_pila.get(key).getNumero()== pila.getNumero()){
                lista_cartas.add(key);
            }
		}
        return lista_cartas; 
    }
    public void setPrecioPila (Pila_subasta pila, int precio){
        for (Pila_subasta key: pila_inversor.keySet()){  
			if(key.getNumero()== pila.getNumero()){
                key.setPrecio(precio);
            }
		}
    }
    public HashMap<Carta_acciones, Pila_subasta> getMapa(){
        return this.mapa_carta_pila;
    }
    public HashMap<Pila_subasta, Integer> getPujasMinimas(){
        return this.pila_puja_minima;
    }
    public void setPujaMinima (Pila_subasta pila, int puja_minima){
        boolean existe = false;
        for (Pila_subasta key: pila_puja_minima.keySet()){  
			if(pila.getNumero() == key.getNumero()){
                pila_puja_minima.put(key, puja_minima);
                existe = true;
            }
		}
        if (existe == false)
            pila_puja_minima.put(pila, puja_minima);
    }
    public int getPujaMinima (Pila_subasta pila){
        int result = 0;
        for (Pila_subasta key: pila_puja_minima.keySet()){  
			if(pila.getNumero() == key.getNumero())
                result = pila_puja_minima.get(key);
		}
        return result;
    }
    public void addInversorPila(Pila_subasta pila, AgentIdentifier id){
        boolean existe = false;
        for (Pila_subasta key: pila_inversor.keySet()){  
			if(pila.getNumero() == key.getNumero()){
                pila_inversor.put(key, id);
                existe = true;
            }        
		}
        if (existe == false)
            this.pila_inversor.put(pila, id);
    }
    public HashMap<Pila_subasta, AgentIdentifier> getMapaPilaInversor(){
        return this.pila_inversor;
    }
    public AgentIdentifier getInversorPorNumeroPila(int num){
        AgentIdentifier id = new AgentIdentifier();
        for (Pila_subasta key: pila_inversor.keySet()){  
			if(num == key.getNumero())
                id = pila_inversor.get(key);
		}
        return id;
    }
    public void unirMapas(){
        for (Carta_acciones carta: mapa_carta_pila.keySet()){  
			for (Pila_subasta pila: pila_inversor.keySet()){  
                    if(pila.getNumero() == mapa_carta_pila.get(carta).getNumero())
                        carta_inversor.put(carta, pila_inversor.get(pila));
                }
		}
    }
    public HashMap<Carta_acciones, AgentIdentifier> getMapaCartaInversor(){
        return this.carta_inversor;
    }
    public void addCartaInversor(Carta_acciones carta, AgentIdentifier id){
        carta_inversor.put(carta, id);        
    }
    
        

}