package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Juego;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constantes.UI.URMBotones.URM_SIZE;

//pantalla de game over que se muestra al morir, permite volver al menu con escape
public class OverOverlayJuego {

    private BufferedImage img;
    private Playing playing;
    private int imgX, imgY, imgW, imgH;
    private UrmBoton menu, jugar;

    //recibe la referencia al estado de juego para poder llamar a resetAll y cambiar de estado
    public OverOverlayJuego(Playing playing) {
        this.playing = playing;
        crearImagen();
        crearBotones();
    }

    //crea los dos botones URM (jugar y menu) y los coloca en su posicion escalada dentro del overlay
    private void crearBotones() {
        int menuX = (int)(335 * Juego.ESCALA);
        int jugarX = (int)(440 * Juego.ESCALA);
        int y = (int)(240 * Juego.ESCALA);
        jugar = new UrmBoton(jugarX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmBoton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    //carga la imagen de fondo del overlay y calcula su posicion centrada en pantalla
    private void crearImagen() {
        img = LoadSave.GetSpriteAtlas(LoadSave.PANTALLA_MUERTE);
        imgW = (int) (img.getWidth() * Juego.ESCALA);
        imgH = (int) (img.getHeight() * Juego.ESCALA);
        imgX = Juego.GAME_WIDTH / 2 - imgW / 2;
        imgY = (int) (100 * Juego.ESCALA);
    }

    //dibuja la imagen de fondo del overlay y los dos botones encima
    public void draw(Graphics g) {
        g.drawImage(img, imgX, imgY, imgW, imgH, null);
        menu.draw(g);
        jugar.draw(g);
    }

    //al pulsar escape resetea el juego y vuelve al menu principal
    public void teclaPresionada(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            Gamestate.state = Gamestate.MENU;
        }
    }

    //actualiza el estado visual de los botones cada frame, necesario para el efecto hover y pressed
    public void update() {
        menu.update();
        jugar.update();
    }

    //devuelve true si el raton esta dentro de los bordes del boton dado
    private boolean comprobarBoton(UrmBoton b, MouseEvent e) {
        return b.getBordes().contains(e.getX(), e.getY());
    }

    //activa el efecto hover del boton sobre el que esta el raton y desactiva el del resto
    public void mouseMoved(MouseEvent e) {
        jugar.setMouseOver(false);
        menu.setMouseOver(false);
        if (comprobarBoton(menu, e)) {
            menu.setMouseOver(true);
        } else if (comprobarBoton(jugar, e)) {
            jugar.setMouseOver(true);
        }
    }

    //ejecuta la accion del boton si el raton se suelta encima del mismo boton donde se pulso
    public void mouseReleased(MouseEvent e) {
        if (comprobarBoton(menu, e)) {
            if (menu.isMousePressed()) {
                //resetea todo el juego y vuelve al menu principal
                playing.resetAll();
                Gamestate.state = Gamestate.MENU;
            }
        } else if (comprobarBoton(jugar, e)) {
            if (jugar.isMousePressed()) {
                //resetea el nivel y vuelve a empezar desde el principio
                playing.resetAll();
            }
        }
        //limpia los flags de pressed y hover de ambos botones tras soltar el raton
        menu.reiniciarBooleanos();
        jugar.reiniciarBooleanos();
    }

    //marca el boton como presionado cuando el raton hace click sobre el
    public void mousePressed(MouseEvent e) {
        if (comprobarBoton(menu, e)) {
            menu.setMousePressed(true);
        } else if (comprobarBoton(jugar, e)) {
            jugar.setMousePressed(true);
        }
    }
}