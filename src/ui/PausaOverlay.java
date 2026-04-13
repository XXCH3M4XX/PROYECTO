package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Juego;
import utils.Constantes;
import utils.LoadSave;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static utils.Constantes.UI.BotonesPausa.*;
import static utils.Constantes.UI.URMBotones.*;
import static utils.Constantes.UI.BotonesVolumen.*;

public class PausaOverlay {
    private Playing playing;
    private BufferedImage fondo;
    //estas variables son para definir el tamaño de la imagen de pausa
    private int fnX, fnY, fnW, fnH;
    private BotonesDeSonido botonMusica, sfxBoton;
    private UrmBoton menuB, replayB, unpauseB;
    private VolumeButton volumeButton;

    public PausaOverlay(Playing playing) {
        this.playing = playing;
        cargarFondo();
        crearBotonesSonido();
        crearURMBotones();
        crearBotonDeVolumen();
    }

    private void crearBotonDeVolumen() {
        int vX = (int)(309 * Juego.ESCALA);
        int vY = (int)(278 * Juego.ESCALA);
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    private void crearURMBotones() {
        int menuX = (int)(313 * Juego.ESCALA);
        int replayX = (int)(387 * Juego.ESCALA);
        int unpauseX = (int)(462 * Juego.ESCALA);
        int bY = (int)(325 * Juego.ESCALA);

        menuB = new UrmBoton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmBoton(replayX, bY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new UrmBoton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
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
        menuB.update();
        replayB.update();
        unpauseB.update();
        volumeButton.update();

    }
    public void draw(Graphics g) {
        //fondo
        g.drawImage(fondo, fnX, fnY, fnW, fnH, null);
        //botones sonido
        botonMusica.draw(g);
        sfxBoton.draw(g);
        //URM botones
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);
        //volumen boton
        volumeButton.draw(g);
    }

    public void mouseMoved(MouseEvent e) {
        botonMusica.setMouseOver(false);
        sfxBoton.setMouseOver(false);
        menuB.setMouseOver(false);
        unpauseB.setMouseOver(false);
        replayB.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if(isIn(e, botonMusica)){
            botonMusica.setMouseOver(true);
        } else if (isIn(e, sfxBoton)) {
            sfxBoton.setMouseOver(true);
        }else if (isIn(e, menuB)) {
            menuB.setMouseOver(true);
        }else if (isIn(e, unpauseB)) {
            unpauseB.setMouseOver(true);
        }else if (isIn(e, replayB)) {
            replayB.setMouseOver(true);
        }else if (isIn(e, volumeButton)) {
            volumeButton.setMouseOver(true);
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
        }else if (isIn(e, menuB)) {
            if (menuB.isMousePressed()){
                Gamestate.state = Gamestate.MENU;
                playing.unpauseJuego();
            }
        }else if (isIn(e, replayB)) {
            if (replayB.isMousePressed()){
                System.out.println("replay lvl!");
            }
        }else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed()){
                playing.unpauseJuego();
            }
        }

        botonMusica.resetBools();
        sfxBoton.resetBools();
        menuB.resetBools();
        unpauseB.resetBools();
        replayB.resetBools();
        volumeButton.resetBools();
    }

    public void mousePressed(MouseEvent e) {
        if(isIn(e, botonMusica)){
            botonMusica.setMousePressed(true);
        } else if (isIn(e, sfxBoton)) {
            sfxBoton.setMousePressed(true);
        } else if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        }else if (isIn(e, replayB)) {
            replayB.setMousePressed(true);
        }else if (isIn(e, unpauseB)) {
            unpauseB.setMousePressed(true);
        }else if (isIn(e, volumeButton)) {
            volumeButton.setMousePressed(true);
        }
    }
    public void mouseDragged(MouseEvent e) {
        if(volumeButton.isMousePressed()){
            volumeButton.changeX(e.getX());
        }
    }
    private boolean isIn(MouseEvent e, BotonesPausa p){
        return (p.getBordes().contains(e.getX(), e.getY()));
    }


}
