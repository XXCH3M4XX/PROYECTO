package gamestates;

import main.Juego;
import ui.BotonMenu;

import java.awt.event.MouseEvent;

public class State {

    protected Juego juego;
    public State(Juego juego){
        this.juego = juego;
    }

    public boolean isIn(MouseEvent e, BotonMenu mb){
        return mb.getBordes().contains(e.getX(), e.getY());
    }

    public Juego getJuego(){
        return juego;
    }

}
