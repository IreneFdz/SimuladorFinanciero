package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import tablero.beliefs.*;
import ontology.predicados.Previsiones_asignadas;

import java.util.Random;

public class AsignarPrevisionesPlan extends Plan {

    public void body()
    {
        System.out.println("ASIGNAR PREVISIONES");
        PrevisionesEmpresas previsiones_empresas = (PrevisionesEmpresas) getBeliefbase().getBelief("PrevisionesEmpresas").getFact();
        Empresas empresas= (Empresas) getBeliefbase().getBelief("Empresas").getFact();
        Inversores inversores= (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        int x = 0;
        previsiones_empresas.mezclarPrevisiones();
		//Poner bonos ronda a 0
		Bonos bonos = (Bonos) getBeliefbase().getBelief("Bonos").getFact();
        if (previsiones_empresas.getPrevision(empresas.getEmpresa(3)).getCantidad() == 0){
            System.out.println( "--- Informacion publica: la empresa "+ empresas.getEmpresa(3).getNombre()+ " tiene una prevision de reparto de dividendos");
        }else{
            System.out.println( "--- Informacion publica: la empresa "+ empresas.getEmpresa(3).getNombre()+ " tiene una prevision de " +previsiones_empresas.getPrevision(empresas.getEmpresa(3)).getCantidad());
        }
        for (AgentIdentifier i : inversores.getIds()) {
                Previsiones_asignadas previsiones_asignadas = new Previsiones_asignadas();
				IMessageEvent msg = createMessageEvent("Inform_Generico");
				msg.getParameterSet(SFipa.RECEIVERS).addValue(i);
				previsiones_asignadas.addPrevision(empresas.getEmpresa(x), previsiones_empresas.getPrevision(empresas.getEmpresa(x)));
                previsiones_asignadas.addPrevision(empresas.getEmpresa(3), previsiones_empresas.getPrevision(empresas.getEmpresa(3)));
                if (previsiones_empresas.getPrevision(empresas.getEmpresa(x)).getCantidad() == 0){
                     System.out.println( "--- El inversor: "+Character.getNumericValue(i.getLocalName().charAt(8))+ 
                    " conoce que la empresa: "+ empresas.getEmpresa(x).getNombre()+ " tiene una prevision de reparto de dividendos");
                }else{
                    System.out.println( "--- El inversor: "+Character.getNumericValue(i.getLocalName().charAt(8))+ 
                    " conoce que la empresa: "+ empresas.getEmpresa(x).getNombre()+ " tiene una prevision de " +previsiones_empresas.getPrevision(empresas.getEmpresa(x)).getCantidad());
                }
            	msg.setContent(previsiones_asignadas);
				sendMessage(msg);
                x++;
                bonos.putBonoRonda(i , 0);
			}
        getBeliefbase().getBelief("Bonos").setFact(bonos);
        getBeliefbase().getBelief("cambiar_turno").setFact(true);
    }
}