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
                panelJuego.setDireccion(ARRIBA);
                panelJuego.setMovimiento(true);
                break;

            case KeyEvent.VK_A:
                panelJuego.setDireccion(IZQUIERDA);
                panelJuego.setMovimiento(true);
                break;

            case KeyEvent.VK_S:
                panelJuego.setDireccion(ABAJO);
                panelJuego.setMovimiento(true);
                break;

            case KeyEvent.VK_D:
                panelJuego.setDireccion(DERECHA);
                panelJuego.setMovimiento(true);
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
            case KeyEvent.VK_A:
            case KeyEvent.VK_S:
            case KeyEvent.VK_D:
                panelJuego.setMovimiento(false);
                break;

        }
    }

    //nigger
}