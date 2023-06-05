package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import ontology.predicados.Recuento_final_hecho;
import ontology.conceptos.Empresa;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Bono;
import tablero.beliefs.*;
import java.util.HashMap;
import jadex.adapter.fipa.AgentIdentifier;

import java.util.Random;

public class RecuentoFinalPlan extends Plan {

    public void body()
    {
        System.out.println ("RECUENTO FINAL");
        PrevisionesEmpresas previsiones = (PrevisionesEmpresas) getBeliefbase().getBelief("PrevisionesEmpresas").getFact();
        PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
        Inversores inversores= (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        Bonos bonos= (Bonos) getBeliefbase().getBelief("Bonos").getFact();
        Empresas empresas= (Empresas) getBeliefbase().getBelief("Empresas").getFact();

        Recuento_final_hecho recuento_hecho = new Recuento_final_hecho();
        //Calculo de accionista mayoritario
        HashMap <AgentIdentifier, Integer> puntuacion = new HashMap<AgentIdentifier, Integer>();
        for (AgentIdentifier id : inversores.getIds()) {
                puntuacion.put(id, 0);
        }
        for (Empresa empresa : empresas.getEmpresas()) {
            HashMap <AgentIdentifier, Integer> numero_acciones = new HashMap<AgentIdentifier, Integer>();
            for (AgentIdentifier id : inversores.getIds()) {
                numero_acciones.put(id, 0);
            }
            for (Carta_acciones carta:pilas_cartas.getMapaCartaInversor().keySet()) {
                if (carta.getTipo() == 1 && carta.getEmpresa().equals(empresa.getNombre())){
                    numero_acciones.put(pilas_cartas.getMapaCartaInversor().get(carta), numero_acciones.get(pilas_cartas.getMapaCartaInversor().get(carta))+1);
                    //System.out.println("Se suma la accion de la empresa "+carta.getEmpresa()+ " identificador "+ carta.getId()+ " del inversor "+ Character.getNumericValue(pilas_cartas.getMapaCartaInversor().get(carta).getLocalName().charAt(8)));
                }
            }
            int mayor = 0;
            AgentIdentifier mayoritario = inversores.getIds().get(0);
            boolean empate = false;
            for (AgentIdentifier i : numero_acciones.keySet()) {
                if (numero_acciones.get(i) > mayor){
                    mayor = numero_acciones.get(i);
                    mayoritario = i;
                }else{
                    if (numero_acciones.get(i) == mayor &&  mayor != 0)
                        empate = true;
                }
            }
            if (empate){
                for (AgentIdentifier i : numero_acciones.keySet()) {
                    if (numero_acciones.get(i) == mayor)
                        puntuacion.put(i, puntuacion.get(i)+5000);
                }
            }if (empate == false && mayor != 0)
                puntuacion.put(mayoritario, puntuacion.get(mayoritario)+10000);
            
          
        }
        HashMap <AgentIdentifier, Integer> resultado = new HashMap<AgentIdentifier, Integer>();
        for (AgentIdentifier x : inversores.getIds()) {
            int puntos = inversores.getDineroPorId(x).getCantidad();
            for (Bono bono:bonos.getBonos().keySet()) {
                if (bonos.getBonos().get(bono).equals(x))
                    puntos += bono.getValor();
            }
            puntos += puntuacion.get(x);
            for (int i = 0; i<empresas.getEmpresas().size(); i++){
                for (Carta_acciones accion: pilas_cartas.getMapaCartaInversor().keySet()){  
                    if (empresas.getEmpresas().get(i).getNombre().equals(accion.getEmpresa())){
                        if (pilas_cartas.getMapaCartaInversor().get(accion).equals(x)){
                            puntos += empresas.getEmpresas().get(i).getValorAcciones()*1000;
                        }      
                    }            
                }
            }
            resultado.put(x, puntos);
        }
        int puntacion_mayor = 0;
        for (AgentIdentifier id : resultado.keySet()) {
            if (resultado.get(id) > puntacion_mayor)
                puntacion_mayor = resultado.get(id);    
        }
        System.out.println ("--- El inversor: "+ Character.getNumericValue(inversores.getIds().get(0).getLocalName().charAt(8)) + " tiene una puntuacion de: "+ resultado.get(inversores.getIds().get(0)));     
        System.out.println ("--- El inversor: "+ Character.getNumericValue(inversores.getIds().get(1).getLocalName().charAt(8)) + " tiene una puntuacion de: "+ resultado.get(inversores.getIds().get(1)));     
        System.out.println ("--- El inversor: "+ Character.getNumericValue(inversores.getIds().get(2).getLocalName().charAt(8)) + " tiene una puntuacion de: "+ resultado.get(inversores.getIds().get(2)));     

        for (AgentIdentifier id : resultado.keySet()) {
            if (resultado.get(id) == puntacion_mayor)
                System.out.println ("El ganador es el inversor numero: "+ Character.getNumericValue(id.getLocalName().charAt(8)));         
        }
        IMessageEvent msg = createMessageEvent("Inform_Generico");
        recuento_hecho.setMapa(resultado);
        for (AgentIdentifier x : inversores.getIds()) {
            msg.getParameterSet(SFipa.RECEIVERS).addValue(x);
            msg.setContent(recuento_hecho);   
        }
        System.out.println ("FIN DE LA SIMULACION");
        sendMessage(msg); 
    }
}