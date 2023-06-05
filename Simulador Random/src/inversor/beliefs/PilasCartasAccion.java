package inversor.beliefs;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Pila_subasta;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import jadex.adapter.fipa.AgentIdentifier;

public class PilasCartasAccion
{
	private HashMap<Carta_acciones, Pila_subasta> mapa_carta_pila;
    private ArrayList<Carta_acciones> lista_cartas;
    private HashMap<Pila_subasta, Integer> mapa_pila_ocultas;
    private HashMap<Pila_subasta, Integer> pila_puja_minima;
    private HashMap<Pila_subasta, AgentIdentifier> pila_inversor;
    private HashMap<Carta_acciones, AgentIdentifier> carta_inversor;

    public PilasCartasAccion(){
        this.mapa_pila_ocultas = new HashMap<Pila_subasta, Integer>();
        this.pila_puja_minima = new HashMap<Pila_subasta, Integer>();
        this.mapa_carta_pila = new HashMap<Carta_acciones, Pila_subasta>();
        this.lista_cartas = new ArrayList<Carta_acciones>();
        this.pila_inversor = new HashMap<Pila_subasta, AgentIdentifier>();
        this.carta_inversor = new HashMap<Carta_acciones, AgentIdentifier>();
    }
    public void setMapa(HashMap<Carta_acciones, Pila_subasta> mapa){
        this.mapa_carta_pila = mapa;
    }
    public void establecerCondicionesIniciales(){
        mapa_carta_pila.clear();
        pila_puja_minima.clear();
        pila_inversor.clear();   
        mapa_pila_ocultas.clear(); 
    }
    public void addCartaPila(Carta_acciones carta, Pila_subasta pila){
        boolean yaEsta = false;
        for (Carta_acciones key: mapa_carta_pila.keySet()){  
			if(key.getId() == carta.getId()){
                yaEsta = true;
            }
		}
        if (yaEsta == false)
            this.mapa_carta_pila.put(carta, pila);
    }
    public ArrayList<Carta_acciones> getCartasPila (Pila_subasta pila){
        for (Carta_acciones key: mapa_carta_pila.keySet()){  
			if(mapa_carta_pila.get(key).equals(pila)){
                lista_cartas.add(key);
            }
		}
        return this.lista_cartas; 
    }
    public ArrayList<Carta_acciones> getCartasPila (int pila){
        for (Carta_acciones key: mapa_carta_pila.keySet()){  
			if(mapa_carta_pila.get(key).getNumero() == pila){
                lista_cartas.add(key);
            }
		}
        return this.lista_cartas; 
    }
    public HashMap<Carta_acciones, Pila_subasta> getMapa(){
        return this.mapa_carta_pila;
    }
    public HashMap<Pila_subasta, Integer> getMapaOcultas(){
        return this.mapa_pila_ocultas;
    }
    public int getNumOcultas(int pila){
        int result = 0;
        for (Pila_subasta key: mapa_pila_ocultas.keySet()){  
			if(key.getNumero() == pila)
                result = mapa_pila_ocultas.get(key);
		}
        return result;
    }
    public void addOculta (Pila_subasta pila){
        int num = 1;
        for (Pila_subasta key: mapa_pila_ocultas.keySet()){  
			if(key.equals(pila)){
                num = mapa_pila_ocultas.get(key) +1;
            }
		}
        mapa_pila_ocultas.put(pila, num);
    }
    public HashMap<Pila_subasta, Integer> getPujasMinimas(){
        return this.pila_puja_minima;
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
    public int getPujaMinima (int pila){
        int result = 0;
        for (Pila_subasta key: pila_puja_minima.keySet()){  
			if(pila == key.getNumero())
                result = pila_puja_minima.get(key);
		}
        return result;
    }
    public void setMapaPilaInversor(HashMap<Pila_subasta, AgentIdentifier> mapa){
        this.pila_inversor = mapa;
    }
    public HashMap<Pila_subasta, AgentIdentifier> getMapaPilaInversor(){
        return this.pila_inversor;
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
        boolean yaEsta = false;
        for (Carta_acciones key: carta_inversor.keySet()){  
			if(key.getId() == carta.getId()){
                yaEsta = true;
            }
		}
        if (yaEsta == false)
            this.carta_inversor.put(carta, id);        
    }

}