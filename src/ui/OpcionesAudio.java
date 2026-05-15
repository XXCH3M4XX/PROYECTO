package ui;

import gamestates.Gamestate;
import main.Juego;

import java.awt.*;
import java.awt.event.MouseEvent;

import static utils.Constantes.UI.BotonesPausa.TAMAÑO_SONIDO;
import static utils.Constantes.UI.BotonesVolumen.SLIDER_WIDTH;
import static utils.Constantes.UI.BotonesVolumen.VOLUME_HEIGHT;

public class OpcionesAudio {

    private VolumeButton volumeButton;
    private BotonesDeSonido botonMusica, sfxBoton;

    private Juego juego;
    public OpcionesAudio(Juego juego){
        this.juego = juego;
        crearBotonesSonido();
        crearBotonDeVolumen();
    }

    //crea el slider de volumen y lo coloca en su posicion escalada
    private void crearBotonDeVolumen() {
        int vX = (int)(309 * Juego.ESCALA);
        int vY = (int)(278 * Juego.ESCALA);
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    //crea los botones de silencio de musica y sfx y los coloca en su posicion escalada
    private void crearBotonesSonido() {
        int sonidoX = (int)(450 * Juego.ESCALA);
        int musicaY = (int)(140 * Juego.ESCALA);
        int sfxY = (int)(186 * Juego.ESCALA);
        botonMusica = new BotonesDeSonido(sonidoX, musicaY, TAMAÑO_SONIDO, TAMAÑO_SONIDO);
        sfxBoton = new BotonesDeSonido(sonidoX, sfxY, TAMAÑO_SONIDO, TAMAÑO_SONIDO);
    }

    public void update(){
        botonMusica.update();
        sfxBoton.update();
        volumeButton.update();
    }

    public void draw(Graphics g){
        botonMusica.draw(g);
        sfxBoton.draw(g);
        volumeButton.draw(g);

    }

    //devuelve true si el evento de raton ocurrio dentro de los bordes del boton dado
    private boolean isIn(MouseEvent e, BotonesPausa p) {
        return p.getBordes().contains(e.getX(), e.getY());
    }

    //mueve el slider de volumen si el boton esta siendo arrastrado
    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()) {
            float valorAntes = volumeButton.getFloatValue();
            volumeButton.changeX(e.getX());
            float valorDes = volumeButton.getFloatValue();
            if(valorAntes != valorDes){
                juego.getAudioPlayer().setVolumen(valorDes);
            }

        }
    }

    //marca el boton como presionado cuando el raton hace click sobre el
    public void mousePressed(MouseEvent e) {
        if (isIn(e, botonMusica)) {
            botonMusica.setMousePressed(true);
        } else if (isIn(e, sfxBoton)) {
            sfxBoton.setMousePressed(true);
        }  else if (isIn(e, volumeButton)) {
            volumeButton.setMousePressed(true);
        }
    }

    //activa el hover del boton sobre el que esta el raton y desactiva el del resto
    public void mouseMoved(MouseEvent e) {
        botonMusica.setMouseOver(false);
        sfxBoton.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if (isIn(e, botonMusica)) {
            botonMusica.setMouseOver(true);
        } else if (isIn(e, sfxBoton)) {
            sfxBoton.setMouseOver(true);
        } else if (isIn(e, volumeButton)) {
            volumeButton.setMouseOver(true);
        }
    }

    //ejecuta la accion del boton si el raton se suelta encima del mismo donde se pulso
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, botonMusica)) {
            if (botonMusica.isMousePressed()) {
                //alterna el silencio de la musica
                botonMusica.setMuted(!botonMusica.isMuted());
                juego.getAudioPlayer().mutearCanciones();
            }
        } else if (isIn(e, sfxBoton)) {
            if (sfxBoton.isMousePressed()) {
                //alterna el silencio de los efectos de sonido
                sfxBoton.setMuted(!sfxBoton.isMuted());
                juego.getAudioPlayer().mutearEfectos();
            }
        }

        //limpia los flags de todos los botones tras soltar el raton
        botonMusica.resetBools();
        sfxBoton.resetBools();
        volumeButton.resetBools();
    }
}
