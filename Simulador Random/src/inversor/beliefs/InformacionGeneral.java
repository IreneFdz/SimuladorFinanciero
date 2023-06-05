package inversor.beliefs;
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
   

    public InformacionGeneral(){
        contador_acciones = 0;
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
    public void setContador(int contador){
        this.contador_acciones = contador;
    }
    public AgentIdentifier getTurno(){
        return this.turno_actual;
    } 
    public void setTurno(AgentIdentifier turno){
        this.turno_actual = turno;
    }
} 
