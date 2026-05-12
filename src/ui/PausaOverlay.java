package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Juego;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import static utils.Constantes.UI.BotonesPausa.*;
import static utils.Constantes.UI.URMBotones.*;
import static utils.Constantes.UI.BotonesVolumen.*;

//overlay de pausa con botones de sonido, volumen y navegacion
public class PausaOverlay {
    //referencia al estado de juego para despausar y resetear
    private Playing playing;

    //imagen de fondo del menu de pausa y sus dimensiones y posicion en pantalla
    private BufferedImage fondo;
    private int fnX, fnY, fnW, fnH;

    //botones de silencio para musica y efectos de sonido
    private BotonesDeSonido botonMusica, sfxBoton;

    //botones de menu, reiniciar y despausar
    private UrmBoton menuB, replayB, unpauseB;

    //slider de volumen
    private VolumeButton volumeButton;

    //recibe la referencia al estado de juego e inicializa todos los elementos del overlay
    public PausaOverlay(Playing playing) {
        this.playing = playing;
        cargarFondo();
        crearBotonesSonido();
        crearURMBotones();
        crearBotonDeVolumen();
    }

    //crea el slider de volumen y lo coloca en su posicion escalada
    private void crearBotonDeVolumen() {
        int vX = (int)(309 * Juego.ESCALA);
        int vY = (int)(278 * Juego.ESCALA);
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    //crea los botones de menu, reiniciar y despausar y los coloca en su posicion escalada
    private void crearURMBotones() {
        int menuX = (int)(313 * Juego.ESCALA);
        int replayX = (int)(387 * Juego.ESCALA);
        int unpauseX = (int)(462 * Juego.ESCALA);
        int bY = (int)(325 * Juego.ESCALA);
        menuB = new UrmBoton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmBoton(replayX, bY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new UrmBoton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
    }

    //crea los botones de silencio de musica y sfx y los coloca en su posicion escalada
    private void crearBotonesSonido() {
        int sonidoX = (int)(450 * Juego.ESCALA);
        int musicaY = (int)(140 * Juego.ESCALA);
        int sfxY = (int)(186 * Juego.ESCALA);
        botonMusica = new BotonesDeSonido(sonidoX, musicaY, TAMAÑO_SONIDO, TAMAÑO_SONIDO);
        sfxBoton = new BotonesDeSonido(sonidoX, sfxY, TAMAÑO_SONIDO, TAMAÑO_SONIDO);
    }

    //carga la imagen de fondo y calcula su posicion centrada en pantalla
    private void cargarFondo() {
        fondo = LoadSave.GetSpriteAtlas(LoadSave.FONDO_PAUSA);
        fnW = (int)(fondo.getWidth() * Juego.ESCALA);
        fnH = (int)(fondo.getHeight() * Juego.ESCALA);
        fnX = Juego.GAME_WIDTH / 2 - fnW / 2;
        fnY = (int)(25 * Juego.ESCALA);
    }

    //actualiza el estado visual de todos los botones cada frame
    public void actualizar() {
        botonMusica.update();
        sfxBoton.update();
        menuB.update();
        replayB.update();
        unpauseB.update();
        volumeButton.update();
    }

    //dibuja el fondo y todos los botones del menu de pausa
    public void draw(Graphics g) {
        g.drawImage(fondo, fnX, fnY, fnW, fnH, null);
        botonMusica.draw(g);
        sfxBoton.draw(g);
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);
        volumeButton.draw(g);
    }

    //activa el hover del boton sobre el que esta el raton y desactiva el del resto
    public void mouseMoved(MouseEvent e) {
        botonMusica.setMouseOver(false);
        sfxBoton.setMouseOver(false);
        menuB.setMouseOver(false);
        unpauseB.setMouseOver(false);
        replayB.setMouseOver(false);
        volumeButton.setMouseOver(false);

        if (isIn(e, botonMusica)) {
            botonMusica.setMouseOver(true);
        } else if (isIn(e, sfxBoton)) {
            sfxBoton.setMouseOver(true);
        } else if (isIn(e, menuB)) {
            menuB.setMouseOver(true);
        } else if (isIn(e, unpauseB)) {
            unpauseB.setMouseOver(true);
        } else if (isIn(e, replayB)) {
            replayB.setMouseOver(true);
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
            }
        } else if (isIn(e, sfxBoton)) {
            if (sfxBoton.isMousePressed()) {
                //alterna el silencio de los efectos de sonido
                sfxBoton.setMuted(!sfxBoton.isMuted());
            }
        } else if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                //vuelve al menu principal y despausa el juego
                Gamestate.state = Gamestate.MENU;
                playing.depausarJuego();
            }
        } else if (isIn(e, replayB)) {
            if (replayB.isMousePressed()) {
                //reinicia el nivel desde el principio y despausa el juego
                playing.resetAll();
                playing.depausarJuego();
            }
        } else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed()) {
                //reanuda la partida sin reiniciar
                playing.depausarJuego();
            }
        }

        //limpia los flags de todos los botones tras soltar el raton
        botonMusica.resetBools();
        sfxBoton.resetBools();
        menuB.reiniciarBooleanos();
        unpauseB.reiniciarBooleanos();
        replayB.reiniciarBooleanos();
        volumeButton.resetBools();
    }

    //marca el boton como presionado cuando el raton hace click sobre el
    public void mousePressed(MouseEvent e) {
        if (isIn(e, botonMusica)) {
            botonMusica.setMousePressed(true);
        } else if (isIn(e, sfxBoton)) {
            sfxBoton.setMousePressed(true);
        } else if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else if (isIn(e, replayB)) {
            replayB.setMousePressed(true);
        } else if (isIn(e, unpauseB)) {
            unpauseB.setMousePressed(true);
        } else if (isIn(e, volumeButton)) {
            volumeButton.setMousePressed(true);
        }
    }

    //mueve el slider de volumen si el boton esta siendo arrastrado
    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()) {
            volumeButton.changeX(e.getX());
        }
    }

    //devuelve true si el evento de raton ocurrio dentro de los bordes del boton dado
    private boolean isIn(MouseEvent e, BotonesPausa p) {
        return p.getBordes().contains(e.getX(), e.getY());
    }
}