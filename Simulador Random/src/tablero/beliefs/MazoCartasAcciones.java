package tablero.beliefs;
import ontology.conceptos.Carta_acciones;
import java.util.ArrayList;
import java.util.Collections;
import jadex.adapter.fipa.AgentIdentifier;

public class MazoCartasAcciones
{
	private ArrayList<Carta_acciones> mazo_cartas;

    public MazoCartasAcciones(){
        this.mazo_cartas = new ArrayList<Carta_acciones>();
    }

    public void addCarta(Carta_acciones carta){
        this.mazo_cartas.add(carta);
    }
    public void mezclarCartas(){
        Collections.shuffle(mazo_cartas);
    }
    public Carta_acciones sacarCarta(){
        Carta_acciones carta = new Carta_acciones();
        carta = mazo_cartas.get(0);
        mazo_cartas.remove(0);
        return carta;
    }  
    public Carta_acciones sacarCartaComisiones(){
        Carta_acciones carta = new Carta_acciones();
        for (int i = 0; i<mazo_cartas.size(); i++){
            if (mazo_cartas.get(i).getTipo() == 3){
                carta = mazo_cartas.get(i);
                mazo_cartas.remove(i);
                break;
            }
        }
        return carta;
    }  
    public Carta_acciones sacarAccion(){
        Carta_acciones carta = new Carta_acciones();
        for (int i = 0; i<mazo_cartas.size(); i++){
            if (mazo_cartas.get(i).getTipo() == 1){
                carta = mazo_cartas.get(i);
                mazo_cartas.remove(i);
                break;
            }
        }
        return carta;
    }  

}