package inputs;

import main.PanelJuego;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInputs implements MouseListener, MouseMotionListener {

    private PanelJuego panelJuego;
    public MouseInputs(PanelJuego panelJuego){

        this.panelJuego = panelJuego;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1){
            panelJuego.getJuego().getJugador().setAtaque(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        panelJuego.requestFocusInWindow();

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {


    }
    //nigger
}
