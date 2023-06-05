package tablero.beliefs;
import ontology.conceptos.Carta_estrategia_de_inversion;
import java.util.ArrayList;
import java.util.Collections;
import jadex.adapter.fipa.AgentIdentifier;

public class MazoCartasEstrategiaInversion
{
	private ArrayList<Carta_estrategia_de_inversion> mazo_cartas;

    public MazoCartasEstrategiaInversion(){
        this.mazo_cartas = new ArrayList<Carta_estrategia_de_inversion>();
    }

    public void addCarta(Carta_estrategia_de_inversion carta){
        this.mazo_cartas.add(carta);
    }
    public void mezclarCartas(){
        Collections.shuffle(mazo_cartas);
    }
    public Carta_estrategia_de_inversion getCarta(int index){
        Carta_estrategia_de_inversion carta = new Carta_estrategia_de_inversion();
        carta = mazo_cartas.get(index);
        return carta;
    }    

}