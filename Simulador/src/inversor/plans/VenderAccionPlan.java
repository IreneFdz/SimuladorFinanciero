package inversor.plans;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import inversor.beliefs.*;
import java.util.ArrayList;
import java.util.HashMap;
import ontology.acciones.Vender_accion;
import ontology.conceptos.Carta_acciones;
import ontology.conceptos.Empresa;


import java.util.Random;

public class VenderAccionPlan extends Plan {

    public void body()
    {
        
        ServiceDescription sd = new ServiceDescription();
        sd.setName("tablero");
        AgentDescription dfadesc = new AgentDescription();
        dfadesc.addService(sd);
        SearchConstraints	sc	= new SearchConstraints();
        sc.setMaxResults(-1);
        IGoal ft = createGoal("df_search");
        ft.getParameter("description").setValue(dfadesc);
        ft.getParameter("constraints").setValue(sc);
        dispatchSubgoalAndWait(ft);
        AgentDescription[] result	= (AgentDescription[])ft.getParameterSet("result").getValues();
        if (result.length>0){
            MisCartasAcciones mis_cartas = (MisCartasAcciones) getBeliefbase().getBelief("MisCartasAcciones").getFact();
            AgentIdentifier id = new AgentIdentifier(getAgentName(), true); 
            InformacionGeneral informacion= (InformacionGeneral) getBeliefbase().getBelief("InformacionGeneral").getFact();
            Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
            Empresas empresas= (Empresas) getBeliefbase().getBelief("Empresas").getFact();
            PrevisionesEmpresas previsiones= (PrevisionesEmpresas) getBeliefbase().getBelief("PrevisionesEmpresas").getFact();
            //Comprobar si tengo al menos una accion 
            boolean tengo_accion = false;
            for (int i = 0; i< mis_cartas.getMisCartas().size(); i++) {
                if(mis_cartas.getMisCartas().get(i).getTipo() == 1)
                    tengo_accion = true;
            }
            // Calcular numero de acciones por empresa
            HashMap <Empresa, Integer> numero_acciones = new HashMap<Empresa, Integer>();
            for (Empresa empresa : empresas.getEmpresas()) {
                int numero_acciones_empresa = 0;
                for (Carta_acciones carta : mis_cartas.getMisCartas()) {
                    if (carta.getTipo() == 1 && carta.getEmpresa().equals(empresa.getNombre()))
                        numero_acciones_empresa ++;       
                }
                numero_acciones.put(empresa, numero_acciones_empresa);
            }
            // Elegir las acciones que quiero vender
            ArrayList <Carta_acciones> lista_acciones = new ArrayList <Carta_acciones>();
            int dinero_disponible = inversores.getDineroPorId(id).getCantidad();
            for (int i = 0; i< mis_cartas.getMisCartas().size(); i++) {
                if (mis_cartas.getMisCartas().get(i).getTipo() == 1){
                    int valor = 0;
                    int limite_dinero = 0;
                    int valor_accion = 0;
                    int acciones_empresa = 0;
                    for (int j = 0; j< empresas.getEmpresas().size(); j++){
                        if (mis_cartas.getMisCartas().get(i).getEmpresa().equals(empresas.getEmpresas().get(j).getNombre())){
                            valor = previsiones.getValorPrevisiones(empresas.getEmpresas().get(j));
                            acciones_empresa = numero_acciones.get(empresas.getEmpresas().get(j));
                            limite_dinero = 15000 - acciones_empresa * 1000;
                            valor_accion = mis_cartas.getMisCartas().get(i).getValor(); 
                        }
                    } 
                    if (valor < 0 && informacion.getRonda() != 7){
                        if (dinero_disponible < limite_dinero){
                            lista_acciones.add(mis_cartas.getMisCartas().get(i));
                            dinero_disponible += valor_accion;
                        }
                    }
                }   
            }
            Vender_accion vender_accion = new Vender_accion();
            vender_accion.setLista(lista_acciones);
            IMessageEvent msgsend = createMessageEvent("Request_Generico");
            AgentIdentifier tablero = result[0].getName();
            msgsend.getParameterSet(SFipa.RECEIVERS).addValue(tablero);
            msgsend.setContent(vender_accion);
            sendMessage(msgsend);    
            
    }
    else
        System.out.println("Tablero no encontrado");   
   
    }
}
