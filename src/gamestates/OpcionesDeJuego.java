package gamestates;

import main.Juego;
import ui.BotonesPausa;
import ui.OpcionesAudio;
import ui.UrmBoton;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constantes.UI.URMBotones.URM_SIZE;

public class OpcionesDeJuego extends State implements Statemethods{

    private OpcionesAudio opcionesAudio;
    private BufferedImage imagenFondo, opcionesImagenFondo;
    private int bgX, bgY, bgW, bgH;
    private UrmBoton menuB;

    public OpcionesDeJuego(Juego juego) {
        super(juego);
        cargarImagenes();
        cargarBotones();
        opcionesAudio = juego.getOpcionesAudio();
    }

    private void cargarBotones() {
        int menuX = (int) (387 * Juego.ESCALA);
        int menuY = (int) (325 * Juego.ESCALA);

        menuB = new UrmBoton(menuX, menuY, URM_SIZE, URM_SIZE, 2);

    }

    private void cargarImagenes() {

        imagenFondo = LoadSave.GetSpriteAtlas(LoadSave.FONDO_PANTALLA);
        opcionesImagenFondo = LoadSave.GetSpriteAtlas(LoadSave.FONDO_OPCIONES);
        bgW = (int) (opcionesImagenFondo.getWidth() * Juego.ESCALA);
        bgH = (int) (opcionesImagenFondo.getHeight() * Juego.ESCALA);
        bgX = Juego.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (25 * Juego.ESCALA);
    }

    @Override
    public void update() {
        menuB.update();
        opcionesAudio.update();

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(imagenFondo, 0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT, null);
        g.drawImage(opcionesImagenFondo, bgX, bgY, bgW, bgH, null);

        menuB.draw(g);
        opcionesAudio.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        opcionesAudio.mouseDragged(e);
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else
            opcionesAudio.mousePressed(e);

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed())
                Gamestate.state = Gamestate.MENU;
        } else
            opcionesAudio.mouseReleased(e);

        menuB.reiniciarBooleanos();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);

        if (isIn(e, menuB))
            menuB.setMouseOver(true);
        else
            opcionesAudio.mouseMoved(e);

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            Gamestate.state = Gamestate.MENU;

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    private boolean isIn(MouseEvent e, BotonesPausa p) {
        return p.getBordes().contains(e.getX(), e.getY());
    }
}
