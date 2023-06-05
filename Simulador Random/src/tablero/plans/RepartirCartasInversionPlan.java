package tablero.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import tablero.beliefs.*;
import ontology.predicados.Cartas_inversion_repartidas;

import java.util.Random;

public class RepartirCartasInversionPlan extends Plan {

    public void body()
    {
        System.out.println("REPARTIR CARTAS INVERSION");
        MazoCartasEstrategiaInversion mazo_cartas = (MazoCartasEstrategiaInversion) getBeliefbase().getBelief("MazoCartasEstrategiaInversion").getFact();
        Inversores inversores= (Inversores) getBeliefbase().getBelief("Inversores").getFact();
        mazo_cartas.mezclarCartas();
        int x = 0;
        for (AgentIdentifier i : inversores.getIds()) {
                Cartas_inversion_repartidas cartas_repartidas = new Cartas_inversion_repartidas();
				IMessageEvent msg = createMessageEvent("Inform_Generico");
				msg.getParameterSet(SFipa.RECEIVERS).addValue(i);
				cartas_repartidas.setCarta(mazo_cartas.getCarta(x));
                inversores.addIdInversion(i, mazo_cartas.getCarta(x));
            	msg.setContent(cartas_repartidas);
				sendMessage(msg);
                x++;
			}
        inversores.printIdInversion();
        getBeliefbase().getBelief("fase").setFact("Informacion");   
    }
}