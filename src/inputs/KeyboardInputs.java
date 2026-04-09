package inputs;

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
        switch(e.getKeyCode()){
            case KeyEvent.VK_A:
                panelJuego.getJuego().getJugador().setIzquierda(true);
                break;
            case KeyEvent.VK_D:
                panelJuego.getJuego().getJugador().setDerecha(true);
                break;
            case KeyEvent.VK_SPACE:
                if (!espacioAnterior) {
                    panelJuego.getJuego().getJugador().setSalto(true);
                    espacioAnterior = true;
                }
                break;
        }
    }

    //detiene el movimiento o libera el flag de salto cuando se suelta la tecla
    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_A:
                panelJuego.getJuego().getJugador().setIzquierda(false);
                break;
            case KeyEvent.VK_D:
                panelJuego.getJuego().getJugador().setDerecha(false);
                break;
            case KeyEvent.VK_SPACE:
                espacioAnterior = false;
                panelJuego.getJuego().getJugador().setSalto(false);
                break;
        }
    }
}