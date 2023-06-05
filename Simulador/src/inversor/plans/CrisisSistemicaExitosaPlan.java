package inversor.plans;
import inversor.beliefs.*;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import java.util.HashMap;
import jadex.adapter.fipa.AgentIdentifier;
import ontology.predicados.Utilizacion_carta_crisis_sistemica_exitosa;
import ontology.conceptos.Dinero;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Empresa;
import inversor.beliefs.*;
import java.util.Iterator;

import java.util.Random;

public class CrisisSistemicaExitosaPlan extends Plan {

    public void body()
    {
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Utilizacion_carta_crisis_sistemica_exitosa crisis_exitosa = (Utilizacion_carta_crisis_sistemica_exitosa)  respuestaTablero.getContent();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        Carta_estrategia_inversion mi_carta = (Carta_estrategia_inversion) getBeliefbase().getBelief("Carta_estrategia_inversion").getFact();
        MisCartasAcciones mis_cartas = (MisCartasAcciones) getBeliefbase().getBelief("MisCartasAcciones").getFact();
        Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        Empresas empresas= (Empresas) getBeliefbase().getBelief("Empresas").getFact();

        //Borrar la carta de operaciones del inversor
        AgentIdentifier id = new AgentIdentifier(getAgentName(), true); 
        for(Iterator<Carta_acciones> iterator = pilas_cartas.getMapaCartaInversor().keySet().iterator(); iterator.hasNext();){
            Carta_acciones key = iterator.next();
            if(crisis_exitosa.getQue().getId() == key.getId())
                iterator.remove();
        }
        //Borrar la carta de operaciones y la carta de estrategia
        
        if (id.equals(crisis_exitosa.getQuien())){
            for (int i = 0; i<mis_cartas.getMisCartas().size(); i++) {
                if(mis_cartas.getMisCartas().get(i).getId() == crisis_exitosa.getQue().getId()){
                    mis_cartas.getMisCartas().remove(i);
                    break;
                }      
            } 
            mi_carta.getCartaEstrategia().setBorrada(true);
        }
        //Actualizar valor de las empresas
        for (Empresa key: crisis_exitosa.getMapa().keySet()){  
            for (int i = 0; i<empresas.getEmpresas().size(); i++){
                if (empresas.getEmpresas().get(i).getNombre().equals(key.getNombre())){
                    empresas.getEmpresas().get(i).setValorAcciones(crisis_exitosa.getMapa().get(key).getDecision() + empresas.getEmpresas().get(i).getValorAcciones());
                
                    //Si la empresa esta en bancarrota
                    if (empresas.getEmpresas().get(i).getValorAcciones() < 1){
                        empresas.getEmpresas().get(i).setBancarrota(true);
                        empresas.getEmpresas().get(i).setValorAcciones(5);
                        for(Iterator<Carta_acciones> iterator = pilas_cartas.getMapaCartaInversor().keySet().iterator(); iterator.hasNext();){
                            Carta_acciones carta = iterator.next();
                            if(carta.getTipo() == 1 && carta.getEmpresa().equals(empresas.getEmpresas().get(i).getNombre())) 
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
                        for (Carta_acciones carta: pilas_cartas.getMapaCartaInversor().keySet()){  
                            //Si las acciones de un inversor estan dobladas
                            if(carta.getTipo() == 1 && carta.getEmpresa().equals(empresas.getEmpresas().get(i).getNombre()) && carta.getDoblada() == true){
                                Dinero dinero = new Dinero();
                                dinero.setCantidad(10000);
                                inversores.sumarDinero(pilas_cartas.getMapaCartaInversor().get(carta), dinero);
                            }
                            if(carta.getTipo() == 1 && carta.getEmpresa().equals(empresas.getEmpresas().get(i).getNombre()))
                                carta.setDoblada(true);
                        } 
                        for (Carta_acciones carta: mis_cartas.getMisCartas()){  
                            if(carta.getTipo() == 1 && carta.getEmpresa().equals(empresas.getEmpresas().get(i).getNombre()) && carta.getDoblada() == true){
                                Dinero dinero = new Dinero();
                                dinero.setCantidad(10000);
                                inversores.sumarDinero(id, dinero);
                            }
                            if(carta.getTipo() == 1 && carta.getEmpresa().equals(empresas.getEmpresas().get(i).getNombre()))
                                carta.setDoblada(true);    
                        }
                    }
                }
            }
        }
        
        getBeliefbase().getBelief("Inversores").setFact(inversores);
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
        getBeliefbase().getBelief("Carta_estrategia_inversion").setFact(mi_carta);
        getBeliefbase().getBelief("MisCartasAcciones").setFact(mis_cartas);
        getBeliefbase().getBelief("Empresas").setFact(empresas);
        getBeliefbase().getBelief("cambiar_turno").setFact(true);
        
    }
}