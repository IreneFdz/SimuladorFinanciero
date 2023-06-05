package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import jadex.adapter.fipa.AgentIdentifier;
import ontology.predicados.Cotizacion_empresas_actualizadas;
import ontology.conceptos.Dinero;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Empresa;
import ontology.conceptos.Prevision;
import inversor.beliefs.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.Random;

public class ActualizarCotizacionesPlan extends Plan {

    public void body()
    {
        IMessageEvent respuestaTablero = (IMessageEvent) getInitialEvent();
        Cotizacion_empresas_actualizadas cotizacion_actualizada = (Cotizacion_empresas_actualizadas)  respuestaTablero.getContent();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        MisCartasAcciones mis_cartas = (MisCartasAcciones) getBeliefbase().getBelief("MisCartasAcciones").getFact();
        Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        Empresas empresas= (Empresas) getBeliefbase().getBelief("Empresas").getFact();
        AgentIdentifier id = new AgentIdentifier(getAgentName(), true); 
        //Actualizar mapa de cartas
        for (Carta_acciones carta: cotizacion_actualizada.getMapa().keySet()){  
            pilas_cartas.addCartaInversor(carta, cotizacion_actualizada.getMapa().get(carta));
        }
        //Actualizar valor de las empresas
        for (int i = 0; i<empresas.getEmpresas().size(); i++){
            int tipo = cotizacion_actualizada.getPrevisionTipo(empresas.getEmpresas().get(i));
            int cantidad = cotizacion_actualizada.getPrevisionCantidad(empresas.getEmpresas().get(i));
            
            if (tipo == 1 || tipo == 2){
                empresas.getEmpresas().get(i).setValorAcciones(cantidad + empresas.getEmpresas().get(i).getValorAcciones());
                //Si la empresa esta en bancarrota
                if (empresas.getEmpresas().get(i).getValorAcciones() < 1){
                    empresas.getEmpresas().get(i).setBancarrota(true);
                    empresas.getEmpresas().get(i).setValorAcciones(5);
                    for(Iterator<Carta_acciones> iterator = pilas_cartas.getMapaCartaInversor().keySet().iterator(); iterator.hasNext();){
                        Carta_acciones carta = iterator.next();
                        if(carta.getTipo() == 1 && carta.getEmpresa().equals(empresas.getEmpresas().get(i).getNombre())) 
                            iterator.remove();
                    }
                    for(Iterator<Carta_acciones> iterator = mis_cartas.getMisCartas().iterator(); iterator.hasNext();){
                        Carta_acciones elem = iterator.next();
                        if (elem.getTipo() == 1 && elem.getEmpresa().equals(empresas.getEmpresas().get(i).getNombre())){
                            iterator.remove();
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
                        //Si las acciones de un inversor estan dobladas
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
            if (tipo ==3){
                //Pagar 1000 de dividendos por accion a los inversores con acciones de esa empresa
                for (Carta_acciones carta: pilas_cartas.getMapaCartaInversor().keySet()){
                    if (carta.getTipo() == 1){
                        if(carta.getEmpresa().equals(empresas.getEmpresas().get(i).getNombre())){
                            Dinero dinero = new Dinero();
                            if(carta.getDoblada() == true){
                                dinero.setCantidad(2000);
                                inversores.sumarDinero(pilas_cartas.getMapaCartaInversor().get(carta), dinero);
                            }else{
                                dinero.setCantidad(1000);
                                inversores.sumarDinero(pilas_cartas.getMapaCartaInversor().get(carta), dinero);
                            }
                        }
                    }  
                }     
            }
        }
        
        getBeliefbase().getBelief("Inversores").setFact(inversores);
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
        getBeliefbase().getBelief("MisCartasAcciones").setFact(mis_cartas);
        getBeliefbase().getBelief("Empresas").setFact(empresas);
        
        
    }
}