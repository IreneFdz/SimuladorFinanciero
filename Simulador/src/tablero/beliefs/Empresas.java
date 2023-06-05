package tablero.beliefs;
import ontology.conceptos.Empresa;
import java.util.ArrayList;
import java.util.Collections;
import jadex.adapter.fipa.AgentIdentifier;

public class Empresas
{
	private ArrayList<Empresa> lista_empresas;

    public Empresas(){
        this.lista_empresas = new ArrayList<Empresa>();
    }

    public void addEmpresa(Empresa empresa){
        lista_empresas.add(empresa);
    }  
    public ArrayList<Empresa> getEmpresas (){
        return this.lista_empresas;
    }
    public void setEmpresas(ArrayList<Empresa> lista_empresas){
        this.lista_empresas= lista_empresas;
    } 
    public Empresa getEmpresa (int index){
        return this.lista_empresas.get(index);
    } 

}