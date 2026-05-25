package inputs;

import gamestates.EstadoJuego;
import main.PanelJuego;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//gestiona las entradas del teclado y las traduce a acciones del jugador
public class KeyboardInputs implements KeyListener {

    //referencia al panel para poder acceder al jugador
    private PanelJuego panelJuego;

    //recibe el panel desde el que se registra este listener
    public KeyboardInputs(PanelJuego panelJuego){
        this.panelJuego = panelJuego;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }



    //activa el movimiento o el salto cuando se pulsa la tecla correspondiente
    @Override
    public void keyPressed(KeyEvent e) {
        switch(EstadoJuego.state){
            case INTRO:
                panelJuego.getJuego().getIntroScreen().keyPressed(e);
                break;
            case MENU:
                panelJuego.getJuego().getMenu().keyPressed(e);
                break;
            case PLAYING:
                panelJuego.getJuego().getPlaying().keyPressed(e);
                break;
            case OPTIONS:
                panelJuego.getJuego().opcionesDeJuego().keyPressed(e);
                break;
            case STATS:
                panelJuego.getJuego().getStats().keyPressed(e);
                break;
            case CONTROLES:
                panelJuego.getJuego().getControles().keyPressed(e);
                break;
            case NOMBRE:
                panelJuego.getJuego().getPantallaIntroducirNombre().keyPressed(e);
                break;
            default:
                break;
        }
    }

    //detiene el movimiento o libera el flag de salto cuando se suelta la tecla
    @Override
    public void keyReleased(KeyEvent e) {
        switch(EstadoJuego.state){
            case MENU:
                panelJuego.getJuego().getMenu().keyReleased(e);
                break;
            case PLAYING:
                panelJuego.getJuego().getPlaying().keyReleased(e);
                break;
            default:
                break;
        }
    }
}