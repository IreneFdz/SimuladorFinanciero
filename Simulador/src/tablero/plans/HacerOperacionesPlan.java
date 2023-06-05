package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.acciones.Utilizar_carta_operaciones;
import ontology.predicados.Utilizacion_carta_operaciones_exitosa;
import ontology.predicados.Causa_fallo_carta_operaciones;
import ontology.conceptos.Detalle_causa_fallo_carta_operaciones;
import ontology.conceptos.Dinero;
import ontology.conceptos.Carta_acciones;
import tablero.beliefs.*;
import java.util.Random;
import java.util.Iterator;

public class HacerOperacionesPlan extends Plan {

    public void body()
    {
        //leer mensaje recibido
		IMessageEvent msgrec = (IMessageEvent) getInitialEvent();
		//guardamos el emisor
		AgentIdentifier sender= (AgentIdentifier) msgrec.getParameter(SFipa.SENDER).getValue();
		Utilizar_carta_operaciones contenido = (Utilizar_carta_operaciones) msgrec.getContent();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        Inversores inversores= (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        Empresas empresas= (Empresas) getBeliefbase().getBelief("Empresas").getFact();

        //Comprobar si el inversor tiene las cartas y son de tipo operaciones
        boolean lista_valida = true;
        boolean no_vacio = false;

        for (Carta_acciones carta: contenido.getMapa().keySet()){  
            boolean carta_valida = false;
            no_vacio = true;
            for (Carta_acciones key: pilas_cartas.getMapaCartaInversor().keySet()){  
                if (carta.getId() == key.getId() && key.getTipo() == 2 && pilas_cartas.getMapaCartaInversor().get(key).equals(sender))
                    carta_valida = true;     
            }
            if (carta_valida == false)
                lista_valida = false;       
        } 
        if (no_vacio)
            System.out.println ("HACER OPERACIONES");
        //Comprobar si la empresa es correcta
        boolean empresas_correctas = true;
        for (Carta_acciones carta: contenido.getMapa().keySet()){  
            boolean empresa_correcta = false;
            for (int i = 0; i< empresas.getEmpresas().size(); i++){
                if (empresas.getEmpresas().get(i).getNombre().equals(contenido.getMapa().get(carta).getNombre()))
                    empresa_correcta = true;
            }
            if (empresa_correcta == false)
                empresas_correctas = false;       
        } 
        if (empresas_correctas && lista_valida){
            Utilizacion_carta_operaciones_exitosa operaciones_exitosas = new Utilizacion_carta_operaciones_exitosa();
            //Cambiar el valor a la empresa
            for (Carta_acciones carta: contenido.getMapa().keySet()){  
                for (int i = 0; i< empresas.getEmpresas().size(); i++){
                    if (empresas.getEmpresas().get(i).getNombre().equals(contenido.getMapa().get(carta).getNombre())){
                        System.out.println("--- La empresa: "+ empresas.getEmpresas().get(i).getNombre()+ "tiene un valor de: "+empresas.getEmpresas().get(i).getValorAcciones());
                        empresas.getEmpresas().get(i).setValorAcciones(empresas.getEmpresas().get(i).getValorAcciones()+carta.getTipoOperaciones());
                        //Si la empresa esta en bancarrota
                        if (empresas.getEmpresas().get(i).getValorAcciones() < 1){
                            System.out.println("--- ¡Empresa en barcarrota!");
                            empresas.getEmpresas().get(i).setBancarrota(true);
                            empresas.getEmpresas().get(i).setValorAcciones(5);
                            for(Iterator<Carta_acciones> iterator = pilas_cartas.getMapaCartaInversor().keySet().iterator(); iterator.hasNext();){
                                Carta_acciones key = iterator.next();
                                    if(key.getTipo() == 1 && key.getEmpresa().equals(contenido.getMapa().get(carta).getNombre())) {
                                        iterator.remove();
                                    }
                            }
                        }
                        //Si la empresa tiene un valor maximo
                        if (empresas.getEmpresas().get(i).getValorAcciones() > 10){
                            System.out.println("--- ¡Empresa en maximo valor!");
                            empresas.getEmpresas().get(i).setMaximo(true);
                            empresas.getEmpresas().get(i).setValorAcciones(6);
                            for (Carta_acciones key: pilas_cartas.getMapaCartaInversor().keySet()){  
                                //Si las acciones de un inversor estan dobladas
                                if(key.getTipo() == 1 && key.getEmpresa().equals(contenido.getMapa().get(carta).getNombre()) && key.getDoblada() == true){
                                    Dinero dinero = new Dinero();
                                    dinero.setCantidad(10000);
                                    inversores.sumarDinero(pilas_cartas.getMapaCartaInversor().get(key), dinero);
                                }
                                if(key.getTipo() == 1 && key.getEmpresa().equals(contenido.getMapa().get(carta).getNombre()))
                                    key.setDoblada(true);
                            } 
                        }
                        System.out.println("--- El inversor: "+ Character.getNumericValue(sender.getLocalName().charAt(8))+ " ha modificado el valor de la empresa: "+ empresas.getEmpresas().get(i).getNombre());
                        System.out.println("--- La empresa: "+ empresas.getEmpresas().get(i).getNombre()+ "tiene un valor de: "+empresas.getEmpresas().get(i).getValorAcciones());
                     
                        
                    }
                }
            }
            operaciones_exitosas.setMapa(contenido.getMapa());
            operaciones_exitosas.setQuien(sender);
            for (AgentIdentifier x : inversores.getIds()) {
                IMessageEvent msg = createMessageEvent("Inform_Generico");
                msg.getParameterSet(SFipa.RECEIVERS).addValue(x);
                msg.setContent(operaciones_exitosas);
                sendMessage(msg);
            }
            //Buscar las cartas de operaciones y borrarlas
            for (Carta_acciones carta: contenido.getMapa().keySet()){  
                for (Carta_acciones key: pilas_cartas.getMapaCartaInversor().keySet()){  
                    if(key.getId()==carta.getId()){
                        pilas_cartas.getMapaCartaInversor().remove(key);
                        break;
                    }
                }     
            } 
        }
        if (lista_valida == false){
            System.out.println ("El inversor no tiene la carta para hacer operaciones");
            //crear mensaje con predicado y concepto
			
            Detalle_causa_fallo_carta_operaciones detalle_causa_fallo_carta_operaciones= new Detalle_causa_fallo_carta_operaciones();
            detalle_causa_fallo_carta_operaciones.setDescripcion("El inversor no tiene la carta necesaria para hacer la operacion requerida.");

            Causa_fallo_carta_operaciones causa_fallo_carta_operaciones = new Causa_fallo_carta_operaciones();
            causa_fallo_carta_operaciones.setQue(detalle_causa_fallo_carta_operaciones);

            //crear mensaje de failure
            IMessageEvent msg = createMessageEvent("Failure_Generico");
            msg.setContent(causa_fallo_carta_operaciones);
			msg.getParameterSet(SFipa.RECEIVERS).addValue(sender);
            sendMessage(msg);
        }

        if (empresas_correctas == false){
            System.out.println ("La empresa elegida para la operacion no es valida.");
            //crear mensaje con predicado y concepto
			
            Detalle_causa_fallo_carta_operaciones detalle_causa_fallo_carta_operaciones= new Detalle_causa_fallo_carta_operaciones();
            detalle_causa_fallo_carta_operaciones.setDescripcion("La empresa elegida para la operacion no es valida.");

            Causa_fallo_carta_operaciones causa_fallo_carta_operaciones = new Causa_fallo_carta_operaciones();
            causa_fallo_carta_operaciones.setQue(detalle_causa_fallo_carta_operaciones);

            //crear mensaje de failure
            IMessageEvent msg = createMessageEvent("Failure_Generico");
            msg.setContent(causa_fallo_carta_operaciones);
			msg.getParameterSet(SFipa.RECEIVERS).addValue(sender);
            sendMessage(msg);
        }
       
        getBeliefbase().getBelief("Inversores").setFact(inversores);
        getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
        getBeliefbase().getBelief("cambiar_turno").setFact(false);
    }
}