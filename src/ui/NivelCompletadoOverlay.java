package ui;

import gamestates.Playing;
import main.Juego;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constantes.UI.URMBotones.URM_SIZE;


public class NivelCompletadoOverlay {

    //lo mismo que con el resto de componentes de la ui
    private Playing jugando;
    private UrmBoton menu, siguiente;
    private BufferedImage imagen;
    private int fondoX, fondoY, fondoAltura, fondoAnchura;

    public NivelCompletadoOverlay(Playing jugando) {
        this.jugando = jugando;
        cargarImagen();
        iniciarBotones();
    }
    private void iniciarBotones() {
        int menuX = (int) (330 * Juego.ESCALA);
        int siguienteX = (int) (445 * Juego.ESCALA);
        int y = (int) (195 * Juego.ESCALA);
        siguiente = new UrmBoton(siguienteX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmBoton(menuX, y, URM_SIZE, URM_SIZE, 2);


    }

    private void cargarImagen() {
       imagen = LoadSave.GetSpriteAtlas(LoadSave.NIVELCOMPLETADO_MENU);
       fondoAnchura = (int) (imagen.getWidth() * Juego.ESCALA);
       fondoAltura = (int) (imagen.getHeight() * Juego.ESCALA);
       fondoX = Juego.GAME_WIDTH / 2 - fondoAnchura / 2;
       fondoY = (int) (75 * Juego.ESCALA);
    }
    public void update() {
        menu.update();
        siguiente.update();
    }
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

        if(comprobarBoton(menu, e)) {
            menu.setMouseOver(true);

        } else if(comprobarBoton(siguiente, e)) {
            siguiente.setMouseOver(true);
        }
    }
    public void mouseReleased(MouseEvent e) {
        if(comprobarBoton(menu, e)) {
            if(menu.isMousePressed()) {
                System.out.println("menu");
            }
        } else if(comprobarBoton(siguiente, e)) {
            if(siguiente.isMousePressed()) {
                System.out.println("siguiente");
            }
        }
        menu.reiniciarBooleanos();
        siguiente.reiniciarBooleanos();
    }

    public void mousePressed(MouseEvent e) {
        if(comprobarBoton(menu, e)) {
            menu.setMousePressed(true);

        } else if(comprobarBoton(siguiente, e)) {
            siguiente.setMousePressed(true);
        }
    }

}
