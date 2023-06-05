package tablero.beliefs;
import ontology.conceptos.Empresa;
import ontology.conceptos.Prevision;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import jadex.adapter.fipa.AgentIdentifier;

public class PrevisionesEmpresas
{
	private HashMap<Empresa, Prevision> mapa;

    public PrevisionesEmpresas(){
        this.mapa = new HashMap<Empresa, Prevision>();
    }

    public void addPrevisionesEmpresas(ArrayList<Empresa> lista_empresas, ArrayList<Prevision> lista_previsiones){
        Collections.shuffle(lista_empresas);        
		Collections.shuffle(lista_previsiones);
        for (int x = 0; x < lista_empresas.size(); x++) {
            mapa.put(lista_empresas.get(x), lista_previsiones.get(x));
        }
    }  
    public void mezclarPrevisiones(){
        ArrayList<Prevision> lista_previsiones = new ArrayList<Prevision>();
        for (Empresa key: mapa.keySet()){
            lista_previsiones.add(mapa.get(key));
        }      
		Collections.shuffle(lista_previsiones);
        int index = 0;
        for (Empresa key: mapa.keySet()){
            mapa.put(key, lista_previsiones.get(index));
            index++;
        }
    }
    public Prevision getPrevision(Empresa empresa){
        return mapa.get(empresa);
    }
    public HashMap<Empresa, Prevision> getPrevisiones(){
        return mapa;
    }
    
}