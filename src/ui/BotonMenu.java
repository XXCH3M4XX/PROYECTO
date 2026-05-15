package ui;

import gamestates.Gamestate;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constantes.UI.botones.*;

//gestiona los botones del menu principal con sus tres estados visuales: reposo, hover y pulsado
public class BotonMenu {

    //posicion en pantalla y fila del spritesheet que corresponde a este boton
    private int xPos, yPos, rowIndex, indice;

    //offset para centrar el boton horizontalmente respecto a su posicion
    private int xOffsetCenter = B_WIDTH / 2;

    //estado del juego al que navega este boton al ser pulsado
    private Gamestate state;

    //frames del boton: 0 reposo, 1 hover, 2 pulsado
    private BufferedImage[] imgs;

    //flags que indican si el raton esta encima o pulsando el boton
    private boolean mouseOver, mousePressed;

    //rectangulo de colision para detectar interacciones del raton
    private Rectangle bordes;

    //inicializa el boton con su posicion, fila del atlas y estado de destino
    public BotonMenu(int xPos, int yPos, int rowIndex, Gamestate state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        cargarImagenes();
        initBordes();
    }

    //inicializa el rectangulo de colision centrado respecto a la posicion del boton
    private void initBordes() {
        bordes = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
    }

    //carga los tres frames del boton desde el spritesheet de botones del menu
    private void cargarImagenes() {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.BOTONES_MENU);
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT,
                    B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
        }
    }

    //dibuja el frame activo del boton en su posicion centrada
    public void draw(Graphics g) {
        g.drawImage(imgs[indice], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
    }

    //selecciona el frame correcto segun el estado del raton
    public void update() {
        indice = 0;
        if (mouseOver) indice = 1;
        if (mousePressed) indice = 2;
    }

    //devuelve true si el raton esta encima del boton
    public boolean isMouseOver() { return mouseOver; }

    //activa o desactiva el efecto hover del boton
    public void setMouseOver(boolean mouseOver) { this.mouseOver = mouseOver; }

    //devuelve true si el boton esta siendo pulsado
    public boolean isMousePressed() { return mousePressed; }

    //activa o desactiva el estado de pulsado del boton
    public void setMousePressed(boolean mousePressed) { this.mousePressed = mousePressed; }

    //cambia el estado global del juego al estado asignado a este boton
    public void applyGameState() { Gamestate.state = state; }

    //resetea los flags de hover y pulsado al soltar el raton
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }
    public Gamestate getGameState() {
        return state;
    }

    //devuelve el rectangulo de colision para comprobar si el raton esta sobre el boton
    public Rectangle getBordes() { return bordes; }
}