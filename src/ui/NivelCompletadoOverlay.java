package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Juego;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constantes.UI.URMBotones.URM_SIZE;

//overlay que aparece al completar un nivel con opciones de ir al menu o pasar al siguiente
public class NivelCompletadoOverlay {

    private Playing jugando;
    private UrmBoton menu, siguiente;
    private BufferedImage imagen;
    private int fondoX, fondoY, fondoAltura, fondoAnchura;

    public NivelCompletadoOverlay(Playing jugando) {
        this.jugando = jugando;
        cargarImagen();
        iniciarBotones();
    }

    //inicializa los botones de menu y siguiente nivel con su posicion en pantalla
    private void iniciarBotones() {
        int menuX = (int)(330 * Juego.ESCALA);
        int siguienteX = (int)(445 * Juego.ESCALA);
        int y = (int)(195 * Juego.ESCALA);
        siguiente = new UrmBoton(siguienteX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmBoton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    //carga la imagen de fondo y calcula su posicion centrada en pantalla
    private void cargarImagen() {
        imagen = LoadSave.GetSpriteAtlas(LoadSave.NIVELCOMPLETADO_MENU);
        fondoAnchura = (int)(imagen.getWidth() * Juego.ESCALA);
        fondoAltura = (int)(imagen.getHeight() * Juego.ESCALA);
        fondoX = Juego.GAME_WIDTH / 2 - fondoAnchura / 2;
        fondoY = (int)(75 * Juego.ESCALA);
    }

    public void update() {
        menu.update();
        siguiente.update();
    }

    //comprueba si el evento de raton ocurrio dentro de los bordes del boton
    private boolean comprobarBoton(UrmBoton b, MouseEvent e) {
        return b.getBordes().contains(e.getX(), e.getY());
    }

    public void draw(Graphics g) {
        g.drawImage(imagen, fondoX, fondoY, fondoAnchura, fondoAltura, null);
        siguiente.draw(g);
        menu.draw(g);
    }

    public void mouseMoved(MouseEvent e) {
        siguiente.setMouseOver(false);
        menu.setMouseOver(false);
        if (comprobarBoton(menu, e)) {
            menu.setMouseOver(true);
        } else if (comprobarBoton(siguiente, e)) {
            siguiente.setMouseOver(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (comprobarBoton(menu, e)) {
            if (menu.isMousePressed()) {
                //resetea todo el juego y vuelve al menu principal
                jugando.resetAll();
                Gamestate.state = Gamestate.MENU;
            }
        } else if (comprobarBoton(siguiente, e)) {
            if (siguiente.isMousePressed()) {
                //carga el siguiente nivel manteniendo el progreso del jugador
                jugando.cargarSiguienteNivel();
            }
        }
        menu.reiniciarBooleanos();
        siguiente.reiniciarBooleanos();
    }

    public void mousePressed(MouseEvent e) {
        if (comprobarBoton(menu, e)) {
            menu.setMousePressed(true);
        } else if (comprobarBoton(siguiente, e)) {
            siguiente.setMousePressed(true);
        }
    }
}