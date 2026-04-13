package inputs;

import gamestates.Gamestate;
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
        switch(Gamestate.state){
            case PLAYING:
                panelJuego.getJuego().getPlaying().mouseClicked(e);
                break;
            default:
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch(Gamestate.state){
            case MENU:
                panelJuego.getJuego().getMenu().mousePressed(e);
                break;
            case PLAYING:
                panelJuego.getJuego().getPlaying().mousePressed(e);
                break;
            default:
                break;
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch(Gamestate.state){
            case MENU:
                panelJuego.getJuego().getMenu().mouseReleased(e);
                break;
            case PLAYING:
                panelJuego.getJuego().getPlaying().mouseReleased(e);
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        switch(Gamestate.state){
            case PLAYING:
                panelJuego.getJuego().getPlaying().mouseDragged(e);
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch(Gamestate.state){
            case MENU:
                panelJuego.getJuego().getMenu().mouseMoved(e);
                break;
            case PLAYING:
                panelJuego.getJuego().getPlaying().mouseMoved(e);
                break;
            default:
                break;
        }
    }

}
