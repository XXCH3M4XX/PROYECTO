package gamestates;

import main.Juego;
import ui.BotonControles;
import ui.BotonesPausa;
import ui.OpcionesAudio;
import ui.UrmBoton;
import utils.CargaSprites;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constantes.UI.URMBotones.URM_SIZE;

//pantalla de opciones accesible desde el menu principal con controles de audio y navegacion
public class OpcionesJuego extends Estado implements MetodosEstadoJuego {

    private OpcionesAudio opcionesAudio;

    //imagenes del fondo de pantalla completa y del panel de opciones
    private BufferedImage imagenFondo, opcionesImagenFondo;

    //posicion y tamaño del panel de opciones centrado en pantalla
    private int bgX, bgY, bgW, bgH;

    //boton para volver al menu principal
    private UrmBoton menuB;

    //boton para acceder a la pantalla de controles
    private BotonControles botonControles;

    //inicializa las imagenes, botones y el componente de audio compartido
    public OpcionesJuego(Juego juego) {
        super(juego);
        cargarImagenes();
        cargarBotones();
        opcionesAudio = juego.getOpcionesAudio();
    }

    //crea los botones de navegacion y los coloca en su posicion escalada
    private void cargarBotones() {
        int menuX = (int) (387 * Juego.ESCALA);
        int menuY = (int) (325 * Juego.ESCALA);

        menuB = new UrmBoton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
        botonControles = new BotonControles(
                (int)(550 * Juego.ESCALA),
                (int)(200 * Juego.ESCALA)
        );
    }

    //carga las imagenes de fondo y calcula la posicion centrada del panel de opciones
    private void cargarImagenes() {
        imagenFondo = CargaSprites.GetSpriteAtlas(CargaSprites.FONDO_PANTALLA);
        opcionesImagenFondo = CargaSprites.GetSpriteAtlas(CargaSprites.FONDO_OPCIONES);
        bgW = (int) (opcionesImagenFondo.getWidth() * Juego.ESCALA);
        bgH = (int) (opcionesImagenFondo.getHeight() * Juego.ESCALA);
        bgX = Juego.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (25 * Juego.ESCALA);
    }

    //actualiza el estado visual de todos los botones y el componente de audio cada frame
    @Override
    public void update() {
        menuB.update();
        opcionesAudio.update();
        botonControles.update();
    }

    //dibuja el fondo, el panel de opciones y todos los botones
    @Override
    public void draw(Graphics g) {
        g.drawImage(imagenFondo, 0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT, null);
        g.drawImage(opcionesImagenFondo, bgX, bgY, bgW, bgH, null);
        menuB.draw(g);
        opcionesAudio.draw(g);
        botonControles.draw(g);
    }

    //mueve el slider de volumen si el boton esta siendo arrastrado
    public void mouseDragged(MouseEvent e) {
        opcionesAudio.mouseDragged(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    //marca el boton como presionado cuando el raton hace click sobre el
    @Override
    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else if (isIn(e, botonControles)) {
            botonControles.setMousePressed(true);
        } else {
            opcionesAudio.mousePressed(e);
        }
    }

    //ejecuta la accion del boton si el raton se suelta encima del mismo donde se pulso
    @Override
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed())
                //vuelve al menu principal
                EstadoJuego.state = EstadoJuego.MENU;
        } else if (isIn(e, botonControles)) {
            if (botonControles.isMousePressed())
                //navega a la pantalla de controles
                EstadoJuego.state = EstadoJuego.CONTROLES;
        } else {
            opcionesAudio.mouseReleased(e);
        }
        //limpia los flags de todos los botones tras soltar el raton
        menuB.reiniciarBooleanos();
        botonControles.resetBools();
    }

    //activa el hover del boton sobre el que esta el raton y desactiva el del resto
    @Override
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        botonControles.setMouseOver(false);

        if (isIn(e, menuB))
            menuB.setMouseOver(true);
        else
            opcionesAudio.mouseMoved(e);

        if (isIn(e, botonControles))
            botonControles.setMouseOver(true);
    }

    //vuelve al menu principal al pulsar escape
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            EstadoJuego.state = EstadoJuego.MENU;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    //devuelve true si el evento de raton ocurrio dentro de los bordes del boton dado
    private boolean isIn(MouseEvent e, BotonesPausa p) {
        return p.getBordes().contains(e.getX(), e.getY());
    }
}