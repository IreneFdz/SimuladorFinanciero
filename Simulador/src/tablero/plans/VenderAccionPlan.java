package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.acciones.Vender_accion;
import ontology.predicados.Causa_fallo_venta_accion;
import ontology.predicados.Venta_accion_exitosa;
import ontology.conceptos.Detalle_causa_fallo_vender_accion;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Dinero;
import ontology.conceptos.Empresa;
import tablero.beliefs.*;
import java.util.Iterator;
import java.util.ArrayList;

import java.util.Random;

public class VenderAccionPlan extends Plan {

    public void body()
    {
        //leer mensaje recibido
		IMessageEvent msgrec = (IMessageEvent) getInitialEvent();
		//guardamos el emisor
		AgentIdentifier sender= (AgentIdentifier) msgrec.getParameter(SFipa.SENDER).getValue();
		Vender_accion contenido = (Vender_accion) msgrec.getContent();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        Inversores inversores= (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        Empresas empresas= (Empresas) getBeliefbase().getBelief("Empresas").getFact();

        
        //Comprobar si el inversor tiene las acciones
        boolean lista_valida = true;
        boolean no_vacio = false;
        for (Carta_acciones accion: contenido.getLista()){  
            no_vacio = true;
            boolean carta = false;
            for (Carta_acciones key: pilas_cartas.getMapaCartaInversor().keySet()){  
                if (accion.getId() == key.getId() && pilas_cartas.getMapaCartaInversor().get(key).equals(sender))
                    carta = true;     
            }
            if (carta == false)
                lista_valida = false;       
        }
        if (no_vacio)
            System.out.println ("VENTA DE ACCIONES");
        if (lista_valida){
            Venta_accion_exitosa venta_exitosa = new Venta_accion_exitosa();
           
            //Borrar las acciones del inversor
            if (contenido.getLista().size() != 0){
                for(Iterator<Carta_acciones> iterator = pilas_cartas.getMapaCartaInversor().keySet().iterator(); iterator.hasNext();){
                    Carta_acciones key = iterator.next();
                    for (Carta_acciones accion: contenido.getLista()){  
                        if(accion.getId() == key.getId())
                            iterator.remove();         
                    }
                }
            }
            int valorLista = 0;
            for (int i = 0; i<empresas.getEmpresas().size(); i++){
                for (Carta_acciones accion: contenido.getLista()){  
                    if (empresas.getEmpresas().get(i).getNombre().equals(accion.getEmpresa()))
                        valorLista += empresas.getEmpresas().get(i).getValorAcciones();      
                }
            }
            Dinero dinero = new Dinero();
            dinero.setCantidad(valorLista*1000);
            //Sumar el dinero coorespondiente a la venta al inversor
            inversores.sumarDinero(sender, dinero);
            for (Carta_acciones accion: contenido.getLista()){  
               System.out.println ("--- El inversor: "+ Character.getNumericValue(sender.getLocalName().charAt(8)) + " ha vendido la accion: "+ accion.getEmpresa());         
            }
            venta_exitosa.setLista(contenido.getLista());
            venta_exitosa.setQuien(sender);
            venta_exitosa.setCuanto(dinero);

                for (AgentIdentifier x : inversores.getIds()) {
                    IMessageEvent msg = createMessageEvent("Inform_Generico");
                    msg.getParameterSet(SFipa.RECEIVERS).addValue(x);
                    msg.setContent(venta_exitosa);
                    sendMessage(msg);
                }
            
        }
        if (lista_valida == false){
			Detalle_causa_fallo_vender_accion detalle_causa_fallo_vender_accion= new Detalle_causa_fallo_vender_accion();
            detalle_causa_fallo_vender_accion.setDescripcion("El inversor no tiene las acciones que quiere vender.");

            Causa_fallo_venta_accion causa_fallo_venta_accion = new Causa_fallo_venta_accion();
            causa_fallo_venta_accion.setQue(detalle_causa_fallo_vender_accion);

            //crear mensaje de failure
            IMessageEvent msg = createMessageEvent("Failure_Generico");
            msg.setContent(causa_fallo_venta_accion);
			msg.getParameterSet(SFipa.RECEIVERS).addValue(sender);
            sendMessage(msg);
        }       
        getBeliefbase().getBelief("Inversores").setFact(inversores);
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas); 
        getBeliefbase().getBelief("cambiar_turno").setFact(false);   
    }
}