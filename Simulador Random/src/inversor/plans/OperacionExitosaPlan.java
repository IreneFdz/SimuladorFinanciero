package inversor.plans;
import inversor.beliefs.*;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import java.util.HashMap;
import jadex.adapter.fipa.AgentIdentifier;
import ontology.predicados.Utilizacion_carta_operaciones_exitosa;
import ontology.conceptos.Dinero;
import ontology.conceptos.Carta_acciones;
import inversor.beliefs.*;
import java.util.Iterator;

import java.util.Random;

public class OperacionExitosaPlan extends Plan {

    public void body()
    {
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Utilizacion_carta_operaciones_exitosa operacion_exitosa = (Utilizacion_carta_operaciones_exitosa)  respuestaTablero.getContent();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        MisCartasAcciones mis_cartas = (MisCartasAcciones) getBeliefbase().getBelief("MisCartasAcciones").getFact();
        Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        Empresas empresas = (Empresas) getBeliefbase().getBelief("Empresas").getFact();
        
        AgentIdentifier id = new AgentIdentifier(getAgentName(), true);       
        for (Carta_acciones carta: operacion_exitosa.getMapa().keySet()){ 
            for (int i = 0; i< empresas.getEmpresas().size(); i++){
                if (empresas.getEmpresas().get(i).getNombre().equals(operacion_exitosa.getMapa().get(carta).getNombre())){
                    empresas.getEmpresas().get(i).setValorAcciones(empresas.getEmpresas().get(i).getValorAcciones()+carta.getTipoOperaciones());
                    //Si la empresa esta en bancarrota
                    if (empresas.getEmpresas().get(i).getValorAcciones() < 1){
                        empresas.getEmpresas().get(i).setBancarrota(true);
                        empresas.getEmpresas().get(i).setValorAcciones(5);
                        for(Iterator<Carta_acciones> iterator = pilas_cartas.getMapaCartaInversor().keySet().iterator(); iterator.hasNext();){
                            Carta_acciones key = iterator.next();
                            if(key.getTipo() == 1 && key.getEmpresa().equals(operacion_exitosa.getMapa().get(carta).getNombre()))
                                iterator.remove();
                        }
                        Iterator<Carta_acciones> it = mis_cartas.getMisCartas().iterator();
                        while (it.hasNext()) {
                            Carta_acciones elem = it.next();
                            if (elem.getTipo() == 1 && elem.getEmpresa().equals(empresas.getEmpresas().get(i).getNombre())) {
                                it.remove();
                            }
                        }
                    } 
                    //Si la empresa tiene un valor maximo
                    if (empresas.getEmpresas().get(i).getValorAcciones() > 10){
                        empresas.getEmpresas().get(i).setMaximo(true);
                        empresas.getEmpresas().get(i).setValorAcciones(6);
                        for (Carta_acciones key: pilas_cartas.getMapaCartaInversor().keySet()){  
                            //Si las acciones de un inversor estan dobladas
                            if(key.getTipo() == 1 && key.getEmpresa().equals(operacion_exitosa.getMapa().get(carta).getNombre()) && key.getDoblada() == true){
                                Dinero dinero = new Dinero();
                                dinero.setCantidad(10000);
                                inversores.sumarDinero(pilas_cartas.getMapaCartaInversor().get(key), dinero);
                            }
                            if(key.getTipo() == 1 && key.getEmpresa().equals(operacion_exitosa.getMapa().get(carta).getNombre()))
                                key.setDoblada(true);
                        } 
                    }
                }
            }
        }
        if (id.equals(operacion_exitosa.getQuien())){
            for (Carta_acciones carta: operacion_exitosa.getMapa().keySet()){ 
                mis_cartas.borrarCartaPorId(carta.getId()); 
            } 
            
            getBeliefbase().getBelief("MisCartasAcciones").setFact(mis_cartas);
            getBeliefbase().getBelief("manipulacion").setFact(true); 
        }
        getBeliefbase().getBelief("MisCartasAcciones").setFact(mis_cartas);
        getBeliefbase().getBelief("Inversores").setFact(inversores);
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
    }
}