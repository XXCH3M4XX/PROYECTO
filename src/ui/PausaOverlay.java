package ui;

import main.Juego;
import utils.Constantes;
import utils.LoadSave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static utils.Constantes.UI.BotonesPausa.*;

public class PausaOverlay {

    private BufferedImage fondo;
    //estas variables son para definir el tamaño de la imagen de pausa
    private int fnX, fnY, fnW, fnH;
    private BotonesDeSonido botonMusica, sfxBoton;
    public PausaOverlay() {
        cargarFondo();
        crearBotonesSonido();
    }

    private void crearBotonesSonido() {
        int sonidoX = (int)(450 * Juego.ESCALA);
        int musicaY = (int)(140 * Juego.ESCALA);
        int sfxY = (int)(186 * Juego.ESCALA);
        botonMusica = new BotonesDeSonido(sonidoX, musicaY, TAMAÑO_SONIDO, TAMAÑO_SONIDO);
        sfxBoton =  new BotonesDeSonido(sonidoX, sfxY, TAMAÑO_SONIDO, TAMAÑO_SONIDO);
    }

    //este metodo, establece la posicion y el tamaño del menu pausa
    private void cargarFondo() {
        fondo = LoadSave.GetSpriteAtlas(LoadSave.FONDO_PAUSA);
        fnW = (int)(fondo.getWidth() * Juego.ESCALA);
        fnH = (int)(fondo.getHeight() * Juego.ESCALA);
        fnX = Juego.GAME_WIDTH / 2 - fnW / 2;
        fnY = (int) (25 * Juego.ESCALA);
    }

    public void actualizar() {
        botonMusica.update();
        sfxBoton.update();

    }
    public void draw(Graphics g) {
        //fondo
        g.drawImage(fondo, fnX, fnY, fnW, fnH, null);
        //botones sonido
        botonMusica.draw(g);
        sfxBoton.draw(g);
    }

    public void mouseMoved(MouseEvent e) {
        botonMusica.setMouseOver(false);
        sfxBoton.setMouseOver(false);
        

        if(isIn(e, botonMusica)){
            botonMusica.setMouseOver(true);
        } else if (isIn(e, sfxBoton)) {
            sfxBoton.setMouseOver(true);
        }
    }
    public void mouseReleased(MouseEvent e) {
        if(isIn(e, botonMusica)){
            if(botonMusica.isMousePressed()){
                botonMusica.setMuted(!botonMusica.isMuted());
            }
        } else if (isIn(e, sfxBoton)) {
            if (sfxBoton.isMousePressed()){
                sfxBoton.setMuted(!sfxBoton.isMuted());
            }
        }
        botonMusica.resetBools();
        sfxBoton.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if(isIn(e, botonMusica)){
            botonMusica.setMousePressed(true);
        } else if (isIn(e, sfxBoton)) {
            sfxBoton.setMousePressed(true);
        }
    }
    public void mouseDragged(MouseEvent e) {

    }
    private boolean isIn(MouseEvent e, BotonesPausa p){
        return (p.getBordes().contains(e.getX(), e.getY()));
    }

}
