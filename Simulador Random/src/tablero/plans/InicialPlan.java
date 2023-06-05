package tablero.plans;
import ontology.conceptos.*;
import ontology.predicados.*;
import ontology.acciones.*;
import tablero.beliefs.*;
import jadex.adapter.fipa.*;
import jadex.runtime.IGoal;
import jadex.runtime.Plan;
import jadex.runtime.IMessageEvent;
import java.util.Collections;
import jadex.adapter.fipa.AgentIdentifier;
import java.lang.Math;
import java.util.Random;
import java.util.ArrayList;

public class InicialPlan extends Plan {

    public void body()
    {
		
		Dinero dinero = new Dinero();
		//leer mensaje recibido
		IMessageEvent msgrec = (IMessageEvent) getInitialEvent();
		//guardamos el emisor
		AgentIdentifier sender= (AgentIdentifier) msgrec.getParameter(SFipa.SENDER).getValue();
		Participar_en_simulacion contenido = (Participar_en_simulacion) msgrec.getContent();
		String id_inversor = sender.getLocalName();
		int num_inversor = Character.getNumericValue(id_inversor.charAt(8));
		
		if(num_inversor ==1){
				System.out.println("COMIENZA LA SIMULACION");
				Inversores inversores = new Inversores();
				PrevisionesEmpresas previsiones_empresas = new PrevisionesEmpresas();
				Empresas empresas = new Empresas();
				MazoCartasEstrategiaInversion mazo_cartas = new MazoCartasEstrategiaInversion();
				MazoCartasAcciones mazo_acciones = new MazoCartasAcciones();
				Bonos bonos = new Bonos();
				InformacionGeneral informacion = new InformacionGeneral();
				PilasCartasAccion pilas_cartas = new PilasCartasAccion();

				informacion.setRonda(1);
				informacion.setFase("Informacion");
				//Empresas
				Empresa empresa1 = new Empresa();
				ArrayList<Empresa> lista_empresas = new ArrayList<Empresa>();
				empresa1.setIdentificador(1);
				empresa1.setValorAcciones(5);
				empresa1.setNombre("Cosmic Computers");
				lista_empresas.add(empresa1);
				Empresa empresa2 = new Empresa();
				empresa2.setValorAcciones(5);
				empresa2.setIdentificador(2);
				empresa2.setNombre("American Automotive");
				lista_empresas.add(empresa2);
				Empresa empresa3 = new Empresa();
				empresa3.setValorAcciones(5);
				empresa3.setIdentificador(3);
				empresa3.setNombre("Bottomline Bank");
				lista_empresas.add(empresa3);
				Empresa empresa4 = new Empresa();
				empresa4.setValorAcciones(5);
				empresa4.setIdentificador(4);
				empresa4.setNombre("Stanford Steel");
				lista_empresas.add(empresa4);
				Empresa empresa5 = new Empresa();
				empresa5.setValorAcciones(5);
				empresa5.setIdentificador(5);
				empresa5.setNombre("Leading Laboratories");
				lista_empresas.add(empresa5);
				Empresa empresa6 = new Empresa();
				empresa6.setValorAcciones(5);
				empresa6.setIdentificador(6);
				empresa6.setNombre("Epic Electric");
				lista_empresas.add(empresa6);
				empresas.setEmpresas(lista_empresas);
				//Previsiones de empresas
				Prevision prevision1 = new Prevision();
				ArrayList<Prevision> lista_previsiones = new ArrayList<Prevision>();
				prevision1.setTipo(2);
				prevision1.setCantidad(-1);
				lista_previsiones.add(prevision1);
				Prevision prevision2 = new Prevision();
				prevision2.setTipo(3);
				lista_previsiones.add(prevision2);
				Prevision prevision3 = new Prevision();
				prevision3.setTipo(2);
				prevision3.setCantidad(-3);
				lista_previsiones.add(prevision3);
				Prevision prevision4 = new Prevision();
				prevision4.setTipo(1);
				prevision4.setCantidad(2);
				lista_previsiones.add(prevision4);
				Prevision prevision5 = new Prevision();
				prevision5.setTipo(2);
				prevision5.setCantidad(-4);
				lista_previsiones.add(prevision5);
				Prevision prevision6 = new Prevision();
				prevision6.setTipo(1);
				prevision6.setCantidad(1);
				lista_previsiones.add(prevision6);
				previsiones_empresas.addPrevisionesEmpresas(lista_empresas, lista_previsiones);

				//Cartas de estrategia de inversion
				Carta_estrategia_de_inversion carta_manipulacion = new Carta_estrategia_de_inversion();
				carta_manipulacion.setTipo("manipulacion a la baja");
				carta_manipulacion.setDescripcion("Vende hasta tres acciones por 8000$ cada una");
				carta_manipulacion.setBorrada(false);
				mazo_cartas.addCarta(carta_manipulacion);
				Carta_estrategia_de_inversion carta_venta_en_corto = new Carta_estrategia_de_inversion();
				carta_venta_en_corto.setTipo("Venta en corto descubierta");
				carta_venta_en_corto.setDescripcion("Recibe 12000$ a cambio de una carta de subida o bajada de acciones.");
				carta_venta_en_corto.setBorrada(false);
				mazo_cartas.addCarta(carta_venta_en_corto);
				Carta_estrategia_de_inversion carta_crisis = new Carta_estrategia_de_inversion();
				carta_crisis.setTipo("Crisis sistemica");
				carta_crisis.setDescripcion("Mueve los precios de las acciones de tres compañias distintas +1/-1 a cambio de una carta de subida o bajada de acciones.");
				carta_crisis.setBorrada(false);
				mazo_cartas.addCarta(carta_crisis);
				Carta_estrategia_de_inversion carta_dividendos = new Carta_estrategia_de_inversion();
				carta_dividendos.setTipo("Dividendos especiales");
				carta_dividendos.setDescripcion("La compañia de tu eleccion paga 1000$ en dividendos.");
				carta_dividendos.setBorrada(false);
				mazo_cartas.addCarta(carta_dividendos);
				mazo_cartas.mezclarCartas();

				//Cartas de acciones, pago de comisiones y operaciones
				int numeroCartasAcciones = 10;
				int id = 0;
				for (int i = 0; i < numeroCartasAcciones; i++){
					Carta_acciones carta_acciones = new Carta_acciones();
					carta_acciones.setId(id+i);
					carta_acciones.setTipo(1);
					carta_acciones.setValor(5);
					carta_acciones.setEmpresa("Cosmic Computers");
					mazo_acciones.addCarta(carta_acciones);
				}
				id = 10;
				for (int i = 0; i < numeroCartasAcciones; i++){
					Carta_acciones carta_acciones = new Carta_acciones();
					carta_acciones.setId(id+i);
					carta_acciones.setTipo(1);
					carta_acciones.setValor(5);
					carta_acciones.setEmpresa("American Automotive");
					mazo_acciones.addCarta(carta_acciones);
				}
				id = 20;
				for (int i = 0; i < numeroCartasAcciones; i++){
					Carta_acciones carta_acciones = new Carta_acciones();
					carta_acciones.setId(id+i);
					carta_acciones.setTipo(1);
					carta_acciones.setValor(5);
					carta_acciones.setEmpresa("Bottomline Bank");
					mazo_acciones.addCarta(carta_acciones);
				}
				id = 30;
				for (int i = 0; i < numeroCartasAcciones; i++){
					Carta_acciones carta_acciones = new Carta_acciones();
					carta_acciones.setId(id+i);
					carta_acciones.setTipo(1);
					carta_acciones.setValor(5);
					carta_acciones.setEmpresa("Stanford Steel");
					mazo_acciones.addCarta(carta_acciones);
				}
				id = 40;
				for (int i = 0; i < numeroCartasAcciones; i++){
					Carta_acciones carta_acciones = new Carta_acciones();
					carta_acciones.setId(id+i);
					carta_acciones.setTipo(1);
					carta_acciones.setValor(5);
					carta_acciones.setEmpresa("Leading Laboratories");
					mazo_acciones.addCarta(carta_acciones);
				}
				id = 50;
				for (int i = 0; i < numeroCartasAcciones; i++){
					Carta_acciones carta_acciones = new Carta_acciones();
					carta_acciones.setId(id+i);
					carta_acciones.setTipo(1);
					carta_acciones.setValor(5);
					carta_acciones.setEmpresa("Epic Electric");
					mazo_acciones.addCarta(carta_acciones);
				}
				int numeroCartasOperaciones = 4;
				id = 60;
				for (int i = 0; i < numeroCartasOperaciones; i++){
					Carta_acciones carta_operaciones = new Carta_acciones();	
					carta_operaciones.setId(id+i);				
					carta_operaciones.setTipo(2);
					carta_operaciones.setTipoOperaciones(-2);
					mazo_acciones.addCarta(carta_operaciones);
				}
				id = 64;
				for (int i = 0; i < numeroCartasOperaciones; i++){
					Carta_acciones carta_operaciones = new Carta_acciones();
					carta_operaciones.setId(id+i);	
					carta_operaciones.setTipo(2);
					carta_operaciones.setTipoOperaciones(2);
					mazo_acciones.addCarta(carta_operaciones);
				}
				id = 68;
				int numeroCartasComisiones = 12;
				for (int i = 0; i < numeroCartasComisiones; i++){
					Carta_acciones carta_comisiones = new Carta_acciones();
					carta_comisiones.setId(id+i);	
					carta_comisiones.setTipo(3);
					carta_comisiones.setA_pagar(3000);
					mazo_acciones.addCarta(carta_comisiones);
				}
				
				informacion.setTurno(sender);
				System.out.println("--- Condiciones iniciales establecidas");
				mazo_acciones.mezclarCartas();   
				getBeliefbase().getBelief("Inversores").setFact(inversores);
				getBeliefbase().getBelief("PrevisionesEmpresas").setFact(previsiones_empresas);
				getBeliefbase().getBelief("Empresas").setFact(empresas);
				getBeliefbase().getBelief("MazoCartasEstrategiaInversion").setFact(mazo_cartas);
				getBeliefbase().getBelief("MazoCartasAcciones").setFact(mazo_acciones);   
				getBeliefbase().getBelief("Bonos").setFact(bonos);    
				getBeliefbase().getBelief("InformacionGeneral").setFact(informacion);   
				getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);  
		}
						
