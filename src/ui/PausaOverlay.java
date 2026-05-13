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

    //botones de menu, reiniciar y despausar
    private UrmBoton menuB, replayB, unpauseB;

    private OpcionesAudio opcionesAudio;

    //recibe la referencia al estado de juego e inicializa todos los elementos del overlay
    public PausaOverlay(Playing playing) {
        this.playing = playing;
        cargarFondo();
        opcionesAudio = playing.getJuego().getOpcionesAudio();
        crearURMBotones();

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
        menuB.update();
        replayB.update();
        unpauseB.update();
        opcionesAudio.update();
    }

    //dibuja el fondo y todos los botones del menu de pausa
    public void draw(Graphics g) {
        g.drawImage(fondo, fnX, fnY, fnW, fnH, null);
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);
        opcionesAudio.draw(g);
    }

    //activa el hover del boton sobre el que esta el raton y desactiva el del resto
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        unpauseB.setMouseOver(false);
        replayB.setMouseOver(false);

        if (isIn(e, menuB)) {
            menuB.setMouseOver(true);
        } else if (isIn(e, unpauseB)) {
            unpauseB.setMouseOver(true);
        } else if (isIn(e, replayB)) {
            replayB.setMouseOver(true);
        } else {
            opcionesAudio.mouseMoved(e);
        }
    }

    //ejecuta la accion del boton si el raton se suelta encima del mismo donde se pulso
    public void mouseReleased(MouseEvent e) {
         if (isIn(e, menuB)) {
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
        } else {
             opcionesAudio.mouseReleased(e);
        }

        //limpia los flags de todos los botones tras soltar el raton
        menuB.reiniciarBooleanos();
        unpauseB.reiniciarBooleanos();
        replayB.reiniciarBooleanos();

    }

    //marca el boton como presionado cuando el raton hace click sobre el
    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else if (isIn(e, replayB)) {
            replayB.setMousePressed(true);
        } else if (isIn(e, unpauseB)) {
            unpauseB.setMousePressed(true);
        } else {
            opcionesAudio.mousePressed(e);
        }
    }

    //mueve el slider de volumen si el boton esta siendo arrastrado
    public void mouseDragged(MouseEvent e) {
        opcionesAudio.mouseDragged(e);
    }

    //devuelve true si el evento de raton ocurrio dentro de los bordes del boton dado
    private boolean isIn(MouseEvent e, BotonesPausa p) {
        return p.getBordes().contains(e.getX(), e.getY());
    }
}