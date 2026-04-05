package inputs;

import main.PanelJuego;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static utils.Constantes.Direcciones.*;

public class KeyboardInputs implements KeyListener {

    private PanelJuego panelJuego;

    public KeyboardInputs(PanelJuego panelJuego){
        this.panelJuego = panelJuego;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Detecta cuando una tecla es pulsada. Si coincide con las teclas de dirección (WASD),
     * actualiza la dirección en el panel de juego y activa el estado de movimiento.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_W:
                panelJuego.getJuego().getJugador().setArriba(true);
                break;
            case KeyEvent.VK_A:
                panelJuego.getJuego().getJugador().setIzquierda(true);
                break;
            case KeyEvent.VK_S:
                panelJuego.getJuego().getJugador().setAbajo(true);
                break;
            case KeyEvent.VK_D:
                panelJuego.getJuego().getJugador().setDerecha(true);
                break;

        }
    }

    /**
     * Detecta cuando se deja de presionar una tecla. Si se suelta cualquiera de las
     * teclas de movimiento, se detiene el desplazamiento del jugador.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_W:
                panelJuego.getJuego().getJugador().setArriba(false);
                break;
            case KeyEvent.VK_A:
                panelJuego.getJuego().getJugador().setIzquierda(false);
                break;
            case KeyEvent.VK_S:
                panelJuego.getJuego().getJugador().setAbajo(false);
                break;
            case KeyEvent.VK_D:
                panelJuego.getJuego().getJugador().setDerecha(false);
                break;

        }
    }

    //nigger
}