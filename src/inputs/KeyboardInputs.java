package inputs;

import main.PanelJuego;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {

    private PanelJuego panelJuego;

    public KeyboardInputs(PanelJuego panelJuego){
        this.panelJuego = panelJuego;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_W:
                panelJuego.changeYDelta(-5);
                break;
            case KeyEvent.VK_A:
                panelJuego.changeXDelta(-5);
                break;
            case KeyEvent.VK_S:
                panelJuego.changeYDelta(5);
                break;
            case KeyEvent.VK_D:
                panelJuego.changeXDelta(5);
                break;

        }
    }

    //nigger
}
