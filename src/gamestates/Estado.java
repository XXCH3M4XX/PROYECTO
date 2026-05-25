package gamestates;

import audio.AudioPlayer;
import main.Juego;
import ui.BotonMenu;

import java.awt.event.MouseEvent;

//clase base de la que heredan todos los estados del juego (menu, jugando, pausa, etc)
public class Estado {

    //referencia al juego principal para acceder a sus sistemas
    protected Juego juego;

    public Estado(Juego juego) {
        this.juego = juego;
    }

    //comprueba si el raton esta dentro de los bordes de un boton del menu
    public boolean isIn(MouseEvent e, BotonMenu mb) {
        return mb.getBordes().contains(e.getX(), e.getY());
    }

    public Juego getJuego() {
        return juego;
    }

    public void setEstadoJuego(EstadoJuego estado) {
        switch(estado) {
            case INTRO:
                juego.getAudioPlayer().playCancion(AudioPlayer.creditosIniciales);
                break;
            case MENU:
                juego.getAudioPlayer().setCancionNivel(AudioPlayer.menu);
                break;
            case PLAYING:
                juego.getAudioPlayer().setCancionNivel(AudioPlayer.musicaNiveles);
                break;

        }
        EstadoJuego.state = estado;
    }
}