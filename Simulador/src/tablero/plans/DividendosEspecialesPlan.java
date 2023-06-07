package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.acciones.Utilizar_dividendos_especiales;
import ontology.predicados.Causa_fallo_dividendos_especiales;
import ontology.predicados.Utilizacion_carta_dividendos_especiales_exitosa;
import ontology.conceptos.Empresa;
import ontology.conceptos.Dinero;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Detalle_causa_fallo_estrategia_inversion;
import tablero.beliefs.*;
import java.util.Iterator;
import java.util.HashMap;

import java.util.Random;

public class DividendosEspecialesPlan extends Plan {

    public void body()
    {
        System.out.println ("DIVIDENDOS ESPECIALES");
        //leer mensaje recibido
		IMessageEvent msgrec = (IMessageEvent) getInitialEvent();
		//guardamos el emisor
		AgentIdentifier sender= (AgentIdentifier) msgrec.getParameter(SFipa.SENDER).getValue();
		Utilizar_dividendos_especiales contenido = (Utilizar_dividendos_especiales) msgrec.getContent();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        Inversores inversores= (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        Empresas empresas= (Empresas) getBeliefbase().getBelief("Empresas").getFact();

        //Comprobar si el inversor tiene la carta y es de tipo estrategia
        boolean tiene_carta = false;
        if (contenido.getCarta().getTipo().equals("Dividendos especiales") && inversores.getInversionPorId(sender).getTipo().equals(contenido.getCarta().getTipo())){
            if (contenido.getCarta().getBorrada() == false)
                tiene_carta = true;
        }
        
        //Comprobar si la empresa es correcta
        boolean empresa_correcta = false;
        for (int i = 0; i<empresas.getEmpresas().size(); i++){
            if (empresas.getEmpresas().get(i).getNombre().equals(contenido.getEmpresa().getNombre()))
                empresa_correcta = true;
        }   
        if (empresa_correcta && tiene_carta){
            Utilizacion_carta_dividendos_especiales_exitosa dividendos_exitosa = new Utilizacion_carta_dividendos_especiales_exitosa();
                        
            //Borrar la carta de estrategia de inversion del inversor
            for(Iterator<AgentIdentifier> iterator = inversores.getMapaInversion().keySet().iterator(); iterator.hasNext();){
                AgentIdentifier key = iterator.next();
                if(contenido.getCarta().getDescripcion().equals(inversores.getMapaInversion().get(key).getDescripcion())){
                    inversores.getMapaInversion().get(key).setBorrada(true);
                }
            }
            HashMap <Carta_acciones, AgentIdentifier> mapa = new HashMap <Carta_acciones, AgentIdentifier>();
            //Pagar 1000 de dividendos por accion a los inversores con acciones de esa empresa
            for (AgentIdentifier x : inversores.getIds()) {
                //System.out.println ("--- El inversor: "+ Character.getNumericValue(x.getLocalName().charAt(8)) + " tiene: "+ inversores.getDineroPorId(x).getCantidad()+ "$");         
            }
            
            for (Carta_acciones carta: pilas_cartas.getMapaCartaInversor().keySet()){  
                if(carta.getTipo() == 1 && carta.getEmpresa().equals(contenido.getEmpresa().getNombre())){
                    Dinero dinero = new Dinero();
                    dinero.setCantidad(1000);
                    inversores.sumarDinero(pilas_cartas.getMapaCartaInversor().get(carta), dinero);
                    mapa.put(carta, pilas_cartas.getMapaCartaInversor().get(carta));
                }
            } 
            System.out.println ("--- El inversor: "+ Character.getNumericValue(sender.getLocalName().charAt(8)) + " ha decidido que la empresa: "+ contenido.getEmpresa().getNombre()+" pague 1000$ de dividendos.");         
            dividendos_exitosa.setQue(contenido.getEmpresa());
            dividendos_exitosa.setQuien(sender);
            dividendos_exitosa.setMapa(mapa);
                for (AgentIdentifier x : inversores.getIds()) {
                    IMessageEvent msg = createMessageEvent("Inform_Generico");
                    msg.getParameterSet(SFipa.RECEIVERS).addValue(x);
                    msg.setContent(dividendos_exitosa);
                    sendMessage(msg);
                }
        }
        if (tiene_carta == false){

            Detalle_causa_fallo_estrategia_inversion detalle_causa_fallo_estrategia_inversion= new Detalle_causa_fallo_estrategia_inversion();
            detalle_causa_fallo_estrategia_inversion.setDescripcion("El inversor no tiene la carta necesaria para utilizar los dividendos especiales.");

            Causa_fallo_dividendos_especiales causa_fallo_dividendos_especiales = new Causa_fallo_dividendos_especiales();
            causa_fallo_dividendos_especiales.setQue(detalle_causa_fallo_estrategia_inversion);

            //crear mensaje de failure
            IMessageEvent msg = createMessageEvent("Failure_Generico");
            msg.setContent(causa_fallo_dividendos_especiales);
			msg.getParameterSet(SFipa.RECEIVERS).addValue(sender);
            sendMessage(msg);
        }

        if (empresa_correcta == false){
			
            Detalle_causa_fallo_estrategia_inversion detalle_causa_fallo_estrategia_inversion= new Detalle_causa_fallo_estrategia_inversion();
            detalle_causa_fallo_estrategia_inversion.setDescripcion("La empresa elegida no es correcta, no se pueden obtener los dividendos especiales.");

            Causa_fallo_dividendos_especiales causa_fallo_dividendos_especiales = new Causa_fallo_dividendos_especiales();
            causa_fallo_dividendos_especiales.setQue(detalle_causa_fallo_estrategia_inversion);

            //crear mensaje de failure
            IMessageEvent msg = createMessageEvent("Failure_Generico");
            msg.setContent(causa_fallo_dividendos_especiales);
			msg.getParameterSet(SFipa.RECEIVERS).addValue(sender);
            sendMessage(msg);
        }
       
        getBeliefbase().getBelief("Inversores").setFact(inversores);
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
        getBeliefbase().getBelief("cambiar_turno").setFact(false);
    }
}
