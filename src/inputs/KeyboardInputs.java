package inputs;

import gamestates.Gamestate;
import main.PanelJuego;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static utils.Constantes.Direcciones.*;

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

    //evita que el key repeat del sistema operativo dispare multiples saltos
    private boolean espacioAnterior = false;

    //activa el movimiento o el salto cuando se pulsa la tecla correspondiente
    @Override
    public void keyPressed(KeyEvent e) {
        switch(Gamestate.state){
            case MENU:
                panelJuego.getJuego().getMenu().keyPressed(e);
                break;
            case PLAYING:
                panelJuego.getJuego().getPlaying().keyPressed(e);
                break;
            default:
                break;
        }
    }

    //detiene el movimiento o libera el flag de salto cuando se suelta la tecla
    @Override
    public void keyReleased(KeyEvent e) {
        switch(Gamestate.state){
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