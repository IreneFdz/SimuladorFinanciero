package tablero.beliefs;
import java.util.ArrayList;
import java.util.Collections;
import jadex.adapter.fipa.AgentIdentifier;
import java.util.HashMap;

public class InformacionGeneral
{
    private int ronda;
    private String fase;
    private int contador_acciones;
    private AgentIdentifier turno_actual;
    private boolean han_participado_todos;
    private boolean obtener_oferta;
    private boolean repetir_puja;
    private boolean primera_puja;
   

    public InformacionGeneral(){
        ronda = 0;
        fase = "Informacion";
        contador_acciones = 0;
        han_participado_todos = false;
        obtener_oferta = false;
        repetir_puja = false;
        primera_puja = false;
    }

    public int getRonda(){
        return this.ronda;
    } 
    public void setRonda(int ronda){
        this.ronda = ronda;
    }
    public String getFase(){
        return this.fase;
    } 
    public void setFase(String fase){
        this.fase = fase;
    }
    public int getContadorAcciones(){
        return this.contador_acciones;
    } 
    public void addContadorAcciones(){
        this.contador_acciones++;
    }
    public AgentIdentifier getTurno(){
        return this.turno_actual;
    } 
    public void setTurno(AgentIdentifier turno){
        this.turno_actual = turno;
    }
    public boolean getHanParticipado(){
        return this.han_participado_todos;
    } 
    public void setHanParticipado(boolean han_participado){
        this.han_participado_todos = han_participado;
    }
    public boolean getObtenerOferta(){
        return this.obtener_oferta;
    } 
    public void setObtenerOferta(boolean obtener_oferta){
        this.obtener_oferta = obtener_oferta;
    }
    public boolean getRepetirPuja(){
        return this.repetir_puja;
    } 
    public void setRepetirPuja(boolean repetir_puja){
        this.repetir_puja = repetir_puja;
    }
    public boolean getPrimeraPuja(){
        return this.primera_puja;
    } 
    public void setPrimeraPuja(boolean primera_puja){
        this.primera_puja = primera_puja;
    }
} 
