package inversor.beliefs;
import java.util.ArrayList;
import java.util.Collections;
import ontology.conceptos.Carta_estrategia_de_inversion;

public class Carta_estrategia_inversion
{
    private Carta_estrategia_de_inversion carta;

    public Carta_estrategia_inversion(){
        carta = new Carta_estrategia_de_inversion();
    }

    public Carta_estrategia_de_inversion getCartaEstrategia(){
        return carta;
    } 
    public void setCartaEstrategia(Carta_estrategia_de_inversion carta){
        this.carta = carta;
    }
    public void printCartaEstrategia(){
        System.out.println(carta.getDescripcion());
    }
}