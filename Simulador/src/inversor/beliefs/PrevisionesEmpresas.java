package inversor.beliefs;
import ontology.conceptos.Empresa;
import ontology.conceptos.Prevision;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import jadex.adapter.fipa.AgentIdentifier;

public class PrevisionesEmpresas
{
	private HashMap<Empresa, Prevision> mapa;
    private ArrayList<Integer> previsiones_posibles;

    public PrevisionesEmpresas(){
        this.mapa = new HashMap<Empresa, Prevision>();
        this.previsiones_posibles = new ArrayList<Integer>();
    }
    public void addPrevision(Empresa empresa, Prevision prevision) {
		this.mapa.put(empresa,prevision);
	} 
    public void setPrevisiones(HashMap<Empresa, Prevision> mapa) {
		this.mapa = mapa;
	}   
    public Prevision getPrevision(Empresa empresa){
        return mapa.get(empresa);
    }
    public HashMap<Empresa, Prevision> getPrevisiones(){
        return this.mapa;
    }
    private ArrayList <Integer> getPrevisionesNoConocidas(){
        ArrayList <Integer> previsiones_no_conocidas = new ArrayList<Integer>();
        previsiones_posibles.add(-1);
        previsiones_posibles.add(0);
        previsiones_posibles.add(-3);
        previsiones_posibles.add(2);
        previsiones_posibles.add(-4);
        previsiones_posibles.add(1);
        for (Integer x: previsiones_posibles){ 
            boolean encontrada = false; 
            for (Empresa key: mapa.keySet()){  
                if ((mapa.get(key).getTipo() == 1 || mapa.get(key).getTipo() == 2) && mapa.get(key).getCantidad() == x)
                    encontrada = true;
                if (mapa.get(key).getTipo() == 3 && mapa.get(key).getCantidad() == 0)
                    encontrada = true;
            }
            if (encontrada == false)
                previsiones_no_conocidas.add(x);
		}
        return previsiones_no_conocidas;
    }
    private int getMediaPrevisionesNoConocidas(){
        ArrayList <Integer> no_conocidas = getPrevisionesNoConocidas();
        int suma = 0;
        int num_no_conocidas = 4;
        for (Integer x: no_conocidas){  
            if (x == 0)
                suma += 1;
            if (x != 0)
                suma += x;
		}
        int media = suma / num_no_conocidas;
        return media;
    }
    public int getValorPrevisiones(Empresa empresa){
        ArrayList <Integer> previsiones_no_conocidas = getPrevisionesNoConocidas();
        int valor = 0;
        int media = getMediaPrevisionesNoConocidas();
        boolean es_conocida = false;
        for (Empresa key: mapa.keySet()){  
            if (empresa.getNombre().equals(key.getNombre())){
                valor = mapa.get(key).getCantidad();
                es_conocida = true;
            }
        }
        if (es_conocida == false)
            valor = media;
        
        return valor;
    }
    public void printPrevisiones() {
        for (Empresa key: mapa.keySet()){  
			System.out.println(key.getNombre()+ " tiene una prevision de " + mapa.get(key).getTipo()+ " "+mapa.get(key).getCantidad());
		} 
	} 

}