package tablero.plans;
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
import tablero.beliefs.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

public class ActualizarCotizacionesPlan extends Plan {

    public void body()
    {
        
        System.out.println ("ACTUALIZAR COTIZACIONES");
        PrevisionesEmpresas previsiones = (PrevisionesEmpresas) getBeliefbase().getBelief("PrevisionesEmpresas").getFact();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        Inversores inversores= (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        Empresas empresas= (Empresas) getBeliefbase().getBelief("Empresas").getFact();

        Cotizacion_empresas_actualizadas cotizacion_actualizada = new Cotizacion_empresas_actualizadas();
        HashMap <Carta_acciones, AgentIdentifier> mapa = new HashMap <Carta_acciones, AgentIdentifier>();
        //Actualizar valor de las empresas 
        for (int i = 0; i<empresas.getEmpresas().size(); i++){
            Prevision prevision = previsiones.getPrevision(empresas.getEmpresas().get(i));
            int tipo = prevision.getTipo();
            
            if (tipo == 1 || tipo == 2){
                int cantidad = prevision.getCantidad();
                empresas.getEmpresas().get(i).setValorAcciones(cantidad + empresas.getEmpresas().get(i).getValorAcciones());
                System.out.println ("--- La empresa:  "+ empresas.getEmpresas().get(i).getNombre()+" ahora tiene un valor de: "+empresas.getEmpresas().get(i).getValorAcciones());
                //Si la empresa esta en bancarrota
                if (empresas.getEmpresas().get(i).getValorAcciones() < 1){
                    System.out.println("--- Empresa en bancarrota");
                    empresas.getEmpresas().get(i).setBancarrota(true);
                    empresas.getEmpresas().get(i).setValorAcciones(5);
                    for(Iterator<Carta_acciones> iterator = pilas_cartas.getMapaCartaInversor().keySet().iterator(); iterator.hasNext();){
                        Carta_acciones carta = iterator.next();
                        if(carta.getTipo() == 1 && carta.getEmpresa().equals(empresas.getEmpresas().get(i).getNombre())) 
                            iterator.remove();
                    }
                }
                //Si la empresa tiene un valor maximo
                if (empresas.getEmpresas().get(i).getValorAcciones() > 10){
                    System.out.println("--- Empresa en maximo valor");
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
                }
            }
            if (tipo ==3){
                //Pagar 1000 de dividendos por accion a los inversores con acciones de esa empresa
                System.out.println ("--- La empresa:  "+ empresas.getEmpresas().get(i).getNombre()+" ha pagado dividendos.");
                for (Carta_acciones carta: pilas_cartas.getMapaCartaInversor().keySet()){  
                    if(carta.getTipo() == 1){
                        if(carta.getEmpresa().equals(empresas.getEmpresas().get(i).getNombre())){
                            Dinero dinero = new Dinero();
                            if(carta.getDoblada() == true){
                                dinero.setCantidad(2000);
                                inversores.sumarDinero(pilas_cartas.getMapaCartaInversor().get(carta), dinero);
                                mapa.put(carta, pilas_cartas.getMapaCartaInversor().get(carta));
                            }else{
                                dinero.setCantidad(1000);
                                inversores.sumarDinero(pilas_cartas.getMapaCartaInversor().get(carta), dinero);
                                mapa.put(carta, pilas_cartas.getMapaCartaInversor().get(carta));
                            }
                        }    
                    }
                }     
            }
        } 
        cotizacion_actualizada.setMapa(mapa);
        cotizacion_actualizada.setPrevisiones(previsiones.getPrevisiones());
        for (AgentIdentifier x : inversores.getIds()) {
            IMessageEvent msg = createMessageEvent("Inform_Generico");
            msg.getParameterSet(SFipa.RECEIVERS).addValue(x);
            msg.setContent(cotizacion_actualizada);
            sendMessage(msg);
        }
        getBeliefbase().getBelief("cambiar_fase").setFact(true);
        getBeliefbase().getBelief("cambiar_turno").setFact(false);
    }
}