		if(num_inversor ==3){
			Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
			MazoCartasAcciones mazo_acciones = (MazoCartasAcciones) getBeliefbase().getBelief("MazoCartasAcciones").getFact();
			Empresas empresas = (Empresas) getBeliefbase().getBelief("Empresas").getFact();
			PilasCartasAccion pilas_cartas = (PilasCartasAccion) getBeliefbase().getBelief("PilasCartasAccion").getFact();
			//crear mensaje inform-result partida iniciada
			System.out.println ("--- Hay  tres inversores, se puede comenzar la simulacion");
			int dineroInversor = (int)(Math.random() * (40 - 20 + 1)) + 20;
			dineroInversor = dineroInversor * 1000;
			dinero.setCantidad(dineroInversor);    
            inversores.addId(sender);
			inversores.addIdDinero(sender, dinero);
			inversores.addIdCarta(sender, mazo_acciones.sacarAccion());
            Partida_iniciada partida_iniciada = new Partida_iniciada();
			partida_iniciada.setMapa(inversores.getMapDineroPorId());
			partida_iniciada.setListaEmpresas(empresas.getEmpresas());
			getBeliefbase().getBelief("Inversores").setFact(inversores);
	
			for (AgentIdentifier i : inversores.getIds()) {
				System.out.println( "--- El inversor: "+Character.getNumericValue(i.getLocalName().charAt(8))+ " tiene: " +inversores.getDineroPorId(i).getCantidad()+ "$");
			}
			System.out.println ("CARTAS DE ACCION INICIALES");
			for (AgentIdentifier i : inversores.getIds()) {
				IMessageEvent msg = createMessageEvent("Inform_Generico");
				msg.getParameterSet(SFipa.RECEIVERS).addValue(i);
				partida_iniciada.setAccion(inversores.getCartaPorId(i));
				pilas_cartas.addCartaInversor(inversores.getCartaPorId(i), i);
				msg.setContent(partida_iniciada);
				sendMessage(msg);
			}
			getBeliefbase().getBelief("PilasCartasAccion").setFact(pilas_cartas);
			getBeliefbase().getBelief("repartir_cartas_inversion").setFact(true);
            
		}
        if(num_inversor < 3){    
			Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
			MazoCartasAcciones mazo_acciones = (MazoCartasAcciones) getBeliefbase().getBelief("MazoCartasAcciones").getFact();
			System.out.println ("--- Esperando a que haya tres inversores para poder comenzar la simulacion");
			int dineroInversor = (int)(Math.random() * (40 - 20 + 1)) + 20;
			dineroInversor = dineroInversor * 1000;
			dinero.setCantidad(dineroInversor);    
            inversores.addId(sender);
			inversores.addIdDinero(sender, dinero);
			inversores.addIdCarta(sender, mazo_acciones.sacarAccion());
			getBeliefbase().getBelief("Inversores").setFact(inversores);
        } 
		if(num_inversor > 3) {
            System.out.println ("--- Error: Hay mas de tres inversores");
			Inversores inversores = (Inversores) getBeliefbase().getBelief("Inversores").getFact();
            //crear mensaje con predicado y concepto
			
            Detalle_causa_fallo_participacion detalle_causa_fallo_participacion= new Detalle_causa_fallo_participacion();
            detalle_causa_fallo_participacion.setDescripcion("Se ha superado el maximo de inversores en la simulacion, no puede participar");

            Causa_fallo_participacion causa_fallo_participacion = new Causa_fallo_participacion();
            causa_fallo_participacion.setQue(detalle_causa_fallo_participacion);

            //crear mensaje de failure
            IMessageEvent msg = createMessageEvent("Failure_Generico");
            msg.setContent(causa_fallo_participacion);
			msg.getParameterSet(SFipa.RECEIVERS).addValue(sender);
            sendMessage(msg);

        }
    }
}